package com.example.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView


class VideoFragment : Fragment() {

    var adapter:MyMovieAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_video, null)

        val myListMovie: ArrayList<MovieInfo> = ArrayList()

        myListMovie?.add(MovieInfo("Это видос","А это автор","Nax"))

        adapter = myListMovie?.let { getActivity()?.let { it1 -> MyMovieAdapter(it1.baseContext, it) } }

        val lsListMovie: ListView? = v.findViewById(R.id.lsListMovie)

        lsListMovie?.setAdapter(adapter as ListAdapter?)

        return v
    }

}