package kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.e_shopping.api.model.Product
import kh.edu.rupp.ite.e_shopping.databinding.ProductItemBinding
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.IMAGES

class ClothesAdapter : RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder>()  {
    private var diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    inner class  ClothesViewHolder(val binding: ProductItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        return ClothesViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((Product) -> Unit)? = null

    var onAddToCartClick: ((Product) -> Unit)? = null

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        val product = differ.currentList[position]
        val images = product.images
        val image = (images!![IMAGES] as List<*>)[0]

        holder.binding.apply {
            Glide.with(holder.itemView).load(image).into(imgProduct)
            tvName.text = product.title
            tvNewPrice.text = product.newPrice
            tvPrice.text = product.price
        }
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(product)
        }
        holder.binding.imgFavorite.setOnClickListener{
            onAddToCartClick?.invoke(product)
        }
    }

}