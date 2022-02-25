package com.example.nikitafe_oblig2

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

class XmlParser(){ //xml parser fra android studio sin dokumentasjon, forandret slik at den vil funke med distrikt 3 xmlen

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<Party> { //returnerer parseren slik at jeg kan bruke den i main activity
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<Party> { //metode som skal returnerer liste av objekter i xmlen
        val xmlParties = mutableListOf<Party>()

        parser.require(XmlPullParser.START_TAG, ns, "districtThree")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "party") { //hvis den finner party i xmlen
                xmlParties.add(readEntry(parser))
            } else {
                skip(parser)
            }
        }
        return xmlParties
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): Party {
        parser.require(XmlPullParser.START_TAG, ns, "party")
        var id: String? = null //id lagrer jeg som string fordi den oppfoerer seg mer som et "navn" for alpakaen istedenfor et tall.
        var votes: Int? = null //votes er Int fordi de er et tall som man skal kunne addere og gjoere andre utregninger med
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "id" -> id = readInfo(parser, parser.name)
                "votes" -> votes = readInfo(parser, parser.name).toIntOrNull()
                else -> skip(parser)
            }
        }
        return Party(id, votes)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readInfo(parser: XmlPullParser, tag : String): String { //forandret paa denne metoden slik at man kunne putte in 2 forskjellige items og fortsatt fa tilbake det man trenger
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val obj = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return obj
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}

data class Party(val id : String?, val votes : Int?) //data klasse som inneholder info om partiets id, og hvor mange stemmer de fikk