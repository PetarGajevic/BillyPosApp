package com.wind.billypos.view.ui.category

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentAddCategoryBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.ui.category.viewmodel.AddCategoryViewModel
import com.wind.billypos.view.ui.operators.OperatorFragmentDirections
import com.wind.billypos.view.ui.operators.viewmodel.OperatorViewModel
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_item_add.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddCategoryFragment: BaseFragment<FragmentAddCategoryBinding, AddCategoryViewModel>() {

    private val addCategoryViewModel: AddCategoryViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.addCategoryViewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_category
    override val viewModel: AddCategoryViewModel
        get() = addCategoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val observerCategory = Observer<Category> {
            it?.let {
                if(it.hasChanges()){
                    when {
                        it.isSuccess() -> {
                            Timber.e("Success %s", it)
                            viewModel.sendCategory(category = it)
                        }
                        it.hasError() -> {
                            Timber.e("Network error")
                        }
                        it.isCategoryNameIsIncorrect() -> {
                            Toast.makeText(requireContext(), getString(R.string.category_name_error), Toast.LENGTH_LONG).show()
                        }
                        it.areDataCorrect() -> {
                           viewModel.insertCategory(category = it)
                        }
                    }
                }
            }
        }

        val observerSyncCategory = Observer<String> {
            if(it == "OK"){
                Toast.makeText(requireContext(), getString(R.string.category_saved_success), Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(), getString(R.string.category_saved_error), Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
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
        val category = Category(
            categoryName = categoryName.text.toString()
        )

        viewModel.setCategory(category = category)
    }
}