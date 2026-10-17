import { Router } from "express";
import { initialRestaurants } from "../data/mockData.js";

const router = Router();
let restaurants = [...initialRestaurants];

router.get("/", (req, res) => {
  const { query, vegOnly } = req.query;
  let filtered = [...restaurants];

  if (vegOnly === "true") {
    filtered = filtered.filter(r => r.isPureVeg);
  }

  if (query && query.trim()) {
    const q = query.trim().toLowerCase();
    filtered = filtered.filter(
      r => r.name.toLowerCase().includes(q) ||
           r.cuisines.some(c => c.toLowerCase().includes(q)) ||
           r.dishes.some(d => d.name.toLowerCase().includes(q))
    );
  }

  res.json({ success: true, count: filtered.length, data: filtered });
});

router.get("/:id", (req, res) => {
  const restaurant = restaurants.find(r => r.id === req.params.id);
  if (!restaurant) {
    return res.status(404).json({ success: false, message: "Restaurant not found" });
  }
  res.json({ success: true, data: restaurant });
});

// Add new dish / item to restaurant menu (Hotel / Restaurant partner action)
router.post("/:id/items", (req, res) => {
  const restaurant = restaurants.find(r => r.id === req.params.id);
  if (!restaurant) {
    return res.status(404).json({ success: false, message: "Restaurant not found" });
  }

  const { name, price, description, category, isVeg, imageEmoji, isBestseller } = req.body;
  if (!name || !name.trim()) {
    return res.status(400).json({ success: false, message: "Dish name is required" });
  }

  const newDish = {
    id: `dish_${Date.now()}_${Math.floor(100 + Math.random() * 900)}`,
    name: name.trim(),
    price: Number(price) || 150,
    originalPrice: Number(price) ? Number(price) + 40 : 190,
    description: description && description.trim() ? description.trim() : "Freshly prepared delicious item.",
    category: category && category.trim() ? category.trim() : "Specialties",
    isVeg: isVeg === undefined ? true : Boolean(isVeg),
    isBestseller: Boolean(isBestseller),
    rating: 4.8,
    ratingCount: 1,
    imageEmoji: imageEmoji || (Boolean(isVeg) ? "🥗" : "🍗")
  };

  if (!restaurant.dishes) {
    restaurant.dishes = [];
  }
  restaurant.dishes.unshift(newDish);

  console.log(`[Restaurant] New item '${newDish.name}' (₹${newDish.price}) added to ${restaurant.name}`);

  res.status(201).json({
    success: true,
    message: `New item '${newDish.name}' added successfully to ${restaurant.name}!`,
    data: newDish
  });
});

export default router;
