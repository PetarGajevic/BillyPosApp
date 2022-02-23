package com.wind.billypos.view.ui.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowItemGridBinding
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Item

class ItemGridAdapter(
    private val itemClick: (Long, Item, Int) -> Unit
) : ListAdapter<Item, ItemGridAdapter.ViewHolderItem>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderItem(
            RowItemGridBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            itemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderItem(
        private val rowItemGridBinding: RowItemGridBinding?,
        private val itemClick: (Long, Item, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowItemGridBinding!!.root) {
        fun bind(model: Item) {
            with(model) {
                itemView.setOnClickListener {
                    itemClick(
                        model.id,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                rowItemGridBinding?.item = this
                rowItemGridBinding?.executePendingBindings()
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

}