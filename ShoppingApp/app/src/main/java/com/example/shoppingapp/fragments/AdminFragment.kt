package com.example.shoppingapp.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.models.Category
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.services.DBService
import kotlinx.coroutines.runBlocking

class AdminFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedPos: Int = 0
    private var isNothingSelected: Boolean = true
    private lateinit var categories: List<Category>
    private var categoriesNames = mutableListOf<String>()
    private lateinit var mNotifyManager: NotificationManager
    private val CHANNEL_ID = "new-product-notify"
    private val NOTIFICATION_ID = 0
    private lateinit var mService: DBService
    private var mBound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as DBService.LocalBinder
            mService = binder.getService()
            mBound = true
            categories = mService.getAllCategories().toList()
            categories.forEach {
                categoriesNames.add(it.name)
            }
            adapter.notifyDataSetChanged()      // risky (if executed before initializing adapter)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(requireActivity(), DBService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        createNotificationChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin, container, false)
        val spinner: Spinner = view.findViewById(R.id.adminCategorySpinner)
        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoriesNames
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        view.findViewById<ImageButton>(R.id.adminSubmit).setOnClickListener { submit(view) }
        return view
    }

    private fun submit(view: View) {
        val pName = view.findViewById<EditText>(R.id.adminNameInput).text.toString()
        Product().apply {
            name = pName
            price = view.findViewById<EditText>(R.id.adminPriceInput).text.toString().toDouble()
            description = view.findViewById<EditText>(R.id.adminDescriptionInput).text.toString()
            category = if (isNothingSelected) null else categories[selectedPos]
        }.also {
            mService.postProduct(it)
            runBlocking { mService.upsertProduct(it) }
            mService.productsChanged = true
        }
        view.findViewById<EditText>(R.id.adminNameInput).setText("")
        view.findViewById<EditText>(R.id.adminPriceInput).setText("")
        view.findViewById<EditText>(R.id.adminDescriptionInput).setText("")
        Toast.makeText(context, "Successfully added product", Toast.LENGTH_SHORT).show()
        val notifyBuilder = getNotificationBuilder(pName)
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableLights(true)
            enableVibration(true)
        }
        // Register the channel with the system
        mNotifyManager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotifyManager.createNotificationChannel(channel)
    }

    private fun getNotificationBuilder(product_name: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle("New product in the store!")
            .setContentText("$product_name has been added!!")
            .setSmallIcon(R.drawable.basket)
            .setAutoCancel(true)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        isNothingSelected = false
        selectedPos = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        isNothingSelected = true
    }

    override fun onDestroy() {
        requireActivity().unbindService(connection)
        super.onDestroy()
    }
}