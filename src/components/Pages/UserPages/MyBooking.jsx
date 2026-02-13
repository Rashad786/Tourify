import React, { useState, useEffect } from "react";
import { Calendar, MapPin, Tag, Clock, ChevronRight, Package, AlertCircle } from "lucide-react";
import { Header, Footer } from "../../Reusable/Banner";
import { useDispatch } from "react-redux";
import { fetchUserBookings } from "../../../Redux/API/API"; 
import { useNavigate } from "react-router-dom";

const BookingCard = ({ booking }) => {
  // Matches your Java PaymentStatus enum: SUCCESS, PENDING, FAILED
  const getStatusColor = (status) => {
    switch (status) {
      case 'SUCCESS': return 'bg-green-100 text-green-700 border-green-200';
      case 'PENDING': return 'bg-yellow-100 text-yellow-700 border-yellow-200';
      case 'FAILED': return 'bg-red-100 text-red-700 border-red-200';
      default: return 'bg-gray-100 text-gray-700 border-gray-200';
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden hover:shadow-lg transition-all duration-300">
      <div className="p-5">
        <div className="flex justify-between items-start mb-4">
          <div className={`px-3 py-1 rounded-full text-xs font-bold border ${getStatusColor(booking.paymentStatus)} uppercase tracking-wider`}>
            {booking.paymentStatus || 'UNKNOWN'}
          </div>
          <span className="text-sm text-gray-400 font-mono">#{booking.bookingId}</span>
        </div>

        {/* Accessing nested tour object from your Java Entity */}
        <h3 className="text-xl font-bold text-gray-800 mb-2 truncate">
          {booking.tour?.tourName || "Package Tour"}
        </h3>
        
        <div className="space-y-3">
          <div className="flex items-center text-gray-600">
            <Calendar className="w-4 h-4 mr-2 text-blue-500" />
            <span className="text-sm">
              {booking.bookingDate ? `Booked: ${booking.bookingDate}` : "Date TBD"}
            </span>
          </div>
          <div className="flex items-center text-gray-600">
            <MapPin className="w-4 h-4 mr-2 text-red-500" />
            <span className="text-sm">{booking.tour?.location || booking.tour?.destination || "Global"}</span>
          </div>
          <div className="flex items-center text-gray-600">
            <Tag className="w-4 h-4 mr-2 text-green-500" />
            <span className="text-sm font-semibold text-gray-900">
              ${booking.totalPrice} <span className="text-gray-400 font-normal">({booking.numberOfTickets} Tickets)</span>
            </span>
          </div>
        </div>

        <div className="mt-6 pt-4 border-t border-gray-50 flex justify-between items-center">
          <button className="text-blue-600 font-medium text-sm hover:underline">
            View Details
          </button>
          <button className="bg-blue-50 text-blue-600 p-2 rounded-lg hover:bg-blue-600 hover:text-white transition-colors">
            <ChevronRight className="w-5 h-5" />
          </button>
        </div>
      </div>
    </div>
  );
};

const MyBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const getBookings = async () => {
      try {
        setLoading(true);
        const resultAction = await dispatch(fetchUserBookings());
        
        if (fetchUserBookings.fulfilled.match(resultAction)) {
          // Check if your backend returns { bookings: [] } or just []
          const data = resultAction.payload.bookings || resultAction.payload;
          setBookings(Array.isArray(data) ? data : []);
        } else {
          setError(resultAction.payload || "Failed to load bookings");
        }
      } catch (err) {
        setError("An unexpected error occurred");
      } finally {
        setLoading(false);
      }
    };

    getBookings();
  }, [dispatch]);

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col pt-16">
      <Header />
      <div className="container mx-auto px-4 py-10 flex-grow">
        <div className="flex items-center space-x-4 mb-8">
          <div className="p-3 bg-blue-600 rounded-lg text-white shadow-lg">
            <Package className="w-8 h-8" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">My Bookings</h1>
            <p className="text-gray-500">Manage your trips and reservations</p>
          </div>
        </div>

        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-xl flex items-center">
            <AlertCircle className="w-5 h-5 mr-2" />
            {error}
          </div>
        )}

        {loading ? (
          <div className="flex justify-center py-20">
            <div className="w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>
          </div>
        ) : bookings.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {bookings.map((booking) => (
              <BookingCard key={booking.bookingId} booking={booking} />
            ))}
          </div>
        ) : (
          <div className="bg-white rounded-2xl p-12 text-center shadow-sm border border-dashed border-gray-300">
            <Clock className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <h2 className="text-xl font-semibold text-gray-700">No Bookings Found</h2>
            <p className="text-gray-500 mb-6">You haven't booked any adventures yet.</p>
            <button 
              onClick={() => navigate("/user/dashboard")}
              className="bg-blue-600 text-white px-8 py-3 rounded-xl font-bold shadow-lg hover:bg-blue-700 transition-all"
            >
              Browse Tours
            </button>
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default MyBookings;