package com.example.cimeriapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth

        regButton.setOnClickListener {
            registerViaEmail()
        }
    }


    fun registerViaEmail() {
        if(dataValid()) {
            auth.createUserWithEmailAndPassword(regEmail.text.toString(), regPassword.text.toString()).addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    Log.i("Registration", "Registration successful!")
                    val user = auth.currentUser
                } else {
                    Log.w("Registration", "Registration error!")
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                    Log.w("Reg", task.exception.toString())
                    val intent: Intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun dataValid(): Boolean  {
        // TODO Provera validnosti unetih podataka
        return true
    }
}