import { Router } from "express";

const router = Router();

const notifications = [
  {
    id: "notif_welcome",
    orderId: "WELCOME",
    title: "Welcome to Tomato! 🍅",
    message: "Flat 50% discount on your first order. Use code TOMATO50 at checkout.",
    time: "Today",
    isRead: false
  }
];

router.get("/", (req, res) => {
  res.json({ success: true, count: notifications.length, data: notifications });
});

export default router;
