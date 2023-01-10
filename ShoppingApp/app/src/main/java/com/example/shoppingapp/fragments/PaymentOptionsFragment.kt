package com.example.shoppingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentOptionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_payment_options, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.GONE  // not so good
        view.findViewById<Button>(R.id.cardButton).setOnClickListener { cardOnClick() }
        view.findViewById<Button>(R.id.stripeButton).setOnClickListener { stripeOnClick() }
        view.findViewById<Button>(R.id.payuButton).setOnClickListener { payuOnClick() }
        return view
    }

    private fun cardOnClick() {
        val action = PaymentOptionsFragmentDirections
            .actionPaymentOptionsFragmentToCardPaymentFragment()
        findNavController().navigate(action)
    }

    private fun stripeOnClick() {
        val action = PaymentOptionsFragmentDirections
            .actionPaymentOptionsFragmentToStripePaymentFragment()
        findNavController().navigate(action)
    }

    private fun payuOnClick() {
        // TODO: payu integration
    }
}