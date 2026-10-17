import { Router } from "express";
import { Order as OrderModel } from "../models/Order.js";
import { Chat as ChatModel } from "../models/Chat.js";

const router = Router();

let orders = [
  {
    id: "TOM-8421",
    userPhone: "9876543210",
    restaurantName: "Haldiram's Sweets & Meals",
    restaurantAddress: "Sector 18, Noida",
    items: [
      { id: "h1", name: "Special Chole Bhature Combo", price: 180, quantity: 2 },
      { id: "h2", name: "Gulab Jamun (2 pcs)", price: 70, quantity: 1 }
    ],
    itemTotal: 430,
    deliveryFee: 35,
    taxes: 21,
    discount: 50,
    totalAmount: 436,
    status: "PREPARING",
    etaMinutes: 20,
    deliveryPartnerName: "Vikram Singh",
    deliveryPartnerPhone: "+91 98101 23456",
    deliveryPartnerRating: 4.8,
    deliveryAddress: "Home • Flat 402, Royal Palms, Sector 62, Noida",
    deliveryOtp: "4829",
    isOtpVerified: false,
    adminNotes: "Admin Update: Chef is cooking your fresh meal in the kitchen (Ban raha hai 👨‍🍳).",
    orderedAt: "03:15 PM"
  },
  {
    id: "TOM-9152",
    userPhone: "9876543210",
    restaurantName: "Burger King",
    restaurantAddress: "DLF Mall of India, Noida",
    items: [
      { id: "bk1", name: "Crispy Veg Double Patty Whopper", price: 199, quantity: 1 },
      { id: "bk2", name: "Peri Peri Cheesy Fries", price: 119, quantity: 1 }
    ],
    itemTotal: 318,
    deliveryFee: 25,
    taxes: 16,
    discount: 0,
    totalAmount: 359,
    status: "OUT_FOR_DELIVERY",
    etaMinutes: 10,
    deliveryPartnerName: "Rahul Sharma",
    deliveryPartnerPhone: "+91 98765 43210",
    deliveryPartnerRating: 4.9,
    deliveryAddress: "Work • Building 3B, Cyber City, Gurgaon",
    deliveryOtp: "7319",
    isOtpVerified: false,
    adminNotes: "Admin Update: Food packed! Partner Rahul Sharma is en route (Raste mein hai 🛵). Share OTP 7319 upon arrival.",
    orderedAt: "03:22 PM"
  }
];

let orderChats = {
  "TOM-8421": [
    {
      id: "chat_init_1",
      orderId: "TOM-8421",
      sender: "admin",
      senderName: "Admin (Haldiram's)",
      message: "Namaste Customer! Your order #TOM-8421 has been accepted. Chef is preparing your Chole Bhature.",
      time: "03:15 PM"
    }
  ]
};

// Seed initial orders into MongoDB
async function seedMongoDB() {
  try {
    for (const ord of orders) {
      await OrderModel.findOneAndUpdate({ id: ord.id }, ord, { upsert: true, new: true });
    }
  } catch (e) {
    // MongoDB seeding background
  }
}
setTimeout(seedMongoDB, 1500);

// Place new order
router.post("/", async (req, res) => {
  const { restaurantName, restaurantAddress, items, itemTotal, deliveryFee, taxes, discount, totalAmount, deliveryAddress, receiverName, userPhone } = req.body;

  if (!items || !items.length) {
    return res.status(400).json({ success: false, message: "Cart cannot be empty" });
  }

  const generatedOtp = req.body.deliveryOtp || String(Math.floor(1000 + Math.random() * 9000));
  const orderId = req.body.id || `TOM-${Math.floor(1000 + Math.random() * 9000)}`;
  const cleanPhone = String(userPhone || "9876543210").replace(/\D/g, "").slice(-10);

  const newOrder = {
    id: orderId,
    userPhone: cleanPhone,
    restaurantName: restaurantName || "Haldiram's Sweets & Meals",
    restaurantAddress: restaurantAddress || "Sector 18, Noida",
    items,
    itemTotal: Number(itemTotal) || 0,
    deliveryFee: Number(deliveryFee) || 0,
    taxes: Number(taxes) || 0,
    discount: Number(discount) || 0,
    totalAmount: Number(totalAmount) || 0,
    status: req.body.status || "PLACED",
    etaMinutes: 25,
    deliveryPartnerName: "Vikram Singh",
    deliveryPartnerPhone: "+91 98101 23456",
    deliveryPartnerRating: 4.8,
    deliveryAddress: deliveryAddress || "Home • Sector 62, Noida",
    deliveryOtp: generatedOtp,
    isOtpVerified: false,
    adminNotes: req.body.adminNotes || "Admin (Kitchen Manager): Order accepted and sent to kitchen.",
    orderedAt: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  };

  orders.unshift(newOrder);

  // Save order to MongoDB
  try {
    await OrderModel.create(newOrder);
  } catch (err) {
    console.warn("MongoDB order save notice:", err.message);
  }

  // Initialize Admin Live Chat for this order
  const welcomeMsg = {
    id: `chat_welcome_${Date.now()}`,
    orderId,
    sender: "admin",
    senderName: `Admin (${newOrder.restaurantName})`,
    message: `Namaste ${receiverName || "Customer"}! We have received your order #${orderId}. The chef has started cooking. Please message us here for any custom requests or updates!`,
    time: "Just now"
  };
  orderChats[orderId] = [welcomeMsg];

  try {
    await ChatModel.create(welcomeMsg);
  } catch (e) {}

  res.status(201).json({
    success: true,
    message: "Order placed successfully! Delivery OTP generated.",
    data: newOrder
  });
});

