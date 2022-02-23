package com.wind.billypos.view.mapper

import android.view.ViewConfiguration
import com.wind.billypos.base.BaseMapper
import com.wind.billypos.base.BaseViewMapper
import com.wind.billypos.base.Error.Companion.SUCCESS
import com.wind.billypos.data.local.model.*
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.RemoteConfiguration
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.PaymentMethod
import com.wind.billypos.utils.enums.PaymentMethodTypeSType
import com.wind.billypos.utils.formatFourDecimal
import com.wind.billypos.utils.formatTwoDecimal
import com.wind.billypos.view.model.*
import org.threeten.bp.LocalDateTime
import java.util.*

class ViewBillyPosMapper {

    val viewCashBalanceMapper: ViewCashBalanceMapper = ViewCashBalanceMapper()
    val viewFiscalDigitalKeyMapper: ViewFiscalDigitalKeyMapper = ViewFiscalDigitalKeyMapper()
    val viewCompanyMapper: ViewCompanyMapper = ViewCompanyMapper()
    val viewCustomerMapper: ViewCustomerMapper = ViewCustomerMapper()
    val viewAddressMapper: ViewAddressMapper = ViewAddressMapper()
    val viewUserMapper: ViewUserMapper = ViewUserMapper()
    val viewDeviceMapper: ViewDeviceMapper = ViewDeviceMapper()
    val viewConfigurationMapper: ViewConfigurationMapper = ViewConfigurationMapper()
    val viewLicenceMapper: ViewLicenceMapper = ViewLicenceMapper()
    val viewCategoryMapper: ViewCategoryMapper = ViewCategoryMapper()
    val viewSubCategoryMapper: ViewSubCategoryMapper = ViewSubCategoryMapper()
    val viewItemMapper: ViewItemMapper = ViewItemMapper()
    val viewTransactionHeadMapper: ViewTransactionHeadMapper = ViewTransactionHeadMapper()
    val viewTransactionBodyMapper: ViewTransactionBodyMapper = ViewTransactionBodyMapper()
    val viewTransactionPaymentMapper: ViewTransactionPaymentMapper = ViewTransactionPaymentMapper()
    val viewTransactionBodyWithItemMapper: ViewTransactionBodyWithItemMapper =
        ViewTransactionBodyWithItemMapper()
    val viewEntityPointMapper: ViewEntityPointMapper = ViewEntityPointMapper()
    val viewOperatorMapper: ViewOperatorMapper = ViewOperatorMapper()

    inner class ViewCashBalanceMapper : BaseViewMapper<LocalCashBalance, CashBalance> {
        override fun toModel(model: LocalCashBalance?): CashBalance {
            return when (model) {
                null -> CashBalance()
                else -> {
                    val cashBalance = CashBalance(
                        id = model.id,
                        uuid = model.uuid,
                        deviceId = model.deviceId,
                        userId = model.userId,
                        cashAmount = model.cashAmount,
                        balanceState = model.balanceState,
                        notes = model.notes,
                        operation = model.operation,
                        cashBalanceState = model.cashBalanceState,
                        responseUUID = model.responseUUID,
                        changeDateTime = model.changeDateTime,
                        createdAt = model.createdAt,
                        cashDepositDate = model.cashDepositDate,
                        isFiscalised = model.isFiscalised,
                        isSync = model.isSync,
                        fcdc = model.fcdc
                    )
                    cashBalance.message = model.message
                    cashBalance.code = if (model.code != 0) model.code else SUCCESS
                    cashBalance
                }
            }
        }

        override fun toLocalModel(model: CashBalance?): LocalCashBalance {
            return when (model) {
                null -> LocalCashBalance()
                else -> {
                    LocalCashBalance(
                        id = model.id,
                        uuid = model.uuid,
                        deviceId = model.deviceId,
                        userId = model.userId,
                        cashAmount = model.cashAmount,
                        balanceState = model.balanceState,
                        notes = model.notes,
                        operation = model.operation,
                        cashBalanceState = model.cashBalanceState,
                        responseUUID = model.responseUUID,
                        changeDateTime = model.changeDateTime,
                        createdAt = model.createdAt,
                        cashDepositDate = model.cashDepositDate,
                        isFiscalised = model.isFiscalised,
                        isSync = model.isSync,
                        fcdc = model.fcdc
                    )
                }
            }
        }
    }


