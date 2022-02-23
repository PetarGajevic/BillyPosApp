package com.wind.billypos.utils.extensions

import com.tickaroo.tikxml.TikXml
import okio.Buffer
import okio.BufferedSource

fun Any.toXMLString(): String {
    val xmlBuilder = TikXml.Builder().writeDefaultXmlDeclaration(false).build()
    val buffer = Buffer()
    xmlBuilder.write(buffer, this)
    return buffer.readUtf8()
}


