import mongoose from "mongoose";

export async function connectDB() {
  const uri = process.env.MONGODB_URI || "mongodb://127.0.0.1:27017/tomato_db";
  try {
    await mongoose.connect(uri, {
      dbName: "tomato_db",
      serverSelectionTimeoutMS: 8000
    });
    console.log(`🍃 Connected to MongoDB Atlas at: ${uri} (dbName: tomato_db)`);
    return true;
  } catch (err) {
    console.error("⚠️ MongoDB connection error:", err.message);
    console.log("ℹ️ Server running with MongoDB auto-reconnect fallback.");
    return false;
  }
}
