package com.example.qrscanner.food
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qrscanner.R
import com.example.qrscanner.databinding.ItemRecipeBinding
import com.example.qrscanner.databinding.ItemRecipeBinding.inflate
import com.squareup.picasso.Picasso


class FoodAdapter(private val recipes: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.RecipeViewHolder>(), Filterable {

    private var filteredRecipes: List<FoodItem> = recipes
    private lateinit var binding: ItemRecipeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
         val view=
            LayoutInflater.from(parent.context).inflate(com.example.qrscanner.R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(filteredRecipes[position])
        val foodList = null
        val foodItem =filteredRecipes[position]
        binding.foodItemName.text=recipes[position].name
        binding.recipeNameTextView.text=recipes[position].description

        Picasso.get().load(foodItem.imageUrl).into(holder.imgFood)

    }


    override fun getItemCount(): Int = filteredRecipes.size



        inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgFood: ImageView = itemView.findViewById(R.id.imageView)

            fun bind(recipes: FoodItem) {
            binding= DataBindingUtil.bind(itemView)!!
            }
        }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
val filteredList = mutableListOf<FoodItem>()
                if (constraint.isNullOrBlank()) {
                filteredList.addAll(recipes)
               } else {
                   val filterPattern = constraint.toString().toLowerCase().trim()
                    for (recipe in recipes) {
                       if (recipe.name.toLowerCase().contains(filterPattern)) {
                          filteredList.add(recipe)
                    }
                }
               }
              val results = FilterResults()
              results.values = filteredList
              return results
         }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

         }
        }
    }

}