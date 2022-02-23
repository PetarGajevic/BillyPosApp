package com.wind.billypos.view.ui.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.databinding.FragmentCategoryBinding
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.ui.category.adapter.CategoryAdapter
import com.wind.billypos.view.ui.category.viewmodel.CategoryViewModel
import com.wind.billypos.view.ui.operators.OperatorFragmentDirections
import com.wind.billypos.view.ui.operators.adapter.OperatorAdapter
import com.wind.billypos.view.ui.operators.viewmodel.OperatorViewModel
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_category.searchView
import kotlinx.android.synthetic.main.fragment_category_details.*
import kotlinx.android.synthetic.main.fragment_operators.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment: BaseFragment<FragmentCategoryBinding, CategoryViewModel>() {

    private val categoryViewModel: CategoryViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.categoryViewModel
    override val layoutId: Int
        get() = R.layout.fragment_category
    override val viewModel: CategoryViewModel
        get() = categoryViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        decorateRecycler()

        val categoryAdapter =
            CategoryAdapter(
                showCategoryClick = { _, category, _ ->
                        findNavController().navigateSafe(
                           CategoryFragmentDirections.actionCategoryFragmentToCategoryDetailsFragment(category = category)
                        )
                },
                enableCategoryClick = { _, category, _ ->
                    enableDisableCategoryDialog(
                        category = category.copy(isActive = true),
                        message = getString(R.string.category_activate, category.categoryName)
                    )
                },
                disableCategoryClick = { _, category, _ ->
                    enableDisableCategoryDialog(
                        category = category.copy(isActive = false),
                        message = getString(R.string.category_deactivate, category.categoryName)
                    )
                }
            )

        rvCategories.adapter = categoryAdapter

        //        Search Operators
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                searchCategories(input = newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchCategories(input = query)
                return false
            }
        })

        val observerCategories = Observer<List<Category>> {
            it.let(categoryAdapter::submitList)
        }

        val observerUpdateCategory = Observer<Boolean> {
            searchCategories(input = searchView.query.toString())
        }

        viewModel.mutableLiveDataCategories.observe(viewLifecycleOwner, observerCategories)
        viewModel.mutableLiveDataUpdateCategory.observe(viewLifecycleOwner, observerUpdateCategory)

        searchCategories(input = searchView.query.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_category -> {
                findNavController().navigateSafe(CategoryFragmentDirections.actionCategoryFragmentToAddCategoryFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_category, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun enableDisableCategoryDialog(category: Category, message: String) {
        ConfirmAlert(requireContext(), object : YesNoDialogInterface {
            override fun onConfirm(dialog: AlertDialog) {
                viewModel.updateCategory(category = category)
                viewModel.updateSubCategories(id = category.id)
            }

            override fun onCancel(dialog: AlertDialog) {}
        }).showDialog(
            message,
            resources.getString(R.string.attention)
        )
    }

    private fun searchCategories(input: String?) {
        viewModel.searchCategories(input = input)
    }

    private fun decorateRecycler(){
        // initialize an instance of linear layout manager
        val layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        ).apply {
            // specify the layout manager for recycler view
            rvCategories.layoutManager = this
        }

        // initialize an instance of divider item decoration
        DividerItemDecoration(
            requireContext(), // context
            layoutManager.orientation
        ).apply {
            // add divider item decoration to recycler view
            // this will show divider line between items
            rvCategories.addItemDecoration(this)
        }
    }


}