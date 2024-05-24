package com.example.qrscanner.data.repository

import com.example.qrscanner.data.model.FoodItem

interface FoodRepository {
    fun getFoodItems(): List<FoodItem>
}