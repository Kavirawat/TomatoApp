export const initialRestaurants = [
  {
    id: "rest_bangla",
    name: "Bangla Sweet House",
    cuisines: ["Mithai", "Street Food", "North Indian"],
    rating: 4.4,
    ratingCount: "3.2k+",
    deliveryTimeMinutes: 45,
    distanceKm: 1.6,
    costForTwo: 200,
    costForOne: 100,
    offer: "₹140 OFF",
    isPureVeg: true,
    isPromoted: true,
    address: "Bangla Sahib Rd, Connaught Place, New Delhi",
    coverEmoji: "🥨",
    dishes: [
      {
        id: "bsh1",
        name: "Special Pure Desi Ghee Jalebi / Imarti (250g)",
        description: "Crispy, juicy authentic saffron-infused desi ghee spiral sweets served piping hot.",
        price: 140,
        originalPrice: 180,
        isVeg: true,
        rating: 4.8,
        ratingCount: 1200,
        category: "Sweets",
        isBestseller: true,
        imageEmoji: "🥨"
      },
      {
        id: "bsh2",
        name: "Chole Bhature Special Plate",
        description: "2 fluffy bhature with spiced Amritsari chole, spicy pickle, and seasoned onions.",
        price: 160,
        originalPrice: 190,
        isVeg: true,
        rating: 4.7,
        ratingCount: 890,
        category: "Chole Bhature",
        isBestseller: true,
        imageEmoji: "🥟"
      }
    ]
  },
  {
    id: "rest_saravana",
    name: "Saravana Bhavan",
    cuisines: ["South Indian", "Desserts", "Juices"],
    rating: 4.5,
    ratingCount: "5.8k+",
    deliveryTimeMinutes: 50,
    distanceKm: 2.4,
    costForTwo: 600,
    costForOne: 300,
    offer: "₹140 OFF",
    isPureVeg: true,
    isPromoted: false,
    address: "Janpath, Connaught Place, New Delhi",
    coverEmoji: "🥞",
    dishes: [
      {
        id: "sb1",
        name: "Special Masala Dosa",
        description: "Golden crisp dosa stuffed with spiced potato masala, served with 3 flavorful chutneys & piping hot sambar.",
        price: 185,
        originalPrice: 220,
        isVeg: true,
        rating: 4.7,
        ratingCount: 2100,
        category: "Dosa",
        isBestseller: true,
        imageEmoji: "🥞"
      },
      {
        id: "sb2",
        name: "Steamed Idli Sambar (2 Pcs)",
        description: "Soft fluffy steamed rice cakes dipped in signature spiced lentil sambar and coconut chutney.",
        price: 110,
        isVeg: true,
        rating: 4.6,
        ratingCount: 1450,
        category: "Idli",
        isBestseller: true,
        imageEmoji: "🥟"
      }
    ]
  },
  {
    id: "rest_kaleva",
    name: "Kaleva",
    cuisines: ["Mithai", "Street Food", "North Indian"],
    rating: 4.5,
    ratingCount: "4.1k+",
    deliveryTimeMinutes: 45,
    distanceKm: 1.9,
    costForTwo: 400,
    costForOne: 200,
    offer: "50% OFF",
    isPureVeg: true,
    isPromoted: true,
    address: "Gole Market, New Delhi",
    coverEmoji: "🍡",
    dishes: [
      {
        id: "kl1",
        name: "Assorted Festive Mithai Thali",
        description: "Rich collection of traditional Chandrakala, Gujiya, Motichoor and Khoya sweets.",
        price: 320,
        originalPrice: 400,
        isVeg: true,
        rating: 4.8,
        ratingCount: 980,
        category: "Sweets",
        isBestseller: true,
        imageEmoji: "🍡"
      }
    ]
  },
  {
    id: "rest_sagar_ratna",
    name: "Sagar Ratna",
    cuisines: ["South Indian", "Chinese", "Fast Food"],
    rating: 4.3,
    ratingCount: "2.9k+",
    deliveryTimeMinutes: 30,
    distanceKm: 2.8,
    costForTwo: 500,
    costForOne: 250,
    offer: "30% OFF",
    isPureVeg: true,
    isPromoted: true,
    address: "K-Block, Connaught Place, New Delhi",
    coverEmoji: "🥞",
    dishes: [
      {
        id: "sr1",
        name: "Mysore Masala Crispy Dosa",
        description: "Crispy red chili garlic paste smeared dosa filled with spiced potato mash, served with rich sambar.",
        price: 195,
        originalPrice: 240,
        isVeg: true,
        rating: 4.6,
        ratingCount: 1100,
        category: "Dosa",
        isBestseller: true,
        imageEmoji: "🥞"
      }
    ]
  },
  {
    id: "rest_bikanervala",
    name: "Bikanervala",
    cuisines: ["Mithai", "Street Food", "Fast Food"],
    rating: 4.2,
    ratingCount: "3.5k+",
    deliveryTimeMinutes: 40,
    distanceKm: 3.1,
    costForTwo: 360,
    costForOne: 180,
    offer: "₹150 OFF",
    isPureVeg: true,
    isPromoted: true,
    address: "Barakhamba Road, New Delhi",
    coverEmoji: "🟡",
    dishes: [
      {
        id: "bkv1",
        name: "Motichoor Desi Ghee Ladoo (500g)",
        description: "Melt-in-mouth tiny gram flour droplets fried in pure desi ghee and simmered in saffron syrup.",
        price: 260,
        originalPrice: 310,
        isVeg: true,
        rating: 4.8,
        ratingCount: 1600,
        category: "Sweets",
        isBestseller: true,
        imageEmoji: "🟡"
      }
    ]
  },
  {
    id: "rest_1",
    name: "Haldiram's Sweets & Meals",
    cuisines: ["North Indian", "Sweets", "Street Food", "Snacks"],
    rating: 4.4,
    ratingCount: "3.8k+",
    deliveryTimeMinutes: 25,
    distanceKm: 2.1,
    costForTwo: 350,
    offer: "50% OFF up to ₹100",
    isPureVeg: true,
    isPromoted: true,
    address: "Sector 18, Central Plaza, Noida",
    coverEmoji: "🍲",
    dishes: [
      {
        id: "h1",
        name: "Special Chole Bhature (2 Pcs)",
        description: "Fluffy fresh bhature served with rich spicy pindi chole, pickled onion and mint chutney.",
        price: 175,
        originalPrice: 210,
        isVeg: true,
        rating: 4.6,
        ratingCount: 890,
        category: "North Indian",
        isBestseller: true,
        imageEmoji: "🥟"
      },
      {
        id: "h2",
        name: "Paneer Butter Masala Combo",
        description: "Cottage cheese cubes simmered in creamy makhani gravy, served with 2 Butter Naan.",
        price: 249,
        originalPrice: 289,
        isVeg: true,
        rating: 4.5,
        ratingCount: 612,
        category: "North Indian",
        isBestseller: true,
        imageEmoji: "🥘"
      },
      {
        id: "h3",
        name: "Raj Kachori Chaat",
        description: "Crispy hollow pastry stuffed with potatoes, sprout lentils, sweet curd and spicy-tangy chutneys.",
        price: 140,
        isVeg: true,
        rating: 4.7,
        ratingCount: 1240,
        category: "Street Food",
        isBestseller: true,
        imageEmoji: "🥗"
      }
    ]
  },
  {
    id: "rest_2",
    name: "Behrouz Biryani - Royal Kitchens",
    cuisines: ["Biryani", "Mughlai", "Kebabs", "Desserts"],
    rating: 4.5,
    ratingCount: "5.1k+",
    deliveryTimeMinutes: 30,
    distanceKm: 3.4,
    costForTwo: 550,
    offer: "₹125 OFF on ₹299+",
    isPureVeg: false,
    isPromoted: true,
    address: "Royal Towers, Golf Course Road, Gurugram",
    coverEmoji: "🍚",
    dishes: [
      {
        id: "b1",
        name: "Dum Gosht Biryani (Mutton)",
        description: "Succulent mutton pieces marinated in royal spices, layered with fragrant basmati rice, served with raita.",
        price: 449,
        originalPrice: 520,
        isVeg: false,
        rating: 4.7,
        ratingCount: 1520,
        category: "Biryani",
        isBestseller: true,
        imageEmoji: "🍗"
      },
      {
        id: "b2",
        name: "Subz-e-Biryani (Veg Dum)",
        description: "Fresh farm garden veggies cooked with aromatic royal herbs and saffron infused basmati rice.",
        price: 299,
        isVeg: true,
        rating: 4.3,
        ratingCount: 890,
        category: "Biryani",
        imageEmoji: "🍚"
      }
    ]
  },
  {
    id: "rest_3",
    name: "Domino's Pizza & More",
    cuisines: ["Pizzas", "Fast Food", "Garlic Bread", "Pasta"],
    rating: 4.2,
    ratingCount: "8.4k+",
    deliveryTimeMinutes: 20,
    distanceKm: 1.8,
    costForTwo: 400,
    offer: "Free Delivery + 40% OFF",
    isPureVeg: false,
    isPromoted: false,
    address: "Metro Mall, Ground Floor, Delhi NCR",
    coverEmoji: "🍕",
    dishes: [
      {
        id: "p1",
        name: "Farmhouse Cheese Burst Pizza",
        description: "Delightful combination of onion, capsicum, tomato and grilled mushroom with molten cheese liquid core.",
        price: 369,
        originalPrice: 429,
        isVeg: true,
        rating: 4.5,
        ratingCount: 2890,
        category: "Pizza",
        isBestseller: true,
        imageEmoji: "🍕"
      },
      {
        id: "p2",
        name: "Stuffed Garlic Breadsticks",
        description: "Freshly baked garlic breadsticks filled with jalapeno and gooey melted mozzarella cheese.",
        price: 159,
        isVeg: true,
        rating: 4.6,
        ratingCount: 3400,
        category: "Sides",
        isBestseller: true,
        imageEmoji: "🥖"
      }
    ]
  }
];

export const initialAddresses = [
  {
    id: "addr_1",
    label: "Home",
    houseNo: "Flat 402, Block B, Green Heights",
    areaOrVillage: "Sector 62",
    cityOrDistrict: "Noida",
    state: "Delhi NCR",
    pinCode: "201309",
    receiverName: "Kavi Rawat",
    receiverPhone: "+91 98765 43210",
    isActive: true
  },
  {
    id: "addr_2",
    label: "Work",
    houseNo: "Tower B, 5th Floor, Cyber City",
    areaOrVillage: "DLF Phase 2",
    cityOrDistrict: "Gurugram",
    state: "Delhi NCR",
    pinCode: "122002",
    receiverName: "Kavi Rawat (Work)",
    receiverPhone: "+91 98765 43210",
    isActive: false
  }
];
