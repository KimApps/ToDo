package com.apps.kim.todo.photopicker

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.apps.kim.todo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.util.ArrayList

/**
Created by KIM on 25.09.2019
 **/

class AlbumAdapter (internal var context: Context, private var layoutResourceId: Int, data: ArrayList<ImageModel>) : ArrayAdapter<ImageModel>(context, layoutResourceId, data) {
    private var data: ArrayList<ImageModel> = ArrayList()
    var onItem: OnAlbum? = null
    private var pHeightItem = 0
    private var pWHIconNext = 0

    internal class RecordHolder {
        var iconNext: ImageView? = null
        var imageItem: ImageView? = null
        var layoutRoot: RelativeLayout? = null
        var txtPath: TextView? = null
        var txtTitle: TextView? = null
    }

    init {
        this.data = data
        this.pHeightItem = getDisplayInfo(context as Activity).widthPixels / 6
        this.pWHIconNext = this.pHeightItem / 4
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: RecordHolder
        var row = convertView
        if (row == null) {
            row = (this.context as Activity).layoutInflater.inflate(this.layoutResourceId, parent, false)
            holder = RecordHolder()
            holder.txtTitle = row!!.findViewById(R.id.name_album) as TextView
            holder.txtPath = row.findViewById(R.id.path_album) as TextView
            holder.imageItem = row.findViewById(R.id.icon_album) as ImageView
            holder.iconNext = row.findViewById(R.id.iconNext) as ImageView
            holder.layoutRoot = row.findViewById(R.id.layoutRoot) as RelativeLayout
            holder.layoutRoot!!.layoutParams.height = this.pHeightItem
            holder.imageItem!!.layoutParams.width = this.pHeightItem
            holder.imageItem!!.layoutParams.height = this.pHeightItem
            holder.iconNext!!.layoutParams.width = this.pWHIconNext
            holder.iconNext!!.layoutParams.height = this.pWHIconNext
            row.tag = holder
        } else {
            holder = row.tag as RecordHolder
        }
        val item = this.data[position]
        holder.txtTitle!!.text = item.name
        holder.txtPath!!.text = item.pathFolder
        Glide.with(this.context)
            .load(File(item.pathFile))
            .apply(RequestOptions().placeholder(R.drawable.piclist_icon_default))
            .into(holder.imageItem!!)

        row.setOnClickListener {
            if (this@AlbumAdapter.onItem != null) {
                this@AlbumAdapter.onItem!!.OnItemAlbumClick(position)
            }
        }
        return row
    }

    companion object {

        fun getDisplayInfo(activity: Activity): DisplayMetrics {
            val dm = DisplayMetrics()
            activity.window.windowManager.defaultDisplay.getMetrics(dm)
            return dm
        }
    }
}