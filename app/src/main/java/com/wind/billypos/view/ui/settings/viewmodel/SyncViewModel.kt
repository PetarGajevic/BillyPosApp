package com.wind.billypos.view.ui.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.preference.Preference
import com.google.gson.Gson
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalance
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositData
import com.wind.billypos.data.remote.model.customer.RemoteCustomer
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHead
import com.wind.billypos.data.remote.model.item.RemoteGetItemResponse
import com.wind.billypos.data.remote.model.operator.RemoteOperator
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.settings.repository.SyncRepository
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class SyncViewModel(private val syncRepository: SyncRepository) : BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()
    private val billyPosMapper = BillyPosMapper()

    //    Items
    val mutableLiveDataNotSyncedItems = MutableLiveData<List<Item>>()
    val mutableLiveDataSyncedItem = MutableLiveData<Item?>()
    val mutableLiveDataGetItemsResponse = MutableLiveData<List<RemoteGetItemResponse>>()
    val mutableLiveDataSendItems = MutableLiveData<Boolean>()

    //    Customers
    val mutableLiveDataNotSyncedCustomers = MutableLiveData<List<Customer>>()
    val mutableLiveDataSyncedCustomer = MutableLiveData<Customer?>()
    val mutableLiveDataGetCustomersResponse = MutableLiveData<List<RemoteCustomer>>()
    val mutableLiveDataSendCustomers = MutableLiveData<Boolean>()

    //    Category
    val mutableLiveDataCategories = MutableLiveData<List<Category>?>()
    val mutableLiveDataInsertedCategories = MutableLiveData<Boolean>()
    val mutableLiveDataInsertedSubCategories = MutableLiveData<Boolean>()

    //    CashBalance
    val mutableLiveDataNotSyncedCashBalance = MutableLiveData<List<CashBalance>>()
    val mutableLiveDataSyncedCashBalance = MutableLiveData<CashBalance?>()
    val mutableLiveDataGetCashBalanceResponse = MutableLiveData<List<RemoteCashBalance>>()
    val mutableLiveDataSendCashBalance = MutableLiveData<Boolean>()

    //      Invoices
    val mutableLiveDataNotSyncedInvoices = MutableLiveData<List<TransactionHead>>()
    val mutableLiveDataSyncedInvoice = MutableLiveData<TransactionHead?>()
    val mutableLiveDataGetInvoicesResponse = MutableLiveData<List<TransactionHead>>()
    val mutableLiveDataSendInvoices = MutableLiveData<Boolean>()

    //  Entity Point
    val mutableLiveDataEntityPoints = MutableLiveData<List<EntityPoint>?>()
    val mutableLiveDataInsertedEntityPoints = MutableLiveData<Boolean>()

    //  Operators
    val mutableLiveDataOperators = MutableLiveData<List<Operator>?>()
    val mutableLiveDataInsertedOperators = MutableLiveData<Boolean>()


    //    Items
    fun sendItem(item: Item) {
        addCompositeDisposable(
            syncRepository.sendItem(item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)),
            onSuccess = {
                if (it.status == "OK") {
                    val localItem = billyPosMapper.itemMapper.toModel(it.data)
                    mutableLiveDataSyncedItem.value =
                        viewBillyPosMapper.viewItemMapper.toModel(localItem)
                } else {
                    mutableLiveDataSyncedItem.value = null
                }
            },
            onError = {
                Timber.e("Error")
                mutableLiveDataSyncedItem.value = null
            }
        )
    }

    fun getNotSyncedItems() {
        addCompositeDisposable(
            syncRepository.getNotSyncedItems().map {
                viewBillyPosMapper.viewItemMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista itema %s", it)
                mutableLiveDataNotSyncedItems.value = it
            },
            onError = {
                mutableLiveDataNotSyncedItems.value = listOf()
            }
        )
    }

    fun findItemById(item: Item) {
        addCompositeDisposable(
            syncRepository.findItemById(item.id),
            onSuccess = {
                Timber.e("Uspjesno updejtovan %s", it)
            }
        )
    }

    fun getItems() {
        addCompositeDisposable(
            syncRepository.getItems(),
            onSuccess = {
                Timber.e("OnSuccess %s", it)
                mutableLiveDataGetItemsResponse.value = it.data
            },
            onError = {
                mutableLiveDataGetItemsResponse.value = listOf()
                Timber.e("On error")
            },
            handleError = true
        )
    }

    fun insertItems(items: List<RemoteGetItemResponse>) {
        addCompositeDisposable(
            syncRepository.insertItems(items = billyPosMapper.itemResponseMapper.toListOfModel(items)),
            onSuccess = {
                mutableLiveDataSendItems.value = true
            },
            onError = {
                mutableLiveDataSendItems.value = true
            }
        )
    }


    // Customers

    fun sendCustomer(customer: Customer) {
        addCompositeDisposable(
            syncRepository.sendCustomer(
                customer = viewBillyPosMapper.viewCustomerMapper.toLocalModel(
                    customer
                )
            ),
            onSuccess = {
                if (it.status == "OK") {
                    val localCustomer = billyPosMapper.customerMapper.toModel(it.data)
                    mutableLiveDataSyncedCustomer.value =
                        viewBillyPosMapper.viewCustomerMapper.toModel(localCustomer)
                } else {
                    mutableLiveDataSyncedCustomer.value = null
                }
                Timber.e("Remote Customer Response %s", it)
            },
            onError = {
                mutableLiveDataSyncedCustomer.value = null
                Timber.e("Error")
            }
        )
    }

    fun getCustomers() {
        addCompositeDisposable(
            syncRepository.getCustomers(),
            onSuccess = {
                Timber.e("OnSuccess %s", it)
                mutableLiveDataGetCustomersResponse.value = it.data
            },
            onError = {
                Timber.e("On error")
                mutableLiveDataGetCustomersResponse.value = listOf()
            }
        )
    }

    fun insertCustomers(customers: List<RemoteCustomer>) {
        addCompositeDisposable(
            syncRepository.insertCustomers(
                customers = billyPosMapper.customerMapper.toListOfModel(
                    customers
                )
            ),
            onSuccess = {
                Timber.e("Usao na success")
                mutableLiveDataSendCustomers.value = true
            },
            onError = {
                Timber.e("Usao na error")
                mutableLiveDataSendCustomers.value = true
            }
        )
    }

    fun getNotSyncedCustomers() {
        addCompositeDisposable(
            syncRepository.getNotSyncedCustomers().map {
                viewBillyPosMapper.viewCustomerMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista itema %s", it)
                mutableLiveDataNotSyncedCustomers.value = it
            },
            onError = {
                mutableLiveDataNotSyncedCustomers.value = listOf()
            }
        )
    }

    fun findCustomerByUUID(customer: Customer) {
        Timber.e("Customer %s", customer)
        addCompositeDisposable(
            syncRepository.findCustomerByUUID(uuid = customer.uuid),
            onSuccess = {
                Timber.e("Uspjesno updejtovan %s", it)
            }
        )
    }

    // Categories

    fun getCategories() {
        addCompositeDisposable(
            syncRepository.getCategories(),
            onSuccess = {
                if (it.status == "OK") {
                    val localCategories = billyPosMapper.categoryMapper.toListOfModel(it.data)
                    val localCategoriesUpdated = localCategories.map { category ->
                        category.copy(
                            subcategories = category.subcategories.map { subcategory ->
                                subcategory.copy(
                                    id_category = category.id
                                )
                            }
                        )
                    }
                    mutableLiveDataCategories.value =
                        viewBillyPosMapper.viewCategoryMapper.toListOfModel(localCategoriesUpdated)
                } else {
                    mutableLiveDataCategories.value = null
                }
            },
            onError = {
                mutableLiveDataCategories.value = null
                Timber.e("Error")
            }
        )
    }

    fun insertCategories(categories: List<Category>) {
        addCompositeDisposable(
            syncRepository.insertCategories(
                categories = viewBillyPosMapper.viewCategoryMapper.toLocalListOfModel(
                    categories
                )
            ),
            onSuccess = {
                mutableLiveDataInsertedCategories.value = true
            },
            onError = {
                mutableLiveDataInsertedCategories.value = true
            }
        )
    }

    fun insertSubCategories(subCategories: List<SubCategory>) {
        addCompositeDisposable(
            syncRepository.insertSubCategories(
                subCategories = viewBillyPosMapper.viewSubCategoryMapper.toLocalListOfModel(
                    subCategories
                )
            ),
            onSuccess = {
                mutableLiveDataInsertedSubCategories.value = true
            },
            onError = {
                mutableLiveDataInsertedSubCategories.value = true
            }
        )
    }


