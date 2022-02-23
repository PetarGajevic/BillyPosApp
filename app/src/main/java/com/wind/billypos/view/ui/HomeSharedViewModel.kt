package com.wind.billypos.view.ui

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.utils.formatTwoDecimal
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.invoice.repository.HomeSharedRepository
import timber.log.Timber

class HomeSharedViewModel(private val homeSharedRepository: HomeSharedRepository) : BaseViewModel() {


    val mutableLivaDataTransactionHead = MutableLiveData<TransactionHead>()
    val mutableLiveDataSelectedItems = MutableLiveData<List<Item?>>()
    val mutableLiveDataTransactionBodyList = MutableLiveData<MutableList<TransactionBody>?>()
    val mutableLiveDataDelete = MutableLiveData<Boolean>()
    val mutableLiveDataDeleteItem = MutableLiveData<Boolean>()
    val mutableLiveDataUpdateItem = MutableLiveData<Boolean>()
    val mutableLiveDataIsCorrective = MutableLiveData<Boolean?>()
    val mutableLiveDataCustomer = MutableLiveData<Customer?>()
    val mutableLiveDataEntityPoint = MutableLiveData<EntityPoint?>()

    val viewBillyPosMapper: ViewBillyPosMapper = ViewBillyPosMapper()

    init {
        mutableLiveDataTransactionBodyList.value = ArrayList()
    }

    fun setEntityPoint(entityPoint: EntityPoint?) {
        mutableLiveDataEntityPoint.value = entityPoint
    }

    fun setTransactionHead(trnHead: TransactionHead) {
        mutableLivaDataTransactionHead.value = trnHead
    }

    fun setCorrectiveTransactionHead(trnHead: TransactionHead) {
        mutableLivaDataTransactionHead.value = trnHead
    }

    fun setTransactionBodyList(){
        mutableLiveDataTransactionBodyList.value = ArrayList()
    }

    fun setCustomer(customer: Customer?){
        mutableLiveDataCustomer.value = customer
    }


    fun getTransactionHead(): TransactionHead? = mutableLivaDataTransactionHead.value


    fun addSelectedItem(item: Item?) {
        mutableLiveDataSelectedItems.value?.toMutableList()?.add(item)
    }


