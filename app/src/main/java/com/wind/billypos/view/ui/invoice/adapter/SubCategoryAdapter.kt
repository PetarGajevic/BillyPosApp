package com.wind.billypos.view.ui.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.databinding.RowCategoryFilterBinding
import com.wind.billypos.databinding.RowSubcategoryFilterBinding
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.SubCategory

class SubCategoryAdapter(
    private val subCategoryClick: (Long, SubCategory, Int) -> Unit
) : ListAdapter<SubCategory, SubCategoryAdapter.ViewHolderSubCategory>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSubCategory {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderSubCategory(
            RowSubcategoryFilterBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            subCategoryClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolderSubCategory, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolderSubCategory(
        private val rowSubcategoryFilterBinding: RowSubcategoryFilterBinding?,
        private val subCategoryClick: (Long, SubCategory, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowSubcategoryFilterBinding!!.root) {
        fun bind(model: SubCategory) {
            with(model) {
                itemView.setOnClickListener {
                    subCategoryClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderSubCategory.adapterPosition
                    )
                }
                rowSubcategoryFilterBinding?.subcategory = this
                rowSubcategoryFilterBinding?.executePendingBindings()
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