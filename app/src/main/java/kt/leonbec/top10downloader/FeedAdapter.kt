package kt.leonbec.top10downloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/*Created by leonbec on 2018/1/17.*/
class FeedAdapter(context: Context, private val resource: Int, private val apps: List<FeedEntry>)
    : ArrayAdapter<FeedEntry>(context, resource) {
    private val FEED_ADAPTER = "FEED_ADAPTER"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
//        Log.d(FEED_ADAPTER, "getCount() called")
        return apps.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        Log.d(FEED_ADAPTER, "getView() called")
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
//            Log.d(FEED_ADAPTER, "no convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
//            Log.d(FEED_ADAPTER, "convertView exists")
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

//        val tvName = view.findViewById<TextView>(R.id.tvName)
//        val tvArtist = view.findViewById<TextView>(R.id.tvArtist)
//        val tvSummary = view.findViewById<TextView>(R.id.tvSummary)

        val currentApp = apps[position]

        viewHolder.tvName.text = currentApp.name
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text=currentApp.summary

        return view
    }
}

class ViewHolder(view: View) {
    val tvName = view.findViewById<TextView>(R.id.tvName)
    val tvArtist = view.findViewById<TextView>(R.id.tvArtist)
    val tvSummary = view.findViewById<TextView>(R.id.tvSummary)
}