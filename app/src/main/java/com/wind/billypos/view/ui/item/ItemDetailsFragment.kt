package com.wind.billypos.view.ui.item

import android.app.AlertDialog
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
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.databinding.FragmentItemDetailsBinding
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.ui.item.viewmodel.ItemDetailsViewModel
import kotlinx.android.synthetic.main.fragment_item_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ItemDetailsFragment: BaseFragment<FragmentItemDetailsBinding, ItemDetailsViewModel>() {

    private val itemDetailsViewModel: ItemDetailsViewModel by viewModel()

    private val args: ItemDetailsFragmentArgs by navArgs()

    private var item = Item()

    override val bindingVariable: Int
        get() = BR.itemDetailsViewModel
    override val layoutId: Int
        get() = R.layout.fragment_item_details
    override val viewModel: ItemDetailsViewModel
        get() = itemDetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        item = args.item
        viewDataBinding?.item = item
        viewModel.mutableLiveDataIsItemActive.value = item.isActive

        disable.setOnClickListener {
            Timber.e("Disable")
            enableDisableCategoryDialog(
                item = item.copy(isActive = false),
                message = getString(R.string.item_deactivate, item.itemName)
            )
        }

        enable.setOnClickListener {
            Timber.e("Enable")
            enableDisableCategoryDialog(
                item = item.copy(isActive = true),
                message = getString(R.string.item_activate, item.itemName)
            )
        }

        val observerCategoryName = Observer<String> {
            itemCategory.text = it
        }

        val observerSubCategoryName = Observer<String> {
            itemSubCategory.text = it
        }

        val observerItem = Observer<Item> {
                item = it
                viewDataBinding?.item = item
                Toast.makeText(requireContext(), getString(R.string.item_updated_successfully), Toast.LENGTH_LONG).show()
        }

        viewModel.mutableLiveDataCategoryName.observe(viewLifecycleOwner, observerCategoryName)
        viewModel.mutableLiveDataSubCategoryName.observe(viewLifecycleOwner, observerSubCategoryName)
        viewModel.mutableLiveDataItem.observe(viewLifecycleOwner, observerItem)

        viewModel.getCategoryByItemId(idCategory = item.idCategory)
        viewModel.getSubCategoryByItemId(idSubCategory = item.idSubcategory)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                if(this.item.isActive == true){
                    findNavController().navigateSafe(ItemDetailsFragmentDirections.actionItemDetailsFragmentToEditItemFragment(item = this.item))
                }else{
                    Toast.makeText(requireContext(), getString(R.string.cant_edit_item, this.item.itemName), Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun enableDisableCategoryDialog(item: Item, message: String) {
        ConfirmAlert(requireContext(), object : YesNoDialogInterface {
            override fun onConfirm(dialog: AlertDialog) {
                viewModel.updateItem(item = item)
            }

            override fun onCancel(dialog: AlertDialog) {}
        }).showDialog(
            message,
            resources.getString(R.string.attention)
        )
    }


}