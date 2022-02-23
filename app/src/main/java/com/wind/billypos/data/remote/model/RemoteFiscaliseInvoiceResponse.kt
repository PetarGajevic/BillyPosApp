package com.wind.billypos.data.remote.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="Envelope")
data class RemoteFiscalizeReceiptResponse(
    @Attribute(name = "xmlns:env")
    val xmlns: String? = "http://schemas.xmlsoap.org/soap/envelope/",
    @Element(name = "env:Header")
    val header: SoapHeaderFiscalizationResponse? = SoapHeaderFiscalizationResponse(),
    @Element(name = "env:Body")
    val body: SoapBodyFiscalizationResponse?
){

    @Xml(name = "SOAP-ENV:Header")
    data class SoapHeaderFiscalizationResponse(
        @Attribute(name = "header")
        val header: String? = null
    )

    @Xml(name = "SOAP-ENV:Body")
    data class SoapBodyFiscalizationResponse(
        @Element(name = "RegisterInvoiceResponse")
        val registerInvoiceResponse : RegisterInvoiceResponse
    )

    @Xml(name = "RegisterInvoiceResponse")
    data class RegisterInvoiceResponse (
        @Attribute(name = "Id")
        val id: String = "Response",
        @Attribute(name = "Version")
        val version: Int = 3,
        @Attribute(name = "xmlns")
        val xmlns: String = "https://eFiskalizimi.tatime.gov.al/FiscalizationService/schema",
        @Attribute(name = "xmlns:ns0")
        val xmlns0: String = "http://www.w3.org/2000/09/xmldsig#",
        @Element(name = "Header")
        val header : InvoiceResponseHeader? = null,
        @PropertyElement(name = "FIC")
        val fic : String? = null,
        @Element(name = "Signature")
        val signature : FiscalizationResponseSignature? = null
    ){

        @Xml(name = "Header")
        data class InvoiceResponseHeader(
            @Attribute(name = "RequestUUID")
            val requestUUID: String? = null,
            @Attribute(name = "SendDateTime")
            val sendDateTime: String? = null,
            @Attribute(name = "UUID")
            val UUID: String? = null
        )

        @Xml(name = "Signature")
        data class FiscalizationResponseSignature(
            @Attribute(name = "xmlns")
            val xmlns: String = "http://www.w3.org/2000/09/xmldsig#",
            @Element(name = "SignedInfo")
            val signedInfo : FiscalizationResponseSignedInfo,
            @PropertyElement(name = "SignatureValue")
            val signatureValue : String? = null,
            @Element(name = "KeyInfo")
            val keyInfo : FiscalizationResponseKeyInfo,
        ){

            @Xml(name = "SignedInfo")
            data class FiscalizationResponseSignedInfo(
                @Element(name = "CanonicalizationMethod")
                val signedInfo : FiscalizationResponseCanonicalizationMethod,
                @Element(name = "SignatureMethod")
                val signatureMethod : FiscalizationResponseSignatureMethod,
                @Element(name = "Reference")
                val reference : FiscalizationResponseReference,
            ){


                @Xml(name = "CanonicalizationMethod")
                data class FiscalizationResponseCanonicalizationMethod(
                    @Attribute(name = "Algorithm")
                    val algorithm: String = "http://www.w3.org/2001/10/xml-exc-c14n#",
                )

                @Xml(name = "SignatureMethod")
                data class FiscalizationResponseSignatureMethod(
                    @Attribute(name = "Algorithm")
                    val algorithm: String = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256",
                )

                @Xml(name = "Reference")
                data class FiscalizationResponseReference(
                    @Attribute(name = "URI")
                    val URI: String = "#Request",
                    @Element(name = "Transforms")
                    val transform: FiscalizationResponseTransforms,
                    @Element(name = "DigestMethod")
                    val digestMethod: FiscalizationResponseDigestMethod,
                    @Element(name = "DigestValue")
                    val digestValue: FiscalizationResponseDigestValue,
                ){

                    @Xml(name = "Transforms")
                    data class FiscalizationResponseTransforms(
                        @Element(name = "Transform")
                        val transform: List<FiscalizationResponseTransform>
                    ){

                        @Xml(name = "Transform")
                        data class FiscalizationResponseTransform(
                            @Attribute(name = "Algorithm")
                            val algorithm: String? = "http://www.w3.org/2000/09/xmldsig#enveloped-signature",
                        )


                    }

                    @Xml(name = "DigestMethod")
                    data class FiscalizationResponseDigestMethod(
                        @Element(name = "Algorithm")
                        val algorithm: String? = "http://www.w3.org/2001/04/xmlenc#sha256"
                    )

                    @Xml(name = "DigestValue")
                    data class FiscalizationResponseDigestValue(
                        @PropertyElement(name = "DigestValue")
                        val digestValue: String? = null
                    )

                }

            }


            @Xml(name = "KeyInfo")
            data class FiscalizationResponseKeyInfo(
                @Element(name = "X509Data")
                val x509DATa : FiscalizationResponseX509DATA
            ){

                @Xml(name = "X509Data")
                data class FiscalizationResponseX509DATA(
                    @PropertyElement(name = "X509Certificate")
                    val x509Certificate: String? = null
                )

            }
        }



    }
}