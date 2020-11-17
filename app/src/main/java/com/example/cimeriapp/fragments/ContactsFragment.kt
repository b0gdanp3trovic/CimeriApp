package com.example.cimeriapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cimeriapp.R
import com.example.cimeriapp.additions.MyAdapter
import com.example.cimeriapp.additions.StaticObjects

class ContactsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_contacts, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.contacts_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MyAdapter(StaticObjects.myData)
//            Toast.makeText(context, StaticObjects.myData.toString(), Toast.LENGTH_LONG).show()
        }
        return view
    }
}