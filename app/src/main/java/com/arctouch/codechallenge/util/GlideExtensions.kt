package com.arctouch.codechallenge.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

infix fun View.loads(url: String?): RequestBuilder<Drawable> = Glide.with(this).load(url)

infix fun AppCompatActivity.loads(url: String?): RequestBuilder<Drawable> = Glide.with(this).load(url)

infix fun <T> RequestBuilder<T>.asThumbnailInto(imageView: ImageView) {
    apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .thumbnail(0.5f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageView)
}

infix fun <T> RequestBuilder<T>.on(imageView: ImageView) {
    apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageView)
}