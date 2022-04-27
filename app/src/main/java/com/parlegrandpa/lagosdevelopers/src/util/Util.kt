package com.parlegrandpa.lagosdevelopers.src.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.parlegrandpa.lagosdevelopers.R

fun loadImage(imageView: ImageView, url: String?, progressDrawable: CircularProgressDrawable?) {
    val options: RequestOptions = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.dog)
    Glide.with(imageView.context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(imageView)
}

fun getProgressDrawable(context: Context?): CircularProgressDrawable? {
    val cpd = CircularProgressDrawable(context!!)
    cpd.strokeWidth = 10f
    cpd.centerRadius = 50f
    cpd.start()
    return cpd
}