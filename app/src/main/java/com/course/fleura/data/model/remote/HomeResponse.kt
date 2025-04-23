package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName

data class ListStoreResponse(

    @field:SerializedName("data")
    val data: List<StoreItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class StoreItem(

    @field:SerializedName("operational_day")
    val operationalDay: String,

    @field:SerializedName("sellerId")
    val sellerId: String,

    @field:SerializedName("address")
    val address: Address,

    @field:SerializedName("balance")
    val balance: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("logo")
    val logo: String,

    @field:SerializedName("operational_hour")
    val operationalHour: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("picture")
    val picture: String
)

// ========================================
// ========================================

data class DetailStoreResponse(

    @field:SerializedName("data")
    val data: DetailStoreData,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class DetailStoreData(

    @field:SerializedName("operational_day")
    val operationalDay: String,

    @field:SerializedName("sellerId")
    val sellerId: String,

    @field:SerializedName("address")
    val address: Address,

    @field:SerializedName("balance")
    val balance: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("logo")
    val logo: String,

    @field:SerializedName("operational_hour")
    val operationalHour: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("picture")
    val picture: String
)

data class Address(

    @field:SerializedName("province")
    val province: String,

    @field:SerializedName("road")
    val road: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("district")
    val district: String,

    @field:SerializedName("postcode")
    val postcode: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("detail")
    val detail: String,

    @field:SerializedName("storeId")
    val storeId: String
)

// ========================================
// ========================================

data class StoreProductResponse(

    @field:SerializedName("data")
    val data: List<StoreProduct>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class StoreProduct(

    @field:SerializedName("price")
    val price: String,

    @field:SerializedName("arrange_time")
    val arrangeTime: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("pre_order")
    val preOrder: Boolean,

    @field:SerializedName("store")
    val store: StoreFromProduct,

    @field:SerializedName("stock")
    val stock: Int,

    @field:SerializedName("category")
    val category: ProductCategory,

    @field:SerializedName("point")
    val point: Int,

    @field:SerializedName("picture")
    val picture: List<ProductPicture>,

    @field:SerializedName("rating")
    val rating: Double,
)

data class StoreFromProduct(

    @field:SerializedName("operational_day")
    val operationalDay: String,

    @field:SerializedName("sellerId")
    val sellerId: String,

    @field:SerializedName("balance")
    val balance: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("logo")
    val logo: String,

    @field:SerializedName("operational_hour")
    val operationalHour: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("picture")
    val picture: String
)

data class ProductPicture(

    @field:SerializedName("path")
    val path: String,

    @field:SerializedName("id")
    val id: String
)

data class ProductCategory(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String
)

// ========================================
// ========================================

data class ProductReviewResponse(

    @field:SerializedName("data")
    val data: List<ReviewItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class ReviewItem(

    @field:SerializedName("rate")
    val rate: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("buyerId")
    val buyerId: String
)

// ========================================
// ========================================

data class SaveProductToCartRequest(

    @field:SerializedName("productId")
    val productId: String,

    @field:SerializedName("quantity")
    val quantity: Int,
)

data class SaveProductToCartResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)


// ========================================
// ========================================

data class ListProductResponse(

    @field:SerializedName("data")
    val data: List<ItemProductList>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class PictureItemProductList(

    @field:SerializedName("path")
    val path: String,

    @field:SerializedName("id")
    val id: String
)

data class StoreProductList(

    @field:SerializedName("operational_day")
    val operationalDay: String,

    @field:SerializedName("sellerId")
    val sellerId: String,

    @field:SerializedName("balance")
    val balance: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("logo")
    val logo: Any,

    @field:SerializedName("operational_hour")
    val operationalHour: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("picture")
    val picture: Any
)

data class ItemProductList(

    @field:SerializedName("price")
    val price: String,

    @field:SerializedName("arrange_time")
    val arrangeTime: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("rating")
    val rating: Int,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("pre_order")
    val preOrder: Boolean,

    @field:SerializedName("store")
    val store: StoreProductList,

    @field:SerializedName("stock")
    val stock: Int,

    @field:SerializedName("category")
    val category: CategoryProductList,

    @field:SerializedName("point")
    val point: Int,

    @field:SerializedName("picture")
    val picture: List<PictureItemProductList>
)

data class CategoryProductList(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String
)



