package com.wind.billypos.view.ui.invoice.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.invoice.repository.InvoiceRepository
import timber.log.Timber


class InvoiceViewModel(private val invoiceRepository: InvoiceRepository) : BaseViewModel() {

    val mutableLiveDataCategories = MutableLiveData<List<Category>>()
    val mutableLiveDataSubCategories = MutableLiveData<List<SubCategory>>()
    val mutableLiveDataItems = MutableLiveData<List<Item>>()
    val mutableLiveDataSubCategoriesShow = MutableLiveData(false)
    val mutableLiveDataCountItems = MutableLiveData<Double>(0.0)
    val mutableLiveDataInvoiceNum = MutableLiveData<Long>()
    val mutableLiveDataSelectedItems = MutableLiveData<List<Item?>>()
    val mutableLivaDataTransactionHead = MutableLiveData<TransactionHead>()


    val viewBillyPosMapper: ViewBillyPosMapper = ViewBillyPosMapper()

    fun setTransactionHead(trnHead: TransactionHead) {
        mutableLivaDataTransactionHead.value = trnHead
    }

    fun getTransactionHead(trnHeadUUID: String?) =
        addCompositeDisposable(
            invoiceRepository.getTransactionHead(trnHeadUUID = trnHeadUUID)
                .map { viewBillyPosMapper.viewTransactionHeadMapper.toModel(it) },
            onSuccess = {
                mutableLivaDataTransactionHead.value = it
            }
        )

    fun setItemsSelected(items: List<Item>) {
        mutableLiveDataCountItems.value = items.size.toDouble()
    }

    fun getItemsBySubCategory(subId: Long) {
        addCompositeDisposable(
            invoiceRepository.getItemsBySubCategory(subId = subId)
                .flatMap {
                    invoiceRepository.getItemsByCategoryAndSubcategory(
                        subId = subId,
                        categoryId = it
                    )
                }.map {
                    viewBillyPosMapper.viewItemMapper.toListOfModel(it)
                },
            onSuccess = {
                Timber.e("LIST ITEMA %s", it)
                mutableLiveDataItems.value = it
            }
        )
    }

    fun getAllCategories() {
        addCompositeDisposable(
            invoiceRepository.getAllCategories().map{
                viewBillyPosMapper.viewCategoryMapper.toListOfModel(it)
            }
            ,
            onSuccess = {
                mutableLiveDataCategories.value = it
            }
        )
    }

    fun getAllSubCategories() {
        addCompositeDisposable(
            invoiceRepository.getAllSubCategories(),
            onSuccess = {
                mutableLiveDataSubCategories.value = it
            }
        )
    }

    fun getSubCategories(id: Long) {
        addCompositeDisposable(
            invoiceRepository.getSubCategories(id = id),
            onSuccess = {
                if (it.isNotEmpty()) {
                    mutableLiveDataSubCategories.value = it
                    mutableLiveDataSubCategoriesShow.value = true
                } else {
                    mutableLiveDataSubCategories.value = it
                    mutableLiveDataSubCategoriesShow.value = false
                }
            }
        )
    }

    fun getItemsByCategory(id: Long) {
        addCompositeDisposable(
            invoiceRepository.getItemsByCategory(id = id)
                .map { viewBillyPosMapper.viewItemMapper.toListOfModel(it) }, onSuccess = {
                Timber.e("LIST ITEMA %s", it)
                mutableLiveDataItems.value = it
            }
        )
    }

    fun getAllItems() {
        addCompositeDisposable(
            invoiceRepository.getAllItems(),
            onSuccess = {
                mutableLiveDataItems.value = it
            }
        )
    }

    fun addSelectedItem(item: Item?) {
        mutableLiveDataSelectedItems.value?.toMutableList()?.add(item)
        mutableLiveDataCountItems.value = mutableLiveDataCountItems.value?.plus(1.0)
    }

    fun nextNum(): Long {
        var number = 1L
        addCompositeDisposable(
            invoiceRepository.nextNum(),
            onSuccess = {
                number = it.inc()
            }
        )
        return number
    }

