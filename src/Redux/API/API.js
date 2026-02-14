import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const baseUrl = import.meta.env.VITE_BASE_URL;

// 1. Create a central instance
const api = axios.create({
  baseURL: baseUrl,
});

// 2. The Interceptor grabs the FRESH token for every request automatically
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 3. Response Interceptor for auto-logout
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.clear();
      window.location.href = "/login"; 
    }
    return Promise.reject(error);
  }
);

// --- AUTH ACTIONS ---

export const userSignUP = createAsyncThunk("userSignUp", async (credentials, { rejectWithValue }) => {
  try {
    // Auth calls don't need the token interceptor usually, but 'api' handles it fine
    const response = await api.post(`/auth/signup`, credentials);
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response?.status === 403 ? "User already exists!" : error.message);
  }
});

export const userLogin = createAsyncThunk("userLogin", async (credentials, { rejectWithValue }) => {
  try {
    const response = await api.post(`/auth/login`, credentials);
    return response.data; // Ensure you return .data
  } catch (error) {
    return rejectWithValue(error.response?.status === 403 ? "Invalid User!" : error.message);
  }
});

// --- ADMIN ACTIONS (Notice how much cleaner they are now!) ---

export const adminTours = createAsyncThunk("adminTours", async () => {
  const response = await api.get(`/admin/tours`);
  return response.data;
});

export const fetchTourDetails = createAsyncThunk("fetchTourDetails", async (tourId) => {
  const response = await api.get(`/admin/tours/${tourId}`);
  return response.data;
});

export const adminTransport = createAsyncThunk("adminTransport", async () => {
  const response = await api.get(`/admin/transports`); // Fixed missing await
  return response.data;
});

export const adminLocation = createAsyncThunk("adminLocation", async () => {
  const response = await api.get(`/admin/locations`); // Fixed missing await
  return response.data;
});

export const deleteTour = createAsyncThunk("deleteTour", async (tourId) => {
  const response = await api.delete(`/admin/tours/${tourId}`);
  return response.data;
});

export const updateTour = createAsyncThunk("updateTour", async ({ tourId, formData }, { rejectWithValue }) => {
  try {
    const response = await api.put(`/admin/tours/${tourId}`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response?.data);
  }
});

// --- USER ACTIONS ---

export const userTours = createAsyncThunk("userTours", async () => {
  const response = await api.get(`/customer/tours`);
  return response.data;
});

export const userBook = createAsyncThunk("userBook", async ({ tourId, numberOfTickets }) => {
  const response = await api.post(`/customer/create-payment-intent/${tourId}`, {}, {
    params: { numberOfTickets }
  });
  return response.data;
});

