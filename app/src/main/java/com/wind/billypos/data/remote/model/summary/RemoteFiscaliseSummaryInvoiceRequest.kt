package com.wind.billypos.data.remote.model.summary

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import com.wind.billypos.data.remote.model.RemoteFiscaliseInvoiceRequest
import java.util.*

@Xml(name = "SOAP-ENV:Envelope")
data class RemoteFiscaliseSummaryInvoiceRequest(
    @Attribute(name = "xmlns:SOAP-ENV")
    val xmlns: String = "http://schemas.xmlsoap.org/soap/envelope/",
    @Element(name = "SOAP-ENV:Header")
    val header: SoapSummaryHeaderFiscalization = SoapSummaryHeaderFiscalization(),
    @Element(name = "SOAP-ENV:Body")
    val body: SoapSummaryBodyFiscalization = SoapSummaryBodyFiscalization()
) {

    @Xml(name = "SOAP-ENV:Header")
    data class SoapSummaryHeaderFiscalization(
        @Attribute(name = "header")
        val header: String? = null
    )

    @Xml(name = "SOAP-ENV:Body")
    data class SoapSummaryBodyFiscalization(
        @Element(name = "RegisterInvoiceRequest")
        val registerSummaryInvoiceRequest: RegisterSummaryInvoiceRequest? = null
    )

    @Xml(name = "RegisterInvoiceRequest")
    data class RegisterSummaryInvoiceRequest(
        @Attribute(name = "xmlns")
        val xmlns: String = "https://eFiskalizimi.tatime.gov.al/FiscalizationService/schema",
        @Attribute(name = "xmlns:ns2")
        val xmlnsNS2: String = "http://www.w3.org/2000/09/xmldsig#",
        @Attribute(name = "Id")
        val id: String = "Request",
        @Attribute(name = "Version")
        val version: Int = 3,
        @Element(name = "Header")
        val header: HeaderSummaryFiscalization? = HeaderSummaryFiscalization(),
        @Element(name = "Invoice")
        val invoice: SummaryInvoice? = null
    ) {

        @Xml(name = "Header")
        data class HeaderSummaryFiscalization(
            @Attribute(name = "SendDateTime")
            val sendDateTime: String? = null,
            @Attribute(name = "UUID")
            val uuid: String? = UUID.randomUUID().toString(),
            @Attribute(name = "SubseqDelivType")
            val subseqDelivType: String? = null
        )

        @Xml(name = "Invoice")
        class SummaryInvoice(
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
            @Element(name = "ns2:SupplyDateOrPeriod")
            val supplyDateOrPeriod: RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.CorrectiveInv? = null,
            @Element(name = "PayMethods")
            val payMethods: SummaryPayMethods?,
            @Element(name = "Seller")
            val seller: SummarySeller? = null,
            @Element(name = "Items")
            val items: SummaryItems,
            @Element(name = "SameTaxes")
            val sameTaxes: SummarySameTaxes? = null,
            @Element(name = "SumInvIICRefs")
            val sumInvIICRefs: SumInvIICRefs? = null
        )
        {


            @Xml(name = "ns2:SupplyDateOrPeriod")
            data class SupplyDateOrPeriod(
                @Attribute(name = "End")
                val end: String? = null,
                @Attribute(name = "Start")
                val start: String? = null,
            )

            @Xml(name = "PayMethods")
            data class SummaryPayMethods(
                @Element(name = "PayMethod")
                val payMethod: List<SummaryPayMethod>
            ) {

                @Xml(name = "PayMethod")
                data class SummaryPayMethod(
                    @Attribute(name = "Amt")
                    val amt: String? = null,
                    @Attribute(name = "Type")
                    val type: String? = null
                )

            }

            @Xml(name = "Seller")
            data class SummarySeller(
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
            data class SummaryItems(
                @Element(name = "I")
                val item: List<SummaryItem>
            ) {

                @Xml(name = "I")
                data class SummaryItem(
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
            data class SummarySameTaxes(
                @Element(name = "SameTax")
                val tax: List<SummarySameTax>
            ) {

                @Xml(name = "SameTax")
                data class SummarySameTax(
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

            @Xml(name = "SumInvIICRefs")
            data class SumInvIICRefs(
                @Element(name = "SumInvIICRef")
                val sumInvIICRef: List<SumInvIICRef>
            ) {

                @Xml(name = "SumInvIICRef")
                data class SumInvIICRef(
                    @Attribute(name = "IIC")
                    val iic: String? = null,
                    @Attribute(name = "IssueDateTime")
                    val issueDateTime: String? = null
                )

            }

        }
    }

    companion object {
        const val ELEMENT_TO_SIGN = "RegisterInvoiceRequest"
    }

}