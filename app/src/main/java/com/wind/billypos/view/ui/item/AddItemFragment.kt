package com.wind.billypos.view.ui.item

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentItemAddBinding
import com.wind.billypos.utils.ItemType
import com.wind.billypos.utils.clearStringToDouble
import com.wind.billypos.utils.formatPrice
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.item.viewmodel.AddItemViewModel
import kotlinx.android.synthetic.main.fragment_item_add.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.text.ParseException

class AddItemFragment : BaseFragment<FragmentItemAddBinding, AddItemViewModel>(),
    AdapterView.OnItemSelectedListener {

    private val addItemViewModel: AddItemViewModel by viewModel()

    private var mSubcategoriesList: List<SubCategory> = ArrayList()
    private lateinit var mCategoryList: List<Category>
    private lateinit var itemCategoryAdapter: ArrayAdapter<String>
    private lateinit var subCategoryAdapter: ArrayAdapter<String>
    private lateinit var vatListAdapter: ArrayList<String>
    private lateinit var typeListAdapter: ArrayList<String>

    private var subCategory = SubCategory()
    private var category = Category()
    private var vat = "0"
    private var type = "ITEM"

    private var unitPrice: Double = 0.0
    private var totalItemPrice: Double = 0.0
    private var priceWithVat: Double = 0.0
    private var itemDiscount: Double = 0.0

    private lateinit var totalPriceTextWatcher: PriceTextWatcher
    private lateinit var priceWithVatTextWatcher: PriceTextWatcher
    private lateinit var discountTextWatcher: PriceTextWatcher

    override val bindingVariable: Int
        get() = BR.addItemViewModel
    override val layoutId: Int
        get() = R.layout.fragment_item_add
    override val viewModel: AddItemViewModel
        get() = addItemViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateView()

        totalPriceTextWatcher = PriceTextWatcher(totalPrice)
        priceWithVatTextWatcher = PriceTextWatcher(itemPrice)
        discountTextWatcher = PriceTextWatcher(discount)

        totalPrice.addTextChangedListener(totalPriceTextWatcher)
        itemPrice.addTextChangedListener(priceWithVatTextWatcher)
        discount.addTextChangedListener(discountTextWatcher)


        val observerItem = Observer<Item> {
            it?.let {
                if (it.hasChanges()) {
                    when {
                        it.isSuccess() -> {
                            viewModel.sendItem(item = it)
                        }
                        it.hasError() -> {
                            Timber.e("Network error")
                        }
                        it.isItemNameIncorrect() -> {
                            itemName.error = getString(R.string.field_is_required)
                        }
                        it.isItemPriceIncorrect() -> {
                            itemPrice.error = getString(R.string.field_is_required)
                        }
                        it.isItemPriceWithDiscountIncorrect() -> {
                            totalPrice.error = getString(R.string.field_is_required)
                        }
                        it.isItemBarcodeIncorrect() -> {
                            itemBarcode.error = getString(R.string.field_is_required)
                        }
                        it.isItemUnitIncorrect() -> {
                            itemUnit.error = getString(R.string.field_is_required)
                        }
                        it.areDataCorrect() -> {
                            viewModel.insertItem(item = it)
                        }
                    }
                }
            }
        }

        val observerCategories = Observer<List<Category>> { list ->
            list?.let {
                mCategoryList = list
                itemCategoryAdapter.clear()

                val arrayCategories = ArrayList<String>()

                for (category in list.indices) {
                    if (category == 0) {
                        Timber.e("Updejtuj lista subcategory %s", category)
                        updateSubCategory(list[category])
                    }
                    list[category].categoryName?.let { it1 -> arrayCategories.add(it1) }
                }
                itemCategoryAdapter.addAll(arrayCategories)
                itemCategory.adapter = itemCategoryAdapter
            }
        }

        val observerSubCategories = Observer<List<SubCategory>> { list ->
            mSubcategoriesList = list

            val arrayItems = ArrayList<String>()
            for (subCategory in mSubcategoriesList) {
                subCategory.subcategoryName?.let { arrayItems.add(it) }
            }
            subCategoryAdapter.addAll(arrayItems)
            itemSubCategory.adapter = subCategoryAdapter

            if(mSubcategoriesList.isEmpty()){
                subCategory = SubCategory()
            }
        }

        val observerSync = Observer<Boolean> {
            it?.let {
                if (it) {
                    val item = viewModel.getItem()?.copy(
                        isSync = true,
                        lastServerSync = LocalDateTime.now()
                    )
                    viewModel.updateItem(item = item)
                }
            }
            Toast.makeText(
                requireContext(),
                getString(R.string.item_saved_success),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        viewModel.mutableLiveDataSubCategories.observe(viewLifecycleOwner, observerSubCategories)
        viewModel.mutableLiveDataCategories.observe(viewLifecycleOwner, observerCategories)
        viewModel.mutableLiveDataItem.observe(viewLifecycleOwner, observerItem)
        viewModel.mutableLiveDataSync.observe(viewLifecycleOwner, observerSync)

        viewModel.getCategories()
    }

    inner class PriceTextWatcher(private val textInputEditText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(stringText: CharSequence, start: Int, before: Int, count: Int) {
            totalPrice.removeTextChangedListener(totalPriceTextWatcher)
            itemPrice.removeTextChangedListener(priceWithVatTextWatcher)
            discount.removeTextChangedListener(discountTextWatcher)
            when (textInputEditText.id) {
                R.id.totalPrice -> {
                    if (totalPrice.text?.isNotEmpty() == true) {
                        try {
                            calculateTotal(stringText)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    } else {
                        totalPrice.setText("")
                    }
                }
                R.id.itemPrice -> {
                    if (itemPrice.text?.isNotEmpty() == true) {
                        try {
                            calculateItemPrice(stringText)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    } else {
                        itemPrice.setText("")
                    }
                }
                R.id.discount -> {
                    if (discount.text?.isNotEmpty() == true) {
                        try {
                            calculateDiscount(stringText)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    } else {
                        discount.setText("")
                    }
                }
            }
            totalPrice.addTextChangedListener(totalPriceTextWatcher)
            itemPrice.addTextChangedListener(priceWithVatTextWatcher)
            discount.addTextChangedListener(discountTextWatcher)
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun updateView() {
        //item types
        val arrayItemTypes = ArrayList<String>()
        for (item in ItemType.values()) {
            arrayItemTypes.add(item.toString())
        }
        typeListAdapter = arrayItemTypes
        val itemTypeAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayItemTypes
        )
        itemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemType.adapter = itemTypeAdapter

        itemType.onItemSelectedListener = this

        //item vat
        val vatArray = arrayListOf("20", "10", "6", "0")
        vatListAdapter = vatArray
        val itemVatAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vatArray)
        itemVatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemVAT.adapter = itemVatAdapter

        itemVAT.onItemSelectedListener = this

        //item category
        itemCategoryAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayListOf()
        )
        itemCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemCategory.adapter = itemCategoryAdapter

        itemCategory.onItemSelectedListener = this


        //item subCategoryAdapter
        subCategoryAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayListOf()
        )
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemSubCategory.adapter = subCategoryAdapter

        itemSubCategory.onItemSelectedListener = this

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_item -> {
                saveItem()
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
        inflater.inflate(R.menu.menu_add_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        when (p0?.id) {
            R.id.itemSubCategory -> {
                subCategory = mSubcategoriesList[position]
                Timber.e("subCategory %s ", subCategory)
            }
            R.id.itemCategory -> {
                category = mCategoryList[position]
                updateSubCategory(category)
            }
            R.id.itemVAT -> {
                vat = vatListAdapter[position]
            }
            R.id.itemType -> {
                type = typeListAdapter[position]
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    private fun updateSubCategory(category: Category) {
        subCategoryAdapter.clear()

        viewModel.getSubcategories(category)
    }

    private fun saveItem() {
        if (itemPrice.text.isNullOrEmpty()) {
            itemPrice.error = getString(R.string.field_is_required)
            return
        }


        val item = Item(
            itemName = itemName.text.toString(),
            itemPrice = priceWithVat,
            unitPrice = totalItemPrice / (1 + (vat.toDouble()) / 100),
            itemPriceWithDiscount = totalItemPrice,
            discount = itemDiscount,
            vat = vat.toDoubleOrNull(),
            itemDesc = itemDescription.text.toString(),
            idCategory = category.id,
            idSubcategory = subCategory.id,
            itemType = type,
            itemUnit = itemUnit.text.toString(),
            barcode = itemBarcode.text.toString()
        )


        viewModel.setItem(item = item)
    }


    private fun calculateTotal(text: CharSequence) {
        var currency = text.clearStringToDouble() / 100
        var currencyFormatted = currency.formatPrice()
        if(currency > priceWithVat){
            val removeLast = text.dropLast(1)
            currency = removeLast.clearStringToDouble() / 100
            currencyFormatted = currency.formatPrice()
            totalPrice.setText(currencyFormatted)
            totalPrice.setSelection(currencyFormatted.length)
            Toast.makeText(requireContext(), getString(R.string.item_total_error), Toast.LENGTH_SHORT).show()
            return
        }
        totalPrice.setText(currencyFormatted)
        totalPrice.setSelection(currencyFormatted.length)
        totalItemPrice = currency
        itemDiscount =  String.format("%.2f", (100 * (1 - (totalItemPrice/priceWithVat)))).toDouble()
        discount.setText(itemDiscount.formatPrice())
    }

    private fun calculateItemPrice(text: CharSequence) {
        val currency = text.clearStringToDouble() / 100
        val currencyFormatted = currency.formatPrice()

        itemPrice.setText(currencyFormatted)
        itemPrice.setSelection(currencyFormatted.length)
        priceWithVat = currency
        totalItemPrice = String.format("%.2f", (priceWithVat - (priceWithVat * (itemDiscount/100)))).toDouble()
        totalPrice.setText(totalItemPrice.formatPrice())
    }


    private fun calculateDiscount(text: CharSequence) {
        var currency = text.clearStringToDouble() / 100
        var currencyFormatted = currency.formatPrice()
        if(currency > 100){
            val removeLast = text.dropLast(1)
            currency = removeLast.clearStringToDouble() / 100
            currencyFormatted = currency.formatPrice()
            discount.setText(currencyFormatted)
            discount.setSelection(currencyFormatted.length)
            Toast.makeText(requireContext(), getString(R.string.discount_error), Toast.LENGTH_SHORT).show()
            return
        }

        discount.setText(currencyFormatted)
        discount.setSelection(currencyFormatted.length)
        itemDiscount = currency
        totalItemPrice = String.format("%.2f", (priceWithVat - (priceWithVat*(itemDiscount/100)))).toDouble()
        totalPrice.setText(totalItemPrice.formatPrice())
    }



}