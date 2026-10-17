import { Router } from "express";
import { initialAddresses } from "../data/mockData.js";

const router = Router();
let addresses = [...initialAddresses];

// Get all saved addresses
router.get("/", (req, res) => {
  res.json({ success: true, count: addresses.length, data: addresses });
});

// Create new address
router.post("/", (req, res) => {
  const { label, houseNo, areaOrVillage, cityOrDistrict, state, pinCode, receiverName, receiverPhone, isActive } = req.body;

  if (!houseNo || !areaOrVillage || !pinCode) {
    return res.status(400).json({ success: false, message: "houseNo, areaOrVillage, and pinCode are required" });
  }

  if (isActive) {
    addresses = addresses.map(a => ({ ...a, isActive: false }));
  }

  const newAddress = {
    id: `addr_${Date.now()}`,
    label: label || "Home",
    houseNo,
    areaOrVillage,
    cityOrDistrict: cityOrDistrict || "Noida",
    state: state || "Delhi NCR",
    pinCode,
    receiverName: receiverName || "Kavi Rawat",
    receiverPhone: receiverPhone || "+91 98765 43210",
    isActive: isActive ?? true
  };

  addresses.unshift(newAddress);
  res.status(201).json({ success: true, data: newAddress });
});

// Set address as active delivery address
router.patch("/:id/active", (req, res) => {
  const address = addresses.find(a => a.id === req.params.id);
  if (!address) {
    return res.status(404).json({ success: false, message: "Address not found" });
  }

  addresses = addresses.map(a => ({
    ...a,
    isActive: a.id === req.params.id
  }));

  res.json({ success: true, message: "Address marked as active for delivery", data: { ...address, isActive: true } });
});

export default router;
