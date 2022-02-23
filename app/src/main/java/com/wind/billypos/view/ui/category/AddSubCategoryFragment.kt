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
import com.wind.billypos.databinding.FragmentAddSubcategoryBinding
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.category.viewmodel.AddSubCategoryViewModel
import kotlinx.android.synthetic.main.fragment_add_subcategory.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddSubCategoryFragment: BaseFragment<FragmentAddSubcategoryBinding, AddSubCategoryViewModel>() {

    private val addSubCategoryViewModel: AddSubCategoryViewModel by viewModel()

    private val args: AddSubCategoryFragmentArgs by navArgs()

    override val bindingVariable: Int
        get() = BR.addOperatorViewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_subcategory
    override val viewModel: AddSubCategoryViewModel
        get() = addSubCategoryViewModel

    private var category = Category()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        category = args.category

        val observerCategory = Observer<SubCategory> {
            it?.let {
                if(it.hasChanges()){
                    when {
                        it.isSuccess() -> {
                            Timber.e("Success %s", it)
                            viewModel.sendSubCategory(subcategory = it)
                        }
                        it.hasError() -> {
                            Timber.e("Network error")
                        }
                        it.isSubCategoryNameIsIncorrect() -> {
                            Toast.makeText(requireContext(), getString(R.string.subcategory_name_error), Toast.LENGTH_LONG).show()
                        }
                        it.areDataCorrect() -> {
                            viewModel.insertSubCategory(subcategory = it)
                        }
                    }
                }
            }
        }

        val observerSyncSubCategory = Observer<String> {
            if(it == "OK"){
                Toast.makeText(requireContext(), getString(R.string.subcategory_saved_success), Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(), getString(R.string.subcategory_saved_error), Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }

        viewModel.mutableLiveDataSubCategory.observe(viewLifecycleOwner, observerCategory)
        viewModel.mutableLiveDataSyncSubCategory.observe(viewLifecycleOwner, observerSyncSubCategory)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_subcategory -> {
                saveSubCategory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_subcategory, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun saveSubCategory(){
        val subcategory = SubCategory(
            subcategoryName = subcategoryName.text.toString(),
            id_category = category.id
        )

        viewModel.setSubCategory(subcategory = subcategory)
    }

}