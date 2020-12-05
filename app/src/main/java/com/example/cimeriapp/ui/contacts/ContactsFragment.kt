package com.example.cimeriapp.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cimeriapp.R
import com.example.cimeriapp.additions.Friend
import com.example.cimeriapp.additions.FriendAdapter
import java.util.ArrayList

class ContactsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsViewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_contacts, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.contacts_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val users = Friend.myList
            adapter = FriendAdapter(users, this)
        }

        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)

        return view
    }
}