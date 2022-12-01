package com.example.shoppingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.ProductDetailsActivity
import com.example.shoppingapp.adapters.ProductAdapter
import com.example.shoppingapp.interfaces.RecyclerRowInterface
import com.example.shoppingapp.models.Product

class ProductsListFragment : Fragment(), RecyclerRowInterface {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_products_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ProductAdapter(this)
        recyclerView.adapter = adapter
        return view
    }

    override fun onClick(product: Product) {
        val intent: Intent = Intent(activity, ProductDetailsActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }
}