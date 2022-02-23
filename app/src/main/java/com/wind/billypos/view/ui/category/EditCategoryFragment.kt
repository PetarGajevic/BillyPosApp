package com.wind.billypos.view.ui.category

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentEditCategoryBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.ui.category.viewmodel.EditCategoryViewModel
import kotlinx.android.synthetic.main.fragment_add_category.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditCategoryFragment: BaseFragment<FragmentEditCategoryBinding, EditCategoryViewModel>() {

    private val editCategoryViewModel: EditCategoryViewModel by viewModel()

    private val args: EditCategoryFragmentArgs by navArgs()

    private var category = Category()

    override val bindingVariable: Int
        get() = BR.editCategoryViewModel
    override val layoutId: Int
        get() = R.layout.fragment_edit_category
    override val viewModel: EditCategoryViewModel
        get() = editCategoryViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        category = args.category
        viewDataBinding?.category = category


        val observerCategory = Observer<Category> {
            it?.let {
                if(it.hasChanges()){
                    when {
                        it.isSuccess() -> {
                            Timber.e("Success %s", it)
                            category = it
                            viewModel.sendUpdateCategory(category = it)
                        }
                        it.hasError() -> {
                            Timber.e("Network error")
                        }
                        it.isCategoryNameIsIncorrect() -> {
                            Toast.makeText(requireContext(), getString(R.string.category_name_error), Toast.LENGTH_LONG).show()
                        }
                        it.areDataCorrect() -> {
                            viewModel.updateCategory(category = it)
                        }
                    }
                }
            }
        }

        val observerSyncCategory = Observer<String> {
            if(it == "OK") {
                Toast.makeText(requireContext(), getString(R.string.category_saved_success), Toast.LENGTH_LONG).show()
                findNavController().navigateSafe(EditCategoryFragmentDirections.actionEditCategoryFragmentToCategoryDetailsFragment(category = category))
            } else {
                Toast.makeText(requireContext(), getString(R.string.category_saved_error), Toast.LENGTH_LONG).show()
                findNavController().navigateSafe(EditCategoryFragmentDirections.actionEditCategoryFragmentToCategoryDetailsFragment(category = category))
            }
        }

        viewModel.mutableLiveDataCategory.observe(viewLifecycleOwner, observerCategory)
        viewModel.mutableLiveDataSyncCategory.observe(viewLifecycleOwner, observerSyncCategory)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_category -> {
                saveCategory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_category, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun saveCategory(){
        val category = category.copy(
            categoryName = categoryName.text.toString()
        )

        viewModel.setCategory(category = category)
    }
}