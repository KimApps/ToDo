package com.apps.kim.todo.photopicker

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import com.apps.kim.todo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.ArrayList

/**
Created by KIM on 25.09.2019
 **/

class ListAlbumAdapter(internal var context: Context, internal var layoutResourceId: Int, data: ArrayList<ImageModel>) : ArrayAdapter<ImageModel>(context, layoutResourceId, data) {
    private var data: ArrayList<ImageModel> = ArrayList()
    var onListAlbum: OnListAlbum? = null
    private var pHeightItem = 0

    internal class RecordHolder {
        var click: ImageView? = null
        var imageItem: ImageView? = null
        var layoutRoot: RelativeLayout? = null
    }

    init {
        this.data = data
        this.pHeightItem = getDisplayInfo(context as Activity).widthPixels / 3
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: RecordHolder
        var row = convertView
        if (row == null) {
            row = (this.context as Activity).layoutInflater.inflate(this.layoutResourceId, parent, false)
            holder = RecordHolder()
            holder.imageItem = row!!.findViewById(R.id.imageItem) as ImageView
            holder.click = row.findViewById(R.id.click) as ImageView
            holder.layoutRoot = row.findViewById(R.id.layoutRoot) as RelativeLayout
            holder.layoutRoot!!.layoutParams.height = this.pHeightItem
            holder.imageItem!!.layoutParams.width = this.pHeightItem
            holder.imageItem!!.layoutParams.height = this.pHeightItem
            holder.click!!.layoutParams.width = this.pHeightItem
            holder.click!!.layoutParams.height = this.pHeightItem
            row.tag = holder
        } else {
            holder = row.tag as RecordHolder
        }
        val item = this.data[position]
        //  Glide.with(this.context).load(item.getPathFile()).asBitmap().override((int) Callback.DEFAULT_DRAG_ANIMATION_DURATION, (int) Callback.DEFAULT_DRAG_ANIMATION_DURATION).animate(R.anim.anim_fade_in).thumbnail((float) AppConst.ZOOM_MIN).error(R.drawable.piclist_icon_default).fallback(R.drawable.piclist_icon_default).placeholder(R.drawable.piclist_icon_default).into(holder.imageItem);

        Glide.with(context)
            .asBitmap()
            .load(item.pathFile)
            .apply(RequestOptions().placeholder(R.drawable.piclist_icon_default))
            .into(holder.imageItem!!)

        // Picasso.with(this.context).load(new File(item.getPathFile())).placeholder(R.drawable.piclist_icon_default).into(holder.imageItem);

        row.setOnClickListener {
            if (this@ListAlbumAdapter.onListAlbum != null) {
                this@ListAlbumAdapter.onListAlbum!!.OnItemListAlbumClick(item)
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