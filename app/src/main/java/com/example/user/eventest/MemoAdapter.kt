package com.example.user.eventest

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.user.eventest.model.Memo


/**
 * Created by User on 11.01.2018.
 * Adapter for list of memo
 */

class MemoAdapter(context: Context, private val eventsData: EventsData) :
        ArrayAdapter<Memo>(context, R.layout.list_row, eventsData.getAllData()) {

    override fun hasStableIds(): Boolean = true

    override fun getItemId(position: Int): Long {
        val memo: Memo = getItem(position)
        return memo.memoID
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tmpView: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            tmpView = LayoutInflater.from(context)
                    .inflate(R.layout.list_row, parent, false)
            viewHolder = ViewHolder(tmpView.findViewById(R.id.tvContent),
                    tmpView.findViewById(R.id.tvDate),
                    tmpView.findViewById(R.id.tvTime),
                    tmpView.findViewById(R.id.imageView),
                    position)
            tmpView.tag = viewHolder
        } else {
            tmpView = convertView
            viewHolder = tmpView.tag as ViewHolder
        }

        val memo: Memo = getItem(position)
        Log.i("MemoAdapter", "p $position id ${memo.memoID}")
        val attachments = eventsData.getAttachments(memo.memoID)
        if (attachments != null) {
            //            todo check mime type
            class AsyncLoadImage : AsyncTask<ViewHolder, Void, Bitmap>() {
                override fun doInBackground(vararg params: ViewHolder): Bitmap {

                    var resized = MediaStore.Images.Media.getBitmap(context.contentResolver,
                            Uri.parse(attachments.pathToAttach).normalizeScheme())
                    resized = ThumbnailUtils.extractThumbnail(resized, 68, 68)
                    return resized
                }

                override fun onPostExecute(result: Bitmap) {
                    super.onPostExecute(result)
                    Log.i("MemoAdapter", "pe vhp${viewHolder.position} p $position " +
                            "id ${memo.memoID} pta:${attachments.pathToAttach}")
                    if (viewHolder.position == position) {
                        viewHolder.imageView.setImageBitmap(result)
                    }
                }
            }

            val asyncLoadImage = AsyncLoadImage()
            asyncLoadImage.execute(viewHolder)
        } else {
            viewHolder.imageView.setImageResource(R.drawable.ic_launcher_background)
        }
        viewHolder.tvContent.text = memo.note
        viewHolder.tvDate.text = memo.getDateString()
        viewHolder.tvTime.text = memo.getTimeString()
        viewHolder.position = position
        return tmpView
    }

    fun refreshEvents() {
        clear()
        addAll(eventsData.getAllData())
        notifyDataSetChanged()
    }

    class ViewHolder(val tvContent: TextView, val tvDate: TextView, val tvTime: TextView,
                     val imageView: ImageView, var position: Int)
}