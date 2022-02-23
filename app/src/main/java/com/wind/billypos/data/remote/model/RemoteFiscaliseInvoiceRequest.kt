package com.wind.billypos.data.remote.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import java.util.*

@Xml(name = "SOAP-ENV:Envelope")
data class RemoteFiscaliseInvoiceRequest(
    @Attribute(name = "xmlns:SOAP-ENV")
    val xmlns: String = "http://schemas.xmlsoap.org/soap/envelope/",
    @Element(name = "SOAP-ENV:Header")
    val header: SoapHeaderFiscalization = SoapHeaderFiscalization(),
    @Element(name = "SOAP-ENV:Body")
    val body: SoapBodyFiscalization = SoapBodyFiscalization()
){

    @Xml(name = "SOAP-ENV:Header")
    data class SoapHeaderFiscalization(
        @Attribute(name = "header")
        val header: String? = null
    )

    @Xml(name = "SOAP-ENV:Body")
    data class SoapBodyFiscalization(
        @Element(name = "RegisterInvoiceRequest")
        val registerInvoiceRequest: RegisterInvoiceRequest? = null
    )

    @Xml(name = "RegisterInvoiceRequest")
    data class RegisterInvoiceRequest (
        @Attribute(name = "Id")
        val id: String = "Request",
        @Attribute(name = "Version")
        val version: Int = 3,
        @Attribute(name = "xmlns")
        val xmlns: String = "https://eFiskalizimi.tatime.gov.al/FiscalizationService/schema",
        @Attribute(name = "xmlns:ns2")
        val xmlnsNS2: String = "http://www.w3.org/2000/09/xmldsig#",
        @Element(name = "Header")
        val header : HeaderFiscalization? = HeaderFiscalization(),
        @Element(name = "Invoice")
        val invoice : Invoice? = null
    ){

        @Xml(name = "Header")
        data class HeaderFiscalization(
            @Attribute(name = "SendDateTime")
            val sendDateTime: String? = null,
            @Attribute(name = "UUID")
            val uuid: String? = UUID.randomUUID().toString(),
            @Attribute(name = "SubseqDelivType")
            val subseqDelivType: String? = null
        )

        @Xml(name = "Invoice")
        class Invoice(
            @Attribute(name = "BusinUnitCode")
            val businUnitCode: String? = null,
            @Attribute(name = "IssueDateTime")
            val issueDateTime: String? = null,
            @Attribute(name = "IIC")
            val IIC: String? = null,
            @Attribute(name = "IICSignature")
            val IICSignature: String? = null,
            @Attribute(name = "InvNum")
            val invNum: String? = null,
            @Attribute(name = "InvOrdNum")
            val invOrdNum: Int? = null,
            @Attribute(name = "IsIssuerInVAT")
            val isIssuerInVAT: Boolean? = null,
            @Attribute(name = "IsReverseCharge")
            val isReverseCharge: Boolean? = null,
            @Attribute(name = "IsSimplifiedInv")
            val isSimplifiedInv: Boolean? = null,
            @Attribute(name = "OperatorCode")
            val operatorCode: String? = null,
            @Attribute(name = "SoftCode")
            val softCode: String? = null,
            @Attribute(name = "TCRCode")
            val TCRCode: String? = null,
            @Attribute(name = "TotPrice")
            val totPrice: String? = null,
            @Attribute(name = "TotPriceWoVAT")
            val totPriceWoVAT: String? = null,
            @Attribute(name = "TotVATAmt")
            val totVATAmt: String? = null,
            @Attribute(name = "TypeOfInv")
            val typeOfInv: String? = null,
            @Element(name = "CorrectiveInv")
            val correctiveInv: CorrectiveInv? = null,
            @Element(name = "PayMethods")
            val payMethods: PayMethods?,
            @Element(name = "Seller")
            val seller: Seller? = null,
            @Element(name = "Items")
            val items: Items,
            @Element(name = "SameTaxes")
            val sameTaxes: SameTaxes? = null
        ){

            @Xml(name = "CorrectiveInv")
            data class CorrectiveInv(
                @Attribute(name = "IICRef")
                val IICRef: String? = null,
                @Attribute(name = "IssueDateTime")
                val issueDateTime: String? = null,
                @Attribute(name = "Type")
                val type: String? = null,
            )

            @Xml(name = "PayMethods")
            data class PayMethods(
                @Element(name = "PayMethod")
                val payMethod: List<PayMethod>
            ){

                @Xml(name = "PayMethod")
                data class PayMethod(
                    @Attribute(name = "Amt")
                    val amt: String? = null,
                    @Attribute(name = "Type")
                    val type: String? = null
                )

            }

            @Xml(name = "Seller")
            data class Seller(
                @Attribute(name = "Address")
                val address: String? = null,
                @Attribute(name = "Country")
                val country: String? = null,
                @Attribute(name = "IDNum")
                val idNum: String? = null,
                @Attribute(name = "IDType")
                val idType: String? = null,
                @Attribute(name = "Name")
                val name: String? = null,
                @Attribute(name = "Town")
                val town: String? = null,
            )

            @Xml(name = "Items")
            data class Items(
                @Element(name = "I")
                val item: List<Item>
            ){

                @Xml(name = "I")
                data class Item(
                    @Attribute(name = "C")
                    val code: String? = null,
                    @Attribute(name = "N")
                    val name: String? = null,
                    @Attribute(name = "PA")
                    val pa: String? = null,
                    @Attribute(name = "PB")
                    val pb: String? = null,
                    @Attribute(name = "Q")
                    val quantity: String? = null,
                    @Attribute(name = "R")
                    val rabat: String? = null,
                    @Attribute(name = "RR")
                    val rr: Boolean = false,
                    @Attribute(name = "U")
                    val measureUnit: String? = null,
                    @Attribute(name = "UPB")
                    val upb: String? = null,
                    @Attribute(name = "UPA")
                    val upa: String? = null,
                    @Attribute(name = "VA")
                    val va: String? = null,
                    @Attribute(name = "VR")
                    val vatRate: String? = null
                )

            }

            @Xml(name = "SameTaxes")
            data class SameTaxes(
                @Element(name = "SameTax")
                val tax: List<SameTax>
            ){

                @Xml(name = "SameTax")
                data class SameTax(
                    @Attribute(name = "NumOfItems")
                    val numOfItems: Int? = null,
                    @Attribute(name = "PriceBefVAT")
                    val priceBefVAT: String? = null,
                    @Attribute(name = "VATAmt")
                    val VATAmt: String? = null,
                    @Attribute(name = "VATRate")
                    val VATRate: String? = null,
                )

            }
        }
    }

    companion object {
        const val ELEMENT_TO_SIGN = "RegisterInvoiceRequest"
    }
}