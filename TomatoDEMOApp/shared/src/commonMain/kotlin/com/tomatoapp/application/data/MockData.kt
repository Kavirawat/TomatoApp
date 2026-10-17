package com.tomatoapp.application.data

import com.tomatoapp.application.model.Dish
import com.tomatoapp.application.model.FoodCategory
import com.tomatoapp.application.model.Restaurant
import org.jetbrains.compose.resources.DrawableResource
import tomatoapp.shared.generated.resources.*

object MockData {

    val categories = listOf(
        FoodCategory("burger", "Burger", "🍔", Res.drawable.cat_burger),
        FoodCategory("drinks", "Drinks", "🍹", Res.drawable.cat_sandwich),
        FoodCategory("snacks", "Snacks", "🍟", Res.drawable.cat_paratha),
        FoodCategory("pizza", "Pizza", "🍕", Res.drawable.dish_pizza),
        FoodCategory("indian", "Indian", "🥘", Res.drawable.dish_paneer),
        FoodCategory("biryani", "Biryani", "🍚", Res.drawable.dish_biryani),
        FoodCategory("dosa", "Dosa", "🥞", Res.drawable.cat_dosa),
        FoodCategory("chole_bhature", "Chole Bhature", "🥟", Res.drawable.cat_chole_bhature),
        FoodCategory("sandwich", "Sandwich", "🥪", Res.drawable.cat_sandwich),
        FoodCategory("sweets", "Sweets", "🍧", Res.drawable.dish_gulab_jamun)
    )

    val promotionalBanners = listOf(
        PromoBanner(
            id = "b1",
            title = "Today's Best Deals",
            subtitle = "up to 75% Off",
            code = "BESTDEALS",
            tag = "Today's Deal",
            imageRes = Res.drawable.banner_pizza
        ),
        PromoBanner(
            id = "b2",
            title = "Weekly Deals",
            subtitle = "up to 75% Off",
            code = "WEEKLY75",
            tag = "Weekly Deal",
            imageRes = Res.drawable.banner_burger
        ),
        PromoBanner(
            id = "b3",
            title = "TOMATO GOLD VIP",
            subtitle = "Free delivery & extra 20% off all orders",
            code = "GOLDDEL",
            tag = "Gold Benefit",
            imageRes = Res.drawable.banner_gold
        ),
        PromoBanner(
            id = "b4",
            title = "BURGER & COMBO FEST",
            subtitle = "Juicy gourmet burgers with crispy sides",
            code = "BURGER40",
            tag = "Weekend Feast",
            imageRes = Res.drawable.banner_burger
        ),
        PromoBanner(
            id = "b5",
            title = "ROYAL THALI SPECIAL",
            subtitle = "Complete Indian feast with sweets & chaat",
            code = "WELCOME50",
            tag = "Chef's Special",
            imageRes = Res.drawable.banner_thali
        ),
        PromoBanner(
            id = "b6",
            title = "SWEET DELIGHTS",
            subtitle = "Gulab Jamun, Rasgulla & premium desserts",
            code = "SWEETS50",
            tag = "Dessert Bar",
            imageRes = Res.drawable.banner_dessert
        )
    )

