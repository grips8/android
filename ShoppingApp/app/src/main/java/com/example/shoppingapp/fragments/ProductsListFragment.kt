package com.example.shoppingapp.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.ProductDetailsActivity
import com.example.shoppingapp.adapters.ProductAdapter
import com.example.shoppingapp.interfaces.ProductRecyclerRowInterface
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.services.DBService
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductsListFragment : Fragment(), ProductRecyclerRowInterface {
    private val CHANNEL_ID = "important-notif"
    private val NOTIFICATION_ID = 1
    private lateinit var mNotifyManager: NotificationManager
    private val adapter: ProductAdapter = ProductAdapter(this)
    private lateinit var mService: DBService
    private var mBound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as DBService.LocalBinder
            mService = binder.getService()
            mBound = true
            adapter.initService(mService)
            if (mService.isUserAllowed())
                enableAdmin()
            val notifyBuilder = getNotificationBuilder(35)
            mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        Intent(requireActivity(), DBService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_products_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return view
    }

    override fun onResume() {
        super.onResume()
        if (mBound && mService.productsChanged) {
            adapter.initService(mService)
            mService.productsChanged = false
        }
    }

    override fun onClick(product: Product) {
        val intent: Intent = Intent(activity, ProductDetailsActivity::class.java)
        // bad code :)
        intent.putExtra("name", product.name)
        intent.putExtra("price", product.price)
        intent.putExtra("description", product.description)
        intent.putExtra("categoryName", if (product.category !== null) product.category?.name else "null")
        startActivity(intent)
    }

    fun enableAdmin() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.menu?.let {
            it.findItem(R.id.adminFragment)?.isVisible = true
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.important_channel_name)
        val descriptionText = getString(R.string.important_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableLights(true)
            enableVibration(true)
        }
        // Register the channel with the system
        mNotifyManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyManager.createNotificationChannel(channel)
    }

    private fun getNotificationBuilder(discount: Int): NotificationCompat.Builder {
        return NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle("Promotion in the store!")
            .setContentText("Discounts up to $discount! Valid this week only!")
            .setSmallIcon(R.drawable.basket)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    override fun onDestroy() {
        requireActivity().unbindService(connection)
        super.onDestroy()
    }
}