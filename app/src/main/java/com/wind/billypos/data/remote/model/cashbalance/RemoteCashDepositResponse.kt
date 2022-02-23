package com.wind.billypos.data.remote.model.cashbalance

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "SOAP-ENV:Envelope")
data class RemoteCashDepositResponse(
    @Attribute(name = "xmlns:env")
    val xmlns: String = "http://schemas.xmlsoap.org/soap/envelope/",
    @Element(name = "SOAP-ENV:Header")
    val header: SoapHeaderCashDepositResponse? = SoapHeaderCashDepositResponse(),
    @Element(name = "env:Body")
    val body: SoapBodyCashDepositResponse
) {

    @Xml(name = "env:Header")
    data class SoapHeaderCashDepositResponse(
        @Attribute(name = "header")
        val header: String? = null
    )

    @Xml(name = "env:Body")
    data class SoapBodyCashDepositResponse(
        @Element(name = "RegisterCashDepositResponse")
        val registerCashDepositResponse : RegisterCashDepositResponse? = null,
    )

    @Xml(name = "RegisterCashDepositResponse")
    data class RegisterCashDepositResponse(
        @Attribute(name = "Id")
        val id: String = "Response",
        @Attribute(name = "Version")
        val version: Int = 3,
        @Attribute(name = "xmlns")
        val xmlns2: String = "https://eFiskalizimi.tatime.gov.al/FiscalizationService/schema",
        @Attribute(name = "xmlns:ns0")
        val xmlns3: String = "http://www.w3.org/2000/09/xmldsig#",
        @Element(name = "Header")
        val header: HeaderCashDepositResponse? = null,
        @PropertyElement(name = "FCDC")
        val FCDC: String? = null,
        @Element(name = "Signature")
        val signature: SignatureCashDepositResponse? = null
    ) {

        @Xml(name = "Header")
        data class HeaderCashDepositResponse(
            @Attribute(name = "RequestUUID")
            val requestUUID: String? = null,
            @Attribute(name = "SendDateTime")
            val sendDateTime: String? = null,
            @Attribute(name = "UUID")
            val UUID: String? = null
        )

        @Xml(name = "Signature")
        data class SignatureCashDepositResponse(
            @Attribute(name = "xmlns")
            val xmlns: String = "http://www.w3.org/2000/09/xmldsig#",
            @Element(name = "SignedInfo")
            val signedInfo: SignedInfoCashDepositResponse,
            @PropertyElement(name = "SignatureValue")
            val signatureValue: String? = null,
            @Element(name = "KeyInfo")
            val keyInfo: KeyInfoCashDepositResponse,
        ) {

            @Xml(name = "SignedInfo")
            data class SignedInfoCashDepositResponse(
                @Element(name = "CanonicalizationMethod")
                val signedInfo: CanonicalizationMethodCashDepositResponse,
                @Element(name = "SignatureMethod")
                val signatureMethod: SignatureMethodCashDepositResponse,
                @Element(name = "Reference")
                val reference: ReferenceCashDepositResponse,

                ) {


                @Xml(name = "CanonicalizationMethod")
                data class CanonicalizationMethodCashDepositResponse(
                    @Attribute(name = "Algorithm")
                    val algorithm: String = "http://www.w3.org/2001/10/xml-exc-c14n#",
                )

                @Xml(name = "SignatureMethod")
                data class SignatureMethodCashDepositResponse(
                    @Attribute(name = "Algorithm")
                    val algorithm: String = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256",
                )

                @Xml(name = "Reference")
                data class ReferenceCashDepositResponse(
                    @Attribute(name = "URI")
                    val URI: String = "#Request",
                    @Element(name = "Transforms")
                    val transform: TransformsCashDepositResponse,
                    @Element(name = "DigestMethod")
                    val digestMethod: DigestMethodCashDepositResponse,
                    @Element(name = "DigestValue")
                    val digestValue: DigestValueCashDepositResponse,
                ) {

                    @Xml(name = "Transforms")
                    data class TransformsCashDepositResponse(
                        @Element(name = "Transform")
                        val transform: List<TransformCashDepositResponse>
                    ) {

                        @Xml(name = "Transform")
                        data class TransformCashDepositResponse(
                            @Attribute(name = "Algorithm")
                            val algorithm: String? = "http://www.w3.org/2000/09/xmldsig#enveloped-signature",
                        )


                    }

                    @Xml(name = "DigestMethod")
                    data class DigestMethodCashDepositResponse(
                        @Element(name = "Algorithm")
                        val algorithm: String? = "http://www.w3.org/2001/04/xmlenc#sha256"
                    )

                    @Xml(name = "DigestValue")
                    data class DigestValueCashDepositResponse(
                        @PropertyElement(name = "DigestValue")
                        val digestValue: String? = null
                    )

                }

            }


            @Xml(name = "KeyInfo")
            data class KeyInfoCashDepositResponse(
                @Element(name = "X509Data")
                val x509DATa: X509DATACashDepositResponse
            ) {

                @Xml(name = "X509Data")
                data class X509DATACashDepositResponse(
                    @PropertyElement(name = "X509Certificate")
                    val x509Certificate: String? = null
                )
            }
        }
    }

}