package com.iser.project313.ui.user_info

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.iser.project313.R
import com.iser.project313.ui.home.BookListingActivity

class CheckSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_session)
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, BookListingActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

    }
}
