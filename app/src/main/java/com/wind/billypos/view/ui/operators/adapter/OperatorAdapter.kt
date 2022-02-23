package com.wind.billypos.view.ui.operators.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowItemBinding
import com.wind.billypos.databinding.RowOperatorBinding
import com.wind.billypos.utils.formatReceipt
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.model.Operator
import kotlinx.android.synthetic.main.row_item.view.*
import kotlinx.android.synthetic.main.row_operator.view.*


class OperatorAdapter(
    private val showOperatorClick: (Long, Operator, Int) -> Unit,
    private val enableOperatorClick: (Long, Operator, Int) -> Unit,
    private val disableOperatorClick: (Long, Operator, Int) -> Unit
) : ListAdapter<Operator, OperatorAdapter.ViewHolderItem>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderItem(
            RowOperatorBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            showOperatorClick,
            enableOperatorClick,
            disableOperatorClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderItem(
        private val rowOperatorBinding: RowOperatorBinding?,
        private val showOperatorClick: (Long, Operator, Int) -> Unit,
        private val enableOperatorClick: (Long, Operator, Int) -> Unit,
        private val disableOperatorClick: (Long, Operator, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowOperatorBinding!!.root) {
        fun bind(model: Operator) {
            with(model) {
                itemView.setOnClickListener {
                    showOperatorClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                itemView.activate.setOnClickListener {
                    enableOperatorClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                itemView.deactivate.setOnClickListener {
                    disableOperatorClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                rowOperatorBinding?.operator = this
                rowOperatorBinding?.executePendingBindings()
            }
        }
    }

}

class DiffCallBack :
    DiffUtil.ItemCallback<Operator>() {

    override fun areItemsTheSame(oldList: Operator, newList: Operator): Boolean =
        oldList.id == newList.id

    override fun areContentsTheSame(oldList: Operator, newList: Operator): Boolean =
        oldList == newList

}