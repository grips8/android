package com.example.shoppingapp.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.BasketAdapter
import com.example.shoppingapp.models.CardDetails
import com.example.shoppingapp.models.CardOrder
import com.example.shoppingapp.models.Order
import com.example.shoppingapp.services.DBService
import com.example.shoppingapp.services.KtorApiService
import com.example.shoppingapp.utils.CardOrderConverter
import com.example.shoppingapp.utils.CardOrderException
import com.example.shoppingapp.utils.JsonBasket
import com.example.shoppingapp.utils.JsonOrder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarMenu
import io.realm.kotlin.types.ObjectId
import retrofit2.Retrofit

class CardPaymentFragment : Fragment() {
    private lateinit var mService: DBService
    private var mBound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as DBService.LocalBinder
            mService = binder.getService()
            mBound = true
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card_payment, container, false)
        view.findViewById<ImageButton>(R.id.orderSubmit).setOnClickListener { postOrderToApi(view) }
        return view
    }

    private fun postOrderToApi(view: View) {
        mService.getBasket()?.let { basket ->
            val cardOrderConverter = CardOrderConverter()

            val cardName = view.findViewById<EditText>(R.id.cardNameInput).text.toString()
            val cardNumber = view.findViewById<EditText>(R.id.cardNumberInput).text.toString()
            val cardExp = view.findViewById<EditText>(R.id.cardExpInput).text.toString()
            val cardCVV = view.findViewById<EditText>(R.id.cardCVVInput).text.toString()

            var cardDetails: CardDetails? = null
            try {
                cardDetails = cardOrderConverter.validateCardDetails(cardName, cardNumber, cardExp, cardCVV)
            }
            catch (e: CardOrderException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                return
            }

            var jsonBasket: JsonBasket? = null
            try {
                jsonBasket = cardOrderConverter.validateBasket(basket)
            }
            catch (e: CardOrderException) {
                e.message?.let { Log.e("Basket validation exception: ", it) }
                Toast.makeText(context, "Basket is corrupted!", Toast.LENGTH_SHORT).show()
                return
            }

            val jsonOrder = JsonOrder(ObjectId.toString(), jsonBasket)
            val cardOrder = CardOrder(jsonOrder, cardDetails)

            if (mBound) {
                mService.postOrder(cardOrder)
                Toast.makeText(context, "Payment was successful", Toast.LENGTH_SHORT).show()
                mService.ordersChanged = true
                mService.basketChanged = true
            }
            else {
                Toast.makeText(context, "Payment was NOT successful", Toast.LENGTH_SHORT).show()
            }
        }
        val action = CardPaymentFragmentDirections.actionCardPaymentFragmentToBasketFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        requireActivity().unbindService(connection)
        super.onDestroy()
    }
}