package com.wind.billypos.view.ui.invoice.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.R
import com.wind.billypos.databinding.RowCategoryFilterBinding
import com.wind.billypos.view.model.Category
import kotlinx.android.synthetic.main.fragment_invoice.view.*

class CategoryAdapter(
    private val categoryClick: (Long, Category, Int) -> Unit
) : ListAdapter<Category, CategoryAdapter.ViewHolderCategory>(DiffCallBack()) {

    private var lastViewClicked : RecyclerView.ViewHolder? = null
    private var categoryNameView: TextView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCategory {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderCategory(
            RowCategoryFilterBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            categoryClick
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolderCategory, position: Int) =
        holder.bind(getItem(position))




    inner class ViewHolderCategory(
        private val rowCategoryFilterBinding: RowCategoryFilterBinding?,
        private val showItemClick: (Long, Category, Int) -> Unit
    ) : RecyclerView.ViewHolder(rowCategoryFilterBinding!!.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(model: Category) {
            with(model) {
                itemView.setOnClickListener {
                    categoryClick(
                        model.id ?: -1L,
                        model,
                        this@ViewHolderCategory.adapterPosition
                    )
                }
                rowCategoryFilterBinding?.category = this
                rowCategoryFilterBinding?.executePendingBindings()
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