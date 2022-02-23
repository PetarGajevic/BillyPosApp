package com.wind.billypos.view.ui.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowCustomerBinding
import com.wind.billypos.databinding.RowItemBinding
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.model.Item

class CustomerAdapter(
    private val showCustomerClick: (Long, Customer, Int) -> Unit
) : ListAdapter<Customer, CustomerAdapter.ViewHolderItem>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderItem(
            RowCustomerBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            showCustomerClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderItem(
        private val rowCustomerBinding: RowCustomerBinding?,
        private val showItemClick: (Long, Customer, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowCustomerBinding!!.root) {
        fun bind(model: Customer) {
            with(model) {
                itemView.setOnClickListener {
                    showItemClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                rowCustomerBinding?.customer = this
                rowCustomerBinding?.executePendingBindings()
            }
        }
    }

}

class DiffCallBack :
    DiffUtil.ItemCallback<Customer>() {

    override fun areItemsTheSame(oldList: Customer, newList: Customer): Boolean =
        oldList.id == newList.id

    override fun areContentsTheSame(oldList: Customer, newList: Customer): Boolean =
        oldList == newList


}