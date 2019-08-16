package com.codingblocks.tinder.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingblocks.tinder.R
import com.codingblocks.tinder.adapters.MultiSelectionAdapter
import com.codingblocks.tinder.adapters.Orientations
import com.codingblocks.tinder.extensions.commitWithAnimation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up_orientation.*


class SignUpOrientation : Fragment() {

    val list = arrayListOf(
        Orientations("Straight"), Orientations("Gay"),
        Orientations("Lesbian"), Orientations("Bisexual"), Orientations("Asexual"),
        Orientations("Demisexual"), Orientations("Pansexual")
    )
    var selectedList = arrayListOf<String>()

    private var adapter: MultiSelectionAdapter? = null

    val db by lazy {
        FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_up_orientation, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        adapter = MultiSelectionAdapter(list)
        recyclerView.adapter = adapter

        adapter?.selectedList?.observe(viewLifecycleOwner, Observer { list1 ->
            button.isEnabled = list1.isNotEmpty()
            selectedList = list1.map { it.name } as ArrayList<String>
        })
        db?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                orientation_checkbox.isChecked = document.getBoolean("show_orientation") ?: false
            }
        }

        button.setOnClickListener {
            button.isEnabled = false
            FirebaseAuth.getInstance().uid?.let { uid ->
                val user = hashMapOf(
                    "show_orientation" to orientation_checkbox.isChecked,
                    "orientations" to selectedList
                )

                db?.set(user, SetOptions.merge())
            }.also {
                it?.apply {
                    addOnSuccessListener {
                        button.isEnabled = true
                        fragmentManager?.commitWithAnimation(SignUpInterestedIn(), "Orientation")
                    }
                    addOnFailureListener {
                        button.isEnabled = true
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }

        }
    }

}
