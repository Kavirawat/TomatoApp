import mongoose from "mongoose";

const orderSchema = new mongoose.Schema({
  id: {
    type: String,
    required: true,
    unique: true
  },
  userPhone: {
    type: String,
    default: "9876543210"
  },
  receiverName: {
    type: String,
    default: "Customer"
  },
  restaurantName: {
    type: String,
    required: true
  },
  restaurantAddress: {
    type: String,
    default: "Local Restaurant Kitchen"
  },
  items: {
    type: Array,
    default: []
  },
  itemTotal: {
    type: Number,
    default: 0
  },
  deliveryFee: {
    type: Number,
    default: 35
  },
  taxes: {
    type: Number,
    default: 15
  },
  discount: {
    type: Number,
    default: 0
  },
  totalAmount: {
    type: Number,
    required: true
  },
  status: {
    type: String,
    enum: ["PLACED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED"],
    default: "PLACED"
  },
  etaMinutes: {
    type: Number,
    default: 25
  },
  deliveryPartnerName: {
    type: String,
    default: "Vikram Singh"
  },
  deliveryPartnerPhone: {
    type: String,
    default: "+91 98101 23456"
  },
  deliveryPartnerRating: {
    type: Number,
    default: 4.8
  },
  deliveryAddress: {
    type: String,
    required: true
  },
  deliveryOtp: {
    type: String,
    required: true
  },
  isOtpVerified: {
    type: Boolean,
    default: false
  },
  adminNotes: {
    type: String,
    default: "Admin: Order accepted and sent to kitchen."
  },
  orderedAt: {
    type: String,
    default: "Just now"
  }
}, {
  timestamps: true
});

export const Order = mongoose.model("Order", orderSchema);
