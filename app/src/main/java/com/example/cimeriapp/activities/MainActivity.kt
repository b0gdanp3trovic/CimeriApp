package com.example.cimeriapp.activities

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cimeriapp.R
import com.example.cimeriapp.additions.StaticObjects
import com.example.cimeriapp.fragments.ContactsFragment
import com.example.cimeriapp.ui.home.HomeFragment
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val contactsFragment = ContactsFragment()

    val users: MutableList<String> = ArrayList()

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doSearch(query)
            }
        }
        database = FirebaseDatabase.getInstance().getReference("users")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(child1 in dataSnapshot.children) {
                    for(child2 in child1.children) {
                        users.add(child2.value.toString())
                    }

                }
                StaticObjects.usersAsStrings(users)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun doSearch(query: String) {
        Log.i("query", query)
    }
}
