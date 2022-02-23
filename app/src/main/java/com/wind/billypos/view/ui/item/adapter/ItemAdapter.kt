package com.wind.billypos.view.ui.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.FragmentItemsBinding
import com.wind.billypos.databinding.RowItemBinding
import com.wind.billypos.utils.formatReceipt
import com.wind.billypos.view.model.Item
import kotlinx.android.synthetic.main.row_item.view.*

class ItemAdapter(
    private val showItemClick: (Long, Item, Int) -> Unit
) : ListAdapter<Item, ItemAdapter.ViewHolderItem>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderItem(
            RowItemBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            showItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderItem(
        private val rowItemBinding: RowItemBinding?,
        private val showItemClick: (Long, Item, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowItemBinding!!.root) {
        fun bind(model: Item) {
            with(model) {
                itemView.setOnClickListener {
                    showItemClick(
                        model.id,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                itemView.itemPrice.text = model.itemPriceWithDiscount.formatReceipt()
                rowItemBinding?.item = this
                rowItemBinding?.executePendingBindings()
            }
        }
    }

}

class DiffCallBack :
    DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldList: Item, newList: Item): Boolean =
        oldList.id == newList.id

    override fun areContentsTheSame(oldList: Item, newList: Item): Boolean =
        oldList == newList


}