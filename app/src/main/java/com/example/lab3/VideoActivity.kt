package com.example.lab3


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_video.*


class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        btnPlay.setOnClickListener {
            var intent = Intent()
            intent.setType("video/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data!=null)
        {
            if (requestCode == 101)
            {
                var uri : Uri? = data.data

                var selectedImage:String = getPath(uri)
                if (selectedImage != null)
                {
                    videoView.setVideoPath(selectedImage)
                    var mediaController: MediaController = MediaController(this)
                    videoView.setMediaController(mediaController)
                    videoView.start()

                }
            }
        }
    }

    private fun getPath(uri: Uri?): String {
        var projection = arrayOf(MediaStore.Video.Media.DATA)
        var cursor =
            uri?.let { applicationContext.contentResolver.query(it, projection, null, null, null) }

        if(cursor != null)
        {
            var columnIndex: Int = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        else
        {
            return ""
        }
    }
}