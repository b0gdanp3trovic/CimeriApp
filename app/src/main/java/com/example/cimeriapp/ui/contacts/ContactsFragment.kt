package com.example.cimeriapp.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cimeriapp.R
import com.example.cimeriapp.additions.MyAdapter
import com.example.cimeriapp.additions.StaticObjects
import com.example.cimeriapp.ui.chat.ChatViewModel
import org.w3c.dom.Text

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
            adapter = MyAdapter(StaticObjects.myData)
//            Toast.makeText(context, StaticObjects.myData.toString(), Toast.LENGTH_LONG).show()
        }

        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)

        return view
    }
}