package kt.leonbec.top10downloader

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
    }

    companion object {
        private class DownloadData : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            override fun doInBackground(vararg params: String?): String {
                val rssFeed = downloadXml(params[0])
                if (rssFeed.isEmpty())
                    Log.e(TAG, "Error downloading")
                return rssFeed
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "Result is $result")
            }

            private fun downloadXml(url: String?): String {
                return URL(url).readText()
            }
        }
    }
}