//    Cash Balance

    fun sendCashBalance(cashBalance: CashBalance) {
        addCompositeDisposable(
            syncRepository.sendCashBalance(
                cashBalance = viewBillyPosMapper.viewCashBalanceMapper.toLocalModel(
                    cashBalance
                )
            ),
            onSuccess = {
                if (it.status == "OK") {
                    val localCustomer = billyPosMapper.getCashBalanceMapper.toModel(it.data)
                    mutableLiveDataSyncedCashBalance.value =
                        viewBillyPosMapper.viewCashBalanceMapper.toModel(localCustomer)
                } else {
                    mutableLiveDataSyncedCashBalance.value = null
                }
                Timber.e("Remote Customer Response %s", it)
            },
            onError = {
                mutableLiveDataSyncedCashBalance.value = null
                Timber.e("Error")
            }
        )
    }

    fun getCashBalance() {
        addCompositeDisposable(
            syncRepository.getCashBalance(),
            onSuccess = {
                Timber.e("OnSuccess %s", it)
                mutableLiveDataGetCashBalanceResponse.value = it.data!!
            },
            onError = {
                Timber.e("On error")
                mutableLiveDataGetCashBalanceResponse.value = listOf()
            }
        )
    }

    fun insertCashBalance(cashBalance: List<RemoteCashBalance>) {
        addCompositeDisposable(
            syncRepository.insertCashBalance(
                cashBalance = billyPosMapper.remoteCashBalanceMapper.toListOfModel(
                    cashBalance
                )
            ),
            onSuccess = {
                Timber.e("Usao na success")
                mutableLiveDataSendCashBalance.value = true
            },
            onError = {
                Timber.e("Usao na error")
                mutableLiveDataSendCashBalance.value = true
            }
        )
    }

    fun getNotSyncedCashBalance() {
        addCompositeDisposable(
            syncRepository.getNotSyncedCashBalance().map {
                viewBillyPosMapper.viewCashBalanceMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista itema %s", it)
                mutableLiveDataNotSyncedCashBalance.value = it
            },
            onError = {
                mutableLiveDataNotSyncedCashBalance.value = listOf()
            }
        )
    }

    fun findCashBalanceByUUID(cashBalance: CashBalance) {
        Timber.e("Customer %s", cashBalance)
        addCompositeDisposable(
            syncRepository.findCashBalanceByUUID(uuid = cashBalance.uuid),
            onSuccess = {
                Timber.e("Uspjesno updejtovan %s", it)
            }
        )
    }


    //    Invoices

    fun sendInvoice(invoice: TransactionHead) {
        addCompositeDisposable(
            syncRepository.sendInvoice(
                invoice = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(
                    invoice
                )
            ),
            onSuccess = {
                if (it.status == "OK") {
//                    val localTransactionHead = billyPosMapper.transactionHeadMapper.toModel(it.data.header)
//                    mutableLiveDataSyncedInvoice.value = viewBillyPosMapper.viewTransactionHeadMapper.toModel(localTransactionHead)
                } else {
                    mutableLiveDataSyncedInvoice.value = null
                }
                Timber.e("Remote Invoice Response %s", it)
            },
            onError = {
                mutableLiveDataSyncedInvoice.value = null
                Timber.e("Error")
            }
        )
    }

    fun getInvoices() {
        addCompositeDisposable(
            syncRepository.getInvoices(),
            onSuccess = {
                Timber.e("OnSuccess %s", it)
                //   mutableLiveDataGetInvoicesResponse.value = it.data!!
            },
            onError = {
                Timber.e("On error")
                mutableLiveDataGetCashBalanceResponse.value = listOf()
            }
        )
    }

    fun insertInvoices(invoices: List<RemoteTransactionHead>) {
        addCompositeDisposable(
            syncRepository.insertInvoices(
                invoices = billyPosMapper.transactionHeadMapper.toListOfModel(
                    invoices
                )
            ),
            onSuccess = {
                Timber.e("Usao na success")
                mutableLiveDataSendInvoices.value = true
            },
            onError = {
                Timber.e("Usao na error")
                mutableLiveDataSendInvoices.value = true
            }
        )
    }

    fun getNotSyncedInvoices() {
        addCompositeDisposable(
            syncRepository.getNotSyncedInvoices().map {
                viewBillyPosMapper.viewTransactionHeadMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista itema %s", it)
                mutableLiveDataNotSyncedInvoices.value = it
            },
            onError = {
                mutableLiveDataNotSyncedInvoices.value = listOf()
            }
        )
    }

    fun findInvoiceByUUID(invoice: TransactionHead) {
        Timber.e("Invoice %s", invoice)
        addCompositeDisposable(
            syncRepository.findInvoiceByUUID(uuid = invoice.uuid),
            onSuccess = {
                Timber.e("Uspjesno updejtovan %s", it)
            }
        )
    }


