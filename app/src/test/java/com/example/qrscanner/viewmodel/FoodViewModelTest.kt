import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.qrscanner.data.model.FoodItem
import com.example.qrscanner.data.repository.FoodRepository
import com.example.qrscanner.domain.usecase.GetFoodItemsUseCase
import com.example.qrscanner.food.FoodViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FoodViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `view model should update live data with items list containing name, description, and image url`() {
        // Given
        val getFoodItemsUseCase = mock(GetFoodItemsUseCase::class.java)
        val expectedItems = listOf(
            FoodItem("Item1", "Description1", "https://example.com/image1.jpg"),
            FoodItem("Item2", "Description2", "https://example.com/image2.jpg")
        )
        `when`(getFoodItemsUseCase.excute()).thenReturn(expectedItems)

        val viewModel = FoodViewModel(getFoodItemsUseCase)
        val observer = mock(Observer::class.java) as Observer<List<FoodItem>>

        // When
        viewModel.foodItems.observeForever(observer)
        viewModel.loadFoodItems()

        // Then
        verify(observer).onChanged(expectedItems)
        assert(viewModel.foodItems.value == expectedItems)
    }
}
