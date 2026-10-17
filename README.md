# 🍅 TomatoApp - Complete Food Delivery Ecosystem

A modern, high-performance food delivery and quick-commerce ecosystem inspired by **Zomato**, **Blinkit**, and **Swiggy**. Built with **Kotlin Multiplatform (Compose Multiplatform)** for cross-platform mobile apps, **React + Vite** for the real-time Restaurant Admin Dashboard, and **Node.js + Express** for the robust backend API.

---

## 📦 Project Architecture

```
TomatoApp/
├── 📱 TomatoDEMOApp/        # Android & iOS App (Kotlin Multiplatform / Compose Multiplatform)
├── 💻 TomatoDEMOAdmin/      # Restaurant & Delivery Partner Admin Dashboard (React + Vite)
├── ⚡ TomatoDEMOServer/     # Backend REST API Server (Node.js + Express)
├── 🚀 start-admin.bat       # Quick launcher for Admin Dashboard
├── 🚀 start-backend.bat     # Quick launcher for Backend Server
└── 📄 README.md             # Project documentation
```

---

## ✨ Key Features

### 📱 1. Mobile App (`TomatoDEMOApp`)
- **Full Kotlin Multiplatform**: Single shared Compose codebase for Android and iOS.
- **Zomato / Blinkit UI**: Modern hero banners, animated food category carousels, dynamic search and filters.
- **Original HD Food Photography**: Full gallery of authentic dishes (Biryani, Paneer, Pizza, Burger, Dosa, etc.) with dietary badges (Veg / Non-Veg).
- **Restaurant Menu & Custom Dishes**: Browse popular dishes or add custom items directly to restaurant menus with original photos.
- **Cart & Dynamic Discount Coupons**: Auto-applying promotional coupons, delivery fees calculation, free delivery thresholds.
- **Location & Address Selector**: Saved address management with FontAwesome pin icons and instant location search.
- **Live Order Tracking**: Real-time status pipeline (`Placed` -> `Preparing` -> `Out for Delivery` -> `Delivered`), delivery partner info, direct chat, and OTP verification.
- **Full Dark Mode Support**: Dynamic theme switching across all screens and components.

### 💻 2. Admin Dashboard (`TomatoDEMOAdmin`)
- **Live Order Management**: Real-time incoming order stream with sound alerts and small order ID chips.
- **Order Pipeline**: One-click actions to advance orders through kitchen preparation and delivery stages.
- **Live Kitchen Simulator**: Simulate new incoming customer orders with random cuisines and addresses.
- **Direct Chat & Support**: Real-time communication between customers, restaurant partners, and delivery drivers.
- **Modern Dark & Light UI**: Responsive dashboard with FontAwesome 5 icons and status metrics.

### ⚡ 3. Backend API Server (`TomatoDEMOServer`)
- **Express.js Architecture**: RESTful endpoints for restaurants, menu items, orders, and authentication.
- **Phone OTP Authentication**: Twilio SMS integration with instant OTP verification.
- **Live In-Memory & MongoDB Sync**: Fast performance with robust fallback handling.

---

## 🚀 Getting Started

### Prerequisites
- **Java JDK 17+** & **Android Studio** (for Mobile App)
- **Node.js 18+** & **npm** (for Backend and Admin)

---

### 1. Start the Backend Server
```bash
cd TomatoDEMOServer
npm install
npm start
```
*Backend runs at: `http://127.0.0.1:5000`*

### 2. Start the Admin Dashboard
```bash
cd TomatoDEMOAdmin
npm install
npm run dev
```
*Admin dashboard runs at: `http://localhost:5173`*

### 3. Run the Mobile App
```bash
cd TomatoDEMOApp
# Run Android App via Gradle wrapper
./gradlew :androidApp:installDebug
```
*Or open `TomatoDEMOApp` directly in Android Studio and click **Run**.*

---

## 🛠️ Tech Stack

- **Mobile App**: Kotlin Multiplatform, Compose Multiplatform, Material 3, Coroutines, Compose Resources
- **Admin Dashboard**: React 18, Vite, Tailwind CSS / Modern CSS, React Icons (FontAwesome 5)
- **Backend**: Node.js, Express, Cors, Dotenv, Twilio SDK
- **Architecture**: Monorepo with independent runnable modules

---

## 📄 License
This project is for demonstration and portfolio purposes.
