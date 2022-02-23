package com.wind.billypos.data.remote.mapper

import com.wind.billypos.base.BaseMapper
import com.wind.billypos.data.local.model.*
import com.wind.billypos.data.remote.model.*
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalance
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceRequest
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositData
import com.wind.billypos.data.remote.model.category.RemoteCategory
import com.wind.billypos.data.remote.model.customer.RemoteCustomer
import com.wind.billypos.data.remote.model.entity.RemoteEntityPoint
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHead
import com.wind.billypos.data.remote.model.item.RemoteGetItemResponse
import com.wind.billypos.data.remote.model.item.RemoteItem
import com.wind.billypos.data.remote.model.item.RemoteTransactionItem
import com.wind.billypos.data.remote.model.operator.RemoteOperator
import com.wind.billypos.data.remote.model.subcategory.RemoteSubCategory
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.toBoolean
import com.wind.billypos.utils.toInt
import com.wind.billypos.view.model.LoginRequest
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class BillyPosMapper {

    val cashBalanceMapper: CashBalanceMapper = CashBalanceMapper()
    val getCashBalanceMapper: GetCashBalanceMapper = GetCashBalanceMapper()
    val remoteCashBalanceMapper: RemoteCashBalanceMapper = RemoteCashBalanceMapper()
    val transactionHeadMapper: TransactionHeadMapper = TransactionHeadMapper()
    val loginRequestMapper: LoginRequestMapper = LoginRequestMapper()
    val transactionItemMapper: TransactionItemMapper = TransactionItemMapper()
    val itemMapper: ItemMapper = ItemMapper()
    val transactionPaymentMethods: TransactionPaymentMethods = TransactionPaymentMethods()
    val customerMapper: CustomerMapper = CustomerMapper()
    val companyMapper: CompanyMapper = CompanyMapper()
    val addressMapper: AddressMapper = AddressMapper()
    val userMapper: UserMapper = UserMapper()
    val deviceMapper: DeviceMapper = DeviceMapper()
    val licenseMapper: LicenceMapper = LicenceMapper()
    val configurationMapper: ConfigurationMapper = ConfigurationMapper()
    val itemResponseMapper: ItemResponseMapper = ItemResponseMapper()
    val categoryMapper: CategoryMapper = CategoryMapper()
    val subCategoryMapper: SubCategoryMapper = SubCategoryMapper()
    val entityPointMapper: EntityPointMapper = EntityPointMapper()
    val operatorMapper: OperatorMapper = OperatorMapper()

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    inner class CashBalanceMapper : BaseMapper<RemoteCashBalanceRequest, LocalCashBalance> {
        override fun toModel(model: RemoteCashBalanceRequest?): LocalCashBalance {
            return when (model) {
                null -> LocalCashBalance()
                else -> {
                    val cashBalance = LocalCashBalance(
                        uuid = model.id,
                        cashAmount = model.cashAmount,
                        operation = model.operation,
                        responseUUID = model.responseUUID,
                        fcdc = model.fcdc
                    )
//                    cashBalance.message = model.message
//                    cashBalance.code = if (model.status != 0) model.status else SUCCESS
                    cashBalance
                }
            }
        }

        override fun toRemoteModel(model: LocalCashBalance?): RemoteCashBalanceRequest {
            return when (model) {
                null -> RemoteCashBalanceRequest()
                else -> {
                    RemoteCashBalanceRequest(
                        id = model.uuid,
                        cashAmount = model.cashAmount,
                        operation = model.operation,
                        responseUUID = model.responseUUID,
                        changeDateTime = model.changeDateTime.toString(),
                        fcdc = model.fcdc
                    )
                }
            }
        }
    }

    inner class GetCashBalanceMapper : BaseMapper<RemoteCashDepositData, LocalCashBalance> {
        override fun toModel(model: RemoteCashDepositData?): LocalCashBalance {
            return when (model) {
                null -> LocalCashBalance()
                else -> {
                    val cashBalance = LocalCashBalance(
                        uuid = model.id,
                        deviceId = model.deviceId,
                        cashAmount = model.cashAmount,
                        balanceState = model.balanceState,
                        operation = model.operation,
                        responseUUID = model.responseUUID,
                        changeDateTime = LocalDateTime.parse(model.changeDateTime, formatter),
                        createdAt = LocalDate.parse(model.createdAt?.substring(0, 10))
                    )
//                    cashBalance.message = model.message
//                    cashBalance.code = if (model.status != 0) model.status else SUCCESS
                    cashBalance
                }
            }
        }


        override fun toRemoteModel(model: LocalCashBalance?): RemoteCashDepositData {
            return when (model) {
                null -> RemoteCashDepositData()
                else -> {
                    RemoteCashDepositData(
                        id = model.uuid,
                        deviceId = model.deviceId,
                        cashAmount = model.cashAmount,
                        balanceState = model.balanceState,
                        operation = model.operation,
                        responseUUID = model.responseUUID,
                        changeDateTime = model.changeDateTime.toString(),
                        createdAt = model.createdAt.toString()
                    )
                }
            }
        }
    }

    inner class RemoteCashBalanceMapper : BaseMapper<RemoteCashBalance, LocalCashBalance> {
        override fun toModel(model: RemoteCashBalance?): LocalCashBalance {
            return when (model) {
                null -> LocalCashBalance()
                else -> {
                    val cashBalance = LocalCashBalance(
                        uuid = model.id,
                        deviceId = model.deviceId,
                        cashAmount = model.cashAmount,
                        balanceState = model.balanceState,
                        operation = model.operation,
                        responseUUID = model.responseUUID,
                        changeDateTime = LocalDateTime.parse(model.changeDateTime, formatter),
                        createdAt = LocalDate.parse(model.createdAt?.subSequence(0, 10)),
                        isFiscalised = model.balanceState == FiscalisationState.FISCALIZED,
                        isSync = true
                    )
//                    cashBalance.message = model.message
//                    cashBalance.code = if (model.status != 0) model.status else SUCCESS
                    cashBalance
                }
            }
        }


        override fun toRemoteModel(model: LocalCashBalance?): RemoteCashBalance {
            return when (model) {
                null -> RemoteCashBalance()
                else -> {
                    RemoteCashBalance(
//                        id = model.uuid,
//                        deviceId = model.deviceId,
//                        cashAmount = model.cashAmount,
//                        balanceState = model.balanceState,
//                        operation = model.operation,
//                        responseUUID = model.responseUUID,
//                        changeDateTime = model.changeDateTime.toString(),
//                        createdAt = model.createdAt.toString()
                    )
                }
            }
        }
    }


    inner class LoginRequestMapper : BaseMapper<RemoteLoginRequest, LoginRequest> {
        override fun toModel(model: RemoteLoginRequest?): LoginRequest {
            return when (model) {
                null -> LoginRequest()
                else -> {
                    val loginRequest = LoginRequest(
                        username = model.username,
                        password = model.password,
                        deviceId = model.deviceId
                    )
//                    loginRequest.message = model.message
//                    loginRequest.code = if (model.status != 0) model.status else SUCCESS
                    loginRequest
                }
            }
        }

        override fun toRemoteModel(model: LoginRequest?): RemoteLoginRequest {
            return when (model) {
                null -> RemoteLoginRequest()
                else -> {
                    RemoteLoginRequest(
                        username = model.username,
                        password = model.password,
                        deviceId = model.deviceId
                    )
                }
            }
        }

    }


    inner class TransactionItemMapper : BaseMapper<RemoteTransactionItem, LocalTransactionBody> {
        override fun toModel(model: RemoteTransactionItem?): LocalTransactionBody {
            return when (model) {
                null -> LocalTransactionBody()
                else -> {
                    val transactionBody = LocalTransactionBody(

                    )
                    transactionBody
                }
            }
        }

        override fun toRemoteModel(model: LocalTransactionBody?): RemoteTransactionItem {
            return when (model) {
                null -> RemoteTransactionItem()
                else -> {
                    RemoteTransactionItem(
                        id = model.uuid,
                        itemId = model.uuid,
                        unitPriceAfterVAT = model.unitPriceWithVat,
                        unitPriceBefVAT = model.unitPrice,
                        priceBefVAT = model.total,
                        priceAfterVAT = model.totalWithVat,
                        quantity = model.quantity,
                        vatRate = model.vatRate,
                        vatAmount = model.vatAmount,
                    )
                }
            }
        }
    }


    inner class TransactionHeadMapper : BaseMapper<RemoteTransactionHead, LocalTransactionHead> {
        override fun toModel(model: RemoteTransactionHead?): LocalTransactionHead {
            return when (model) {
                null -> LocalTransactionHead()
                else -> {
                    val transactionHead = LocalTransactionHead(

                    )
                    transactionHead
                }
            }
        }

        override fun toRemoteModel(model: LocalTransactionHead?): RemoteTransactionHead {

            return when (model) {
                null -> RemoteTransactionHead()
                else -> {
                    RemoteTransactionHead(
                        id = model.uuid,
                        iic = model.iic,
                        fic = model.fiscalFIC,
                        invOrdNum = model.invoiceNo,
                        invNum = model.invoiceNum,
                        requestUUID = model.fiscalUUID,
                        totalWithoutVat = model.total,
                        totalWithVat = model.totalWithVat,
                        totVatAmount = model.vatAmount,
                        customerId = model.idCustomer,
                        isReversed = model.isReversed?.toInt(),
                        idTransactionReversed = model.idTransactionParent,
                        issueDateTime = model.createdAt.format(formatter),
                        fiscalizedAt = model.fiscalizedAt?.format(formatter),
                        items = transactionItemMapper.toRemoteListOfModel(model.items!!),
                        paymentMethods = transactionPaymentMethods.toRemoteListOfModel(model.paymentMethods!!)
                    )
                }
            }
        }
    }

    inner class TransactionPaymentMethods :
        BaseMapper<RemotePaymentMethods, LocalTransactionPayment> {
        override fun toModel(model: RemotePaymentMethods?): LocalTransactionPayment {
            return when (model) {
                null -> LocalTransactionPayment()
                else -> {
                    val transactionBody = LocalTransactionPayment(

                    )
                    transactionBody
                }
            }
        }

        override fun toRemoteModel(model: LocalTransactionPayment?): RemotePaymentMethods {
            return when (model) {
                null -> RemotePaymentMethods()
                else -> {
                    RemotePaymentMethods(
                        id = model.uuid,
                        paymentMethod = model.paymentMethod.toString(),
                        amount = model.amount
                    )
                }
            }
        }
    }

    inner class ItemMapper : BaseMapper<RemoteItem, LocalItem> {
        override fun toModel(model: RemoteItem?): LocalItem {
            return when (model) {
                null -> LocalItem()
                else -> {
                    val item = LocalItem(
                        id = model.id!!,
                        barcode = model.barcode,
                        idCompany = model.idCompany,
                        itemDesc = model.itemDesc,
                        itemImage = model.itemImage,
                        itemName = model.itemName,
                        itemType = model.itemType,
                        itemUnit = model.itemUnit,
                        itemPrice = model.itemPrice,
                        idAddress = model.idAddress,
                        idCategory = model.idCategory,
                        idSubcategory = model.idSubcategory,
                        // costPrice = model.costPrice,
                        vat = model.vat,
                        discount = model.discount
                    )
                    item
                }
            }
        }

        override fun toRemoteModel(model: LocalItem?): RemoteItem {
            return when (model) {
                null -> RemoteItem()
                else -> {
                    RemoteItem(
                        id = model.id,
                        barcode = model.barcode,
                        idCompany = model.idCompany,
                        itemDesc = model.itemDesc,
                        itemImage = model.itemImage,
                        itemName = model.itemName,
                        itemType = model.itemType,
                        itemUnit = model.itemUnit,
                        itemPrice = model.itemPrice,
                        idAddress = model.idAddress,
                        idCategory = model.idCategory,
                        idSubcategory = model.idSubcategory,
                        //  costPrice = model.costPrice,
                        vat = model.vat,
                        discount = model.discount
                    )
                }
            }
        }
    }


    inner class CustomerMapper : BaseMapper<RemoteCustomer, LocalCustomer> {
        override fun toModel(model: RemoteCustomer?): LocalCustomer {
            return when (model) {
                null -> LocalCustomer()
                else -> {
                    val customer = LocalCustomer(
                        uuid = model.id!!,
                        name = model.name,
                        idType = model.idType,
                        idNum = model.idNum,
                        email = model.email,
                        address = model.address,
                        city = model.city,
                        country = model.country,
                        isSync = true,
                        lastServerSync = LocalDateTime.now()
                    )
                    customer
                }
            }
        }

        override fun toRemoteModel(model: LocalCustomer?): RemoteCustomer {
            return when (model) {
                null -> RemoteCustomer()
                else -> {
                    RemoteCustomer(
                        id = model.uuid,
                        name = model.name,
                        idType = model.idType,
                        idNum = model.idNum,
                        email = model.email,
                        address = model.address,
                        city = model.city,
                        country = model.country
                    )
                }
            }
        }
    }


    inner class CompanyMapper : BaseMapper<RemoteCompany, LocalCompany> {
        override fun toModel(model: RemoteCompany?): LocalCompany {
            return when (model) {
                null -> LocalCompany()
                else -> {
                    val company = LocalCompany(
                        id = model.id,
                        nuis = model.nuis,
                        name = model.name,
                        fullAddress = model.fullAddress,
                        city = model.city,
                        country = model.country,
                        logoImg = model.logoImg
                    )
                    company
                }
            }
        }

        override fun toRemoteModel(model: LocalCompany?): RemoteCompany {
            return when (model) {
                null -> RemoteCompany()
                else -> {
                    RemoteCompany(
                        id = model.id,
                        nuis = model.nuis,
                        name = model.name,
                        fullAddress = model.fullAddress,
                        city = model.city,
                        country = model.country,
                        logoImg = model.logoImg
                    )
                }
            }
        }
    }

    inner class AddressMapper : BaseMapper<RemoteAddress, LocalAddress> {
        override fun toModel(model: RemoteAddress?): LocalAddress {
            return when (model) {
                null -> LocalAddress()
                else -> {
                    val address = LocalAddress(
                        id = model.id,
                        name = model.name,
                        logoImg = model.logoImg,
                        fullAddress = model.fullAddress,
                        businUnitCode = model.businUnitCode,
                        city = model.city,
                        country = model.country
                    )
                    address
                }
            }
        }

        override fun toRemoteModel(model: LocalAddress?): RemoteAddress {
            return when (model) {
                null -> RemoteAddress()
                else -> {
                    RemoteAddress(
                        id = model.id,
                        name = model.name,
                        logoImg = model.logoImg,
                        fullAddress = model.fullAddress,
                        businUnitCode = model.businUnitCode,
                        city = model.city,
                        country = model.country
                    )
                }
            }
        }
    }

    inner class UserMapper : BaseMapper<RemoteUserData, LocalUserData> {
        override fun toModel(model: RemoteUserData?): LocalUserData {
            return when (model) {
                null -> LocalUserData()
                else -> {
                    val user = LocalUserData(
                        id = model.id,
                        email = model.email,
                        fullName = model.fullName,
                        operatorCode = model.operatorCode,
                        device = model.device
                    )
                    user
                }
            }
        }

        override fun toRemoteModel(model: LocalUserData?): RemoteUserData {
            return when (model) {
                null -> RemoteUserData()
                else -> {
                    RemoteUserData(
                        id = model.id,
                        email = model.email,
                        fullName = model.fullName,
                        operatorCode = model.operatorCode,
                        device = model.device
                    )
                }
            }
        }
    }


    inner class DeviceMapper : BaseMapper<RemoteDevice, LocalDevice> {
        override fun toModel(model: RemoteDevice?): LocalDevice {
            return when (model) {
                null -> LocalDevice()
                else -> {
                    val device = LocalDevice(
                        id = model.id,
                        deviceName = model.deviceName,
                        deviceImei = model.deviceImei,
                        licenseKey = model.licenseKey,
                        tcrCode = model.tcrCode,
                        validFrom = model.validFrom,
                        validTo = model.validTo
                    )
                    device
                }
            }
        }

        override fun toRemoteModel(model: LocalDevice?): RemoteDevice {
            return when (model) {
                null -> RemoteDevice()
                else -> {
                    RemoteDevice(
                        id = model.id,
                        deviceName = model.deviceName,
                        deviceImei = model.deviceImei,
                        licenseKey = model.licenseKey,
                        tcrCode = model.tcrCode,
                        validFrom = model.validFrom,
                        validTo = model.validTo
                    )
                }
            }
        }
    }


    inner class LicenceMapper : BaseMapper<RemoteLicense, LocalLicense> {
        override fun toModel(model: RemoteLicense?): LocalLicense {
            return when (model) {
                null -> LocalLicense()
                else -> {
                    val license = LocalLicense(
                        id = model.id,
                        deviceId = model.deviceId,
                        deviceImei = model.deviceImei,
                        licenseKey = model.licenseKey,
                        issueDate = model.issueDate,
                        expiryDate = model.expiryDate,
                        validFrom = model.validFrom,
                        validTo = model.validTo,
                        state = model.state
                    )
                    license
                }
            }
        }

        override fun toRemoteModel(model: LocalLicense?): RemoteLicense {
            return when (model) {
                null -> RemoteLicense()
                else -> {
                    RemoteLicense(
                        id = model.id,
                        deviceId = model.deviceId,
                        deviceImei = model.deviceImei,
                        licenseKey = model.licenseKey,
                        issueDate = model.issueDate,
                        expiryDate = model.expiryDate,
                        validFrom = model.validFrom,
                        validTo = model.validTo,
                        state = model.state
                    )
                }
            }
        }
    }

    inner class ConfigurationMapper : BaseMapper<RemoteConfiguration, LocalConfiguration> {
        override fun toModel(model: RemoteConfiguration?): LocalConfiguration {
            return when (model) {
                null -> LocalConfiguration()
                else -> {
                    val configuration = LocalConfiguration(
                        company = companyMapper.toModel(model.company),
                        address = addressMapper.toModel(model.address),
                        userData = userMapper.toModel(model.userData),
                        device = deviceMapper.toModel(model.device),
                        license = licenseMapper.toModel(model.license),
                        deviceId = model.deviceId,
                        maintainerCode = model.maintainerCode,
                        softCode = model.softCode,
                        verifyInvoiceURL = model.verifyInvoiceURL,
                        nextInvOrdNum = model.nextInvOrdNum
                    )
                    configuration
                }
            }
        }

        override fun toRemoteModel(model: LocalConfiguration?): RemoteConfiguration {
            return when (model) {
                null -> RemoteConfiguration()
                else -> {
                    RemoteConfiguration(
                        company = companyMapper.toRemoteModel(model.company),
                        address = addressMapper.toRemoteModel(model.address),
                        userData = userMapper.toRemoteModel(model.userData),
                        device = deviceMapper.toRemoteModel(model.device),
                        license = licenseMapper.toRemoteModel(model.license),
                        deviceId = model.deviceId,
                        maintainerCode = model.maintainerCode,
                        softCode = model.softCode,
                        verifyInvoiceURL = model.verifyInvoiceURL,
                        nextInvOrdNum = model.nextInvOrdNum
                    )
                }
            }
        }
    }


    inner class ItemResponseMapper : BaseMapper<RemoteGetItemResponse, LocalItem> {
        override fun toModel(model: RemoteGetItemResponse?): LocalItem {
            return when (model) {
                null -> LocalItem()
                else -> {
                    val item = LocalItem(
                        uuid = model.id!!,
                        barcode = model.barcode,
                        idCompany = model.idCompany,
                        itemDesc = model.itemDesc,
                        itemImage = model.itemImage,
                        itemName = model.itemName,
                        itemType = model.itemType,
                        itemUnit = model.itemUnit,
                        itemPrice = model.itemPrice,
                        // itemPriceWithWat = model.itemPrice?.times(1 + (model.vat!!/100)),
                        idCategory = model.idCategory,
                        idSubcategory = model.idSubcategory,
                        //costPrice = model.costPrice,
                        vat = model.vat,
                        isSync = true,
                        lastServerSync = LocalDateTime.now()
                    )
                    item
                }
            }
        }

        override fun toRemoteModel(model: LocalItem?): RemoteGetItemResponse {
            return when (model) {
                null -> RemoteGetItemResponse()
                else -> {
                    RemoteGetItemResponse(
                        id = model.uuid,
                        barcode = model.barcode,
                        idCompany = model.idCompany,
                        itemDesc = model.itemDesc,
                        itemImage = model.itemImage,
                        itemName = model.itemName,
                        itemType = model.itemType,
                        itemUnit = model.itemUnit,
                        itemPrice = model.itemPrice,
                        idCategory = model.idCategory,
                        idSubcategory = model.idSubcategory,
                        //  costPrice = model.costPrice,
                        vat = model.vat,
                    )
                }
            }
        }
    }


    inner class CategoryMapper : BaseMapper<RemoteCategory, LocalCategory> {
        override fun toModel(model: RemoteCategory?): LocalCategory {
            return when (model) {
                null -> LocalCategory()
                else -> {
                    val item = LocalCategory(
                        id = model.id,
                        categoryName = model.categoryName,
                        categoryOrder = model.categoryOrder,
                        categoryColor = model.categoryColor,
                        subcategories = subCategoryMapper.toListOfModel(model.subcategories!!),
                        status = model.status?.toBoolean()
                    )
                    item
                }
            }
        }

        override fun toRemoteModel(model: LocalCategory?): RemoteCategory {
            return when (model) {
                null -> RemoteCategory()
                else -> {
                    RemoteCategory(
                        categoryName = model.categoryName,
                        categoryColor = model.categoryColor,
                        categoryOrder = model.categoryOrder,
                        status = model.status?.toInt()
                    )
                }
            }
        }
    }

    inner class SubCategoryMapper : BaseMapper<RemoteSubCategory, LocalSubCategory> {
        override fun toModel(model: RemoteSubCategory?): LocalSubCategory {
            return when (model) {
                null -> LocalSubCategory()
                else -> {
                    val item = LocalSubCategory(
                        id = model.id,
                        subcategoryName = model.subcategoryName,
                        subcategoryOrder = model.subcategoryOrder,
                        status = model.status?.toBoolean()
                    )
                    item
                }
            }
        }


        override fun toRemoteModel(model: LocalSubCategory?): RemoteSubCategory {
            return when (model) {
                null -> RemoteSubCategory()
                else -> {
                    RemoteSubCategory(
                        subcategoryName = model.subcategoryName,
                        subcategoryOrder = model.subcategoryOrder,
                        status = model.status?.toInt()
                    )
                }
            }
        }
    }

    inner class EntityPointMapper : BaseMapper<RemoteEntityPoint, LocalEntityPoint> {
        override fun toModel(model: RemoteEntityPoint?): LocalEntityPoint {
            return when (model) {
                null -> LocalEntityPoint()
                else -> {
                    val entity = LocalEntityPoint(
                        uuid = model.id,
                        entityName = model.entityName,
                        entityColor = model.entityColor,
                        idBusinessUnit = model.idBusinessUnit
                    )
                    entity
                }
            }
        }


        override fun toRemoteModel(model: LocalEntityPoint?): RemoteEntityPoint {
            return when (model) {
                null -> RemoteEntityPoint()
                else -> {
                    RemoteEntityPoint(

                    )
                }
            }
        }
    }


    inner class OperatorMapper : BaseMapper<RemoteOperator, LocalOperator> {
        override fun toModel(model: RemoteOperator?): LocalOperator {
            return when (model) {
                null -> LocalOperator()
                else -> {
                    val entity = LocalOperator(
                        firstName = model.fullName?.split(" ")?.getOrNull(0),
                        lastName = model.fullName?.split(" ")?.getOrNull(1),
                        operatorCode = model.operatorCode,
                        pin = model.password,
                        nationalIdNo = model.nationalIdNo,
                        userRole = model.userRole,
//                        permissions = model.permissions?.map { (it as? RemoteOperator.Permissions)?.key!! },
                        status = model.status,
                    )
                    entity
                }
            }
        }


        override fun toRemoteModel(model: LocalOperator?): RemoteOperator {
            return when (model) {
                null -> RemoteOperator()
                else -> {
                    RemoteOperator(
                        businUnitId = RemoteOperator.BusinUnitId(id = model.businUnit),
                        cashRegister = RemoteOperator.CashRegister(id = model.cashRegister),
                        username = model.firstName + " " + model.lastName,
                        operatorCode = model.operatorCode,
                        password = model.pin,
                        nationalIdNo = model.nationalIdNo,
                        userRole = model.userRole,
                        permissions = model.permissions,
                        status = model.status,
                    )
                }
            }
        }
    }


}