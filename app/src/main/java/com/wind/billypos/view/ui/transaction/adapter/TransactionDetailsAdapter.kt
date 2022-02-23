package com.wind.billypos.view.ui.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowCartTransactionBodyBinding
import com.wind.billypos.databinding.RowTransactionBodyBinding
import com.wind.billypos.view.model.TransactionBody

class TransactionDetailsAdapter() : ListAdapter<TransactionBody, TransactionDetailsAdapter.ViewHolderTransactionBody>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTransactionBody {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderTransactionBody(
            RowTransactionBodyBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: ViewHolderTransactionBody, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderTransactionBody(
        private val rowTransactionBodyBinding: RowTransactionBodyBinding?
    ) : RecyclerView.ViewHolder(rowTransactionBodyBinding!!.root) {
        fun bind(model: TransactionBody) {
            with(model) {
                rowTransactionBodyBinding?.transactionBody = this
                rowTransactionBodyBinding?.executePendingBindings()
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