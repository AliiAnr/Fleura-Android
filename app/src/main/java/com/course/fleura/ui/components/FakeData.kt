package com.course.fleura.ui.components

import com.course.fleura.R
import com.course.fleura.ui.screen.navigation.DetailDestinations

data class Category(
    val id: Long,
    val name: String,
    val image: Int
)

data class Corousel(
    val id: Long,
    val image: Int
)

data class Store(
    val id: Long,
    val name: String,
    val openHours: String,
    val image: Int
)

data class Flower(
    val id: Long,
    val image: Int,
    val storeName: String,
    val flowerName: String,
    val rating: Double,
    val reviewsCount: Int,
    val price: Long
)

data class Common(
    val id: Long,
    val image: Int,
    val name: String,
    val price: Long
)

data class OrderItem(
    val id: Long,
    val imageRes: Int,
    val name: String,
    val quantity: Int,
    val isRedeemAble: Boolean = false,
    val points: Int = 0,
    val price: Long
)

data class Order(
    val id: Long,
    val storeName: String,
    val items: List<OrderItem>,
    val isRedeemOrder: Boolean = false,
    val totalPrice: Long,
    val totalPoints: Long
)

data class RewardItem(
    val id: Long,
    val name: String,
    val imageRes: Int,
    val points: Long,
    val available: Boolean
)

data class Profile(
    val name: String,
    val email: String,
    val image: Int,
    val phone: String,
    val address: List<String>,
    val points: Long,
)

data class MerchantCategory(
    val title: String,
    val items: List<Flower>
)

data class Item(
    val name: String,
    val description: String,
    val price: String,
    val image: Int
)

data class Merchant(
    val id: Long,
    val name: String,
    val description: String,
    val image: Int,
    val rating: Double,
    val reviewCount: Int,
    val openHours: String,
    val openDays: String,
    val address: String,
    val phone: String,
    val isFavorite: Boolean,
    val categories: List<MerchantCategory>
)

data class AccountList(
    val name: String,
    val icon: Int,
)

const val TEMP_ID : Long = 0


object FakeCategory {
    val categories: List<Category> = listOf(
        Category(1, "All Variants", R.drawable.category_1),
        Category(2, "Fresh Flower", R.drawable.category_2),
        Category(3, "Flower Bouquet", R.drawable.category_3),
        Category(4, "Chocolate Bouquet", R.drawable.category_4),
    )

    val corousels: List<Corousel> = listOf(
        Corousel(1, R.drawable.corousel_1),
        Corousel(2, R.drawable.corousel_2),
        Corousel(3, R.drawable.corousel_3),
    )

    val stores: List<Store> = listOf(
        Store(1, "Buga Adik", "06:00 am - 10:00 pm", R.drawable.store_1),
        Store(2, "Flower WwW", "09:00 am - 10:00 pm", R.drawable.store_2),
        Store(3, "Flwey", "11:00 am - 11:00 pm", R.drawable.store_3),
    )

    val flowers: List<Flower> = listOf(
        Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
        Flower(2, R.drawable.flower_2, "Flower WwW", "Lily", 4.0, 50, 20000),
        Flower(3, R.drawable.flower_3, "Flwey", "Sunflower", 4.2, 70, 15000),
    )

    val commons: List<Common> = listOf(
        Common(1, R.drawable.cc_1, "Flower Chocolate", 10000),
        Common(2, R.drawable.cc_2, "Flower Bouquet", 20000),
        Common(3, R.drawable.cc_3, "Chocolate Bouquet", 15000),
        Common(4, R.drawable.cc_4, "Flower Bouquet", 20000),
        Common(5, R.drawable.cc_5, "Chocolate Bouquet", 15000),
        Common(6, R.drawable.cc_6, "Flower Bouquet", 20000),
        Common(7, R.drawable.cc_7, "Chocolate Bouquet", 15000),
        Common(8, R.drawable.cc_8, "Flower Bouquet", 20000),
    )

    val orders: List<Order> = listOf(
        Order(
            1,
            "Buga Adik",
            listOf(
                OrderItem(1, R.drawable.order_1, "Rose", 1, true, 1000, 10000),
                OrderItem(2, R.drawable.order_2, "Lily", 2, true, 2000, 20000),
            ),
            true,
            20000,
            3000
        ),

        )

    val rewardItems = listOf(
        RewardItem(1, "Sunflower", R.drawable.cc_4, 2500, true),
        RewardItem(2, "Classic Bouquet", R.drawable.order_1, 1300, true),
        RewardItem(3, "Artificial Flower", R.drawable.flower_1, 3000, false),
        RewardItem(4, "Purple Bouquet", R.drawable.flower_2, 1800, true),
        RewardItem(5, "Sunflower", R.drawable.cc_4, 2500, true),
        RewardItem(6, "Classic Bouquet", R.drawable.order_1, 1300, true),
        RewardItem(7, "Artificial Flower", R.drawable.flower_1, 3000, false),
        RewardItem(8, "Purple Bouquet", R.drawable.flower_2, 1800, true)
    )

    val accountItem: List<AccountList> = listOf(
        AccountList("Edit Profile", R.drawable.editprofile),
        AccountList("Address", R.drawable.map),
        AccountList("Language", R.drawable.language),
    )

    val generalItem: List<AccountList> = listOf(
        AccountList("Help Center", R.drawable.help),
        AccountList("Privacy", R.drawable.privacy),
        AccountList("Security", R.drawable.security),
        AccountList("Feedback", R.drawable.feedback),
        AccountList("Logout", R.drawable.logout),
    )

    val merchantItem: Merchant = Merchant(
        1,
        "Buga Adik",
        "Buga Adik is a flower shop that provides various types of flowers and bouquets. We provide the best quality flowers and bouquets for you.",
        R.drawable.store_1,
        4.5,
        100,
        "06:00 am - 10:00 pm",
        "Monday - Sunday",
        "Jl. Raya Bogor No. 1, Bogor",
        "08123456789",
        false,
        listOf(
            MerchantCategory(
                "All Variants",
                listOf(
                    Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
                    Flower(2, R.drawable.flower_2, "Buga Adik", "Lily", 4.0, 50, 20000),
                    Flower(3, R.drawable.flower_3, "Buga Adik", "Sunflower", 4.2, 70, 15000),
                )
            ),
            MerchantCategory(
                "Fresh Flower",
                listOf(
                    Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
                    Flower(2, R.drawable.flower_2, "Buga Adik", "Lily", 4.0, 50, 20000),
                    Flower(3, R.drawable.flower_3, "Buga Adik", "Sunflower", 4.2, 70, 15000),
                )
            ),
            MerchantCategory(
                "Flower Bouquet",
                listOf(
                    Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
                    Flower(2, R.drawable.flower_2, "Buga Adik", "Lily", 4.0, 50, 20000),
                    Flower(3, R.drawable.flower_3, "Buga Adik", "Sunflower", 4.2, 70, 15000),
                )
            ),
            MerchantCategory(
                "Chocolate Bouquet",
                listOf(
                    Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
                    Flower(2, R.drawable.flower_2, "Buga Adik", "Lily", 4.0, 50, 20000),
                    Flower(3, R.drawable.flower_3, "Buga Adik", "Sunflower", 4.2, 70, 15000),
                )
            ),
        )
    )
}