package com.wind.billypos.view.ui.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wind.billypos.R
import com.wind.billypos.databinding.RowCartTransactionBodyBinding
import com.wind.billypos.databinding.RowTransactionBodyBinding
import com.wind.billypos.utils.formatReceipt
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionBodyWithItem
import kotlinx.android.synthetic.main.row_cart_transaction_body.view.*


class TransactionBodyAdapter(
    private val transactionBodyClick: (Long,TransactionBody, Int) -> Unit
) : ListAdapter<TransactionBody, TransactionBodyAdapter.ViewHolderTransactionBody>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTransactionBody {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderTransactionBody(
            RowCartTransactionBodyBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            transactionBodyClick
        )
    }



    override fun onBindViewHolder(holder: ViewHolderTransactionBody, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderTransactionBody(
        private val rowCartTransactionBodyBinding: RowCartTransactionBodyBinding?,
        private val transactionBodyClick: (Long, TransactionBody, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowCartTransactionBodyBinding!!.root) {
        fun bind(model: TransactionBody) {
            with(model) {
                itemView.setOnClickListener {
                    transactionBodyClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderTransactionBody.adapterPosition
                    )
                }
                itemView.itemPrice.text = model.totalWithVat?.formatReceipt()
                rowCartTransactionBodyBinding?.transactionBody = this
                rowCartTransactionBodyBinding?.executePendingBindings()
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