    fun insert(trnHead: TransactionHead, item: Item) {
        val insert: Boolean
        val vatAmount = item.itemPriceWithDiscount*(1-(1/ (1.plus(item.vat?.div(100) ?: 0.0)))).formatTwoDecimal()

//        val transactionHead = trnHead.copy(
//            totalWithVat = trnHead.totalWithVat + item.itemPriceWithDiscount,
//            vatAmount = trnHead.vatAmount.plus(vatAmount),
//        )
//
//        val head = transactionHead.copy(
//            total = transactionHead.totalWithVat - transactionHead.vatAmount
//        )
//
//
//        mutableLivaDataTransactionHead.value = head
//        Timber.e("Transaction Head %s", head)

        var bodyItem = mutableLiveDataTransactionBodyList.value?.find { transactionBody ->
            transactionBody.idTransactionHead == trnHead.uuid && transactionBody.idItem == item.uuid
        }


        var bodyItemCopy = TransactionBody()
        if (bodyItem?.uuid == null) {

            if (bodyItem == null) {
                bodyItem = TransactionBody()
            }

            bodyItemCopy = bodyItem.copy(
                idTransactionHead = trnHead.uuid,
                idCompany = trnHead.idCompany,
                idAddress = trnHead.idAddress,
                idDevice = trnHead.idDevice,
                idUser = trnHead.idUser,
                idItem = item.uuid,
                idCategory = item.idCategory,
                rabat = item.discount,
                unitPrice = item.unitPrice,
                unitPriceWithVat = item.itemPriceWithDiscount,
                itemName = item.itemName,
                itemPrice = item.itemPrice,
                itemPriceWithDiscount = item.itemPriceWithDiscount,
                vatRate = item.vat,
                quantity = 1.0,
                item = item
            )
            insert = true
        } else {
            bodyItemCopy = bodyItem.copy(
                quantity = bodyItem.quantity?.plus(1)
            )
            insert = false
        }

      //  mutableLiveDataCountItems.value = mutableLiveDataCountItems.value?.plus(1.0)

////        bodyItemCopy = bodyItemCopy.copy(
////            total = bodyItemCopy.quantity?.times(bodyItemCopy.unitPrice!!),
////            totalWithVat = bodyItemCopy.quantity?.times(bodyItemCopy.itemPriceWithDiscount),
//////            vatAmount =  bodyItemCopy.vatAmount?.plus(item.itemPriceWithDiscount - item.unitPrice),
////            idTransactionHead = head.uuid
////        )
//
//        val body = bodyItemCopy.copy(
//           // vatAmount =  bodyItemCopy.itemPriceWithDiscount*(1-(1/ (1.plus(item.vat?.div(100) ?: 0.0)))),
//            idTransactionHead = head.uuid,
//            vatAmount = bodyItemCopy.vatAmount?.plus(vatAmount),
//            total = bodyItemCopy.unitPrice?.let { bodyItemCopy.quantity?.times(it) },
//            totalWithVat = bodyItemCopy.itemPriceWithDiscount.let { bodyItemCopy.quantity?.times(it) }
//        )

//        val totalWithVat = bodyItemCopy.quantity?.times(bodyItemCopy.itemPriceWithDiscount)?.formatTwoDecimal()
//        val totalAmount = totalWithVat?.times((1-(1/ (1.plus(item.vat?.div(100) ?: 0.0)))))?.formatTwoDecimal()
//        val total = bodyItemCopy.quantity?.times(bodyItemCopy.unitPrice ?: 0.0)?.formatTwoDecimal()//totalWithVat?.minus(totalAmount ?: 0.0)?.formatTwoDecimal()
//        Timber.e("totalWithVat %s", totalWithVat)
//        Timber.e("totalAmount %s", totalAmount)
//        Timber.e("total %s", total)

        val totalWithVat = bodyItemCopy.quantity?.times(bodyItemCopy.itemPriceWithDiscount)?.formatTwoDecimal()
        val totalAmount = totalWithVat?.times((1-(1/ (1.plus(item.vat?.div(100) ?: 0.0)))))?.formatTwoDecimal()
        val total = totalWithVat?.minus(totalAmount ?: 0.0)?.formatTwoDecimal()//bodyItemCopy.quantity?.times(bodyItemCopy.unitPrice ?: 0.0)?.formatTwoDecimal()
        Timber.e("totalWithVat %s", totalWithVat)
        Timber.e("totalAmount %s", totalAmount)
        Timber.e("total %s", total)

        bodyItemCopy = bodyItemCopy.copy(
            totalWithVat = totalWithVat,
            idTransactionHead = trnHead.uuid
        )

        val body = bodyItemCopy.copy(
            vatAmount = totalAmount, //bodyItemCopy.vatAmount?.plus(vatAmount),
            total = total,
        )

        Timber.e("TBody %s", body)

//        val transactionHead = trnHead.copy(
//            totalWithVat = trnHead.totalWithVat + item.itemPriceWithDiscount,
//            vatAmount = trnHead.vatAmount.plus(vatAmount),
//        )
//
//        val head = transactionHead.copy(
//            total = transactionHead.totalWithVat - transactionHead.vatAmount
//        )

//        val headVatAmount = bodyItemCopy.itemPriceWithDiscount.times((1-(1/ (1.plus(item.vat?.div(100) ?: 0.0))))).formatTwoDecimal()


        if(insert){
            mutableLiveDataTransactionBodyList.value?.add(body)
        }else{
            var findBody = TransactionBody()
            mutableLiveDataTransactionBodyList.value?.map {
                if (it.uuid == body.uuid){
                    findBody = it
                }
            }
            val index = mutableLiveDataTransactionBodyList.value?.indexOf(findBody)
            if (index != null) {
                mutableLiveDataTransactionBodyList.value?.set(index, body)
            }
        }

        mutableLiveDataTransactionBodyList.value = mutableLiveDataTransactionBodyList.value

        var trnHeadVatAmount = 0.0
        var trnHeadTotal = 0.0
        var trnHeadTotalWithVat = 0.0
        mutableLiveDataTransactionBodyList.value?.map {
            trnHeadVatAmount += it.vatAmount!!
            trnHeadTotal += it.total!!
            trnHeadTotalWithVat += it.totalWithVat!!
        }
      //  val headVatAmount = bodyItemCopy.itemPriceWithDiscount.times((1-(1/ (1.plus(item.vat?.div(100) ?: 0.0))))).formatTwoDecimal()
//        Timber.e("headVatAmount %s", headVatAmount)

//        val transactionHead = trnHead.copy(
//            totalWithVat = (trnHead.totalWithVat + bodyItemCopy.itemPriceWithDiscount).formatTwoDecimal(),
//            vatAmount = (trnHead.vatAmount + headVatAmount).formatTwoDecimal()//trnHead.vatAmount.plus(totalAmount ?: 0.0),
//        )

        val head = trnHead.copy(
            totalWithVat = trnHeadTotalWithVat.formatTwoDecimal(),
            total = trnHeadTotal.formatTwoDecimal(),
            vatAmount = trnHeadVatAmount.formatTwoDecimal()
        )

        mutableLivaDataTransactionHead.value = head
        Timber.e("Transaction Head %s", head)
    }

