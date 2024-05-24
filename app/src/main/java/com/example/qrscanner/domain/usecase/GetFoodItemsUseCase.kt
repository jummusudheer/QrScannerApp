package com.example.qrscanner.domain.usecase

import android.util.Log
import com.example.qrscanner.data.model.FoodItem
import com.example.qrscanner.data.repository.FoodRepository

class GetFoodItemsUseCase(private val foodRepository: FoodRepository){
    fun excute(): List<FoodItem>{
        val items=foodRepository.getFoodItems()
        Log.d("GetFoodItemsUseCase", "excuting use case items:$items")
        return items
    }
}