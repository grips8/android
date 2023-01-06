package com.example.shoppingapp.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.BasketAdapter
import com.example.shoppingapp.services.DBService

class BasketFragment : Fragment() {
    private val adapter: BasketAdapter = BasketAdapter()
    private lateinit var mService: DBService
    private var mBound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as DBService.LocalBinder
            mService = binder.getService()
            mBound = true
            adapter.initService(mService)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_basket, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.basketRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        view.findViewById<Button>(R.id.orderButton).setOnClickListener { goToCheckout() }
        return view
    }

    private fun goToCheckout() {
        if (adapter.itemCount == 0) {
            Toast.makeText(context, "No products in basket!", Toast.LENGTH_SHORT).show()
            return
        }
        val fragmentManager: FragmentManager = this.parentFragmentManager

        fragmentManager.commit {
            replace(R.id.nav_fragment_container, CardPaymentFragment())
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mBound && mService.basketChanged) {
            adapter.initService(mService)
            mService.basketChanged = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(requireActivity(), DBService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        requireActivity().unbindService(connection)
        super.onDestroy()
    }
}