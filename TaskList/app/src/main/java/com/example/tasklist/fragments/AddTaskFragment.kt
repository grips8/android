package com.example.tasklist.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tasklist.R
import com.example.tasklist.models.DataTask
import com.example.tasklist.utils.DBHelper

class AddTaskFragment() : Fragment(R.layout.fragment_add_task) {
    private lateinit var dbHelper: DBHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.addConfirmButton).setOnClickListener {
            dbHelper.insertTask(DataTask(
                0,
                view.findViewById<EditText>(R.id.addName).text.toString(),
                view.findViewById<EditText>(R.id.addDesc).text.toString(),
                view.findViewById<EditText>(R.id.addDate).text.toString(),
                view.findViewById<EditText>(R.id.addStatus).text.toString()
            ))
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, RecyclerFragment(), "recycler fragment")
            fragmentTransaction.commit()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHelper = DBHelper(context)
    }
}