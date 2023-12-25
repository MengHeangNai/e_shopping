package kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.rupp.ite.e_shopping.databinding.ClothesExtraAdsItemBinding
import kh.edu.rupp.ite.e_shopping.ui.model.Product
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.IMAGES


class AdsRecyclerAdapter : RecyclerView.Adapter<AdsRecyclerAdapter.AdsViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    inner class AdsViewHolder(val binding: ClothesExtraAdsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdsViewHolder {
        return AdsViewHolder(
            ClothesExtraAdsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        val product = differ.currentList[position]
        val images = product.images
        val image = (images!![IMAGES] as List<*>)[0]

        holder.binding.apply {
            Glide.with(holder.itemView).load(image).into(imgAd)
            tvAdPrice.text = "$${product.price}"
            tvAdName.text = product.title
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }


        holder.binding.btnAddToCart.setOnClickListener {
            onAddToCartClick?.invoke(product)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((Product) -> Unit)? = null

    var onAddToCartClick: ((Product) -> Unit)? = null

}