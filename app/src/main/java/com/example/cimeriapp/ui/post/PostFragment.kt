package com.example.cimeriapp.ui.post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cimeriapp.R
import com.example.cimeriapp.additions.IntroSliderAdapter
import kotlinx.android.synthetic.main.activity_register.*
import java.nio.file.Path
import java.util.logging.Logger


class PostFragment : Fragment() {


    private lateinit var postViewModel: PostViewModel

    companion object {
         const val IMAGE_CHOOSE = 1000;
         const val PERMISSION_CODE = 1001;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postViewModel =
            ViewModelProviders.of(this).get(PostViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_post, container, false)
        val roomSpinner: Spinner = root.findViewById(R.id.num_of_rooms_spinner)
        val currencySpinner: Spinner = root.findViewById(R.id.currency_spinner)
        val  uploadPhotoButtonPost : Button = root.findViewById(R.id.uploadPhotoButtonPost)


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.room_num_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roomSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currencySpinner.adapter = adapter
        }


        uploadPhotoButtonPost.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    chooseImageGallery();
                }
            } else {
                chooseImageGallery();
            }
        }

        postViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val photosPathList: MutableList<String> = ArrayList<String>()
        if(requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK && data != null) {
            if(data.clipData != null) {
                val count = data.clipData!!.itemCount
                for(i in 0 until count){
                    val imageUri: String? = data.clipData!!.getItemAt(i).uri.path
                    Log.i("Ovaj uri", imageUri.toString())
                    if(imageUri != null) {
                        photosPathList.add(imageUri)
                    }
                }
            } else if (data.data != null) {
                val imagePath: String? = data.data!!.path
                if (imagePath != null) {
                    photosPathList.add(imagePath)
                }
            }
        }

        Log.i("wholeList", photosPathList.toString())
    }

    private fun displayImagePreview(pathList: List<String>) {
        if (pathList.isNotEmpty()){
            for (i in pathList) {
                val imageBitmap: Bitmap = BitmapFactory.decodeFile(i)
                val imageToDisplay: ImageView = requireActivity().findViewById(R.id.imageSlideIcon)
                //imageToDisplay.setImageResource(imageBitmap)
            }
        }
    }


    private fun chooseImageGallery() {


        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CHOOSE)
    }
}