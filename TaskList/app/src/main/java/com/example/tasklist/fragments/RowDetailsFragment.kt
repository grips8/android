package com.example.tasklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentRowDetailsBinding
import com.example.tasklist.models.DataTask

class RowDetailsFragment(private val dataTask: DataTask) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRowDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_row_details, container, false)
        val view: View = binding.root
        binding.model = dataTask
        return view
    }
}