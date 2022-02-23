package com.wind.billypos.view.ui.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowTransactionBodyDetailsBinding
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.model.TransactionBody

class EditItemsAdapter(
    private val editItemClick: (Long?, TransactionBody, Int) -> Unit
) : ListAdapter<TransactionBody, EditItemsAdapter.ViewHolderTransactionBody>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTransactionBody {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderTransactionBody(
            RowTransactionBodyDetailsBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            editItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderTransactionBody, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderTransactionBody(
        private val rowTransactionBodyDetailsBinding: RowTransactionBodyDetailsBinding?,
        private val editItemClick: (Long, TransactionBody, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowTransactionBodyDetailsBinding!!.root) {
        fun bind(model: TransactionBody) {
            with(model) {
                itemView.setOnClickListener {
                    editItemClick(
                        model.id,
                        model,
                        this@ViewHolderTransactionBody.adapterPosition
                    )
                }
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