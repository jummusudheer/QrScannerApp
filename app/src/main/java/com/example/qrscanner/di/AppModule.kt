package com.example.qrscanner.di

import com.example.qrscanner.data.datasource.FoodDataSource
import com.example.qrscanner.data.datasource.FoodDatasourceImpl
import com.example.qrscanner.data.repository.FoodRepository
import com.example.qrscanner.data.repository.FoodRepositoryImpl
import com.example.qrscanner.domain.usecase.GetFoodItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFoodDataSource(): FoodDataSource = FoodDatasourceImpl()

    @Provides
    fun provideFoodRepository(foodDataSource: FoodDataSource): FoodRepository = FoodRepositoryImpl(foodDataSource)

    @Provides
    fun provideGetFoodItemsUseCase(foodRepository: FoodRepository): GetFoodItemsUseCase = GetFoodItemsUseCase(foodRepository)
}