export const confirmBooking = createAsyncThunk("confirmBooking", async ({ bookingId, paymentIntentId }) => {
  const response = await api.post(`/customer/confirm-payment/${bookingId}`, {}, {
    params: { paymentIntentId }
  });
  return response.data;
});
 
 
 // import { createAsyncThunk } from "@reduxjs/toolkit";
  // import axios from "axios";
  // const baseUrl = import.meta.env.VITE_BASE_URL;
  // const token = localStorage.getItem("token");

  // // 1. Create a central instance
  // const api = axios.create({
  //   baseURL: baseUrl,
  // });

  // // 2. Add an Interceptor to attach the token automatically
  // api.interceptors.request.use((config) => {
  //   const token = localStorage.getItem("token");
  //   if (token) {
  //     config.headers.Authorization = `Bearer ${token}`;
  //   }
  //   return config;
  // });

  // // 3. Add an Interceptor to handle expired tokens (401 errors)
  // api.interceptors.response.use(
  //   (response) => response,
  //   (error) => {
  //     if (error.response && error.response.status === 401) {
  //       localStorage.clear();
  //       window.location.href = "/login"; // Auto-redirect if token is dead
  //     }
  //     return Promise.reject(error);
  //   }
  // );

  // // SignUP
  // export const userSignUP = createAsyncThunk(
  //   "userSignUp",
  //   async (credintials) => {
  //     try {
  //       const request = await axios.post(`${baseUrl}/auth/signup`, credintials);

  //       return request;
  //     } catch (error) {
  //       if (error.status === 403) {
  //         return { error: "User already exists!" };
  //       }
  //     }
  //   }
  // );

  // // SignIn

  // export const userLogin = createAsyncThunk("userLogin", async (credentials) => {
  //   try {
  //     // Making the POST request and waiting for the response
  //     const response = await axios.post(`${baseUrl}/auth/login`, credentials);

  //     return response;
  //   } catch (error) {
  //     if (error.status === 403) {
  //       return { error: "Invalid User!" };
  //     }
  //   }
  // });

  // // admin get all tours

  // export const adminTours = createAsyncThunk("adminTours", async () => {
  //   try {
  //     const token = localStorage.getItem("token");
  //     if (!token) {
  //       return;
  //     }
  //     const response = await axios.get(`${baseUrl}/admin/tours`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });

  // //get admin tour by Id

  // export const fetchTourDetails = createAsyncThunk(
  //   "fetchTourDetails",
  //   async (tourId, { rejectWithValue }) => {
  //     try {
  //       const token = localStorage.getItem("token");
  //       if (!token) {
  //         return;
  //       }
  //       const response = await axios.get(`${baseUrl}/admin/tours/${tourId}`, {
  //         headers: {
  //           Authorization: `Bearer ${token}`,
  //         },
  //       });
  //       return response.data;
  //     } catch (error) {
  //       return rejectWithValue(error.response.data);
  //     }
  //   }
  // );

  // // get admin all transport

  // export const adminTransport = createAsyncThunk("adminTransport", async () => {
  //   try {
  //     const token = localStorage.getItem('token');
  //     if(!token){
  //       return;
  //     }
  //     const response = axios.get(`${baseUrl}/admin/transports`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });

  // // get admin all location

  // export const adminLocation = createAsyncThunk("adminLocation", async () => {
  //   try {
  //     const token = localStorage.getItem('token');
  //     if(!token){
  //       return;
  //     }
  //     const response = axios.get(`${baseUrl}/admin/locations`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });
  // // get admin tour delete

  // export const deleteTour = createAsyncThunk("deleteTour", async (tourId) => {
  //   try {
  //     const token = localStorage.getItem('token');
  //     if(!token){
  //       return;
  //     }
  //     const response = axios.delete(`${baseUrl}/admin/tours/${tourId}`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });

  // // get admin all lodging

  // export const adminLodging = createAsyncThunk("adminLodging", async () => {
  //   try {
  //     const token = localStorage.getItem('token');
  //     if(!token){
  //       return;
  //     }
  //     const response = axios.get(`${baseUrl}/admin/lodgings`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });

  // // update tour

  // export const updateTour = createAsyncThunk(
  //   "updateTour",
  //   async ({ tourId, formData }, { rejectWithValue }) => {
  //     try {
  //       const token = localStorage.getItem("token");
  //       const response = await axios.put(
  //         `${baseUrl}/admin/tours/${tourId}`,
  //         formData,
  //         {
  //           headers: {
  //             "Content-Type": "multipart/form-data", // Important: use multipart/form-data for file uploads
  //             Authorization: `Bearer ${token}`,
  //           },
  //         }
  //       );
  //       return response.data;
  //     } catch (error) {
  //       console.error("Update tour error:", error);
  //       return rejectWithValue(
  //         error.response ? error.response.data : error.message
  //       );
  //     }
  //   }
  // );

  // // update location

  // export const editLocation = createAsyncThunk(
  //   "editLocation",
  //   async ({ locationId, updatedLocation }) => {
  //     const token = localStorage.getItem("token");
  //     if(!token){
  //       return;
  //     }
  //     const response = await axios.put(
  //       `${baseUrl}/admin/locations/${locationId}`,
  //       updatedLocation,
  //       {
  //         headers: {
  //           "Content-Type": "application/json",
  //           Authorization: `Bearer ${token}`,
  //         },
  //       }
  //     );
  //     return response;
  //   }
  // );

  // // update transport

  // export const editTransport = createAsyncThunk(
  //   "editTransport",
  //   async ({ transportId, updatedTransport }) => {
  //     const token = localStorage.getItem("token");
  //     if(!token){
  //       return;
  //     }
  //     const response = await axios.put(
  //       `${baseUrl}/admin/transports/${transportId}`,
  //       updatedTransport,
  //       {
  //         headers: {
  //           "Content-Type": "application/json",
  //           Authorization: `Bearer ${token}`,
  //         },
  //       }
  //     );
  //     return response;
  //   }
  // );

  // // update lodging

  // export const editLodging = createAsyncThunk(
  //   "editLodging",
  //   async ({ lodgingId, updatedLodging }) => {
  //     const token = localStorage.getItem("token");
  //     if(!token){
  //       return;
  //     }
  //     const response = await axios.put(
  //       `${baseUrl}/admin/lodgings/${lodgingId}`,
  //       updatedLodging,
  //       {
  //         headers: {
  //           "Content-Type": "application/json",
  //           Authorization: `Bearer ${token}`,
  //         },
  //       }
  //     );
  //     return response;
  //   }
  // );

  // // admin ticket Summary

  // export const allTickets = createAsyncThunk("allTickets", async () => {
  //   try {
  //     const token = localStorage.getItem("token");
  //     if (!token) {
  //       return;
  //     }
  //     const response = axios.get(`${baseUrl}/admin/tourTicketSummary`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });

  // // admin tour book details
  // export const bookDetails = createAsyncThunk("bookDetails", async (tourId) => {
  //   try {
  //     const token = localStorage.getItem("token");
  //     if (!token) {
  //       return;
  //     }
  //     const response = await axios.get(`${baseUrl}/admin/tourDetails/${tourId}`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });

  // // user section-----

  // // user get all tours

  // export const userTours = createAsyncThunk("userTours", async () => {
  //   try {
  //     const token = localStorage.getItem("token");
  //     if (!token) {
  //       return;
  //     }
  //     const response = await axios.get(`${baseUrl}/customer/tours`, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     return response;
  //   } catch (error) {
  //     return error;
  //   }
  // });

  // // get user tour by ID
  // export const UserTourDetail = createAsyncThunk(
  //   "UserTourDetails",
  //   async (tourId, { rejectWithValue }) => {
  //     try {
  //       const token = localStorage.getItem("token");
  //       if (!token) {
  //         return;
  //       }
  //       const response = await axios.get(`${baseUrl}/customer/tours/${tourId}`, {
  //         headers: {
  //           Authorization: `Bearer ${token}`,
  //         },
  //       });
  //       return response.data;
  //     } catch (error) {
  //       return rejectWithValue(error.response.data);
  //     }
  //   }
  // );

  // // user book tour

  // export const userBook = createAsyncThunk(
  //   "userBook",

  //   async ({ tourId, numberOfTickets }) => {
  //     const token = localStorage.getItem("token");
  //       if (!token) {
  //         return;
  //       }
  //     try {
  //       const request = await axios.post(
  //         `${baseUrl}/customer/create-payment-intent/${tourId}?numberOfTickets=${numberOfTickets}`,
  //         // The second argument should be the request body (if any)
  //         {},  // Empty object if no body is needed
  //         {
  //           headers: {
  //             Authorization: `Bearer ${token}`,
  //           },
  //         }
  //       );
  //       return request.data;  // Return .data instead of entire request
  //     } catch (error) {
  //       console.error('Booking error:', error.response ? error.response.data : error.message);
  //       throw error;  // Rethrow to allow Redux to handle the error
  //     }
  //   }
  // );

  // // user confirm booking
  // export const confirmBooking = createAsyncThunk(
  //   "confirmBooking",
  //   async ({ bookingId,paymentIntentId}, { rejectWithValue }) => {
  //     try {
  //       const response = await axios.post(
  //         `${baseUrl}/customer/confirm-payment/${bookingId}?paymentIntentId=${paymentIntentId}`,
  //     {},
  //         {
  //           headers: {
  //             "Content-Type": "application/json",
  //             Authorization: `Bearer ${token}`,
  //           },
  //         }
  //       );
  //       return response;
  //     } catch (error) {
  //       return rejectWithValue(error);
  //     }
  //   }
  // );
