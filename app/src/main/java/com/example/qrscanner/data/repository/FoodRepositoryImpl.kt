package com.example.qrscanner.data.repository
import com.example.qrscanner.data.datasource.FoodDataSource
import com.example.qrscanner.data.model.FoodItem

class FoodRepositoryImpl(private val foodDataSource: FoodDataSource): FoodRepository{
    override fun getFoodItems(): List<FoodItem> {
        return foodDataSource.getFoodItems()
    }
}
