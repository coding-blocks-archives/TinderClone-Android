package com.codingblocks.tinder.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.codingblocks.tinder.R
import com.codingblocks.tinder.adapters.CardStackAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_swiping.*
import kotlinx.android.synthetic.main.matched_view.view.*


class SwipingFragment : Fragment(), CardStackListener {

    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private val cardStackAdapter by lazy { CardStackAdapter() }
    private val usersDb by lazy { Firebase.firestore.collection("users") }
    private val uid by lazy { FirebaseAuth.getInstance().uid }
    private val sharedPrefs by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_swiping, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCardStackView()
        fetchUsers()
    }

    private fun fetchUsers() {
        val list = arrayListOf<User>()
        usersDb.whereEqualTo("gender", sharedPrefs.getString(INTERSTEDIN,"")).get().addOnSuccessListener { querySnapshot ->
            querySnapshot.documents.forEach {
                val user = it.toObject<User>()
                usersDb.document("$uid/liked_people/${user?.auth_id}").get()
                    .addOnSuccessListener {
                        if (!it.exists() || it["request_type"]?.equals("received")!!) {
                            usersDb.document("$uid/matches/${user?.auth_id}").get()
                                .addOnSuccessListener {
                                    if (!it.exists()) {
                                        if (user != null) {
                                            list.add(user)
                                            cardStackAdapter.setUsers(list)
                                            cardStackAdapter.notifyDataSetChanged()
                                        }
                                    }
                                }

                        }

                    }
            }
        }
    }

    private fun setupCardStackView() {
        manager.apply {
            setStackFrom(StackFrom.None)
            setVisibleCount(3)
//            setTranslationInterval(8.0f)
//            setScaleInterval(0.95f)
            setMaxDegree(20.0f)
            setSwipeThreshold(0.1f)
            setDirections(listOf(Direction.Left, Direction.Right, Direction.Top))
            setCanScrollHorizontal(true)
            setCanScrollVertical(true)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }
        card_stack_view.apply {
            layoutManager = manager
            adapter = cardStackAdapter
            itemAnimator.apply {
                if (this is DefaultItemAnimator) {
                    supportsChangeAnimations = false
                }
            }
        }

    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.d(
            "CardStackView",
            "onCardSwiped: p = ${manager.topPosition}, d = $direction, listElement = ${cardStackAdapter.getUsers()[manager.topPosition - 1]}"
        )
        if (direction?.name == "Right") {
            checkMatch(cardStackAdapter.getUsers()[manager.topPosition - 1])
        }


    }

    private fun checkMatch(user: User) {

        usersDb.document("$uid/liked_people/${user.auth_id}").get()
            .addOnSuccessListener {
                if (it.exists()) { //case of a match
                    matched_view.visibility = View.VISIBLE
                    Picasso.get().load(user.photos[0]).into(matched_view.matched_user)
                    Picasso.get().load(user.photos[0]).into(matched_view.current_user)
                    addToMatches(user)
                } else { //case of a new like
                    sendLike(user.auth_id)
                }
            }


    }

    private fun addToMatches(user: User) {
        val matchMap = hashMapOf(
            "date" to System.currentTimeMillis(),
            "name" to user.name
        )
        usersDb.document("$uid/liked_people/${user.auth_id}")
            .delete()
            .addOnSuccessListener {
                usersDb.document("${user.auth_id}/liked_people/$uid")
                    .delete()
                    .addOnSuccessListener {
                        usersDb.document("$uid/matches/${user.auth_id}")
                            .set(matchMap)
                            .addOnSuccessListener {
                                matchMap["name"] = "Pulkit"
                                usersDb.document("${user.auth_id}/matches/$uid")
                                    .set(matchMap)
                            }

                    }
            }

    }

    private fun sendLike(authId: String) {
        val notificatoinRef = usersDb.document(authId).collection("notifications").document()
        val newNotificationId = notificatoinRef.id
        val likeMap = hashMapOf(
            "request_type" to "send",
            "id" to newNotificationId
        )

        usersDb.document(uid ?: "")
            .collection("liked_people").document(authId).set(likeMap).addOnSuccessListener {
                //update request type
                likeMap["request_type"] = "received"

                usersDb.document(authId).collection("liked_people")
                    .document(uid ?: "").set(likeMap)
            }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }
}
