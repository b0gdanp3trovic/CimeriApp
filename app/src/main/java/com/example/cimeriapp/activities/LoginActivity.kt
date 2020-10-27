package com.example.cimeriapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.cimeriapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val RC_SIGN_IN = 1
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        loginGoogle.setOnClickListener {
            googleSignIn()
        }

        loginButton.setOnClickListener {
            emailPassSignIn()
        }
        
        login_linear.setOnClickListener {
            onClick(this.login_linear)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }


    fun emailPassSignIn() {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString()).addOnCompleteListener(this) { task ->
            if(task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.w("Registration", "Registration error!")
                Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                Log.w("Reg", task.exception.toString())
            }
        }
    }

    private fun googleSignIn() {
        val intent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            Toast.makeText(this, "Successfully signed in as ${account?.displayName}.", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun onClick(view: View) {
        if (view.id != R.id.loginGoogle && view.id != R.id.loginFacebook && view.id != R.id.loginEmail) {
            val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
    
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if (loginEmail.isFocused) loginPassword.requestFocus()
                else {
                    if (loginPassword.isFocused) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else super.onKeyUp(keyCode, event)
                }
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }
}
