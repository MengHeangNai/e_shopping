package kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview

import Constants.Companion.IMAGES
import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.e_shopping.api.model.Product
import kh.edu.rupp.ite.e_shopping.databinding.ProductItemBinding

class ProductsRecyclerAdapter() :
    RecyclerView.Adapter<ProductsRecyclerAdapter.BestProductsRecyclerAdapterViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null

    inner class BestProductsRecyclerAdapterViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestProductsRecyclerAdapterViewHolder {
        return BestProductsRecyclerAdapterViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BestProductsRecyclerAdapterViewHolder, position: Int) {
        val product = differ.currentList[position]
        val images = product.images
        val image = (images!![IMAGES] as List<*>)[0]
        holder.binding.apply {
            Glide.with(holder.itemView).load(image).into(imgProduct)
            tvName.text = product.title
            tvPrice.text = "$${product.price}"
            tvNewPrice.visibility = View.GONE
        }

        product.newPrice?.let {
            if (product.newPrice.isNotEmpty() && product.newPrice != "0") {
                holder.binding.apply {
                    tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvNewPrice.text = "$${product.newPrice}"
                    tvNewPrice.visibility = View.VISIBLE
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}