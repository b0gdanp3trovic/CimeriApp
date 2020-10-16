package com.example.cimeriapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        regButton.setOnClickListener {
            register()
        }
    }


    fun register() {
        if(dataValid()) {
            val user: ParseUser = ParseUser()
        }
    }

    fun dataValid(): Boolean  {
        // TODO Provera validnosti unetih podataka

        return true
    }
}