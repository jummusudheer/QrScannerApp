package com.example.qrscanner.data.datasource

import com.example.qrscanner.data.model.FoodItem

interface FoodDataSource {
    fun getFoodItems(): List<FoodItem>
}