package com.wind.billypos.view.ui.order.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.R
import com.wind.billypos.databinding.RowOrderInvoiceBinding
import com.wind.billypos.view.model.EntityPoint
import com.wind.billypos.view.model.TransactionHead
import kotlinx.android.synthetic.main.row_item.view.*
import kotlinx.android.synthetic.main.row_order_invoice.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber


class TransactionOrderAdapter(
    private val orderTransactionClick: (Long, TransactionHead, Int) -> Unit,
    private val orderTransactionLongClick: (Long, TransactionHead, Int) -> Unit,
    private val orderTransactionInfoClick: (Long, TransactionHead, Int) -> Unit
) : ListAdapter<TransactionHead, TransactionOrderAdapter.ViewHolderTransactionOrder>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTransactionOrder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderTransactionOrder(
            RowOrderInvoiceBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            orderTransactionClick,
            orderTransactionLongClick,
            orderTransactionInfoClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderTransactionOrder, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderTransactionOrder(
        private val rowOrderInvoiceBinding: RowOrderInvoiceBinding?,
        private val orderTransactionClick: (Long, TransactionHead, Int) -> Unit,
        private val orderTransactionLongClick: (Long, TransactionHead, Int) -> Unit,
        private val orderTransactionInfoClick: (Long, TransactionHead, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowOrderInvoiceBinding!!.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(model: TransactionHead) {
            with(model) {
                itemView.setOnClickListener {
                    orderTransactionClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderTransactionOrder.adapterPosition
                    )
                }
                itemView.setOnLongClickListener {
                    orderTransactionLongClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderTransactionOrder.adapterPosition
                    )
                    true
                }
                itemView.transactionInfo.setOnClickListener {
                    orderTransactionInfoClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderTransactionOrder.adapterPosition
                    )
                }
//                Set Order status
                itemView.statusTransaction.setText(model.getTransactionStateLabel())
                if (model.isFiscalized == true) {
                    itemView.statusTransaction.setBackgroundColor(R.color.green_500)
                } else {
                    itemView.statusTransaction.setBackgroundColor(R.color.gray_100)
                }
//                 Set Time
                itemView.itemDate.text =
                    model.createdAt.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

                rowOrderInvoiceBinding?.transaction = this
                rowOrderInvoiceBinding?.executePendingBindings()
            }
        }
    }

    class DiffCallBack :
        DiffUtil.ItemCallback<TransactionHead>() {

        override fun areItemsTheSame(oldList: TransactionHead, newList: TransactionHead): Boolean =
            oldList.id == newList.id

        override fun areContentsTheSame(
            oldList: TransactionHead,
            newList: TransactionHead
        ): Boolean =
            oldList == newList

    }

}

