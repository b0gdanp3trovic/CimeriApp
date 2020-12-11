package com.example.cimeriapp.additions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.cimeriapp.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class Friend(nickname1: String, profilePicture1: String) {
    var nickname: String = nickname1
    var profilePicture: String = profilePicture1

    companion object StaticFriend {
        @JvmStatic
        lateinit var myList: ArrayList<Friend>
    }

}

class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val friendCardText: TextView = v.findViewById(R.id.nickname)
    val friendCardImage: ImageView = v.findViewById(R.id.profilePicture)
}

class FriendAdapter(private val myDB: java.util.ArrayList<Friend>, private val rv: RecyclerView) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.friendCardText.text = myDB[position].nickname
        val name = myDB[position].profilePicture
        val storageRef = Firebase.storage.reference
        storageRef.child("images/profile_pictures/$name").downloadUrl.addOnSuccessListener {
            GlideApp.with(rv).load(it).into(holder.friendCardImage)
        }
    }

    override fun getItemCount() = myDB.size

}

@GlideModule
class MyAppGlideModule: AppGlideModule()