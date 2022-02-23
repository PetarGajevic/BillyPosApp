package com.wind.billypos.view.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowCategoryBinding
import com.wind.billypos.databinding.RowSubcategoryBinding
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.SubCategory
import kotlinx.android.synthetic.main.row_operator.view.*



class SubCategoryAdapter(
    private val showSubCategoryClick: (Long, SubCategory, Int) -> Unit,
    private val enableSubCategoryClick: (Long, SubCategory, Int) -> Unit,
    private val disableSubCategoryClick: (Long, SubCategory, Int) -> Unit
) : ListAdapter<SubCategory, SubCategoryAdapter.ViewHolderItem>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderItem(
            RowSubcategoryBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            showSubCategoryClick,
            enableSubCategoryClick,
            disableSubCategoryClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderItem(
        private val rowSubcategoryBinding: RowSubcategoryBinding?,
        private val showCategoryClick: (Long, SubCategory, Int) -> Unit,
        private val enableCategoryClick: (Long, SubCategory, Int) -> Unit,
        private val disableCategoryClick: (Long, SubCategory, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowSubcategoryBinding!!.root) {
        fun bind(model: SubCategory) {
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
                rowSubcategoryBinding?.subcategory = this
                rowSubcategoryBinding?.executePendingBindings()
            }
        }
    }

   class DiffCallBack :
        DiffUtil.ItemCallback<SubCategory>() {

        override fun areItemsTheSame(oldList: SubCategory, newList: SubCategory): Boolean =
            oldList.id == newList.id

        override fun areContentsTheSame(oldList: SubCategory, newList: SubCategory): Boolean =
            oldList == newList

    }

}