// Get all orders (Combines MongoDB with memory)
router.get("/", async (req, res) => {
  try {
    const dbOrders = await OrderModel.find().sort({ createdAt: -1 });
    if (dbOrders && dbOrders.length > 0) {
      return res.json({ success: true, count: dbOrders.length, data: dbOrders });
    }
  } catch (e) {}
  res.json({ success: true, count: orders.length, data: orders });
});

// Get single order
router.get("/:id", async (req, res) => {
  try {
    const dbOrder = await OrderModel.findOne({ id: req.params.id });
    if (dbOrder) return res.json({ success: true, data: dbOrder });
  } catch (e) {}

  const order = orders.find(o => o.id === req.params.id);
  if (!order) {
    return res.status(404).json({ success: false, message: "Order not found" });
  }
  res.json({ success: true, data: order });
});

// Admin situation & status update
router.patch("/:id/status", async (req, res) => {
  const { status, adminNotes } = req.body;
  let order = orders.find(o => o.id === req.params.id);

  if (!order) {
    try {
      order = await OrderModel.findOne({ id: req.params.id });
    } catch (e) {}
  }

  if (!order) {
    return res.status(404).json({ success: false, message: "Order not found" });
  }

  // Prevent marking DELIVERED without OTP verification
  if (status === "DELIVERED" && !order.isOtpVerified) {
    return res.status(400).json({
      success: false,
      message: "Order cannot be marked DELIVERED directly. OTP verification is required."
    });
  }

  if (status) order.status = status;

  if (adminNotes) {
    order.adminNotes = adminNotes;
  } else if (status === "PREPARING") {
    order.adminNotes = "Admin Update: Chef is cooking your fresh meal in the kitchen (Ban raha hai 👨‍🍳).";
  } else if (status === "OUT_FOR_DELIVERY") {
    order.adminNotes = `Admin Update: Food packed! Partner ${order.deliveryPartnerName} is en route (Raste mein hai 🛵). Share OTP ${order.deliveryOtp} upon arrival.`;
  }

  // Update in MongoDB
  try {
    await OrderModel.findOneAndUpdate({ id: order.id }, { status: order.status, adminNotes: order.adminNotes });
  } catch (e) {}

  // Post update message into chat
  if (!orderChats[order.id]) {
    orderChats[order.id] = [];
  }
  const statusMsg = {
    id: `msg_status_${Date.now()}`,
    orderId: order.id,
    sender: "admin",
    senderName: "Kitchen Manager",
    message: order.adminNotes,
    time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  };
  orderChats[order.id].push(statusMsg);
  try {
    await ChatModel.create(statusMsg);
  } catch (e) {}

  res.json({ success: true, message: "Order status updated by Admin", data: order });
});

// Verify Delivery OTP to complete delivery
router.post("/:id/verify-otp", async (req, res) => {
  const { otp } = req.body;
  let order = orders.find(o => o.id === req.params.id);

  if (!order) {
    try {
      order = await OrderModel.findOne({ id: req.params.id });
    } catch (e) {}
  }

  if (!order) {
    return res.status(404).json({ success: false, message: "Order not found" });
  }

  if (String(otp).trim() !== String(order.deliveryOtp)) {
    return res.status(400).json({
      success: false,
      message: "Invalid OTP! Delivery cannot be completed without correct 4-digit code."
    });
  }

  order.isOtpVerified = true;
  order.status = "DELIVERED";
  order.adminNotes = `Admin: Delivery verified and completed successfully with OTP ${order.deliveryOtp}. Enjoy your meal!`;

  // Update in MongoDB
  try {
    await OrderModel.findOneAndUpdate({ id: order.id }, { isOtpVerified: true, status: "DELIVERED", adminNotes: order.adminNotes });
  } catch (e) {}

  res.json({
    success: true,
    message: "OTP Verified! Order marked as DELIVERED.",
    data: order
  });
});

// Get Chat Messages for order
router.get("/:id/chat", async (req, res) => {
  const orderId = req.params.id;
  try {
    const dbChats = await ChatModel.find({ orderId }).sort({ createdAt: 1 });
    if (dbChats && dbChats.length > 0) {
      return res.json({ success: true, count: dbChats.length, data: dbChats });
    }
  } catch (e) {}

  const messages = orderChats[orderId] || [];
  res.json({ success: true, count: messages.length, data: messages });
});

// Send Chat Message to Admin/Restaurant
router.post("/:id/chat", async (req, res) => {
  const { senderName, sender } = req.body;
  const msgContent = String(req.body.message || req.body.text || "").trim();
  const orderId = req.params.id;

  let order = orders.find(o => o.id === orderId);
  if (!order) {
    try {
      order = await OrderModel.findOne({ id: orderId });
    } catch (e) {}
  }

  if (!order) {
    return res.status(404).json({ success: false, message: "Order not found" });
  }

  // CLOSE CHAT ONCE DELIVERED: Customer cannot send messages after delivery!
  if (order.status === "DELIVERED" && sender !== "admin") {
    return res.status(400).json({
      success: false,
      message: "Order has already been delivered. Chat is now closed."
    });
  }

  if (!msgContent) {
    return res.status(400).json({ success: false, message: "Message text cannot be empty" });
  }

  if (!orderChats[orderId]) {
    orderChats[orderId] = [];
  }

  const isSenderAdmin = sender === "admin";
  const newMsg = {
    id: `msg_${Date.now()}`,
    orderId,
    sender: isSenderAdmin ? "admin" : "user",
    senderName: senderName || (isSenderAdmin ? "Admin Support" : "Customer"),
    message: msgContent,
    time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  };

  orderChats[orderId].push(newMsg);
  try {
    await ChatModel.create(newMsg);
  } catch (e) {}

  res.status(201).json({
    success: true,
    data: { sentMessage: newMsg }
  });
});

export default router;