    val restaurants = listOf(
        Restaurant(
            id = "rest_raj_palace",
            name = "Tomato Kitchen & Cafe",
            cuisines = listOf("Chinese", "North Indian"),
            rating = 4.5,
            ratingCount = "12k+",
            deliveryTimeMinutes = 20,
            distanceKm = 1.2,
            costForTwo = 400,
            costForOne = 200,
            deliveryTimeRange = "20 mins",
            offer = "50% OFF",
            isPureVeg = false,
            isPromoted = true,
            address = "Madhapur, Hyderabad (500081)",
            coverEmoji = "🍛",
            coverImageRes = Res.drawable.banner_pizza,
            dishes = listOf(
                Dish(
                    id = "rp_veg_sandwich",
                    name = "Veg Sandwich",
                    description = "Fresh toasted sandwich loaded with crunchy veggies, green chutney and cheese.",
                    price = 98.0,
                    originalPrice = 120.0,
                    isVeg = true,
                    rating = 4.6,
                    ratingCount = 1420,
                    category = "Popular",
                    isBestseller = true,
                    imageEmoji = "🥪",
                    imageRes = Res.drawable.cat_sandwich
                ),
                Dish(
                    id = "rp_fried_rice",
                    name = "Fried Rice",
                    description = "Aromatic wok-tossed fried rice with spring onions and oriental vegetables.",
                    price = 98.0,
                    originalPrice = 120.0,
                    isVeg = true,
                    rating = 4.5,
                    ratingCount = 980,
                    category = "Popular",
                    isBestseller = true,
                    imageEmoji = "🍚",
                    imageRes = Res.drawable.dish_biryani
                ),
                Dish(
                    id = "rp_baked_deserts",
                    name = "Baked Deserts",
                    description = "Delectable warm baked dessert with rich vanilla and chocolate fusion.",
                    price = 80.0,
                    originalPrice = 100.0,
                    isVeg = true,
                    rating = 4.7,
                    ratingCount = 850,
                    category = "Desserts",
                    isBestseller = true,
                    imageEmoji = "🧁",
                    imageRes = Res.drawable.dish_gulab_jamun
                )
            )
        ),
        Restaurant(
            id = "rest_bangla",
            name = "Bangla Sweet House",
            cuisines = listOf("Mithai", "Street Food", "North Indian"),
            rating = 4.4,
            ratingCount = "3.2k+",
            deliveryTimeMinutes = 45,
            distanceKm = 1.6,
            costForTwo = 200,
            costForOne = 100,
            deliveryTimeRange = "45-50 min",
            offer = "₹140 OFF",
            isPureVeg = true,
            isPromoted = true,
            address = "Bangla Sahib Rd, Connaught Place, New Delhi",
            coverEmoji = "🥨",
            coverImageRes = Res.drawable.rest_bangla_sweet,
            dishes = listOf(
                Dish(
                    id = "bsh1",
                    name = "Special Pure Desi Ghee Jalebi / Imarti (250g)",
                    description = "Crispy, juicy authentic saffron-infused desi ghee spiral sweets served piping hot.",
                    price = 140.0,
                    originalPrice = 180.0,
                    isVeg = true,
                    rating = 4.8,
                    ratingCount = 1200,
                    category = "Sweets",
                    isBestseller = true,
                    imageEmoji = "🥨",
                    imageRes = Res.drawable.rest_bangla_sweet
                ),
                Dish(
                    id = "bsh2",
                    name = "Chole Bhature Special Plate",
                    description = "2 fluffy bhature with spiced Amritsari chole, spicy pickle, and seasoned onions.",
                    price = 160.0,
                    originalPrice = 190.0,
                    isVeg = true,
                    rating = 4.7,
                    ratingCount = 890,
                    category = "Chole Bhature",
                    isBestseller = true,
                    imageEmoji = "🥟",
                    imageRes = Res.drawable.cat_chole_bhature
                ),
                Dish(
                    id = "bsh3",
                    name = "Kaju Katli (250g Box)",
                    description = "Diamond cut cashew nut fudge made with premium cashews and silver vark.",
                    price = 280.0,
                    isVeg = true,
                    rating = 4.9,
                    ratingCount = 650,
                    category = "Sweets",
                    isBestseller = true,
                    imageEmoji = "🍬",
                    imageRes = Res.drawable.dish_gulab_jamun
                )
            )
        ),
        Restaurant(
            id = "rest_saravana",
            name = "Saravana Bhavan",
            cuisines = listOf("South Indian", "Desserts", "Juices"),
            rating = 4.5,
            ratingCount = "5.8k+",
            deliveryTimeMinutes = 50,
            distanceKm = 2.4,
            costForTwo = 600,
            costForOne = 300,
            deliveryTimeRange = "50-55 min",
            offer = "₹140 OFF",
            isPureVeg = true,
            isPromoted = false,
            address = "Janpath, Connaught Place, New Delhi",
            coverEmoji = "🥞",
            coverImageRes = Res.drawable.rest_saravana_bhavan,
            dishes = listOf(
                Dish(
                    id = "sb1",
                    name = "Special Masala Dosa",
                    description = "Golden crisp dosa stuffed with spiced potato masala, served with 3 flavorful chutneys & piping hot sambar.",
                    price = 185.0,
                    originalPrice = 220.0,
                    isVeg = true,
                    rating = 4.7,
                    ratingCount = 2100,
                    category = "Dosa",
                    isBestseller = true,
                    imageEmoji = "🥞",
                    imageRes = Res.drawable.cat_dosa
                ),
                Dish(
                    id = "sb2",
                    name = "Steamed Idli Sambar (2 Pcs)",
                    description = "Soft fluffy steamed rice cakes dipped in signature spiced lentil sambar and coconut chutney.",
                    price = 110.0,
                    isVeg = true,
                    rating = 4.6,
                    ratingCount = 1450,
                    category = "Idli",
                    isBestseller = true,
                    imageEmoji = "🥟",
                    imageRes = Res.drawable.cat_idli
                ),
                Dish(
                    id = "sb3",
                    name = "Rava Onion Masala Dosa",
                    description = "Semolina crisp crepe tossed with crushed peppercorns, roasted onions, green chilies and butter.",
                    price = 210.0,
                    isVeg = true,
                    rating = 4.5,
                    ratingCount = 890,
                    category = "Dosa",
                    isBestseller = false,
                    imageEmoji = "🥞",
                    imageRes = Res.drawable.dish_dosa
                )
            )
        ),
        Restaurant(
            id = "rest_kaleva",
            name = "Kaleva",
            cuisines = listOf("Mithai", "Street Food", "North Indian"),
            rating = 4.5,
            ratingCount = "4.1k+",
            deliveryTimeMinutes = 45,
            distanceKm = 1.9,
            costForTwo = 400,
            costForOne = 200,
            deliveryTimeRange = "45-50 min",
            offer = "50% OFF",
            isPureVeg = true,
            isPromoted = true,
            address = "Gole Market, New Delhi",
            coverEmoji = "🍡",
            coverImageRes = Res.drawable.rest_kaleva,
            dishes = listOf(
                Dish(
                    id = "kl1",
                    name = "Assorted Festive Mithai Thali",
                    description = "Rich collection of traditional Chandrakala, Gujiya, Motichoor and Khoya sweets.",
                    price = 320.0,
                    originalPrice = 400.0,
                    isVeg = true,
                    rating = 4.8,
                    ratingCount = 980,
                    category = "Sweets",
                    isBestseller = true,
                    imageEmoji = "🍡",
                    imageRes = Res.drawable.rest_kaleva
                ),
                Dish(
                    id = "kl2",
                    name = "Dahi Bhalla Papdi Chaat",
                    description = "Soft lentil dumplings soaked in sweet beaten yogurt, sprinkled with roasted cumin, pomegranate and chutney.",
                    price = 130.0,
                    isVeg = true,
                    rating = 4.7,
                    ratingCount = 1120,
                    category = "Street Food",
                    isBestseller = true,
                    imageEmoji = "🥗",
                    imageRes = Res.drawable.dish_chaat
                ),
                Dish(
                    id = "kl3",
                    name = "Aloo Tikki Burger & Fries",
                    description = "Crispy golden spiced potato cutlet in toasted sesame buns with tangy mint mayo.",
                    price = 119.0,
                    isVeg = true,
                    rating = 4.4,
                    ratingCount = 540,
                    category = "Burger",
                    imageEmoji = "🍔",
                    imageRes = Res.drawable.cat_burger
                )
            )
        ),
        Restaurant(
            id = "rest_haldirams_delhi",
            name = "Haldiram's",
            cuisines = listOf("North Indian", "Sweets", "Street Food"),
            rating = 4.4,
            ratingCount = "6.5k+",
            deliveryTimeMinutes = 35,
            distanceKm = 2.2,
            costForTwo = 300,
            costForOne = 150,
            deliveryTimeRange = "35-40 min",
            offer = "50% OFF",
            isPureVeg = true,
            isPromoted = false,
            address = "Chandni Chowk / Connaught Place, New Delhi",
            coverEmoji = "🍱",
            coverImageRes = Res.drawable.rest_haldirams_delhi,
            dishes = listOf(
                Dish(
                    id = "hd1",
                    name = "Deluxe North Indian Thali",
                    description = "Dal Makhani, Shahi Paneer, Pulao, 2 Laccha Parathas, Gulab Jamun, Raita and Salad.",
                    price = 285.0,
                    originalPrice = 350.0,
                    isVeg = true,
                    rating = 4.7,
                    ratingCount = 2800,
                    category = "North Indian",
                    isBestseller = true,
                    imageEmoji = "🍱",
                    imageRes = Res.drawable.rest_haldirams_delhi
                ),
                Dish(
                    id = "hd2",
                    name = "Aloo Pyaaz Stuffed Paratha (2 Pcs)",
                    description = "Tandoor roasted whole wheat parathas stuffed with spiced potatoes and onions, served with curd & butter.",
                    price = 165.0,
                    isVeg = true,
                    rating = 4.6,
                    ratingCount = 1200,
                    category = "Paratha",
                    isBestseller = true,
                    imageEmoji = "🫓",
                    imageRes = Res.drawable.cat_paratha
                ),
                Dish(
                    id = "hd3",
                    name = "Grilled Bombay Club Sandwich",
                    description = "Triple-layered toasted bread packed with mint chutney, cucumber, tomatoes, beet and cheese.",
                    price = 145.0,
                    isVeg = true,
                    rating = 4.5,
                    ratingCount = 760,
                    category = "Sandwich",
                    isBestseller = true,
                    imageEmoji = "🥪",
                    imageRes = Res.drawable.cat_sandwich
                )
            )
        ),
        Restaurant(
            id = "rest_sagar_ratna",
            name = "Sagar Ratna",
            cuisines = listOf("South Indian", "Chinese", "Fast Food"),
            rating = 4.3,
            ratingCount = "2.9k+",
            deliveryTimeMinutes = 30,
            distanceKm = 2.8,
            costForTwo = 500,
            costForOne = 250,
            deliveryTimeRange = "30-35 min",
            offer = "30% OFF",
            isPureVeg = true,
            isPromoted = true,
            address = "K-Block, Connaught Place, New Delhi",
            coverEmoji = "🥞",
            coverImageRes = Res.drawable.rest_sagar_ratna,
            dishes = listOf(
                Dish(
                    id = "sr1",
                    name = "Mysore Masala Crispy Dosa",
                    description = "Crispy red chili garlic paste smeared dosa filled with spiced potato mash, served with rich sambar.",
                    price = 195.0,
                    originalPrice = 240.0,
                    isVeg = true,
                    rating = 4.6,
                    ratingCount = 1100,
                    category = "Dosa",
                    isBestseller = true,
                    imageEmoji = "🥞",
                    imageRes = Res.drawable.rest_sagar_ratna
                ),
                Dish(
                    id = "sr2",
                    name = "Medu Vada Plate (2 Pcs)",
                    description = "Deep fried savory black gram donuts, crunchy on outside and soft inside, with coconut chutney.",
                    price = 125.0,
                    isVeg = true,
                    rating = 4.5,
                    ratingCount = 820,
                    category = "South Indian",
                    imageEmoji = "🍩",
                    imageRes = Res.drawable.cat_idli
                )
            )
        ),
        Restaurant(
            id = "rest_bikanervala",
            name = "Bikanervala",
            cuisines = listOf("Mithai", "Street Food", "Fast Food"),
            rating = 4.2,
            ratingCount = "3.5k+",
            deliveryTimeMinutes = 40,
            distanceKm = 3.1,
            costForTwo = 360,
            costForOne = 180,
            deliveryTimeRange = "40-45 min",
            offer = "₹150 OFF",
            isPureVeg = true,
            isPromoted = true,
            address = "Barakhamba Road, New Delhi",
            coverEmoji = "🟡",
            coverImageRes = Res.drawable.rest_bikanervala,
            dishes = listOf(
                Dish(
                    id = "bkv1",
                    name = "Motichoor Desi Ghee Ladoo (500g)",
                    description = "Melt-in-mouth tiny gram flour droplets fried in pure desi ghee and simmered in saffron syrup.",
                    price = 260.0,
                    originalPrice = 310.0,
                    isVeg = true,
                    rating = 4.8,
                    ratingCount = 1600,
                    category = "Sweets",
                    isBestseller = true,
                    imageEmoji = "🟡",
                    imageRes = Res.drawable.rest_bikanervala
                ),
                Dish(
                    id = "bkv2",
                    name = "Paneer Tikka Stuffed Kulcha",
                    description = "Oven baked soft leavened bread stuffed with marinated cottage cheese, served with spicy chole.",
                    price = 175.0,
                    isVeg = true,
                    rating = 4.4,
                    ratingCount = 780,
                    category = "North Indian",
                    isBestseller = true,
                    imageEmoji = "🫓",
                    imageRes = Res.drawable.dish_paneer
                )
            )
        ),
        Restaurant(
            id = "rest_1",
            name = "Haldiram's Sweets & Meals",
            cuisines = listOf("North Indian", "Sweets", "Street Food", "Snacks"),
            rating = 4.4,
            ratingCount = "3.8k+",
            deliveryTimeMinutes = 25,
            distanceKm = 2.1,
            costForTwo = 350,
            offer = "50% OFF up to ₹100",
            isPureVeg = true,
            isPromoted = true,
            address = "Sector 18, Central Plaza",
            coverEmoji = "🍲",
            coverImageRes = Res.drawable.banner_thali,
            dishes = listOf(
                Dish(
                    id = "h1",
                    name = "Special Chole Bhature (2 Pcs)",
                    description = "Fluffy fresh bhature served with rich spicy pindi chole, pickled onion and mint chutney.",
                    price = 175.0,
                    originalPrice = 210.0,
                    isVeg = true,
                    rating = 4.6,
                    ratingCount = 890,
                    category = "North Indian",
                    isBestseller = true,
                    imageEmoji = "🥟",
                    imageRes = Res.drawable.dish_chole_bhature
                ),
                Dish(
                    id = "h2",
                    name = "Paneer Butter Masala Combo",
                    description = "Cottage cheese cubes simmered in creamy makhani gravy, served with 2 Butter Naan.",
                    price = 249.0,
                    originalPrice = 289.0,
                    isVeg = true,
                    rating = 4.5,
                    ratingCount = 612,
                    category = "North Indian",
                    isBestseller = true,
                    imageEmoji = "🥘",
                    imageRes = Res.drawable.dish_paneer
                ),
                Dish(
                    id = "h3",
                    name = "Raj Kachori Chaat",
                    description = "Crispy hollow pastry stuffed with potatoes, sprout lentils, sweet curd and spicy-tangy chutneys.",
                    price = 140.0,
                    isVeg = true,
                    rating = 4.7,
                    ratingCount = 1240,
                    category = "Street Food",
                    isBestseller = true,
                    imageEmoji = "🥗",
                    imageRes = Res.drawable.dish_chaat
                ),
                Dish(
                    id = "h4",
                    name = "Gulab Jamun (2 Pcs)",
                    description = "Warm melt-in-mouth milk solids dumplings soaked in cardamom saffron sugar syrup.",
                    price = 90.0,
                    isVeg = true,
                    rating = 4.8,
                    ratingCount = 420,
                    category = "Sweets",
                    imageEmoji = "🍧",
                    imageRes = Res.drawable.dish_gulab_jamun
                ),
                Dish(
                    id = "h5",
                    name = "Pav Bhaji Deluxe",
                    description = "Spicy mashed vegetable curry topped with generous butter, served with 2 toasted pavs.",
                    price = 160.0,
                    isVeg = true,
                    rating = 4.3,
                    ratingCount = 310,
                    category = "Street Food",
                    imageEmoji = "🍞",
                    imageRes = Res.drawable.dish_chaat
                )
            )
        ),
        Restaurant(
            id = "rest_2",
            name = "Behrouz Biryani - Royal Kitchens",
            cuisines = listOf("Biryani", "Mughlai", "Kebabs", "Desserts"),
            rating = 4.5,
            ratingCount = "5.1k+",
            deliveryTimeMinutes = 30,
            distanceKm = 3.4,
            costForTwo = 550,
            offer = "₹125 OFF on ₹299+",
            isPureVeg = false,
            isPromoted = true,
            address = "Royal Towers, Golf Course Road",
            coverEmoji = "🍚",
            coverImageRes = Res.drawable.banner_biryani,
            dishes = listOf(
                Dish(
                    id = "b1",
                    name = "Dum Gosht Biryani (Mutton)",
                    description = "Succulent mutton pieces marinated in royal spices, layered with fragrant basmati rice, served with raita.",
                    price = 449.0,
                    originalPrice = 520.0,
                    isVeg = false,
                    rating = 4.7,
                    ratingCount = 1520,
                    category = "Biryani",
                    isBestseller = true,
                    imageEmoji = "🍗",
                    imageRes = Res.drawable.dish_biryani
                ),
                Dish(
                    id = "b2",
                    name = "Subz-e-Biryani (Veg Dum)",
                    description = "Fresh farm garden veggies cooked with aromatic royal herbs and saffron infused basmati rice.",
                    price = 299.0,
                    originalPrice = 350.0,
                    isVeg = true,
                    rating = 4.3,
                    ratingCount = 890,
                    category = "Biryani",
                    isBestseller = false,
                    imageEmoji = "🍚",
                    imageRes = Res.drawable.dish_biryani
                ),
                Dish(
                    id = "b3",
                    name = "Murgh Tikka Kebab (6 Pcs)",
                    description = "Tender boneless chicken marinated in tandoori spices and char-grilled to perfection.",
                    price = 320.0,
                    isVeg = false,
                    rating = 4.6,
                    ratingCount = 740,
                    category = "Kebabs",
                    isBestseller = true,
                    imageEmoji = "🍢",
                    imageRes = Res.drawable.dish_biryani
                ),
                Dish(
                    id = "b4",
                    name = "Shahi Tukda with Rabri",
                    description = "Crispy golden fried bread soaked in saffron syrup and smothered in thick creamy malai rabri.",
                    price = 135.0,
                    isVeg = true,
                    rating = 4.8,
                    ratingCount = 510,
                    category = "Desserts",
                    imageEmoji = "🍮",
                    imageRes = Res.drawable.dish_gulab_jamun
                )
            )
        ),
        Restaurant(
            id = "rest_3",
            name = "Domino's Pizza & More",
            cuisines = listOf("Pizzas", "Fast Food", "Garlic Bread", "Pasta"),
            rating = 4.2,
            ratingCount = "8.4k+",
            deliveryTimeMinutes = 20,
            distanceKm = 1.8,
            costForTwo = 400,
            offer = "Free Delivery + 40% OFF",
            isPureVeg = false,
            isPromoted = false,
            address = "Metro Mall, Ground Floor",
            coverEmoji = "🍕",
            coverImageRes = Res.drawable.banner_pizza,
            dishes = listOf(
                Dish(
                    id = "p1",
                    name = "Farmhouse Cheese Burst Pizza",
                    description = "Delightful combination of onion, capsicum, tomato and grilled mushroom with molten cheese liquid core.",
                    price = 369.0,
                    originalPrice = 429.0,
                    isVeg = true,
                    rating = 4.5,
                    ratingCount = 2890,
                    category = "Pizza",
                    isBestseller = true,
                    imageEmoji = "🍕",
                    imageRes = Res.drawable.dish_pizza
                ),
                Dish(
                    id = "p2",
                    name = "Pepper Barbecue Chicken Pizza",
                    description = "Pepper barbecue chicken for that flavorful kick on fresh hand-tossed crust with mozzarella.",
                    price = 399.0,
                    isVeg = false,
                    rating = 4.4,
                    ratingCount = 2100,
                    category = "Pizza",
                    isBestseller = true,
                    imageEmoji = "🍕",
                    imageRes = Res.drawable.dish_pizza
                ),
                Dish(
                    id = "p3",
                    name = "Stuffed Garlic Breadsticks",
                    description = "Freshly baked garlic breadsticks filled with jalapeno and gooey melted mozzarella cheese.",
                    price = 159.0,
                    isVeg = true,
                    rating = 4.6,
                    ratingCount = 3400,
                    category = "Sides",
                    isBestseller = true,
                    imageEmoji = "🥖",
                    imageRes = Res.drawable.dish_dosa
                ),
                Dish(
                    id = "p4",
                    name = "Choco Lava Cake",
                    description = "Warm chocolate cake with a rich molten center that flows like lava on first bite.",
                    price = 109.0,
                    isVeg = true,
                    rating = 4.8,
                    ratingCount = 4500,
                    category = "Desserts",
                    isBestseller = true,
                    imageEmoji = "🧁",
                    imageRes = Res.drawable.dish_gulab_jamun
                )
            )
        ),
        Restaurant(
            id = "rest_4",
            name = "Burger King Express",
            cuisines = listOf("Burgers", "American", "Fries", "Beverages"),
            rating = 4.1,
            ratingCount = "4.2k+",
            deliveryTimeMinutes = 22,
            distanceKm = 2.6,
            costForTwo = 300,
            offer = "Flat 40% OFF on Combos",
            isPureVeg = false,
            isPromoted = false,
            address = "Cyber Hub, Food Court",
            coverEmoji = "🍔",
            coverImageRes = Res.drawable.banner_burger,
            dishes = listOf(
                Dish(
                    id = "bk1",
                    name = "Crispy Veg Double Patty Burger",
                    description = "Double crispy spiced potato patties with fresh lettuce, onions, and signature creamy mayo.",
                    price = 129.0,
                    originalPrice = 169.0,
                    isVeg = true,
                    rating = 4.2,
                    ratingCount = 1400,
                    category = "Burger",
                    isBestseller = true,
                    imageEmoji = "🍔",
                    imageRes = Res.drawable.dish_burger
                ),
                Dish(
                    id = "bk2",
                    name = "Chicken Whopper Burger",
                    description = "Flame-grilled chicken patty topped with juicy tomatoes, fresh lettuce, mayo and pickles on sesame seed bun.",
                    price = 199.0,
                    originalPrice = 249.0,
                    isVeg = false,
                    rating = 4.5,
                    ratingCount = 2100,
                    category = "Burger",
                    isBestseller = true,
                    imageEmoji = "🍔",
                    imageRes = Res.drawable.dish_burger
                ),
                Dish(
                    id = "bk3",
                    name = "King Peri Peri Fries (Large)",
                    description = "Golden crispy french fries tossed in hot & zesty peri-peri spice mix.",
                    price = 119.0,
                    isVeg = true,
                    rating = 4.4,
                    ratingCount = 1890,
                    category = "Sides",
                    imageEmoji = "🍟",
                    imageRes = Res.drawable.dish_burger
                )
            )
        ),
        Restaurant(
            id = "rest_5",
            name = "Punjab Grill & Tandoor",
            cuisines = listOf("North Indian", "Mughlai", "Dal Makhani", "Tandoori"),
            rating = 4.6,
            ratingCount = "2.9k+",
            deliveryTimeMinutes = 35,
            distanceKm = 4.0,
            costForTwo = 700,
            offer = "Flat 20% OFF",
            isPureVeg = false,
            isPromoted = false,
            address = "Heritage Market, Main Road",
            coverEmoji = "🥘",
            coverImageRes = Res.drawable.banner_thali,
            dishes = listOf(
                Dish(
                    id = "pg1",
                    name = "Slow Cooked Dal Makhani",
                    description = "Black lentils simmered overnight with butter, cream, and freshly ground spices. Pure indulgence.",
                    price = 289.0,
                    originalPrice = 330.0,
                    isVeg = true,
                    rating = 4.8,
                    ratingCount = 1900,
                    category = "North Indian",
                    isBestseller = true,
                    imageEmoji = "🍲",
                    imageRes = Res.drawable.dish_paneer
                ),
                Dish(
                    id = "pg2",
                    name = "Butter Chicken Delhi Style (Boneless)",
                    description = "Tandoori chicken pieces cooked in a rich, buttery, velvety tomato gravy with kasoori methi.",
                    price = 379.0,
                    originalPrice = 430.0,
                    isVeg = false,
                    rating = 4.7,
                    ratingCount = 2300,
                    category = "North Indian",
                    isBestseller = true,
                    imageEmoji = "🍗",
                    imageRes = Res.drawable.dish_biryani
                ),
                Dish(
                    id = "pg3",
                    name = "Garlic Butter Naan (2 Pcs)",
                    description = "Crispy leavened oven bread infused with chopped roasted garlic and brush of melted butter.",
                    price = 99.0,
                    isVeg = true,
                    rating = 4.6,
                    ratingCount = 1200,
                    category = "Breads",
                    imageEmoji = "🫓",
                    imageRes = Res.drawable.dish_paneer
                )
            )
        ),
        Restaurant(
            id = "rest_6",
            name = "Chai Point & All Day Bites",
            cuisines = listOf("Chai & Coffee", "Snacks", "Fast Food", "Bakery"),
            rating = 4.3,
            ratingCount = "3.2k+",
            deliveryTimeMinutes = 15,
            distanceKm = 1.2,
            costForTwo = 200,
            offer = "Speedy 15 mins delivery",
            isPureVeg = true,
            isPromoted = false,
            address = "Tech Hub, Tower C",
            coverEmoji = "☕",
            coverImageRes = Res.drawable.banner_dessert,
            dishes = listOf(
                Dish(
                    id = "cp1",
                    name = "Ginger Cardamom Hot Chai Flask (500ml)",
                    description = "Freshly brewed desi milk chai infused with fresh hand-crushed ginger and fragrant elaichi. Serves 3.",
                    price = 149.0,
                    isVeg = true,
                    rating = 4.7,
                    ratingCount = 2100,
                    category = "Chai & Coffee",
                    isBestseller = true,
                    imageEmoji = "☕",
                    imageRes = Res.drawable.dish_chaat
                ),
                Dish(
                    id = "cp2",
                    name = "Crispy Bun Maska & Jam",
                    description = "Toasted soft bakery bun loaded with generous salted amul butter and fruit jam.",
                    price = 79.0,
                    isVeg = true,
                    rating = 4.4,
                    ratingCount = 980,
                    category = "Snacks",
                    imageEmoji = "🥐",
                    imageRes = Res.drawable.dish_dosa
                ),
                Dish(
                    id = "cp3",
                    name = "Spicy Paneer Tikka Sandwich",
                    description = "Grilled multi-grain bread stuffed with spicy marinated paneer cubes and green mint mayo.",
                    price = 159.0,
                    isVeg = true,
                    rating = 4.3,
                    ratingCount = 650,
                    category = "Snacks",
                    imageEmoji = "🥪",
                    imageRes = Res.drawable.dish_paneer
                )
            )
        )
    )

