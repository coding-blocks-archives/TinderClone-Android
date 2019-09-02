package com.codingblocks.tinder.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.codingblocks.tinder.R
import com.codingblocks.tinder.adapters.Matches
import com.codingblocks.tinder.adapters.MatchesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_messages.*


class MessagesFragment : Fragment() {

    private val uid by lazy { FirebaseAuth.getInstance().uid }
    private val matchesDb by lazy { Firebase.firestore.collection("users/$uid/matches") }
    private val sharedPrefs by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    private var matchesAdapter: MatchesAdapter? = null
    val list = arrayListOf<Matches>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_messages, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        matchesDb.
        matchesAdapter = MatchesAdapter(list)
        matchesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = matchesAdapter
        }
    }


}
