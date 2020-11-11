package com.example.cimeriapp.additions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cimeriapp.R

class StaticObjects {
    companion object Converter {
        lateinit var sb:StringBuilder

        @JvmStatic
        var myData: MutableList<String> = ArrayList()

        @JvmStatic
        fun usersAsStrings(users1: MutableList<String>) {
            val users = ArrayList<String>()
            for (i in 0 until users1.size) {
                if (i % 3 == 0) {
                    sb = StringBuilder("")
                    sb.append(users1[i])
                    sb.append(" ")
                }
                if (i % 3 == 1) {
                    sb.append(users1[i])
                    sb.append(" ")
                }
                if (i % 3 == 2) {
                    sb.append(users1[i])
                    users.add(sb.toString())
                }
            }
            myData = users
        }
    }
}


class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val myOwnTextView: TextView = v.findViewById<TextView>(R.id.randomText)
}

class MyAdapter(private val myDB: MutableList<String>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.frame_textview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.myOwnTextView.text = myDB[position]
        holder.myOwnTextView.text
    }

    override fun getItemCount() = myDB.size

}

