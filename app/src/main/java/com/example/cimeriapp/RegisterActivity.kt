package com.example.cimeriapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
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
            auth.createUserWithEmailAndPassword(
                regEmail.text.toString(),
                regPassword.text.toString()
            ).addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Log.i("Registration", "Registration successful!")
                    val user = auth.currentUser
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("Registration", "Registration error!")
                    Log.w("Reg", task.exception.toString())
                    // DA L POSTOJI U BAZI VEĆ
                    try {
                        throw task.exception as Throwable
                    }
                    catch(e: FirebaseAuthUserCollisionException) {
                        regEmail.error = getString(R.string.collisionEmail)
                        regEmail.requestFocus()
                    }
                }
            }
        }
    }

    fun dataValid(): Boolean  {
        //IME I PREZIME
        if(regFullName.text.toString().isEmpty()){
            regFullName.error = getString(R.string.invalidFullName)
            regFullName.requestFocus()
            return false
        }
        //EMAIL
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(regEmail.text.toString()).matches()) {
            regEmail.error = getString(R.string.invalidEmail)
            regEmail.requestFocus()
            return false
        }

        //PASS
        if(regPassword.text.toString().length < 8 || regPassword.text.toString().count { c -> c.isUpperCase() } == 0) {
            regPassword.error = (getString(R.string.passwordReq))
            regPassword.requestFocus()
            return false
        }
        //PASS POTVRDA
        if(!regPassword.text.toString().equals(regConfirmPassword.text.toString())) {
            regPassword.error = getString(R.string.passwordMatch)
            regPassword.requestFocus()
            return false
        }
        return true
    }


}