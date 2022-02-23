package com.wind.billypos.data.remote.model.cashbalance

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import com.wind.billypos.data.remote.model.typeconverter.RemoteTypeConverter
import org.threeten.bp.ZonedDateTime

@Xml(name = "SOAP-ENV:Envelope")
data class RemoteCashDepositRequest(
    @Attribute(name = "xmlns:SOAP-ENV")
    val xmlns: String = "http://schemas.xmlsoap.org/soap/envelope/",
    @Element(name = "SOAP-ENV:Header")
    val header: SoapHeaderCashDeposit? = SoapHeaderCashDeposit(),
    @Element(name = "SOAP-ENV:Body")
    val body: SoapBodyCashDeposit
){

    @Xml(name = "SOAP-ENV:Header")
    data class SoapHeaderCashDeposit(
        @Attribute(name = "header")
        val header: String? = null
    )

    @Xml(name = "SOAP-ENV:Body")
    data class SoapBodyCashDeposit(
        @Element(name = "RegisterCashDepositRequest")
        val registerCashDepositRequest : RegisterCashDepositRequest? = null,
    )

    @Xml(name = "RegisterCashDepositRequest")
    data class RegisterCashDepositRequest (
        @Attribute(name = "Id")
        val id: String = "Request",
        @Attribute(name = "Version")
        val version: Int = 3,
        @Attribute(name = "xmlns")
        val xmlns: String = "https://eFiskalizimi.tatime.gov.al/FiscalizationService/schema",
        @Attribute(name = "xmlns:ns2")
        val xmlnsNS2: String = "http://www.w3.org/2000/09/xmldsig#",
        @Element(name = "Header")
        val header : HeaderCashDeposit? = HeaderCashDeposit(),
        @Element(name = "CashDeposit")
        val cashDeposit : CashDeposit? = null
    ){

        @Xml(name = "Header")
        data class HeaderCashDeposit(
            @Attribute(
                name = "SendDateTime",
                converter = RemoteTypeConverter.LocalDateTimeVATConverter::class
            )
            val sendDateTime: ZonedDateTime? = ZonedDateTime.now(),
            @Attribute(name = "UUID")
            val UUID: String? = java.util.UUID.randomUUID().toString()
        )

        @Xml(name = "CashDeposit")
        data class CashDeposit(
            @Attribute(name = "CashAmt")
            val cashAmt: String? = null,
            @Attribute(
                name = "ChangeDateTime",
                converter = RemoteTypeConverter.LocalDateTimeVATConverter::class
            )
            val changeDateTime: ZonedDateTime? = ZonedDateTime.now(),
            @Attribute(name = "IssuerNUIS")
            val issuerNUIS: String? = null,
            @Attribute(name = "Operation")
            val operation: String? = null,
            @Attribute(name = "TCRCode")
            val TCRCode: String? = null
        )

    }

//    @Xml(name = "SOAP-ENV:Envelope")
//    data class ResponseCashDeposite(
//        @Attribute(name = "xmlns:env")
//        val xmlns: String? = "http://schemas.xmlsoap.org/soap/envelope/",
//        @Element(name = "env:Header")
//        val header: SoapHeaderCashDepositResponse? = null,
//        @Element(name = "env:Body")
//        val body: SoapBodyCashDepositResponse
//    ){
//
//        @Xml(name = "env:Header")
//        data class SoapHeaderCashDepositResponse(
//            @Attribute(name = "header")
//            val header: String? = null
//        )
//
//        @Xml(name = "env:Body")
//        data class SoapBodyCashDepositResponse(
//            @Element(name = "RegisterCashDepositResponse")
//            val registerCashDepositResponse : RegisterCashDepositResponse? = null,
//        )
//
//        @Xml(name = "RegisterCashDepositResponse")
//        data class RegisterCashDepositResponse (
//            @Attribute(name = "Id")
//            val id: String = "Response",
//            @Attribute(name = "Version")
//            val version: Int = 1,
//            @Attribute(name = "xmlns")
//            val xmlns: String = "https://efi.tax.gov.me/fs/schema",
//            @Element(name = "Header")
//            val header : HeaderCashDepositResponse? = null,
//            @PropertyElement(name = "FCDC")
//            val FCDC : String? = null,
//            @Element(name = "Signature")
//            val signature : SignatureCashDepositResponse? = null
//        ){
//
//            @Xml(name = "Header")
//            data class HeaderCashDepositResponse(
//                @Attribute(name = "RequestUUID")
//                val requestUUID: String? = null,
//                @Attribute(name = "SendDateTime")
//                val sendDateTime: String? = null,
//                @Attribute(name = "UUID")
//                val UUID: String? = null
//            )
//
//            @Xml(name = "Signature")
//            data class SignatureCashDepositResponse(
//                @Attribute(name = "xmlns")
//                val xmlns: String = "http://www.w3.org/2000/09/xmldsig#",
//                @Element(name = "SignedInfo")
//                val signedInfo : SignedInfoCashDepositResponse,
//                @PropertyElement(name = "SignatureValue")
//                val signatureValue : String? = null,
//                @Element(name = "KeyInfo")
//                val keyInfo : KeyInfoCashDepositResponse,
//            ){
//
//                @Xml(name = "SignedInfo")
//                data class SignedInfoCashDepositResponse(
//                    @Element(name = "CanonicalizationMethod")
//                    val signedInfo : CanonicalizationMethodCashDepositResponse,
//                    @Element(name = "SignatureMethod")
//                    val signatureMethod : SignatureMethodCashDepositResponse,
//                    @Element(name = "Reference")
//                    val reference : ReferenceCashDepositResponse,
//
//                    ){
//
//
//                    @Xml(name = "CanonicalizationMethod")
//                    data class CanonicalizationMethodCashDepositResponse(
//                        @Attribute(name = "Algorithm")
//                        val algorithm: String = "http://www.w3.org/2001/10/xml-exc-c14n#",
//                    )
//
//                    @Xml(name = "SignatureMethod")
//                    data class SignatureMethodCashDepositResponse(
//                        @Attribute(name = "Algorithm")
//                        val algorithm: String = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256",
//                    )
//
//                    @Xml(name = "Reference")
//                    data class ReferenceCashDepositResponse(
//                        @Attribute(name = "URI")
//                        val URI: String = "#Request",
//                        @Element(name = "Transforms")
//                        val transform: TransformsCashDepositResponse,
//                        @Element(name = "DigestMethod")
//                        val digestMethod: DigestMethodCashDepositResponse,
//                        @Element(name = "DigestValue")
//                        val digestValue: DigestValueCashDepositResponse,
//                    ){
//
//                        @Xml(name = "Transforms")
//                        data class TransformsCashDepositResponse(
//                            @Element(name = "Transform")
//                            val transform: List<TransformCashDepositResponse>
//                        ){
//
//                            @Xml(name = "Transform")
//                            data class TransformCashDepositResponse(
//                                @Attribute(name = "Algorithm")
//                                val algorithm: String? = "http://www.w3.org/2000/09/xmldsig#enveloped-signature",
//                            )
//
//
//                        }
//
//                        @Xml(name = "DigestMethod")
//                        data class DigestMethodCashDepositResponse(
//                            @Element(name = "Algorithm")
//                            val algorithm: String? = "http://www.w3.org/2001/04/xmlenc#sha256"
//                        )
//
//                        @Xml(name = "DigestValue")
//                        data class DigestValueCashDepositResponse(
//                            @PropertyElement(name = "DigestValue")
//                            val digestValue: String? = null
//                        )
//
//                    }
//
//                }
//
//
//                @Xml(name = "KeyInfo")
//                data class KeyInfoCashDepositResponse(
//                    @Element(name = "X509Data")
//                    val x509DATa : X509DATACashDepositResponse
//                ){
//
//                    @Xml(name = "X509Data")
//                    data class X509DATACashDepositResponse(
//                        @PropertyElement(name = "X509Certificate")
//                        val x509Certificate: String? = null
//                    )
//                }
//            }
//        }
//    }

    companion object {
        const val ELEMENT_TO_SIGN = "RegisterCashDepositRequest"
        const val INITIAL = "INITIAL"
        const val WITHDRAW = "WITHDRAW"
    }
}