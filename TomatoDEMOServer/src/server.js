import express from "express";
import cors from "cors";
import dotenv from "dotenv";

import { connectDB } from "./config/db.js";
import restaurantRoutes from "./routes/restaurantRoutes.js";
import addressRoutes from "./routes/addressRoutes.js";
import orderRoutes from "./routes/orderRoutes.js";
import notificationRoutes from "./routes/notificationRoutes.js";
import authRoutes from "./routes/authRoutes.js";

dotenv.config();

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// Health check with DB status
app.get("/api/health", (req, res) => {
  res.json({
    status: "ok",
    service: "TomatoDEMOServer",
    timestamp: new Date().toISOString(),
    version: "1.0.0",
    database: "MongoDB"
  });
});

// Mount Routes
app.use("/api/auth", authRoutes);
app.use("/api/restaurants", restaurantRoutes);
app.use("/api/addresses", addressRoutes);
app.use("/api/orders", orderRoutes);
app.use("/api/notifications", notificationRoutes);

// Error Handler
app.use((err, req, res, next) => {
  console.error("Unhandled Error:", err);
  res.status(500).json({ success: false, message: "Internal Server Error" });
});

const HOST = process.env.HOST || "0.0.0.0";

// Connect to MongoDB and then start server
connectDB().then(() => {
  app.listen(PORT, HOST, () => {
    console.log(`🍅 TomatoDEMOServer running on http://127.0.0.1:${PORT} (Host: ${HOST})`);
    console.log(`- Health Check: http://127.0.0.1:${PORT}/api/health`);
    console.log(`- Auth (Phone): http://127.0.0.1:${PORT}/api/auth/send-otp`);
    console.log(`- Restaurants:  http://127.0.0.1:${PORT}/api/restaurants`);
    console.log(`- Addresses:    http://127.0.0.1:${PORT}/api/addresses`);
    console.log(`- Orders & OTP: http://127.0.0.1:${PORT}/api/orders`);
  });
});
