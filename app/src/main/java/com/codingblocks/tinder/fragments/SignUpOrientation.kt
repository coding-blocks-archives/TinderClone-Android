package com.codingblocks.tinder.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingblocks.tinder.R
import com.codingblocks.tinder.adapters.MultiSelectionAdapter
import com.codingblocks.tinder.adapters.Orientations
import com.codingblocks.tinder.extensions.commitWithAnimation
import kotlinx.android.synthetic.main.fragment_sign_up_orientation.*


class SignUpOrientation : Fragment() {

    val list = arrayListOf(
        Orientations("Straight"), Orientations("Gay"),
        Orientations("Lesbian"), Orientations("Bisexual"), Orientations("Asexual"),
        Orientations("Demisexual"), Orientations("Pansexual")
    )

    private var adapter: MultiSelectionAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_orientation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        adapter = MultiSelectionAdapter(list)
        recyclerView.adapter = adapter

        adapter?.selectedList?.observe(viewLifecycleOwner, Observer{
            button.isEnabled = it.isNotEmpty()
        })

        button.setOnClickListener {
            fragmentManager?.commitWithAnimation(SignUpInterestedIn(),"Orientation")

        }
    }

}
