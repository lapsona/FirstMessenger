package com.example.firstmessenger

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        shesvla.setOnClickListener {
            val email = emaili.text.toString()
            val pws = paroli.text.toString()

            Log.d("Login", "attempt login with email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pws)

            dabruneba.setOnClickListener {
                finish()
            }


        }
        password_reset.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("პაროლის აღდგენა")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
            val username = view.findViewById<EditText>(R.id.et_username)
            builder.setView(view)
            builder.setPositiveButton("აღდგენა", DialogInterface.OnClickListener {_, _ ->
                forgotPassword(username)
            })
            builder.setNegativeButton("დახურვა", DialogInterface.OnClickListener {_, _ ->  })
            builder.show()

        }






    }
    private fun forgotPassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            return
        }

        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"email sent", Toast.LENGTH_SHORT).show()
                }
            }
}
}