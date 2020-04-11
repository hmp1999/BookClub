package com.iser.project313.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.iser.project313.R

open class BaseActivity : AppCompatActivity() {

    lateinit var progressDialog : AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showProgressDialog(){
        progressDialog  = AlertDialog.Builder(this)
            .setView(R.layout.dialog_loader)
            .setCancelable(false)
            .create()
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.show()
    }

    fun hideProgressDialog(){
       if (progressDialog!=null &&  progressDialog.isShowing)
           progressDialog.hide()
    }


}
