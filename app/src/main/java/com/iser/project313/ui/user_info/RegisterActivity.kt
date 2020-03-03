package com.iser.project313.ui.user_info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.iser.project313.R
import com.iser.project313.ui.home.BookListingActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        firebaseAuth = FirebaseAuth.getInstance()
        btn_register.setOnClickListener {
            progress_circular.visibility = View.VISIBLE
            if (detailsOk()) {
                firebaseAuth.createUserWithEmailAndPassword(
                    edt_email.text.toString(),
                    edt_password.text.toString()
                )
                    .addOnCompleteListener { result ->
                        progress_circular.visibility = View.GONE
                        if (!result.isSuccessful)
                            Snackbar.make(it, "Authentication Failed", Snackbar.LENGTH_LONG).show()
                        else startActivity(Intent(this, BookListingActivity::class.java))
                    }
            }
        }
        button_already_account.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        btn_forgot_password.setOnClickListener {
            if (edt_email.text != null && edt_email.text!!.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(edt_email.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                RegisterActivity@ this,
                                "Password reset link has been sent",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                email_parent.error = "Please provide email address to reset"
            }
        }

    }

    private fun detailsOk(): Boolean {
        if (edt_email.text != null)
            if (edt_email.text!!.isEmpty()) {
                email_parent.error = "Empty Field"
                return false
            }
        if (edt_password.text != null) {
            if (edt_password.text!!.isEmpty() || edt_password.text!!.length < 6) {
                password_parent.error = "Password must be at least of 6 characters"
                return false
            }
        }
        return true
    }
}
