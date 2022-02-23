package com.wind.billypos.view.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowCategoryBinding
import com.wind.billypos.databinding.RowOperatorBinding
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Operator
import kotlinx.android.synthetic.main.row_operator.view.*


class CategoryAdapter(
    private val showCategoryClick: (Long, Category, Int) -> Unit,
    private val enableCategoryClick: (Long, Category, Int) -> Unit,
    private val disableCategoryClick: (Long, Category, Int) -> Unit
) : ListAdapter<Category, CategoryAdapter.ViewHolderItem>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderItem(
            RowCategoryBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            showCategoryClick,
            enableCategoryClick,
            disableCategoryClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderItem(
        private val rowCategoryBinding: RowCategoryBinding?,
        private val showCategoryClick: (Long, Category, Int) -> Unit,
        private val enableCategoryClick: (Long, Category, Int) -> Unit,
        private val disableCategoryClick: (Long, Category, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowCategoryBinding!!.root) {
        fun bind(model: Category) {
            with(model) {
                itemView.setOnClickListener {
                    showCategoryClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                itemView.activate.setOnClickListener {
                    enableCategoryClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                itemView.deactivate.setOnClickListener {
                    disableCategoryClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderItem.adapterPosition
                    )
                }
                rowCategoryBinding?.category = this
                rowCategoryBinding?.executePendingBindings()
            }
        }
    }

     class DiffCallBack :
        DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldList: Category, newList: Category): Boolean =
            oldList.id == newList.id

        override fun areContentsTheSame(oldList: Category, newList: Category): Boolean =
            oldList == newList

    }

}

