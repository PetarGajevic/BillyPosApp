package com.wind.billypos.view.ui.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowCartTransactionBodyBinding
import com.wind.billypos.databinding.RowTransactionBodyDetailsBinding
import com.wind.billypos.view.model.TransactionBody

class PrintItemsAdapter() : ListAdapter<TransactionBody, PrintItemsAdapter.ViewHolderTransactionBody>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTransactionBody {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderTransactionBody(
            RowTransactionBodyDetailsBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderTransactionBody, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderTransactionBody(
        private val rowTransactionBodyDetailsBinding: RowTransactionBodyDetailsBinding?
    ) : RecyclerView.ViewHolder(rowTransactionBodyDetailsBinding!!.root) {
        fun bind(model: TransactionBody) {
            with(model) {
                rowTransactionBodyDetailsBinding?.transactionBodyDetails = this
                rowTransactionBodyDetailsBinding?.executePendingBindings()
            }
        }
    }


    class DiffCallBack :
        DiffUtil.ItemCallback<TransactionBody>() {

        override fun areItemsTheSame(oldList: TransactionBody, newList: TransactionBody): Boolean =
            oldList == newList


        override fun areContentsTheSame(oldList: TransactionBody, newList: TransactionBody): Boolean =
            oldList == newList

    }

}