package com.example.user.eventest

import android.content.ContentResolver
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
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.user.eventest.eventbus.events.TAG
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

            if (getMimeType(attachments.pathToAttach).startsWith("image/")) {
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
                Log.i(TAG, "type = not image")
                viewHolder.imageView.setImageResource(R.drawable.ic_insert_drive_file_white_24px)
//                 todo check non image mime type
            }
            viewHolder.imageView.visibility = View.VISIBLE
        } else {
            viewHolder.imageView.visibility = View.GONE
        }
        viewHolder.tvContent.text = memo.note
        viewHolder.tvDate.text = memo.getDateString()
        viewHolder.tvTime.text = memo.getTimeString()
        viewHolder.position = position
        return tmpView
    }

    private fun getMimeType(url: String?): String {
        val uri = Uri.parse(url)
        val mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            context.contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase())
        }
        Log.i(TAG, "type = $mimeType")
        return mimeType
    }

    fun refreshEvents() {
        clear()
        addAll(eventsData.getAllData())
        notifyDataSetChanged()
    }

    class ViewHolder(val tvContent: TextView, val tvDate: TextView, val tvTime: TextView,
                     val imageView: ImageView, var position: Int)
}