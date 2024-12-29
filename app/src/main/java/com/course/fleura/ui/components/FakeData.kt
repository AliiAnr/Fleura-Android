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
}