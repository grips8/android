package com.example.shoppingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.BasketAdapter

class BasketFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_basket, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.basketRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = BasketAdapter()
        recyclerView.adapter = adapter
        return view
    }
}