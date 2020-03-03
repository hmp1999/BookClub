package com.iser.project313.ui.user_info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.iser.project313.MainActivity
import com.iser.project313.R
import com.iser.project313.ui.home.BookListingActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btn_login.setOnClickListener {
            progress_circular.visibility = View.VISIBLE
            if (isDetailsOK()) {
                firebaseAuth.signInWithEmailAndPassword(
                    edt_email.text.toString(),
                    edt_password.text.toString()
                )
                    .addOnCompleteListener { result ->
                        if (!result.isSuccessful) {
                            Toast.makeText(
                                LoginActivity@ this,
                                "Authentication Failed",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            startActivity(Intent(this, BookListingActivity::class.java))
                            finish()
                        }
                        progress_circular.visibility = View.GONE
                    }
            }
        }
        btn_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }


    }

    private fun isDetailsOK(): Boolean {
        if (edt_email.text != null) {
            if (edt_email.text!!.isEmpty()) {
                email_parent.error = "Email is empty"
                return false
            }
        }
        if (edt_password.text != null) {
            if (edt_password.text!!.isEmpty() || edt_password.text!!.length < 6) {
                password_parent.error = "Password must be of at least 6 characters"
                return false
            }
        }
        return true
    }
}