    /** Company Mapper **/
    inner class ViewCompanyMapper : BaseViewMapper<LocalCompany, Company> {
        override fun toModel(model: LocalCompany?): Company {
            return when (model) {
                null -> Company()
                else -> {
                    val copmany = Company(
                        id = model.id,
                        nuis = model.nuis,
                        name = model.name,
                        fullAddress = model.fullAddress,
                        city = model.city,
                        country = model.country,
                        logoImg = model.logoImg,
                        fiscalDigitalKey = viewFiscalDigitalKeyMapper.toModel(model.fiscalDigitalKey),
                    )
                    copmany.message = model.message
                    copmany.code = if (model.code != 0) model.code else SUCCESS
                    copmany
                }
            }
        }

        override fun toLocalModel(model: Company?): LocalCompany {
            return when (model) {
                null -> LocalCompany()
                else -> {
                    LocalCompany(
                        id = model.id,
                        nuis = model.nuis,
                        name = model.name,
                        fullAddress = model.fullAddress,
                        city = model.city,
                        country = model.country,
                        logoImg = model.logoImg,
                        fiscalDigitalKey = viewFiscalDigitalKeyMapper.toLocalModel(model.fiscalDigitalKey),
                    )
                }
            }
        }
    }


    /** Address Mapper **/
    inner class ViewAddressMapper : BaseViewMapper<LocalAddress, Address> {
        override fun toModel(model: LocalAddress?): Address {
            return when (model) {
                null -> Address()
                else -> {
                    val address = Address(
                        id = model.id,
                        name = model.name,
                        logoImg = model.logoImg,
                        fullAddress = model.fullAddress,
                        businUnitCode = model.businUnitCode,
                        city = model.city,
                        country = model.country
                    )
                    address.message = model.message
                    address.code = if (model.code != 0) model.code else SUCCESS
                    address
                }
            }
        }

        override fun toLocalModel(model: Address?): LocalAddress {
            return when (model) {
                null -> LocalAddress()
                else -> {
                    LocalAddress(
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


    /** User Mapper **/
    inner class ViewUserMapper : BaseViewMapper<LocalUserData, UserData> {
        override fun toModel(model: LocalUserData?): UserData {
            return when (model) {
                null -> UserData()
                else -> {
                    val user = UserData(
                        id = model.id,
                        email = model.email,
                        fullName = model.fullName,
                        operatorCode = model.operatorCode,
                        device = model.device,
                    )
                    user.message = model.message
                    user.code = if (model.code != 0) model.code else SUCCESS
                    user
                }
            }
        }

        override fun toLocalModel(model: UserData?): LocalUserData {
            return when (model) {
                null -> LocalUserData()
                else -> {
                    LocalUserData(
                        id = model.id,
                        email = model.email,
                        fullName = model.fullName,
                        operatorCode = model.operatorCode,
                        device = model.device,
                    )
                }
            }
        }
    }

    /** Device Mapper **/
    inner class ViewDeviceMapper : BaseViewMapper<LocalDevice, Device> {
        override fun toModel(model: LocalDevice?): Device {
            return when (model) {
                null -> Device()
                else -> {
                    val device = Device(
                        id = model.id,
                        deviceName = model.deviceName,
                        deviceImei = model.deviceImei,
                        licenseKey = model.licenseKey,
                        tcrCode = model.tcrCode,
                        validFrom = model.validFrom,
                        validTo = model.validTo
                    )
                    device.message = model.message
                    device.code = if (model.code != 0) model.code else SUCCESS
                    device
                }
            }
        }

        override fun toLocalModel(model: Device?): LocalDevice {
            return when (model) {
                null -> LocalDevice()
                else -> {
                    LocalDevice(
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

    /** Device Mapper **/
    inner class ViewLicenceMapper : BaseViewMapper<LocalLicense, License> {
        override fun toModel(model: LocalLicense?): License {
            return when (model) {
                null -> License()
                else -> {
                    val license = License(
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
                    license.message = model.message
                    license.code = if (model.code != 0) model.code else SUCCESS
                    license
                }
            }
        }

        override fun toLocalModel(model: License?): LocalLicense {
            return when (model) {
                null -> LocalLicense()
                else -> {
                    LocalLicense(
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

    /** Fiscal Certificate Mapper **/
    inner class ViewFiscalDigitalKeyMapper :
        BaseViewMapper<LocalFiscalDigitalKey, Company.FiscalDigitalKey> {
        override fun toModel(model: LocalFiscalDigitalKey?): Company.FiscalDigitalKey {
            return when (model) {
                null -> Company.FiscalDigitalKey()
                else -> {
                    val fiscalCertificate = Company.FiscalDigitalKey(
                        id = model.id,
                        certificate = model.certificate,
                        privateKey = model.privateKey,
                        pib = model.pib,
                        expireDate = model.expireDate,
                    )
                    fiscalCertificate.message = model.message
                    fiscalCertificate.code = if (model.code != 0) model.code else SUCCESS
                    fiscalCertificate
                }
            }
        }

        override fun toLocalModel(model: Company.FiscalDigitalKey?): LocalFiscalDigitalKey {
            return when (model) {
                null -> LocalFiscalDigitalKey()
                else -> {
                    LocalFiscalDigitalKey(
                        id = model.id,
                        certificate = model.certificate,
                        privateKey = model.privateKey,
                        pib = model.pib,
                        expireDate = model.expireDate,
                    )
                }
            }
        }
    }


    /** Category Mapper **/
    inner class ViewCategoryMapper :
        BaseViewMapper<LocalCategory, Category> {
        override fun toModel(model: LocalCategory?): Category {
            return when (model) {
                null -> Category()
                else -> {
                    val category = Category(
                        id = model.id,
                        idCompany = model.idCompany,
                        categoryName = model.categoryName,
                        categoryColor = model.categoryColor,
                        categoryOrder = model.categoryOrder,
                        subcategories = viewSubCategoryMapper.toListOfModel(model.subcategories),
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt
                    )
                    category.message = model.message
                    category.code = if (model.code != 0) model.code else SUCCESS
                    category
                }
            }
        }

        override fun toLocalModel(model: Category?): LocalCategory {
            return when (model) {
                null -> LocalCategory()
                else -> {
                    LocalCategory(
                        id = model.id,
                        idCompany = model.idCompany,
                        categoryName = model.categoryName,
                        categoryColor = model.categoryColor,
                        categoryOrder = model.categoryOrder,
                        subcategories = viewSubCategoryMapper.toLocalListOfModel(model.subcategories),
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt
                    )
                }
            }
        }
    }

    /** SubCategory Mapper **/
    inner class ViewSubCategoryMapper :
        BaseViewMapper<LocalSubCategory, SubCategory> {
        override fun toModel(model: LocalSubCategory?): SubCategory {
            return when (model) {
                null -> SubCategory()
                else -> {
                    val subCategory = SubCategory(
                        id = model.id,
                        idCompany = model.idCompany,
                        subcategoryName = model.subcategoryName,
                        id_category = model.id_category,
                        subcategoryOrder = model.subcategoryOrder,
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt
                    )
                    subCategory.message = model.message
                    subCategory.code = if (model.code != 0) model.code else SUCCESS
                    subCategory
                }
            }
        }

        override fun toLocalModel(model: SubCategory?): LocalSubCategory {
            return when (model) {
                null -> LocalSubCategory()
                else -> {
                    LocalSubCategory(
                        id = model.id,
                        idCompany = model.idCompany,
                        subcategoryName = model.subcategoryName,
                        id_category = model.id_category,
                        subcategoryOrder = model.subcategoryOrder,
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt
                    )
                }
            }
        }
    }

    /** Item Mapper **/
    inner class ViewItemMapper :
        BaseViewMapper<LocalItem, Item> {
        override fun toModel(model: LocalItem?): Item {
            return when (model) {
                null -> Item()
                else -> {
                    val item = Item(
                        id = model.id,
                        uuid = model.uuid,
                        idCompany = model.idCompany,
                        idAddress = model.idAddress,
                        idCategory = model.idCategory,
                        idSubcategory = model.idSubcategory,
                        itemName = model.itemName,
                        itemDesc = model.itemDesc,
                        barcode = model.barcode,
                        itemImage = model.itemImage,
                        itemUnit = model.itemUnit,
                        itemType = model.itemType,
                        itemPrice = model.itemPrice,
                        itemPriceWithDiscount = model.itemPriceWithDiscount,
                        discount = model.discount,
                        unitPrice = model.unitPrice,
                        vat = model.vat,
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt,
                        categoryColor = model.categoryColor,
                        lastServerSync = model.lastServerSync,
                        isSync = model.isSync
                    )
                    item.message = model.message
                    item.code = if (model.code != 0) model.code else SUCCESS
                    item
                }
            }
        }

        override fun toLocalModel(model: Item?): LocalItem {
            return when (model) {
                null -> LocalItem()
                else -> {
                    LocalItem(
                        id = model.id,
                        uuid = model.uuid,
                        idCompany = model.idCompany,
                        idAddress = model.idAddress,
                        idCategory = model.idCategory,
                        idSubcategory = model.idSubcategory,
                        itemName = model.itemName,
                        itemDesc = model.itemDesc,
                        barcode = model.barcode,
                        itemImage = model.itemImage,
                        itemUnit = model.itemUnit,
                        itemType = model.itemType,
                        itemPrice = model.itemPrice.formatTwoDecimal(),
                        itemPriceWithDiscount = model.itemPriceWithDiscount.formatTwoDecimal(),
                        discount = model.discount.formatTwoDecimal(),
                        unitPrice = model.unitPrice.formatTwoDecimal(),
                        vat = model.vat,
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt,
                        categoryColor = model.categoryColor,
                        lastServerSync = model.lastServerSync,
                        isSync = model.isSync
                    )
                }
            }
        }
    }


    /** Transaction Head Mapper **/
    inner class ViewTransactionHeadMapper :
        BaseViewMapper<LocalTransactionHead, TransactionHead> {
        override fun toModel(model: LocalTransactionHead?): TransactionHead {
            return when (model) {
                null -> TransactionHead()
                else -> {
                    val transactionHead = TransactionHead(
                        id = model.id,
                        uuid = model.uuid,
                        idCompany = model.idCompany,
                        idAddress = model.idAddress,
                        idDevice = model.idDevice,
                        idUser = model.idUser,
                        idTransactionParent = model.idTransactionParent,
                        total = model.total,
                        totalWithVat = model.totalWithVat,
                        vatAmount = model.vatAmount,
                        discountValue = model.discountValue,
                        invoiceNum = model.invoiceNum,
                        deviceTCR = model.deviceTCR,
                        uuidTransaction = model.uuidTransaction,
                        fiscalUUID = model.fiscalUUID,
                        fiscalFIC = model.fiscalFIC,
                        reversedFIC = model.reversedFIC,
                        iic = model.iic,
                        invoiceNo = model.invoiceNo,
                        transactionState = model.transactionState,
                        paymentMethod = model.paymentMethod,
                        receiptDateTime = model.receiptDateTime,
                        createdAt = model.createdAt,
                        dayCreated = model.dayCreated,
                        fiscalizedAt = model.fiscalizedAt,
                        isFiscalized = model.isFiscalized,
                        isSync = model.isSync,
                        reversedAt = model.reversedAt,
                        isReversed = model.isReversed,
                        idCustomer = model.idCustomer,
                        lastServerSync = model.lastServerSync,
                        isOrderInvoice = model.isOrderInvoice,
                        isPaid = model.isPaid,
                        idEntity = model.idEntity,
                        isSummaryInvoice = model.isSummaryInvoice,
                        items = viewTransactionBodyMapper.toListOfModel(model.items!!),
                        paymentMethods = viewTransactionPaymentMapper.toListOfModel(model.paymentMethods!!)
                    )
                    transactionHead.message = model.message
                    transactionHead.code = if (model.code != 0) model.code else SUCCESS
                    transactionHead
                }
            }
        }

        override fun toLocalModel(model: TransactionHead?): LocalTransactionHead {
            return when (model) {
                null -> LocalTransactionHead()
                else -> {
                    LocalTransactionHead(
                        id = model.id,
                        uuid = model.uuid,
                        idCompany = model.idCompany,
                        idAddress = model.idAddress,
                        idDevice = model.idDevice,
                        idUser = model.idUser,
                        idTransactionParent = model.idTransactionParent,
                        total = model.total,
                        totalWithVat = model.totalWithVat,
                        vatAmount = model.vatAmount,
                        discountValue = model.discountValue,
                        invoiceNum = model.invoiceNum,
                        deviceTCR = model.deviceTCR,
                        uuidTransaction = model.uuidTransaction,
                        fiscalUUID = model.fiscalUUID,
                        fiscalFIC = model.fiscalFIC,
                        reversedFIC = model.reversedFIC,
                        iic = model.iic,
                        invoiceNo = model.invoiceNo,
                        transactionState = model.transactionState,
                        paymentMethod = model.paymentMethod,
                        receiptDateTime = model.receiptDateTime,
                        createdAt = model.createdAt,
                        dayCreated = model.dayCreated,
                        fiscalizedAt = model.fiscalizedAt,
                        isFiscalized = model.isFiscalized,
                        isSync = model.isSync,
                        reversedAt = model.reversedAt,
                        isReversed = model.isReversed,
                        idCustomer = model.idCustomer,
                        lastServerSync = model.lastServerSync,
                        isOrderInvoice = model.isOrderInvoice,
                        isPaid = model.isPaid,
                        idEntity = model.idEntity,
                        isSummaryInvoice = model.isSummaryInvoice,
                        items = viewTransactionBodyMapper.toLocalListOfModel(model.items!!),
                        paymentMethods = viewTransactionPaymentMapper.toLocalListOfModel(model.paymentMethods!!)
                    )
                }
            }
        }
    }


    /** Transaction Body Mapper **/
    inner class ViewTransactionBodyMapper :
        BaseViewMapper<LocalTransactionBody, TransactionBody> {
        override fun toModel(model: LocalTransactionBody?): TransactionBody {
            return when (model) {
                null -> TransactionBody()
                else -> {
                    val transactionBody = TransactionBody(
                        id = model.id,
                        uuid = model.uuid,
                        idTransactionHead = model.idTransactionHead,
                        idCompany = model.idCompany,
                        idAddress = model.idAddress,
                        idDevice = model.idDevice,
                        idUser = model.idUser,
                        idItem = model.idItem,
                        itemName = model.itemName,
                        itemUnit = model.itemUnit,
                        idCategory = model.idCategory,
                        unitPrice = model.unitPrice,
                        unitPriceWithVat = model.unitPriceWithVat,
                        quantity = model.quantity,
                        rabat = model.rabat,
                        vatRate = model.vatRate,
                        vatAmount = model.vatAmount,
                        itemPrice = model.itemPrice,
                        itemPriceWithDiscount = model.itemPriceWithDiscount,
                        total = model.total,
                        totalWithVat = model.totalWithVat,
                        costPrice = model.costPrice,
                        lastServerSync = model.lastServerSync,
                        item = viewItemMapper.toModel(model.item)
                    )
                    transactionBody.message = model.message
                    transactionBody.code = if (model.code != 0) model.code else SUCCESS
                    transactionBody
                }
            }
        }

        override fun toLocalModel(model: TransactionBody?): LocalTransactionBody {
            return when (model) {
                null -> LocalTransactionBody()
                else -> {
                    LocalTransactionBody(
                        id = model.id,
                        uuid = model.uuid,
                        idTransactionHead = model.idTransactionHead,
                        idCompany = model.idCompany,
                        idAddress = model.idAddress,
                        idDevice = model.idDevice,
                        idUser = model.idUser,
                        idItem = model.idItem,
                        itemName = model.itemName,
                        itemUnit = model.itemUnit,
                        idCategory = model.idCategory,
                        unitPrice = model.unitPrice?.formatFourDecimal(),
                        unitPriceWithVat = model.unitPriceWithVat?.formatFourDecimal(),
                        quantity = model.quantity,
                        rabat = model.rabat,
                        vatRate = model.vatRate,
                        vatAmount = model.vatAmount?.formatFourDecimal(),
                        itemPrice = model.itemPrice?.formatFourDecimal(),
                        itemPriceWithDiscount = model.itemPriceWithDiscount.formatFourDecimal(),
                        total = model.total?.formatFourDecimal(),
                        totalWithVat = model.totalWithVat?.formatFourDecimal(),
                        costPrice = model.costPrice,
                        lastServerSync = model.lastServerSync,
                        item = viewItemMapper.toLocalModel(model.item)
                    )
                }
            }
        }
    }

    /** Transaction Body With Item Mapper **/
    inner class ViewTransactionBodyWithItemMapper :
        BaseViewMapper<LocalTransactionBodyWithItem, TransactionBodyWithItem> {
        override fun toModel(model: LocalTransactionBodyWithItem?): TransactionBodyWithItem {
            return when (model) {
                null -> TransactionBodyWithItem()
                else -> {
                    val transactionBody = TransactionBodyWithItem(
                        body = viewTransactionBodyMapper.toModel(model.body),
                        item = viewItemMapper.toModel(model.item)
                    )
                    transactionBody.message = model.message
                    transactionBody.code = if (model.code != 0) model.code else SUCCESS
                    transactionBody
                }
            }
        }

        override fun toLocalModel(model: TransactionBodyWithItem?): LocalTransactionBodyWithItem {
            return when (model) {
                null -> LocalTransactionBodyWithItem()
                else -> {
                    LocalTransactionBodyWithItem(
                        body = viewTransactionBodyMapper.toLocalModel(model.body),
                        item = viewItemMapper.toLocalModel(model.item)
                    )
                }
            }
        }
    }

    /** Transaction Payment Mapper **/
    inner class ViewTransactionPaymentMapper :
        BaseViewMapper<LocalTransactionPayment, TransactionPayment> {
        override fun toModel(model: LocalTransactionPayment?): TransactionPayment {
            return when (model) {
                null -> TransactionPayment()
                else -> {
                    val transactionPayment = TransactionPayment(
                        id = model.id,
                        uuid = model.uuid,
                        idTransactionHead = model.idTransactionHead,
                        paymentMethod = model.paymentMethod,
                        amount = model.amount,
                        paymentDetails = model.paymentDetails,
                        createdAt = model.createdAt
                    )
                    transactionPayment.message = model.message
                    transactionPayment.code = if (model.code != 0) model.code else SUCCESS
                    transactionPayment
                }
            }
        }

        override fun toLocalModel(model: TransactionPayment?): LocalTransactionPayment {
            return when (model) {
                null -> LocalTransactionPayment()
                else -> {
                    LocalTransactionPayment(
                        id = model.id,
                        uuid = model.uuid,
                        idTransactionHead = model.idTransactionHead,
                        paymentMethod = model.paymentMethod,
                        amount = model.amount,
                        paymentDetails = model.paymentDetails,
                        createdAt = model.createdAt
                    )
                }
            }
        }
    }


    /** Customer Mapper **/
    inner class ViewCustomerMapper :
        BaseViewMapper<LocalCustomer, Customer> {
        override fun toModel(model: LocalCustomer?): Customer {
            return when (model) {
                null -> Customer()
                else -> {
                    val customer = Customer(
                        id = model.id,
                        uuid = model.uuid,
                        name = model.name,
                        email = model.email,
                        phone = model.phone,
                        address = model.address,
                        city = model.city,
                        country = model.country,
                        idType = model.idType,
                        idNum = model.idNum,
                        idCompany = model.idCompany,
                        deviceId = model.deviceId,
                        userID = model.userID,
                        createdAt = model.createdAt,
                        lastServerSync = model.lastServerSync,
                        isSync = model.isSync
                    )
                    customer.message = model.message
                    customer.code = if (model.code != 0) model.code else SUCCESS
                    customer
                }
            }
        }

        override fun toLocalModel(model: Customer?): LocalCustomer {
            return when (model) {
                null -> LocalCustomer()
                else -> {
                    LocalCustomer(
                        id = model.id,
                        uuid = model.uuid,
                        name = model.name,
                        email = model.email,
                        phone = model.phone,
                        address = model.address,
                        city = model.city,
                        country = model.country,
                        idType = model.idType,
                        idNum = model.idNum,
                        idCompany = model.idCompany,
                        deviceId = model.deviceId,
                        userID = model.userID,
                        createdAt = model.createdAt,
                        lastServerSync = model.lastServerSync,
                        isSync = model.isSync
                    )
                }
            }
        }
    }


    inner class ViewConfigurationMapper : BaseViewMapper<LocalConfiguration, Configuration> {
        override fun toModel(model: LocalConfiguration?): Configuration {
            return when (model) {
                null -> Configuration()
                else -> {
                    val configuration = Configuration(
                        company = viewCompanyMapper.toModel(model.company),
                        address = viewAddressMapper.toModel(model.address),
                        userData = viewUserMapper.toModel(model.userData),
                        device = viewDeviceMapper.toModel(model.device),
                        license = viewLicenceMapper.toModel(model.license),
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

        override fun toLocalModel(model: Configuration?): LocalConfiguration {
            return when (model) {
                null -> LocalConfiguration()
                else -> {
                    LocalConfiguration(
                        company = viewCompanyMapper.toLocalModel(model.company),
                        address = viewAddressMapper.toLocalModel(model.address),
                        userData = viewUserMapper.toLocalModel(model.userData),
                        device = viewDeviceMapper.toLocalModel(model.device),
                        license = viewLicenceMapper.toLocalModel(model.license),
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

    inner class ViewEntityPointMapper : BaseViewMapper<LocalEntityPoint, EntityPoint> {
        override fun toModel(model: LocalEntityPoint?): EntityPoint {
            return when (model) {
                null -> EntityPoint()
                else -> {
                    val entity = EntityPoint(
                        id = model.id,
                        uuid = model.uuid,
                        entityName = model.entityName,
                        entityColor = model.entityColor,
                        idBusinessUnit = model.idBusinessUnit,
                        createdAt = model.createdAt,
                        updatedAt = model.updatedAt,
                    )
                    entity
                }
            }
        }

        override fun toLocalModel(model: EntityPoint?): LocalEntityPoint {
            return when (model) {
                null -> LocalEntityPoint()
                else -> {
                    LocalEntityPoint(
                        id = model.id,
                        uuid = model.uuid,
                        entityName = model.entityName,
                        entityColor = model.entityColor,
                        idBusinessUnit = model.idBusinessUnit,
                        createdAt = model.createdAt,
                        updatedAt = model.updatedAt,
                    )
                }
            }
        }
    }

    inner class ViewOperatorMapper : BaseViewMapper<LocalOperator, Operator> {
        override fun toModel(model: LocalOperator?): Operator {
            return when (model) {
                null -> Operator()
                else -> {
                    val entity = Operator(
                        id = model.id,
                        businUnit = model.businUnit,
                        cashRegister = model.cashRegister,
                        firstName = model.firstName,
                        lastName = model.lastName,
                        operatorCode = model.operatorCode,
                        pin = model.pin,
                        nationalIdNo = model.nationalIdNo,
                        userRole = model.userRole,
                        permissions = model.permissions,
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt,
                    )
                    entity
                }
            }
        }

        override fun toLocalModel(model: Operator?): LocalOperator {
            return when (model) {
                null -> LocalOperator()
                else -> {
                    LocalOperator(
                        id = model.id,
                        businUnit = model.businUnit,
                        cashRegister = model.cashRegister,
                        firstName = model.firstName,
                        lastName = model.lastName,
                        operatorCode = model.operatorCode,
                        pin = model.pin,
                        nationalIdNo = model.nationalIdNo,
                        userRole = model.userRole,
                        permissions = model.permissions,
                        status = model.status,
                        isActive = model.isActive,
                        createdAt = model.createdAt,
                    )
                }
            }
        }
    }


}