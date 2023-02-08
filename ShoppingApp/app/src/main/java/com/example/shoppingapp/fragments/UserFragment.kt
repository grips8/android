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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.LoginActivity
import com.example.shoppingapp.adapters.OrdersAdapter
import com.example.shoppingapp.interfaces.OrderRecyclerRowInterface
import com.example.shoppingapp.models.Order
import com.example.shoppingapp.services.DBService
import com.example.shoppingapp.utils.ConvNotificationHelper
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking

class UserFragment : Fragment(), OrderRecyclerRowInterface {
    private val adapter: OrdersAdapter = OrdersAdapter(this)
    private lateinit var mService: DBService
    private var mBound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as DBService.LocalBinder
            mService = binder.getService()
            mBound = true
            adapter.initService(mService)
            ConvNotificationHelper.showConvNotification(requireContext())
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConvNotificationHelper.createChannel(requireContext())
        Intent(requireActivity(), DBService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_user, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.VISIBLE
        val recyclerView: RecyclerView = view.findViewById(R.id.userOrdersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        view.findViewById<Button>(R.id.logoutButton).setOnClickListener{ logout() }
        view.findViewById<Button>(R.id.notificationButton).setOnClickListener{ sayHi() }
        return view
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
    }

    private fun sayHi() {
        if (mBound)
            runBlocking {
                val reply = mService.postHiToServer()
                ConvNotificationHelper.showServerHiNotification(requireContext(), reply)
            }
    }

    override fun onResume() {
        super.onResume()
        if (mBound && mService.ordersChanged) {
            adapter.initService(mService)
            mService.ordersChanged = false
        }
    }

    override fun onDestroy() {
        requireActivity().unbindService(connection)
        super.onDestroy()
    }

    override fun onClick(order: Order) {
        val action = UserFragmentDirections.actionUserFragmentToOrderViewFragment(order.basket!!._id)
        findNavController().navigate(action)
    }
}