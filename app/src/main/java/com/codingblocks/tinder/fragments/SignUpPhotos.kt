package com.codingblocks.tinder.fragments


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.codingblocks.tinder.HomeActivity
import com.codingblocks.tinder.R
import com.codingblocks.tinder.adapters.PhotoClickListener
import com.codingblocks.tinder.adapters.Photos
import com.codingblocks.tinder.adapters.PhotosAdapter
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_sign_up_photos.*
import java.io.IOException
import java.util.*


class SignUpPhotos : Fragment() {

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var currentPosition = -1

    private var adapter: PhotosAdapter? = null
    private var storageReference: StorageReference? = null

    val bitmapList = arrayListOf<Bitmap>()
    val list = arrayListOf<Photos>()
    val db by lazy {
        FirebaseAuth.getInstance().uid?.let { Firebase.firestore.collection("users").document(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_up_photos, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageReference = FirebaseStorage.getInstance().reference
        for (i in 0..9) {
            list.add(Photos(null, null))
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = PhotosAdapter(list)
        recyclerView.adapter = adapter
        adapter?.selectedList?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            button.isEnabled = it.isNotEmpty()
        })
        adapter?.onClick = object : PhotoClickListener {
            override fun onClick(position: Int) {
                currentPosition = position
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST
                )
            }

        }
        button.setOnClickListener {
            button.isEnabled = false
            FirebaseAuth.getInstance().uid?.let { uid ->
                val user = hashMapOf(
                    "photos" to list.filter { !it.url.isNullOrBlank() }.map { it.url }
                )

                db?.set(user, SetOptions.merge())
            }.also {
                it?.apply {
                    addOnSuccessListener {
                        button.isEnabled = true
                        requireActivity().finish()
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    }
                    addOnFailureListener {
                        button.isEnabled = true
                    }
                }

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                uploadImage()
                bitmapList.add(currentPosition, bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            val imageId: String =
                if (currentPosition == 0) {
                    FirebaseAuth.getInstance().uid.toString()
                } else {
                    UUID.randomUUID().toString()
                }
            val ref = storageReference?.child("uploads/$imageId")
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        list.add(
                            currentPosition,
                            Photos(downloadUri.toString(), bitmapList[currentPosition])
                        )
                        adapter?.notifyItemChanged(currentPosition)
                    } else {
                        // Handle failures
                    }
                }?.addOnFailureListener {

                }
        } else {
        }
    }
}
