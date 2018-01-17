package kt.leonbec.top10downloader

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

/*
* Created by leonbec on 2018/1/16.
*/
class ParseApp {
    private val TAG = "ParseApp"
    val apps = mutableListOf<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        var status = true
        var inEntry = false
        var value = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
//                        Log.d(TAG, "parse: starting tag for $tagName")
                        if (tagName == "entry")
                            inEntry = true
                    }

                    XmlPullParser.TEXT -> value = xpp.text

                    XmlPullParser.END_TAG -> {
//                        Log.d(TAG, "parse: ending tag for $tagName")
                        if (inEntry)
                            when (tagName) {
                                "entry" -> {
                                    apps.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "name" -> currentRecord.name = value
                                "artist" -> currentRecord.artist = value
                                "releaseDate" -> currentRecord.releaseData = value
                                "summary" -> currentRecord.summary = value
                                "image" -> currentRecord.imageUrl = value
                            }
                    }
                }
                eventType = xpp.next()
            }
//            for (app in apps) {
//                Log.d(TAG, "******************")
//                Log.d(TAG, app.toString())
//            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }
}