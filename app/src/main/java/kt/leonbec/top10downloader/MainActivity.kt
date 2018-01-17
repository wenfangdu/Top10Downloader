package kt.leonbec.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val MAIN_ACTIVITY="MAIN_ACTIVITY"
    private var downloadData: DownloadData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(MAIN_ACTIVITY,"onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val feedUrl = when (item.itemId) {
            R.id.mnuFree -> "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
            R.id.mnuPaid -> "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml"
            R.id.mnuSongs -> "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml"
            else -> return super.onOptionsItemSelected(item)
        }
        downloadUrl(feedUrl)
        return true
    }

    override fun onDestroy() {
        Log.d(MAIN_ACTIVITY,"onDestroy called")
        super.onDestroy()
        downloadData?.cancel(true)
    }

    private fun downloadUrl(feedUrl: String) {
        downloadData = DownloadData(this, lvApps)
        downloadData?.execute(feedUrl)
    }

    private class DownloadData(context: Context, listView: ListView)
        : AsyncTask<String, Void, String>() {
        private val DOWNLOAD_DATA = "DOWNLOAD_DATA"

        private val context: Context by lazy { context }
        private val listView: ListView by lazy { listView }

        override fun doInBackground(vararg params: String?): String {
//            Log.d(DOWNLOAD_DATA, "starts with ${params[0]}")
            val rssFeed = downloadXml(params[0])
            if (rssFeed.isEmpty())
                Log.e(DOWNLOAD_DATA, "error downloading")
            return rssFeed
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parseApps = ParseApp()
            parseApps.parse(result)

//            val arrayAdapter = ArrayAdapter<FeedEntry>(context, R.layout.list_item, parseApps.apps)
//            val mlFeedEntry= mutableListOf<FeedEntry>(FeedEntry())
//            val arrayAdapter = ArrayAdapter<FeedEntry>(context, R.layout.list_item, mlFeedEntry)
//            listView.adapter = arrayAdapter
            val feedAdapter = FeedAdapter(context, R.layout.list_record, parseApps.apps)
            listView.adapter = feedAdapter
        }

        private fun downloadXml(url: String?): String {
            return URL(url).readText()
        }
    }
}

class FeedEntry {
    var name = ""
    var artist = ""
    var releaseData = ""
    var summary = ""
    var imageUrl = ""

    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseData
            imageUrl = $imageUrl
            """.trimIndent()
    }
}