    fun insert(trnHead: TransactionHead, item: Item) {
        val head = trnHead.copy(
            total = trnHead.total + item.unitPrice,
            totalWithVat = trnHead.totalWithVat + item.itemPriceWithDiscount,
            vatAmount = trnHead.vatAmount.plus( item.itemPriceWithDiscount - item.unitPrice)
        )
        Timber.e("Head: %s", head)
        if (head.id == null) {
            insertTransactionHead(head)
        } else {
            updateTransactionHead(head)
        }

        var bodyItem = invoiceRepository.findForItem(
            trnHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(head),
            item = item
        )

        var bodyItemCopy = LocalTransactionBody()
        if (bodyItem?.id == null) {

            if (bodyItem == null) {
                bodyItem = LocalTransactionBody()
            }

            bodyItemCopy = bodyItem.copy(
                idTransactionHead = head.uuid,
                idCompany = head.idCompany,
                idAddress = head.idAddress,
                idDevice = head.idDevice,
                idUser = head.idUser,
                idItem = item.uuid,
                idCategory = item.idCategory,
                rabat = item.discount,
                unitPrice = item.unitPrice,
                itemName = item.itemName,
                itemPrice = item.itemPrice,
                itemPriceWithDiscount = item.itemPriceWithDiscount,
                vatRate = item.vat,
                vatAmount = bodyItem.vatAmount?.plus(item.itemPriceWithDiscount - item.unitPrice),
                quantity = 1.0,
                item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)
            )
        } else {
            bodyItemCopy = bodyItem.copy(
                quantity = bodyItem.quantity?.plus(1)
            )
        }
        mutableLiveDataCountItems.value = mutableLiveDataCountItems.value?.plus(1.0)

        bodyItemCopy = bodyItemCopy.copy(
            total = bodyItemCopy.quantity?.times(bodyItemCopy.itemPrice!!),
            totalWithVat = bodyItemCopy.quantity?.times(bodyItemCopy.itemPriceWithDiscount!!),
            idTransactionHead = head.uuid
        )

        invoiceRepository.insert(trnBody = bodyItemCopy)

    }

    fun deleteTransactionHead(transactionHead: TransactionHead) {
        invoiceRepository.delete(
            viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(transactionHead)
        )
    }

    private fun insertTransactionHead(transactionHead: TransactionHead) {
        addCompositeDisposable(
            invoiceRepository.insert(
                viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(transactionHead)
            ).map {
                viewBillyPosMapper.viewTransactionHeadMapper.toModel(it)
            },
            onSuccess = {
                mutableLivaDataTransactionHead.value = it
            }
        )
    }

    private fun updateTransactionHead(transactionHead: TransactionHead) {
        addCompositeDisposable(
            invoiceRepository.update(
                viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(transactionHead)
            ),
            onSuccess = {
                getTransactionHead(mutableLivaDataTransactionHead.value?.uuid)
            }
        )
    }

    fun hideSubCategories() {
        mutableLiveDataSubCategoriesShow.value = false
    }

    fun getLastInvoiceNum(){
        addCompositeDisposable(
            invoiceRepository.getLastInvoiceNum()
        , onSuccess = {
                Timber.e("Last Num %s" , it.inc() )
                mutableLiveDataInvoiceNum.value = it.inc()
            }
        , onError = {
                Timber.e("Last Num Error %s" , "1" )
                mutableLiveDataInvoiceNum.value = 1
            }
        )
    }

    fun getItemsByTransactionHead(transactionHeadUUID: String){
        var count = 0.0
        addCompositeDisposable(
            invoiceRepository.getItemsByTransactionHead(transactionHeadUUID = transactionHeadUUID)
                .map { viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it) }
                ,onSuccess = {
                    it.map { trnBody ->
                       count = count.plus(trnBody.quantity!!)
                    }
                mutableLiveDataCountItems.value = count
            }
        )
    }


}