//      Entity Points

    fun getEntityPoints() {
        addCompositeDisposable(
            syncRepository.getEntityPoints(),
            onSuccess = {
                if (it.status == "OK") {
                    val localEntityPoints = billyPosMapper.entityPointMapper.toListOfModel(it.data)
                    mutableLiveDataEntityPoints.value =
                        viewBillyPosMapper.viewEntityPointMapper.toListOfModel(localEntityPoints)
                } else {
                    mutableLiveDataEntityPoints.value = null
                }
            },
            onError = {
                mutableLiveDataEntityPoints.value = null
                Timber.e("Error")
            }
        )
    }

    fun insertEntityPoints(entityPoints: List<EntityPoint>) {
        addCompositeDisposable(
            syncRepository.insertEntityPoints(
                entityPoints = viewBillyPosMapper.viewEntityPointMapper.toLocalListOfModel(
                    entityPoints
                )
            ),
            onSuccess = {
                mutableLiveDataInsertedEntityPoints.value = true
            },
            onError = {
                mutableLiveDataInsertedEntityPoints.value = true
            }
        )
    }


//    Operators

    @Suppress("UNCHECKED_CAST")
    fun getOperators(){
        addCompositeDisposable(
            syncRepository.getOperators(),
            onSuccess = { it ->
                if (it.status == "OK") {
                    Timber.e("Object %s: ", it)
                        it.data.forEach { localOperator ->

                            Timber.e("localOperator %s : ",  localOperator.permissions as? List<RemoteOperator.Permissions>)
                            Timber.e("localOperator %s : ",  Gson().fromJson(localOperator.permissions.toString(), RemoteOperator.Permissions::class.java))
                            val listOfPermissions =  (localOperator.permissions as? List<RemoteOperator.Permissions>)
                            Timber.e("listOfPermissions %s: ", listOfPermissions)
                            listOfPermissions?.forEach {
                                Timber.e("Permissions %s: ", it)
                            }
//                          val list =  localOperator.permissions?.map { any ->
//                              Timber.e("key: %s", (any as? RemoteOperator.Permissions)?.key!!)
//                            }

                        }

                    val localOperators = billyPosMapper.operatorMapper.toListOfModel(it.data)
                    mutableLiveDataOperators.value =
                        viewBillyPosMapper.viewOperatorMapper.toListOfModel(localOperators)
                    Timber.e("localOperators %s", localOperators)
                } else {
                    mutableLiveDataOperators.value = null
                }
            },
            onError = {
                mutableLiveDataOperators.value = null
                Timber.e("Error")
            }
        )
    }


    fun insertOperators(operators: List<Operator>){
        addCompositeDisposable(
            syncRepository.insertOperators(
                operators = viewBillyPosMapper.viewOperatorMapper.toLocalListOfModel(
                    operators
                )
            ),
            onSuccess = {
                mutableLiveDataInsertedOperators.value = true
            },
            onError = {
                mutableLiveDataInsertedOperators.value = true
            }
        )
    }

}