    fun getTransactions() = mutableLiveDataTransactionBodyList.value

    fun deleteItem(trnBody: TransactionBody?){
        Timber.e("Brise se item")
        mutableLiveDataTransactionBodyList.value?.remove(trnBody)
        mutableLiveDataTransactionBodyList.value = mutableLiveDataTransactionBodyList.value
        mutableLiveDataDeleteItem.value = true
    }

    fun calculateTransactionHead(trnBodyList: List<TransactionBody>){
        var total = 0.0
        var totalWithVat = 0.0
        var vatAmount = 0.0
        trnBodyList.map {
            total += it.total!!
            totalWithVat += it.totalWithVat!!
            vatAmount += it.vatAmount!!
        }

        mutableLivaDataTransactionHead.value = mutableLivaDataTransactionHead.value?.copy(
            total = total.formatTwoDecimal(),
            totalWithVat = totalWithVat.formatTwoDecimal(),
            vatAmount = vatAmount.formatTwoDecimal()
        )

    }


    fun updateTransactionItem(trnBody: TransactionBody?) {

        val transactionBody = trnBody?.copy(
          //  vatAmount = trnBody.vatAmount?.plus(vatAmount!!),
//            total = trnBody.quantity?.times(trnBody.unitPrice!!),
            totalWithVat = trnBody.quantity?.times(trnBody.itemPriceWithDiscount),
        )

        val vatAmount =
            transactionBody?.totalWithVat?.times((1-(1/ (1.plus(trnBody.vatRate?.div(100) ?: 0.0)))))


        val trnItem = transactionBody?.copy(
            vatAmount = vatAmount,
            total = transactionBody.totalWithVat?.minus(vatAmount!!)
        )



        var findBody = TransactionBody()
        mutableLiveDataTransactionBodyList.value?.map {
            if (it.uuid == trnItem?.uuid){
                findBody = it
            }
        }
        val index = mutableLiveDataTransactionBodyList.value?.indexOf(findBody)
        if (index != null) {
            mutableLiveDataTransactionBodyList.value?.set(index, trnItem!!)
        }

        mutableLiveDataTransactionBodyList.value = mutableLiveDataTransactionBodyList.value
        mutableLiveDataUpdateItem.value = true
    }


    fun getTransactionBodyList(trnHeadUUID: String?){
        addCompositeDisposable(
            homeSharedRepository.getTransactionBodyList(trnHeadUUID = trnHeadUUID).map {
                viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataTransactionBodyList.value = it.toMutableList()
            }
        )
    }

}