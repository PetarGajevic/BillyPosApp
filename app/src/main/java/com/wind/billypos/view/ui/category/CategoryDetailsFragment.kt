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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.databinding.FragmentCategoryDetailsBinding
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.category.adapter.SubCategoryAdapter
import com.wind.billypos.view.ui.category.viewmodel.CategoryDetailsViewModel
import com.wind.billypos.view.ui.category.viewmodel.CategoryViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_category_details.*
import kotlinx.android.synthetic.main.fragment_category_details.searchView
import kotlinx.android.synthetic.main.fragment_edit_operator.*
import kotlinx.android.synthetic.main.fragment_operators.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryDetailsFragment :
    BaseFragment<FragmentCategoryDetailsBinding, CategoryDetailsViewModel>() {

    private val categoryDetailsViewModel: CategoryDetailsViewModel by viewModel()

    private val args: CategoryDetailsFragmentArgs by navArgs()

    private var category = Category()

    override val bindingVariable: Int
        get() = BR.categoryDetailsViewModel
    override val layoutId: Int
        get() = R.layout.fragment_category_details
    override val viewModel: CategoryDetailsViewModel
        get() = categoryDetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        category = args.category
        viewDataBinding?.category = category

//        Add line between subcategory items
        decorateRecycler()

        val subCategoryAdapter = SubCategoryAdapter(
            showSubCategoryClick = { _, subcategory, _ ->
                if (subcategory.isActive == true) {
                    findNavController().navigateSafe(
                        CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToSubCategoryDetailsFragment(
                            subcategory = subcategory
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cant_edit_subcategory, subcategory.subcategoryName),
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            enableSubCategoryClick = { _, subcategory, _ ->
                enableDisableSubCategoryDialog(
                    subCategory = subcategory.copy(isActive = true),
                    message = getString(
                        R.string.subcategory_activate,
                        subcategory.subcategoryName
                    )
                )
            },
            disableSubCategoryClick = { _, subcategory, _ ->
                    enableDisableSubCategoryDialog(
                        subCategory = subcategory.copy(isActive = false),
                        message = getString(
                            R.string.subcategory_deactivate,
                            subcategory.subcategoryName
                        )
                    )
            }
        )

        rvSubcategories.adapter = subCategoryAdapter

        val observerSubCategories = Observer<List<SubCategory>> {
            it.let(subCategoryAdapter::submitList)
        }

        val observerUpdateSubCategory = Observer<Boolean> {
            viewModel.getSubcategories(id = category.id)
        }

        val observerActivateSubCategories = Observer<Boolean> {
            viewModel.getSubcategories(id = category.id)
        }

        val observerActivateCategory = Observer<Boolean> {
            viewModel.findCategoryById(id = category.id)
        }

        val observerCategory = Observer<Category> {
            category = it
            viewDataBinding?.category = category
        }

        editCategory.setOnClickListener {
            if(category.isActive == true){
                findNavController().navigateSafe(CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToEditCategoryFragment(category = category))
            }else{
                Toast.makeText(requireContext(), getString(R.string.cant_edit_category, category.categoryName), Toast.LENGTH_LONG).show()
            }
        }

        subcategorySwitch.setOnCheckedChangeListener { _, check ->
            viewModel.updateSubCategories(id = category.id, isActive = check)
            if(category.isActive != null && check){
                viewModel.activateCategory(id = category.id)
            }

            switchText.text = if(check){
              getString(R.string.deactivate_all_subcategories)
            }else{
                getString(R.string.activate_all_subcategories)
            }
        }

        //        Search Subcategories
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                searchSubCategories(input = newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchSubCategories(input = query)
                return false
            }
        })

        viewModel.mutableLiveDataSubCategories.observe(viewLifecycleOwner, observerSubCategories)
        viewModel.mutableLiveDataActivateSubCategory.observe(viewLifecycleOwner, observerActivateSubCategories)
        viewModel.mutableLiveDataActivateCategory.observe(viewLifecycleOwner, observerActivateCategory)
        viewModel.mutableLiveDataCategory.observe(viewLifecycleOwner, observerCategory)
        viewModel.mutableLiveDataSubUpdateSubCategory.observe(
            viewLifecycleOwner,
            observerUpdateSubCategory
        )

        viewModel.getSubcategories(id = category.id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_subcategory -> {
                findNavController().navigateSafe(
                    CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToAddSubCategoryFragment(
                        category = category
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_category_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun enableDisableSubCategoryDialog(subCategory: SubCategory, message: String) {
        ConfirmAlert(requireContext(), object : YesNoDialogInterface {
            override fun onConfirm(dialog: AlertDialog) {
                viewModel.updateSubCategory(subcategory = subCategory)
                if (category.isActive != true) {
                    viewModel.activateCategory(id = category.id)
                }
            }

            override fun onCancel(dialog: AlertDialog) {}
        }).showDialog(
            message,
            resources.getString(R.string.attention)
        )
    }

    private fun searchSubCategories(input: String?) {
        viewModel.searchSubCategories(input = input, idCategory = category.id)
    }

    private fun decorateRecycler(){
        // initialize an instance of linear layout manager
        val layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        ).apply {
            // specify the layout manager for recycler view
            rvSubcategories.layoutManager = this
        }

        // initialize an instance of divider item decoration
        DividerItemDecoration(
            requireContext(), // context
            layoutManager.orientation
        ).apply {
            // add divider item decoration to recycler view
            // this will show divider line between items
            rvSubcategories.addItemDecoration(this)
        }
    }

}