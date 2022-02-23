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
import com.wind.billypos.databinding.FragmentSubcategoryDetailsBinding
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.category.viewmodel.SubCategoryDetailsViewModel
import kotlinx.android.synthetic.main.fragment_subcategory_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SubCategoryDetailsFragment: BaseFragment<FragmentSubcategoryDetailsBinding, SubCategoryDetailsViewModel>() {

    private val subCategoryDetailsViewModel: SubCategoryDetailsViewModel by viewModel()

    private val args: SubCategoryDetailsFragmentArgs by navArgs()

    private var subCategory = SubCategory()

    override val bindingVariable: Int
        get() = BR.subCategoryDetailsViewModel
    override val layoutId: Int
        get() = R.layout.fragment_subcategory_details
    override val viewModel: SubCategoryDetailsViewModel
        get() = subCategoryDetailsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        subCategory = args.subcategory
        viewDataBinding?.subcategory = subCategory

        val observerSubCategory = Observer<SubCategory> {
            it?.let {
                if(it.hasChanges()){
                    when {
                        it.isSuccess() -> {
                            Timber.e("Success %s", it)
                            viewModel.sendUpdateSubCategory(subcategory = it)
                        }
                        it.hasError() -> {
                            Timber.e("Network error")
                        }
                        it.isSubCategoryNameIsIncorrect() -> {
                            Toast.makeText(requireContext(), getString(R.string.subcategory_name_error), Toast.LENGTH_LONG).show()
                        }
                        it.areDataCorrect() -> {
                            viewModel.updateSubCategory(subCategory = it)
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

        viewModel.mutableLiveDataSubCategory.observe(viewLifecycleOwner, observerSubCategory)
        viewModel.mutableLiveDataSyncSubCategory.observe(viewLifecycleOwner, observerSyncSubCategory)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_subcategory_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_subcategory -> {
                saveSubCategory(subCategory = subCategory)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveSubCategory(subCategory: SubCategory) {
        val subcategory = subCategory.copy(
            subcategoryName = subcategoryName.text.toString()
        )

        viewModel.setSubCategory(subcategory = subcategory)
    }

}