package com.example.shoppingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.LoginActivity
import com.firebase.ui.auth.AuthUI

class UserFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_user, container, false)
        // Inflate the layout for this fragment
        view.findViewById<Button>(R.id.logoutButton).setOnClickListener{
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
        }
        return view
    }
}