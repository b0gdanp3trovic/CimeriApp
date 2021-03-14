package com.example.cimeriapp.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.cimeriapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        regButton.setOnClickListener {
            registerViaEmail()
        }

        reg_linear.setOnClickListener {
            onClick(this.reg_linear)
        }

        takePhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    PopUpWindow(this).show(supportFragmentManager, "do it")
                }
            } else {
                PopUpWindow(this).show(supportFragmentManager, "do it")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            selectedPhotoUri = data?.data
            takePhoto.setImageURI(data?.data)
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            takePhoto.setImageBitmap(imageBitmap)
        }
    }

    fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    fun chooseImageCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, CAMERA_REQUEST)
    }

    companion object {
        const val IMAGE_CHOOSE = 1000
        const val CAMERA_REQUEST = 1888
        private const val PERMISSION_CODE = 1001
    }

    private fun registerViaEmail() {
        if (dataValid()) {
            auth.createUserWithEmailAndPassword(
                regEmail.text.toString(),
                regPassword.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.i("Registration", "Registration successful!")
                    val intent = Intent(this, LoginActivity::class.java)
                    uploadImageToFirebaseStorage()
                    saveUserToDB()
                    startActivity(intent)
                } else {
                    Log.w("Registration", "Registration error!")
                    Log.w("Reg", task.exception.toString())
                    // DA LI POSTOJI U BAZI VEĆ
                    try {
                        throw task.exception as Throwable
                    } catch (e: FirebaseAuthUserCollisionException) {
                        regEmail.error = getString(R.string.collisionEmail)
                        regEmail.requestFocus()
                    }
                }
            }
        }
    }

    private fun uploadImageToFirebaseStorage() {
        val uid = FirebaseAuth.getInstance().uid
        val name = "$uid.jpg"
        val ref = Firebase.storage.reference.child("images/profile_pictures/$name")
        val bitmap = (takePhoto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        ref.putBytes(data)
    }

    private fun saveUserToDB() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().reference
        if (uid != null) {
            ref.child("users1/$uid/nick").push()
            ref.child("users1/$uid/nick").setValue(regFullName.text.toString())
        }
    }

    private fun dataValid(): Boolean {
        //IME I PREZIME
        if (regFullName.text.toString().isEmpty()) {
            regFullName.error = getString(R.string.invalidFullName)
            regFullName.requestFocus()
            return false
        }
        //EMAIL
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(regEmail.text.toString()).matches()) {
            regEmail.error = getString(R.string.invalidEmail)
            regEmail.requestFocus()
            return false
        }

        //PASS
        if (regPassword.text.toString().length < 8 || regPassword.text.toString()
                .count { c -> c.isUpperCase() } == 0
        ) {
            regPassword.error = (getString(R.string.passwordReq))
            regPassword.requestFocus()
            return false
        }
        //PASS POTVRDA
        if (!regPassword.text.toString().equals(regConfirmPassword.text.toString())) {
            regPassword.error = getString(R.string.passwordMatch)
            regPassword.requestFocus()
            return false
        }
        return true
    }

    private fun onClick(view: View) {
        if (view.id != R.id.regButton && view.id != R.id.regFullName && view.id != R.id.regEmail && view.id != R.id.regPassword && view.id != R.id.regConfirmPassword) {
            val inputMethodManager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if (regFullName.isFocused) regEmail.requestFocus()
                else {
                    if (regEmail.isFocused) regPassword.requestFocus()
                    else {
                        if (regPassword.isFocused) regConfirmPassword.requestFocus()
                        else {
                            if (regConfirmPassword.isFocused) {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else super.onKeyUp(keyCode, event)
                        }
                    }
                }
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    class PopUpWindow (private val registerActivity: RegisterActivity) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                // Use the Builder class for convenient dialog construction
                val builder = AlertDialog.Builder(it)
                builder.setPositiveButton(R.string.gallery
                ) { _, _ ->
                    registerActivity.chooseImageGallery()
                }
                    .setNegativeButton(R.string.camera
                    ) { _, _ ->
                        registerActivity.chooseImageCamera()
                    }

                    .setNeutralButton(R.string.close
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                // Create the AlertDialog object and return it
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}


