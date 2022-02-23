package com.wind.billypos.view.ui.item

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentItemsBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.ui.item.adapter.ItemAdapter
import com.wind.billypos.view.ui.item.viewmodel.ItemViewModel
import kotlinx.android.synthetic.main.fragment_customer.*
import kotlinx.android.synthetic.main.fragment_items.*
import kotlinx.android.synthetic.main.fragment_items.searchView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ItemFragment: BaseFragment<FragmentItemsBinding, ItemViewModel>() {

    private val itemViewModel: ItemViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.itemViewModel
    override val layoutId: Int
        get() = R.layout.fragment_items
    override val viewModel: ItemViewModel
        get() = itemViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter =
            ItemAdapter(
                showItemClick = { _, item, _ ->
                   findNavController().navigateSafe(ItemFragmentDirections.actionItemFragmentToItemDetailsFragment(item = item))
                }
            )

        rvItems.adapter = itemAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                searchItems(input = newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchItems(input = query)
                return false
            }
        })

        val observerItems = Observer<List<Item>> { items ->
            items.let(itemAdapter::submitList)
        }

        viewModel.mutableLiveDataItems.observe(
            viewLifecycleOwner,
            observerItems
        )

        viewModel.getItems()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_item -> {
                findNavController().navigateSafe(ItemFragmentDirections.actionItemFragmentToAddItemFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchItems(input: String?){
        viewModel.searchItems(input = input)
    }
}