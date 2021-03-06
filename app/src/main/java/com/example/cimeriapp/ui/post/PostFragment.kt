package com.example.cimeriapp.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cimeriapp.R

class PostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel

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

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.room_num_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roomSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also {
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currencySpinner.adapter = adapter
        }

        postViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }
}