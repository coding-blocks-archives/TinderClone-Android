package com.codingblocks.tinder.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.codingblocks.tinder.R
import androidx.recyclerview.widget.DefaultItemAnimator
import com.codingblocks.tinder.adapters.CardStackAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_swiping.*


class SwipingFragment : Fragment(), CardStackListener {

    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private val cardStackAdapter by lazy { CardStackAdapter() }
    private val usersDb by lazy { Firebase.firestore.collection("users") }
    private val uid by lazy { FirebaseAuth.getInstance().uid }

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
        usersDb.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.toObjects<User>()
            cardStackAdapter.setUsers(list)
            cardStackAdapter.notifyDataSetChanged()
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
            sendlike(cardStackAdapter.getUsers()[manager.topPosition - 1])
        }


    }

    private fun sendlike(user: User) {
        val notificatoinRef = usersDb.document(user.auth_id).collection("notifications").document()
        val newNotificationId = notificatoinRef.id
        val likeMap = hashMapOf(
            "request_type" to "send",
            "id" to newNotificationId
        )

        usersDb.document(uid ?: "")
            .collection("liked_people").document(user.auth_id).set(likeMap).addOnSuccessListener {
                //update request type
                likeMap["request_type"] = "recieved"

                usersDb.document(user.auth_id).collection("liked_people")
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
