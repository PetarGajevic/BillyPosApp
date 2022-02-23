package com.wind.billypos.services

import android.content.Context
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.model.Configuration
import io.reactivex.Observable
import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import timber.log.Timber
import java.io.StringReader
import java.io.StringWriter
import java.security.MessageDigest
import java.security.Security
import java.security.Signature
import java.security.cert.X509Certificate
import java.util.*
import javax.xml.bind.DatatypeConverter
import javax.xml.crypto.dsig.*
import javax.xml.crypto.dsig.dom.DOMSignContext
import javax.xml.crypto.dsig.keyinfo.KeyInfo
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
import javax.xml.crypto.dsig.keyinfo.X509Data
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec
import javax.xml.crypto.dsig.spec.TransformParameterSpec
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.collections.ArrayList


object FiscalisationCertificationUtil{



    fun signDocument(requestToSign: String, elementToSign: String, fiscalDigitalKey: Company.FiscalDigitalKey): Observable<String> =
        Observable.create {

            val XML_REQUEST_ID = "Request"
//            val XML_SCHEMA_NS = "https://eFiskalizimi.tatime.gov.al/FiscalizationService/schema"
            val XML_SCHEMA_NS = "https://eFiskalizimi.tatime.gov.al/FiscalizationService/schema"
            val XML_SIG_METHOD = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
//            val XML_SIG_METHOD = "http://www.w3.org/2000/09/xmldsig#"

            val requestToSign = requestToSign.trim()

            val xmlSigFactory: XMLSignatureFactory = XMLSignatureFactory.getInstance(
                "DOM",
                XMLDSigRI()
            )
            Timber.e("Ucitaj xml u doc bilder")
            // Ucitaj xml u doc bilder
            val docFactory = DocumentBuilderFactory.newInstance()
            docFactory.isNamespaceAware = true
            val docBuilder = docFactory.newDocumentBuilder()
            Timber.e("Before doc")
            val doc = docBuilder.parse(InputSource(StringReader(requestToSign)))
            Timber.e("After doc")
            // Pronadji root element odnosno onaj koji treba da se potpise
            val nodeToSignList: NodeList =
                doc.getElementsByTagNameNS(XML_SCHEMA_NS, elementToSign)
            Timber.e("Velicina nodToSign:  %s" , nodeToSignList.length)
            Timber.e("elementToSign:  %s" , elementToSign)
            Timber.e("XML_SCHEMA_NS:  %s" , XML_SCHEMA_NS)
            // Ako ne pronadjes taj element baci gresku
            if (nodeToSignList.length == 0) {
                throw Exception(String.format("XML element %s not found", elementToSign))
            }
            Timber.e("Postavi nod za potpisivanje za ovaj koji si pronasao")
            // Postavi nod za potpisivanje za ovaj koji si pronasao
            val nodeToSign: Node = nodeToSignList.item(0)

            // Napravi listu transform elemenata
            val transformList: MutableList<Transform> = ArrayList()
            transformList.add(
                xmlSigFactory.newTransform(
                    Transform.ENVELOPED,
                    null as TransformParameterSpec?
                )
            )
            transformList.add(
                xmlSigFactory.newTransform(
                    CanonicalizationMethod.EXCLUSIVE,
                    null as C14NMethodParameterSpec?
                )
            )

            Timber.e("Dodaj digest elemenat na xml")
            // Dodaj digest elemenat na xml
            val ref: Reference = xmlSigFactory.newReference(
                "#$XML_REQUEST_ID",
                xmlSigFactory.newDigestMethod(DigestMethod.SHA256, null),
                transformList,
                null,
                null
            )

            // Kreiraj SignatureMethod element4
            val signatureMethod: SignatureMethod =
                xmlSigFactory.newSignatureMethod(XML_SIG_METHOD, null as SignatureMethodParameterSpec?)

            // Kreiraj SignedInfo element
            val signedInfo: SignedInfo = xmlSigFactory.newSignedInfo(
                xmlSigFactory.newCanonicalizationMethod(
                    CanonicalizationMethod.EXCLUSIVE,
                    null as C14NMethodParameterSpec?
                ),
                signatureMethod,
                Collections.singletonList(ref)
            )
            Timber.e("Dodaj setifikat")
            // Dodaj setifikat
            val certificateList: MutableList<X509Certificate?> = ArrayList()
            certificateList.add(fiscalDigitalKey?.certificate)

            // Kreiraj KeyInfo element
            val keyInfoFactory: KeyInfoFactory = xmlSigFactory.keyInfoFactory
            val x509Data: X509Data = keyInfoFactory.newX509Data(certificateList)
            val keyInfo: KeyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data))

            // Napravi kontekst za potpisivanje
            val dsc = DOMSignContext(fiscalDigitalKey.privateKey, nodeToSign)
            dsc.setIdAttributeNS(nodeToSign as Element, null, "Id")

            // Potpisi dokument
            val signature: XMLSignature = xmlSigFactory.newXMLSignature(signedInfo, keyInfo)
            signature.sign(dsc)

            Timber.e("Izbaci potpisani element u string")
            // Izbaci potpisani element u string
            val transformFactory: TransformerFactory = TransformerFactory.newInstance()
            val transformer: Transformer = transformFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            val sw = StringWriter()
            val streamRes = StreamResult(sw)
            transformer.transform(DOMSource(doc), streamRes)
            it.onNext(sw.toString())
        }

     fun generateIKOF(
        iicData: String,
        fiscalDigitalKey: Company.FiscalDigitalKey?
    ): Observable<Pair<String, String>> {
        return Observable.create {

            try{
                // Kreiraj ikof po RSASSA-PKCS-v1_5
                val signature: Signature = Signature.getInstance("SHA256withRSA")
                signature.initSign(fiscalDigitalKey?.privateKey)
                signature.update(iicData.toByteArray())

                val iicSignature: ByteArray = signature.sign()
                val iicSignatureString: String =
                    DatatypeConverter.printHexBinary(iicSignature).toUpperCase(Locale.getDefault())


                // Heshovanje potpisa sa MD5 algoritmom za kreiranje IKOF-a
                val md: MessageDigest = MessageDigest.getInstance("MD5")
                val iic: ByteArray = md.digest(iicSignature)

                val iicString: String = DatatypeConverter.printHexBinary(iic).toUpperCase(Locale.getDefault())


                it.onNext(Pair(first = iicString, second = iicSignatureString))

            }catch (e: Exception){
                e.printStackTrace()
                it.onError(e)
            }
        }
    }

}