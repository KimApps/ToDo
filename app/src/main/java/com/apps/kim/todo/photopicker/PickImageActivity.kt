package com.apps.kim.todo.photopicker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.app.slice.SliceItem
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apps.kim.todo.R
import com.apps.kim.todo.tools.classes.KEY_DATA_RESULT
import com.apps.kim.todo.tools.classes.KEY_LIMIT_MAX_IMAGE
import com.apps.kim.todo.tools.classes.KEY_LIMIT_MIN_IMAGE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class PickImageActivity : AppCompatActivity(), View.OnClickListener, OnAlbum, OnListAlbum {
    private val TAG = "PickImageActivity"
    lateinit var albumAdapter: AlbumAdapter
    var dataAlbum: ArrayList<ImageModel> = ArrayList()
    var dataListPhoto: ArrayList<ImageModel> = ArrayList()
    lateinit var gridViewAlbum: GridView
    lateinit var gridViewListAlbum: GridView
    lateinit var horizontalScrollView: HorizontalScrollView
    lateinit var layoutListItemSelect: LinearLayout
    var limitImageMax = 30
    var limitImageMin = 2
    lateinit var listAlbumAdapter: ListAlbumAdapter
    var listItemSelect: ArrayList<ImageModel> = ArrayList()
    var pWHBtnDelete: Int = 0
    var pWHItemSelected: Int = 0
    var pathList: ArrayList<String> = ArrayList()
    lateinit var txtTotalImage: TextView
    private var mHandler: Handler? = null
    private var pd: ProgressDialog? = null
    private val READ_STORAGE_CODE = 1001
    private val WRITE_STORAGE_CODE = 1002

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_pick_image)

        val bundle = intent.extras
        if (bundle != null) {
            this.limitImageMax = bundle.getInt(KEY_LIMIT_MAX_IMAGE, 7)
            this.limitImageMin = bundle.getInt(KEY_LIMIT_MIN_IMAGE, 1)
            if (this.limitImageMin > this.limitImageMax) {
                finish()
            }
            if (this.limitImageMin < 1) {
                finish()
            }
            Log.e("PickImageActivity", "limitImageMin = " + this.limitImageMin)
            Log.e("PickImageActivity", "limitImageMax = " + this.limitImageMax)
        }
        this.pWHItemSelected = (getDisplayInfo(this).heightPixels.toFloat() / 100.0f * 25.0f).toInt() / 100 * 80
        this.pWHBtnDelete = this.pWHItemSelected / 100 * 25
        //  this.txtTitle = (TextView) findViewById(R.id.txtTitle);

        this.gridViewListAlbum = findViewById<GridView>(R.id.gridViewListAlbum)
        this.txtTotalImage = findViewById<TextView>(R.id.txtTotalImage)

        (findViewById<TextView>(R.id.btnDone)).setOnClickListener(this)
        this.layoutListItemSelect = findViewById<LinearLayout>(R.id.layoutListItemSelect)
        this.horizontalScrollView = findViewById<HorizontalScrollView>(R.id.horizontalScrollView)
        this.horizontalScrollView.layoutParams.height = this.pWHItemSelected
        this.gridViewAlbum = findViewById<GridView>(R.id.gridViewAlbum)

        pd = ProgressDialog(this)
        pd?.isIndeterminate = true
        pd?.setMessage("Loading...")

        mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (pd?.isShowing == true) {
                    pd?.dismiss()
                }
            }
        }

        try {
            this.dataAlbum.sortWith(Comparator { lhs, rhs -> lhs.name.compareTo(rhs.name, true) })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        this.albumAdapter = AlbumAdapter(this, R.layout.piclist_row_album, this.dataAlbum)
        this.albumAdapter.onItem = this

        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            GetItemAlbum().execute(*arrayOfNulls(0))
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_CODE)
        }
        updateTxtTotalImage()

    }

    private inner class GetItemAlbum : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {
            val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf("_data", "bucket_display_name"), null, null, null)
            if (cursor != null) {
                val columnIndexData = cursor.getColumnIndexOrThrow("_data")
                while (cursor.moveToNext()) {
                    val pathFile = cursor.getString(columnIndexData)
                    val file = File(pathFile)
                    if (file.exists()) {
                        val check = checkFile(file)
                        if (!Check(file.parent, pathList) && check) {
                            pathList.add(file.parent)
                            dataAlbum.add(ImageModel(file.parentFile.name, pathFile, file.parent))
                        }
                    }
                }
                cursor.close()
            }
            return ""
        }

        override fun onPostExecute(result: String) {
            gridViewAlbum.adapter = albumAdapter
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}
    }

    private inner class GetItemListAlbum internal constructor(internal var pathAlbum: String) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void): String {
            val file = File(this.pathAlbum)
            if (file.isDirectory) {
                for (fileTmp in file.listFiles()) {
                    if (fileTmp.exists()) {
                        val check = checkFile(fileTmp)
                        if (!fileTmp.isDirectory && check) {
                            dataListPhoto.add(ImageModel(fileTmp.name, fileTmp.absolutePath, fileTmp.absolutePath))
                            publishProgress(*arrayOfNulls(0))
                        }
                    }
                }
            }
            return ""
        }

        override fun onPostExecute(result: String) {
            try {
                Collections.sort<ImageModel>(dataListPhoto, Comparator<ImageModel> { item, t1 ->
                    val fileI = File(item.pathFolder)
                    val fileJ = File(t1.pathFolder)
                    if (fileI.lastModified() > fileJ.lastModified()) {
                        return@Comparator -1
                    }
                    if (fileI.lastModified() < fileJ.lastModified()) {
                        1
                    } else 0
                })
            } catch (e: Exception) {
            }
            listAlbumAdapter.notifyDataSetChanged()
        }

        override fun onPreExecute() {}
        override fun onProgressUpdate(vararg values: Void) {}
    }

    private fun isPermissionGranted(permission: String): Boolean {
        //Getting the permission status
        val result = ContextCompat.checkSelfPermission(this, permission)

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED

        //If permission is not granted returning false
    }


    //Requesting permission
    private fun requestPermission(permission: String, code: Int) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, arrayOf(permission), code)
    }

    //This method will be called when the user will tap on allow or deny
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        //Checking the request code of our request
        if (requestCode == READ_STORAGE_CODE) {

            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetItemAlbum().execute(*arrayOfNulls(0))
            } else {
                finish()
            }
        } else if (requestCode == WRITE_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

            }
        }
    }

    private fun Check(a: String, list: ArrayList<String>): Boolean {
        return !list.isEmpty() && list.contains(a)
    }

    fun refreshGridViewAlbum() {
        this.albumAdapter = AlbumAdapter(this, R.layout.piclist_row_album, this.dataAlbum)
        this.albumAdapter.onItem = this
        this.gridViewAlbum.adapter = this.albumAdapter
        this.gridViewAlbum.visibility = View.GONE
        this.gridViewAlbum.visibility = View.VISIBLE
    }

    fun refreshGridViewListAlbum() {
        this.listAlbumAdapter = ListAlbumAdapter(this, R.layout.piclist_row_list_album, this.dataListPhoto)
        this.listAlbumAdapter.onListAlbum = this
        this.gridViewListAlbum.adapter = this.listAlbumAdapter
        this.gridViewListAlbum.visibility = View.GONE
        this.gridViewListAlbum.visibility = View.VISIBLE
    }

    fun getFolderSize(directory: File?): Long {
        var length: Long = 0
        if (directory == null) {
            return 0
        }
        if (!directory.exists()) {
            return 0
        }
        val files = directory.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isFile) {
                    var isCheck = false
                    for (k in SliceItem.FORMAT_IMAGE) {
                        if (file.name.endsWith(k)) {
                            isCheck = true
                            break
                        }
                    }
                    if (isCheck) {
                        length++
                    }
                }
            }
        }
        return length
    }

    private fun addItemSelect(item: ImageModel) {
        item.id = (this.listItemSelect.size)
        this.listItemSelect.add(item)
        updateTxtTotalImage()
        val viewItemSelected = View.inflate(this, R.layout.piclist_item_selected, null)
        val imageItem = viewItemSelected.findViewById(R.id.imageItem) as ImageView
        val btnDelete = viewItemSelected.findViewById(R.id.btnDelete) as ImageView

        imageItem.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this as Activity)
            .asBitmap()
            .apply(RequestOptions().placeholder(R.drawable.piclist_icon_default))
            .load(item.pathFile)
            .into(imageItem)

        btnDelete.setOnClickListener {
            layoutListItemSelect.removeView(viewItemSelected)
            listItemSelect.remove(item)
            updateTxtTotalImage()
        }

        layoutListItemSelect.addView(viewItemSelected)
        viewItemSelected.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
        sendScroll()

    }

    private fun updateTxtTotalImage() {
        this.txtTotalImage.text = String.format(resources.getString(R.string.text_images), *arrayOf<Any>(Integer.valueOf(this.listItemSelect.size)))
    }

    private fun sendScroll() {
        val handler = Handler()
        Thread(Runnable { handler.post { horizontalScrollView.fullScroll(66) } }).start()
    }

    private fun showListAlbum(pathAlbum: String) {
        this.listAlbumAdapter = ListAlbumAdapter(this, R.layout.piclist_row_list_album, this.dataListPhoto)
        this.listAlbumAdapter.onListAlbum = this
        this.gridViewListAlbum.adapter = this.listAlbumAdapter
        this.gridViewListAlbum.visibility = View.VISIBLE
        GetItemListAlbum(pathAlbum).execute(*arrayOfNulls(0))
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btnDone) {
            val listString = getListString(this.listItemSelect)
            if (listString.size >= this.limitImageMin) {
                done(listString)
            } else {
                Toast.makeText(this, "Please select at lease " + this.limitImageMin + " images", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun done(listString: ArrayList<String>) {
        val mIntent = Intent()
        setResult(Activity.RESULT_OK, mIntent)
        mIntent.putStringArrayListExtra(KEY_DATA_RESULT, listString)
        finish()
    }

    private fun getListString(listItemSelect: ArrayList<ImageModel>): ArrayList<String> {
        val listString = ArrayList<String>()
        for (i in listItemSelect.indices) {
            listString.add(listItemSelect[i].pathFile)
        }
        return listString
    }

    private fun checkFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (!file.isFile) {
            return true
        }
        val name = file.name
        if (name.startsWith(".") || file.length() == 0L) {
            return false
        }
        var isCheck = false
        for (k in SliceItem.FORMAT_IMAGE) {
            if (name.endsWith(k)) {
                isCheck = true
                break
            }
        }
        return isCheck
    }

    override fun onBackPressed() {
        if (this.gridViewListAlbum.visibility == View.VISIBLE) {
            this.dataListPhoto.clear()
            this.listAlbumAdapter.notifyDataSetChanged()
            this.gridViewListAlbum.visibility = View.GONE
            //   this.txtTitle.setText(getResources().getString(R.string.text_title_activity_album));
            return
        }
        super.onBackPressed()
    }

    private fun getDisplayInfo(activity: Activity): DisplayMetrics {
        val dm = DisplayMetrics()
        activity.window.windowManager.defaultDisplay.getMetrics(dm)
        return dm
    }

    override fun OnItemAlbumClick(position: Int) {
        showListAlbum(this.dataAlbum[position].pathFolder)
    }

    override fun OnItemListAlbumClick(item: ImageModel) {
        if (this.listItemSelect.size < this.limitImageMax) {
            addItemSelect(item)
        } else {
            Toast.makeText(this, "Limit " + this.limitImageMax + " images", Toast.LENGTH_SHORT).show()
        }
    }

}
