import mongoose from "mongoose";

const chatSchema = new mongoose.Schema({
  id: {
    type: String,
    required: true
  },
  orderId: {
    type: String,
    required: true,
    index: true
  },
  sender: {
    type: String,
    enum: ["user", "admin"],
    required: true
  },
  senderName: {
    type: String,
    required: true
  },
  message: {
    type: String,
    required: true
  },
  time: {
    type: String,
    default: "Just now"
  }
}, {
  timestamps: true
});

export const Chat = mongoose.model("Chat", chatSchema);
