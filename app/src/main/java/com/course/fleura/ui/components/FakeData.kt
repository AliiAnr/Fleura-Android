package com.course.fleura.ui.components

import com.course.fleura.R

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

object FakeCategory {
    val categories : List<Category> = listOf(
        Category(1, "All Variants", R.drawable.category_1),
        Category(2, "Fresh Flower", R.drawable.category_2),
        Category(3, "Flower Bouquet", R.drawable.category_3),
        Category(4, "Chocolate Bouquet", R.drawable.category_4),
    )

    val corousels : List<Corousel> = listOf(
        Corousel(1, R.drawable.corousel_1),
        Corousel(2, R.drawable.corousel_2),
        Corousel(3, R.drawable.corousel_3),
    )

    val stores : List<Store> = listOf(
            Store(1, "Buga Adik", "06:00 am - 10:00 pm", R.drawable.store_1),
            Store(2, "Flower WwW", "09:00 am - 10:00 pm", R.drawable.store_2),
            Store(3, "Flwey", "11:00 am - 11:00 pm", R.drawable.store_3),
    )

    val flowers : List<Flower> = listOf(
        Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
        Flower(2, R.drawable.flower_2, "Flower WwW", "Lily", 4.0, 50, 20000),
        Flower(3, R.drawable.flower_3, "Flwey", "Sunflower", 4.2, 70, 15000),
    )
}