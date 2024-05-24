package com.example.qrscanner.data.datasource

import com.example.qrscanner.data.model.FoodItem
class FoodDatasourceImpl: FoodDataSource {
    var foodList: MutableList<FoodItem> = mutableListOf()
    override fun getFoodItems(): List<FoodItem> {
        foodList.add(
            FoodItem(
                "Kellogg's",
                "Delicious Kellogg's",
                "https://i5.walmartimages.com/asr/9c9ef863-4f03-4a5a-bf1c-18ff3d948e8b_1.352903b24dfbb0664dc16b4b9d7976ea.jpeg"
            )
        )
        foodList.add(
            FoodItem(
                "Badam",
                "Amazing Badam Mix recipe",
                "https://www.quicklly.com/upload_images/product/1603411078-mtr-badam-drink-mix.jpg"
            )
        )
        return foodList
    }
}