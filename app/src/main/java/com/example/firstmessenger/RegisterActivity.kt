package com.example.firstmessenger


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log.d as d1

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register.setOnClickListener {
            performRegister()
        }

        agdgena.setOnClickListener {
            d1("RegisterActivity", "try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerphoto.setOnClickListener {

            Log.d("RegisterActivity", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // ra surati chavardeba
            Log.d("RegisterActivity", "Photo was selected")

            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            registerphoto.setBackgroundDrawable(bitmapDrawable)
        }


    }
    private fun performRegister() {
        val email = emaili.text.toString()
        val pws = paroli.text.toString()

        if (email.isEmpty() || pws.isEmpty()) {
            Toast.makeText(this, "გთხოვთ წესიერად  შეიყვანოთ იმეილი და პაროლი", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("RegisterActivity", "Emaili aris: " + email)
        Log.d("RegisterActivity", "Password $pws")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pws)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "წარმატებით შეიქმნა")

            }
            .addOnFailureListener{
                Log.d("Main", "ვერ შეიქმნა User: ${it.message}")
                Toast.makeText(this, "გთხოვთ წესიერად შეიყვანოთ იმეილი და პაროლი", Toast.LENGTH_SHORT).show()

                saveUserToFirebaseDatabase(it.toString())
            }
            .addOnFailureListener{

            }

    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,username.text.toString())
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "shevinaxet useri database")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }



}

class User(val uid: String, val username: String)