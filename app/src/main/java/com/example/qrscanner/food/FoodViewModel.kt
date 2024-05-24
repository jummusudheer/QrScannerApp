package com.example.qrscanner.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qrscanner.data.model.FoodItem
import com.example.qrscanner.domain.usecase.GetFoodItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(private val getFoodItemsUseCase: GetFoodItemsUseCase) : ViewModel() {
    private val _foodItems =MutableLiveData<List<FoodItem>>()
    val foodItems: LiveData<List<FoodItem>> get() = _foodItems

    fun loadFoodItems(){

        val foodList=getFoodItemsUseCase.excute()
        _foodItems.value= foodList

    }


//    var foodList: MutableList<FoodItem> = mutableListOf()
//    init {
//        foodList.add(FoodItem("Kellogg's", "Delicious Kellogg's", "https://i5.walmartimages.com/asr/9c9ef863-4f03-4a5a-bf1c-18ff3d948e8b_1.352903b24dfbb0664dc16b4b9d7976ea.jpeg"))
//        foodList.add(FoodItem("Badam", "Amazing Badam Mix recipe", "https://www.quicklly.com/upload_images/product/1603411078-mtr-badam-drink-mix.jpg"))
//    }


}