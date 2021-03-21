package com.badmitry.dynamicfeaturedescription.descriptionscreen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.badmitry.dynamicfeaturedescription.R
import com.badmitry.dynamicfeaturedescription.databinding.DescriptionLayoutBinding
import com.badmitry.translater.view.base.isOnline
import com.badmitry.translater.view.main.MainActivity
import com.badmitry.translator.view.main.AlertDialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class DescriptionActivity : AppCompatActivity() {

    private var binding: DescriptionLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.description_layout)
        setActionbarHomeButtonAsUp()
        binding?.descriptionScreenSwipeRefreshLayout?.setOnRefreshListener{ startLoadingOrShowError() }
        setData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setActionbarHomeButtonAsUp() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setData() {
        val bundle = intent.extras
        binding?.let{
            it.descriptionHeader.text = bundle?.getString(MainActivity.WORD_EXTRA)
            it.descriptionTextview.text = bundle?.getString(MainActivity.DESCRIPTION_EXTRA)
            val imageLink = bundle?.getString(MainActivity.URL_EXTRA)
            if (imageLink.isNullOrBlank()) {
                stopRefreshAnimationIfNeeded()
            } else {
//                usePicassoToLoadPhoto(it.descriptionImageview, imageLink)
                useGlideToLoadPhoto(it.descriptionImageview, imageLink)
            }
        }

    }

    private fun startLoadingOrShowError() {
        if (isOnline(applicationContext)) {
            setData()
        } else {
            AlertDialogFragment.newInstance(
                getString(R.string.dialog_title_device_is_offline),
                getString(R.string.dialog_message_device_is_offline)
            ).show(
                supportFragmentManager,
                DIALOG_FRAGMENT_TAG
            )
            stopRefreshAnimationIfNeeded()
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        binding?.let{
            if (it.descriptionScreenSwipeRefreshLayout.isRefreshing) {
                it.descriptionScreenSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun usePicassoToLoadPhoto(imageView: ImageView, imageLink: String) {
        Picasso.with(applicationContext).load("https:$imageLink")
            .placeholder(R.drawable.ic_not_image).fit().centerCrop()
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    stopRefreshAnimationIfNeeded()
                }

                override fun onError() {
                    stopRefreshAnimationIfNeeded()
                    imageView.setImageResource(R.drawable.ic_not_image)
                }
            })
    }

    private fun useGlideToLoadPhoto(imageView: ImageView, imageLink: String) {
        Glide.with(imageView)
            .load("https:$imageLink")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    imageView.setImageResource(R.drawable.ic_not_image)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_not_image)
                    .centerCrop()
            )
            .into(imageView)
    }

    companion object {

        private const val DIALOG_FRAGMENT_TAG = "8c7dff51-9769-4f6d-bbee-a3896085e76e"

//        private const val WORD_EXTRA = "f76a288a-5dcc-43f1-ba89-7fe1d53f63b0"
//        private const val DESCRIPTION_EXTRA = "0eeb92aa-520b-4fd1-bb4b-027fbf963d9a"
//        private const val URL_EXTRA = "6e4b154d-e01f-4953-a404-639fb3bf7281"
//
//        fun getIntent(
//            context: Context,
//            word: String,
//            description: String,
//            url: String?
//        ): Intent = Intent(context, DescriptionActivity::class.java).apply {
//            putExtra(WORD_EXTRA, word)
//            putExtra(DESCRIPTION_EXTRA, description)
//            putExtra(URL_EXTRA, url)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}