    val coupons = mapOf(
        "FLAT60" to Coupon("FLAT60", 60.0, 120.0, 0.0, "60% instant off on all items (upto ₹120)"),
        "WELCOME50" to Coupon("WELCOME50", 50.0, 100.0, 0.0, "50% off on all products (upto ₹100)"),
        "TOMATO50" to Coupon("TOMATO50", 50.0, 150.0, 0.0, "50% discount on entire order (upto ₹150)"),
        "TASTY20" to Coupon("TASTY20", 20.0, 80.0, 0.0, "20% instant discount on total bill"),
        "GOLDDEL" to Coupon("GOLDDEL", 100.0, 35.0, 0.0, "Free delivery discount (Save ₹35)"),
        "BIRYANI40" to Coupon("BIRYANI40", 40.0, 80.0, 0.0, "40% off on biryani & rice dishes"),
        "BURGER40" to Coupon("BURGER40", 40.0, 100.0, 0.0, "40% off on burgers & snacks")
    )

    val indianStatesWithCities = listOf(
        StateLocation("Delhi NCR", listOf("Connaught Place, New Delhi", "Sector 62, Noida", "Cyber Hub, Gurugram", "Indirapuram, Ghaziabad", "South Extension, New Delhi")),
        StateLocation("Maharashtra", listOf("Bandra West, Mumbai", "Koregaon Park, Pune", "Andheri East, Mumbai", "Vashi, Navi Mumbai", "Civil Lines, Nagpur")),
        StateLocation("Karnataka", listOf("Indiranagar, Bengaluru", "Koramangala, Bengaluru", "Whitefield, Bengaluru", "Jayalakshmipuram, Mysuru", "Hampankatta, Mangaluru")),
        StateLocation("Uttar Pradesh", listOf("Hazratganj, Lucknow", "Gomti Nagar, Lucknow", "Sector 18, Noida", "Swaroop Nagar, Kanpur", "Sigra, Varanasi")),
        StateLocation("Rajasthan", listOf("C-Scheme, Jaipur", "Malviya Nagar, Jaipur", "Ratanada, Jodhpur", "Panchwati, Udaipur", "Vaishali Nagar, Ajmer")),
        StateLocation("Gujarat", listOf("SG Highway, Ahmedabad", "Navrangpura, Ahmedabad", "Alkapuri, Vadodara", "Vesu, Surat", "Kalawad Road, Rajkot")),
        StateLocation("Punjab", listOf("Sector 17, Chandigarh", "Model Town, Ludhiana", "Ranjit Avenue, Amritsar", "Urban Estate, Jalandhar", "Leela Bhawan, Patiala")),
        StateLocation("West Bengal", listOf("Park Street, Kolkata", "Salt Lake Sector V, Kolkata", "Ballygunge, Kolkata", "City Centre, Siliguri", "Benachity, Durgapur")),
        StateLocation("Telangana", listOf("Banjara Hills, Hyderabad", "Gachibowli, Hyderabad", "Jubilee Hills, Hyderabad", "Hanamkonda, Warangal")),
        StateLocation("Tamil Nadu", listOf("T. Nagar, Chennai", "Anna Nagar, Chennai", "RS Puram, Coimbatore", "KK Nagar, Madurai", "Thillai Nagar, Trichy")),
        StateLocation("Madhya Pradesh", listOf("Vijay Nagar, Indore", "Chappan Dukan, Indore", "MP Nagar, Bhopal", "Arera Colony, Bhopal", "Wright Town, Jabalpur")),
        StateLocation("Haryana", listOf("Golf Course Road, Gurugram", "Sector 29, Gurugram", "Sector 14, Faridabad", "Sector 8, Panchkula", "Model Town, Panipat")),
        StateLocation("Bihar", listOf("Boring Road, Patna", "Kankarbagh, Patna", "Bailey Road, Patna", "Tilka Manjhi, Bhagalpur", "Zero Mile, Muzaffarpur")),
        StateLocation("Kerala", listOf("Panampilly Nagar, Kochi", "MG Road, Thiruvananthapuram", "Kallai Road, Kozhikode", "Round East, Thrissur")),
        StateLocation("Goa", listOf("Panaji Waterfront, Panaji", "Candolim Beach Road, North Goa", "Margao Market, South Goa", "Anjuna, North Goa")),
        StateLocation("Uttarakhand", listOf("Rajpur Road, Dehradun", "Mall Road, Mussoorie", "Tallital, Nainital", "Haridwar Bypass, Haridwar")),
        StateLocation("Himachal Pradesh", listOf("The Mall, Shimla", "McLeod Ganj, Dharamshala", "Model Town, Manali", "Old Kangra, Kangra")),
        StateLocation("Jammu & Kashmir", listOf("Residency Road, Srinagar", "Gandhi Nagar, Jammu", "Dal Lake Boulevard, Srinagar")),
        StateLocation("Assam", listOf("GS Road, Guwahati", "Uzan Bazar, Guwahati", "Chandan Nagar, Jorhat")),
        StateLocation("Odisha", listOf("Saheed Nagar, Bhubaneswar", "Patia, Bhubaneswar", "CDA Sector 9, Cuttack")),
        StateLocation("Andhra Pradesh", listOf("Siripuram, Visakhapatnam", "Benz Circle, Vijayawada", "Koramangala, Tirupati")),
        StateLocation("Jharkhand", listOf("Main Road, Ranchi", "Bistupur, Jamshedpur", "Bank More, Dhanbad")),
        StateLocation("Chhattisgarh", listOf("Pandri, Raipur", "Telibandha, Raipur", "Vyapar Vihar, Bilaspur")),
        StateLocation("Chandigarh", listOf("Sector 35, Chandigarh", "Sector 22, Chandigarh", "Sector 8, Chandigarh")),
        StateLocation("Puducherry", listOf("White Town, Puducherry", "MG Road, Puducherry"))
    )
}

data class StateLocation(
    val state: String,
    val popularAreas: List<String>
)

data class PromoBanner(
    val id: String,
    val title: String,
    val subtitle: String,
    val code: String,
    val tag: String,
    val imageRes: DrawableResource? = null
)

data class Coupon(
    val code: String,
    val percentage: Double,
    val maxDiscount: Double,
    val minOrderAmount: Double,
    val description: String
)
