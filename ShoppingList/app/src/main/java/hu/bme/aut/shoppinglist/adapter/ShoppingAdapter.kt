package hu.bme.aut.shoppinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.shoppinglist.R
import hu.bme.aut.shoppinglist.data.ShoppingItem

class ShoppingAdapter(private val listener: ShoppingItemClickListener) : RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {

    private val items = mutableListOf<ShoppingItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.descriptionTextView.text = item.description
        holder.categoryTextView.text = item.category.name
        holder.priceTextView.text = item.estimatedPrice.toString() + " Ft"
        holder.iconImageView.setImageResource(getImageResource(item.category))
        holder.isBoughtCheckBox.isChecked = item.isBought

        holder.item = item
    }

    @DrawableRes
    private fun getImageResource(category: ShoppingItem.Category) = when (category) {
        ShoppingItem.Category.BOOK -> R.drawable.open_book
        ShoppingItem.Category.ELECTRONIC -> R.drawable.lightning
        ShoppingItem.Category.FOOD -> R.drawable.groceries
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ShoppingItemClickListener {
        fun onItemChanged(item: ShoppingItem)
        fun onItemRemoved(item: ShoppingItem)
    }

    fun addItem(item: ShoppingItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<ShoppingItem>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: ShoppingItem) {
        notifyItemRemoved(items.indexOf(item))
        items.remove(item)
    }

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val iconImageView: ImageView
        val nameTextView: TextView
        val descriptionTextView: TextView
        val categoryTextView: TextView
        val priceTextView: TextView
        val isBoughtCheckBox: CheckBox
        val removeButton: ImageButton

        var item: ShoppingItem? = null

        init {
            iconImageView = itemView.findViewById(R.id.ShoppingItemIconImageView)
            nameTextView = itemView.findViewById(R.id.ShoppingItemNameTextView)
            descriptionTextView = itemView.findViewById(R.id.ShoppingItemDescriptionTextView)
            categoryTextView = itemView.findViewById(R.id.ShoppingItemCategoryTextView)
            priceTextView = itemView.findViewById(R.id.ShoppingItemPriceTextView)
            isBoughtCheckBox = itemView.findViewById(R.id.ShoppingItemIsBoughtCheckBox)
            removeButton = itemView.findViewById(R.id.ShoppingItemRemoveButton)
            isBoughtCheckBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                item?.let {
                    val newItem = it.copy(
                        isBought = isChecked
                    )
                    item = newItem
                    listener.onItemChanged(newItem)
                }
            })
            removeButton.setOnClickListener {
                item?.let {
                    listener.onItemRemoved(it)
                    removeItem(it)
                }
            }
        }
    }
}