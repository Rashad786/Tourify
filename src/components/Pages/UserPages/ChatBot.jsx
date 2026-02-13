import React, { useState, useRef, useEffect } from "react";
import { useDispatch } from "react-redux";
import { chatWithBot } from "../../../Redux/API/API";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

const ChatBot = () => {
  const dispatch = useDispatch();
  const [messages, setMessages] = useState([
    { role: "bot", content: "👋 Hi! I'm your travel assistant. Ask me anything about tours, destinations, or budgets!" },
  ]);
  const [input, setInput] = useState("");
  const [isTyping, setIsTyping] = useState(false);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = async () => {
    if (!input.trim() || isTyping) return;

    const userMessage = { role: "user", content: input };
    setMessages((prev) => [...prev, userMessage]);
    setInput("");
    setIsTyping(true);

    // Add a temporary typing indicator
    setMessages((prev) => [...prev, { role: "bot", content: "🤖 Planning your trip..." }]);

    try {
      const result = await dispatch(chatWithBot(input)).unwrap();
      
      setMessages((prev) => [
        ...prev.slice(0, -1), // Remove the "Typing..." message
        { role: "bot", content: result.reply || "🤖 No response from server." },
      ]);
    } catch (error) {
      setMessages((prev) => [
        ...prev.slice(0, -1),
        { role: "bot", content: "❌ Sorry, I'm having trouble connecting to the travel service." },
      ]);
    } finally {
      setIsTyping(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleSend();
  };

  return (
    <div className="flex flex-col h-screen bg-gray-100">
      {/* Header */}
      <div className="flex items-center px-6 py-4 bg-gradient-to-r from-blue-700 to-blue-500 shadow-md">
        <h2 className="text-xl font-bold text-white">TourPlanner AI Assistant</h2>
      </div>

      {/* Message Area */}
      <div className="flex-1 overflow-y-auto px-4 py-6 space-y-6 bg-gray-50">
        {messages.map((msg, i) => (
          <div
            key={i}
            className={`flex ${msg.role === "user" ? "justify-end" : "justify-start"}`}
          >
            <div
              className={`max-w-[85%] px-5 py-4 rounded-2xl shadow-sm ${
                msg.role === "user"
                  ? "bg-blue-600 text-white rounded-br-none"
                  : "bg-white text-gray-800 rounded-bl-none border border-gray-200"
              }`}
            >
              {/* Markdown Parser for Tables and Formatting */}
              <ReactMarkdown
                remarkPlugins={[remarkGfm]}
                components={{
                  // Styling for AI-generated tables
                  table: ({ node, ...props }) => (
                    <div className="overflow-x-auto my-3">
                      <table className="border-collapse border border-gray-300 w-full text-xs" {...props} />
                    </div>
                  ),
                  th: ({ node, ...props }) => <th className="border border-gray-300 p-2 bg-gray-100 font-bold" {...props} />,
                  td: ({ node, ...props }) => <td className="border border-gray-300 p-2" {...props} />,
                  // Styling for lists
                  ul: ({ node, ...props }) => <ul className="list-disc ml-4 space-y-1" {...props} />,
                  ol: ({ node, ...props }) => <ol className="list-decimal ml-4 space-y-1" {...props} />,
                }}
              >
                {msg.content}
              </ReactMarkdown>
            </div>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      {/* Input Area */}
      <div className="p-4 bg-white border-t">
        <div className="flex items-center max-w-4xl mx-auto">
          <input
            type="text"
            value={input}
            disabled={isTyping}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder={isTyping ? "AI is thinking..." : "Ask about tours (e.g., Suggest a 3-day Paris tour)..."}
            className="flex-1 px-4 py-3 text-sm border border-gray-300 rounded-l-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-100"
          />
          <button
            onClick={handleSend}
            disabled={isTyping}
            className="px-6 py-3 text-white bg-blue-600 rounded-r-lg hover:bg-blue-700 transition-colors disabled:bg-blue-400"
          >
            {isTyping ? "..." : "Send"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatBot;