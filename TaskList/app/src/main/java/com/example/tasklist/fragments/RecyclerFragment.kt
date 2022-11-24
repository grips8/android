package com.example.tasklist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.adapters.TaskAdapter
import com.example.tasklist.models.DataTask

/**
 * A simple [Fragment] subclass.
 * Use the [RecyclerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecyclerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val data: Array<DataTask> = arrayOf(
            DataTask("name1", "desc1", "24.01.1999", "status1"),
            DataTask("name2", "desc2", "25.01.1999", "status2"),
            DataTask("name3", "desc3", "26.01.1999","status3"),
            DataTask("name4", "desc4", "24.01.1999", "status4"),
            DataTask("name5", "desc5", "25.01.1999", "status5"),
            DataTask("name6", "desc6", "26.01.1999","status6"),
            DataTask("name7", "desc7", "24.01.1999", "status7"),
            DataTask("name8", "desc8", "25.01.1999", "status8"),
            DataTask("name9", "desc9", "26.01.1999","status9"),
            DataTask("name10", "desc10", "24.01.1999", "status10"),
            DataTask("name11", "desc11", "25.01.1999", "status11"),
            DataTask("name12", "desc12", "26.01.1999","status12"),
            DataTask("name13", "desc13", "24.01.1999", "status13"),
            DataTask("name14", "desc14", "25.01.1999", "status14"),
            DataTask("name15", "desc15", "26.01.1999","status15"),
            DataTask("name16", "desc16", "24.01.1999", "status16"),
            DataTask("name17", "desc17", "25.01.1999", "status17"),
            DataTask("name18", "desc18", "26.01.1999","status18"),
            DataTask("name19", "desc19", "24.01.1999", "status19"),
            DataTask("name20", "desc20", "25.01.1999", "status20"),
            DataTask("name21", "desc21", "26.01.1999","status21")
        )

        val view: View = inflater.inflate(R.layout.fragment_recycler, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = TaskAdapter(data)
        return view
    }

}