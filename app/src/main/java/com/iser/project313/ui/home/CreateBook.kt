package com.iser.project313.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.iser.project313.R
import com.iser.project313.ui.user_info.LoginActivity
import kotlinx.android.synthetic.main.activity_create_book.*


class CreateBook : AppCompatActivity() {

    private val PICK_IMAGE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)
        setSupportActionBar(toolbar2)
        supportActionBar?.title = "Sell Book"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_create.setOnClickListener {
            progress_circular?.visibility = View.VISIBLE
            if (validateData())
                createBook()
            else
                progress_circular?.visibility = View.GONE
        }
        image_container?.setOnClickListener {
            chooseCover()
        }
    }

    private fun chooseCover() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    private fun validateData(): Boolean {
        return when {
            edt_title?.text == null || TextUtils.isEmpty(edt_title?.text.toString()) -> {
                title_container?.error = "Please enter book title"
                false
            }
            edt_author?.text == null || TextUtils.isEmpty(edt_author?.text.toString()) -> {
                author_container?.error = "Please enter book author"
                false
            }
            edt_price?.text == null || TextUtils.isEmpty(edt_price?.text.toString()) -> {
                price_container?.error = "Please enter book price"
                false
            }
            edt_short_description?.text == null || TextUtils.isEmpty(edt_short_description.text.toString()) -> {
                short_desc_container?.error = "Please enter short description"
                false
            }
            edt_long_description?.text == null || TextUtils.isEmpty(edt_long_description?.text.toString()) -> {
                long_desc_container?.error = "Please enter long description of the book"
                false
            }
            else -> true
        }
    }

    private fun createBook() {
        val title: String = edt_title.text.toString()
        val author: String = edt_author?.text.toString()
        val price: String = edt_price?.text.toString()
        val shortDescription: String = edt_short_description?.text.toString()
        val longDescription: String? = edt_long_description?.text?.toString()

        if (FirebaseAuth.getInstance().currentUser != null) {
            val bookInfo = BookInfo(
                title,
                price,
                0,
                author,
                shortDescription,
                longDescription,
                FirebaseAuth.getInstance().currentUser?.email
            )
            val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
            val rootRef = firebaseDatabase.reference
            rootRef.child("availableBooks").push().setValue(bookInfo) { databaseError, _ ->
                if (databaseError == null) {
                    Snackbar.make(edt_title, "Book published successfully", Snackbar.LENGTH_LONG)
                        .show()
                    finish()
                } else {
                    Snackbar.make(
                        edt_title,
                        "Something went wrong \n try again",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                progress_circular?.visibility = View.GONE
            }
        } else {
            Toast.makeText(this, "You are not authorised to perform this action", Toast.LENGTH_LONG)
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
