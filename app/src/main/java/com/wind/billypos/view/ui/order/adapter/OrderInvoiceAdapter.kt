package com.wind.billypos.view.ui.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowEntityGridBinding
import com.wind.billypos.databinding.RowItemBinding
import com.wind.billypos.utils.formatReceipt
import com.wind.billypos.view.model.EntityPoint
import com.wind.billypos.view.model.Item
import kotlinx.android.synthetic.main.row_item.view.*


class OrderInvoiceAdapter(
    private val orderInvoiceClick: (Long, EntityPoint, Int) -> Unit
) : ListAdapter<EntityPoint, OrderInvoiceAdapter.ViewHolderOrderInvoice>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOrderInvoice {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderOrderInvoice(
            RowEntityGridBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            orderInvoiceClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderOrderInvoice, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderOrderInvoice(
        private val rowEntityGridBinding: RowEntityGridBinding?,
        private val orderInvoiceClick: (Long, EntityPoint, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowEntityGridBinding!!.root) {
        fun bind(model: EntityPoint) {
            with(model) {
                itemView.setOnClickListener {
                    orderInvoiceClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderOrderInvoice.adapterPosition
                    )
                }
                rowEntityGridBinding?.entity = this
                rowEntityGridBinding?.executePendingBindings()
            }
        }
    }

    class DiffCallBack :
        DiffUtil.ItemCallback<EntityPoint>() {

        override fun areItemsTheSame(oldList: EntityPoint, newList: EntityPoint): Boolean =
            oldList.id == newList.id &&
                    oldList.isOpen == newList.isOpen


        override fun areContentsTheSame(oldList: EntityPoint, newList: EntityPoint): Boolean =
            oldList == newList &&
                    oldList.isOpen == newList.isOpen


    }

}

