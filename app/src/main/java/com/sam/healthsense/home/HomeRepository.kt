package com.sam.healthsense.home

import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.storage.FirebaseStorage
import jakarta.inject.Inject
import java.util.UUID


//interface HomeRepository{
//    fun logDetailScreenViewEvent()
//    fun uploadImage(uri: Uri)
//}
//class HomeRepositoryImpl @Inject constructor(
//    private val analytics: FirebaseAnalytics,
//    private val storage: FirebaseStorage
//): HomeRepository {
//    override fun logDetailScreenViewEvent() {
//        val metadata = Bundle().apply {
//            putString(FirebaseAnalytics.Param.SCREEN_NAME, "Detail")
//        }
//        analytics.logEvent(
//            FirebaseAnalytics.Event.SCREEN_VIEW,
//            metadata
//        )
//
//    }
//
//    @OptIn(UnstableApi::class)
//    override fun uploadImage(uri: Uri) {
//        val filename = uri.lastPathSegment ?: UUID.randomUUID().toString()
//        val imageDirectoryReference = storage.reference.child("images")
//        val fileReference = imageDirectoryReference.child(filename)
//
//        fileReference.putFile(uri)
//        fileReference.putFile(uri)
//            .addOnSuccessListener {
//                Log.d("TAG", "File $filename uploaded successfully")
//
//            }
//            .addOnFailureListener {
//                Log.d("TAG", "Failed to upload File $filename")
//
//            }
//    }
//}