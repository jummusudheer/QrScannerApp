package com.example.qrscanner.food
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.qrscanner.data.model.FoodItem
import com.example.qrscanner.databinding.FragmentFoodBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class FoodFragment : Fragment() {

    private lateinit var adapter: FoodAdapter
    private lateinit var foodViewModel:FoodViewModel
//    private var foodList: MutableList<FoodItem> = mutableListOf()
    private lateinit var binding: FragmentFoodBinding
    private lateinit var editText: EditText
    private lateinit var requestQueue: RequestQueue
    private val apiKey = "https://api.spoonacular.com/recipes/715538/similar"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        foodList.add(FoodItem("Kellogg's", "Delicious Kellogg's", "https://i5.walmartimages.com/asr/9c9ef863-4f03-4a5a-bf1c-18ff3d948e8b_1.352903b24dfbb0664dc16b4b9d7976ea.jpeg"))
//        foodList.add(FoodItem("Badam", "Amazing Badam Mix recipe", "https://www.quicklly.com/upload_images/product/1603411078-mtr-badam-drink-mix.jpg"))

        //        return inflater.inflate(R.layout.fragment_food, container, false)
        binding= FragmentFoodBinding.inflate(inflater, container, false)
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add more recipes as needed
//       binding.searchView.setOnClickListener {
//           val filteredRecipes = filterRecipesByName(searchQuery = "Pizza")
//           if (filteredRecipes.isNotEmpty()) {
//               val selectedRecipe = filteredRecipes[0]
//               displayRecipe(selectedRecipe)
//           } else {
//               Toast.makeText(requireContext(), "Please enter a food item", Toast.LENGTH_SHORT)
//                   .show()
//           }
//       }
//        // Set up RecyclerView
        adapter = FoodAdapter(emptyList())
       binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        foodViewModel.foodItems.observe(viewLifecycleOwner, Observer { foodItems ->
            Log.d("FoodList","foodItems observed with size:${foodItems.size}")
            adapter.submitList(foodItems)
        })

        foodViewModel.loadFoodItems()

      //  generateFoodData()

    }

    // Function to filter recipes by name
//    fun filterRecipesByName(searchQuery: String): List<FoodItem> {
//        return foodList.filter {
//            it.name.contains(searchQuery, ignoreCase = true)
//            it.description.contains(searchQuery,ignoreCase = true)
//        }
//    }

    fun displayRecipe(recipe: FoodItem){
        println("Name: ${recipe.name}")
        println("Description: ${recipe.description}")
        println("Image: ${recipe.imageUrl}")
    }

    private fun fetchRecipes(foodItem: String) {
        val url = "https://api.spoonacular.com/recipes/search?query=$foodItem&apiKey=$apiKey"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val recipesArray = response.getJSONArray("results")
                if (recipesArray.length() > 0) {
                    // Display the first recipe for simplicity
                    val recipe = recipesArray.getJSONObject(0)
                    val recipeTitle = recipe.getString("title")
                    val recipeId = recipe.getInt("id")
                    Toast.makeText(requireContext(), "Recipe found: $recipeTitle", Toast.LENGTH_SHORT).show()
                    // You can handle further actions like showing details or saving the recipe
                } else {
                    Toast.makeText(requireContext(), "No recipes found for $foodItem", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun generateFoodData() {

    }
}
