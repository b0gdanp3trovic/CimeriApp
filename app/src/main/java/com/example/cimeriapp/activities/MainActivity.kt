package com.example.cimeriapp.activities

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cimeriapp.R
import com.example.cimeriapp.additions.Friend
import com.example.cimeriapp.ui.contacts.ContactsFragment
import com.example.cimeriapp.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val contactsFragment = ContactsFragment()

    val users: java.util.ArrayList<Friend> = ArrayList()

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doSearch(query)
            }
        }
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            database = FirebaseDatabase.getInstance().getReference("users1/$uid/friends")
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var user: Friend? = null
                    for (child1 in dataSnapshot.children) {
                        val pp = child1.key.toString() + ".jpg"
                        for (child2 in child1.children) {
                            user = Friend(child2.value.toString(), pp)
                        }
                        if (user != null) {
                            users.add(user)
                        }
                    }
                    Friend.myList = users
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_chat,
                R.id.navigation_notifications,
                R.id.navigation_contacts
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun doSearch(query: String) {
        Log.i("query", query)
    }
}
