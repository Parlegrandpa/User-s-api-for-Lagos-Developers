package com.parlegrandpa.lagosdevelopers.src.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.parlegrandpa.lagosdevelopers.R
import com.parlegrandpa.lagosdevelopers.src.model.UserItemDetailModelFactory
import com.parlegrandpa.lagosdevelopers.src.util.getProgressDrawable
import com.parlegrandpa.lagosdevelopers.src.util.loadImage
import com.parlegrandpa.lagosdevelopers.src.viewmodel.UserItemDetailViewModel
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {

    lateinit var viewModel: UserItemDetailViewModel

    var userItemId: Int = 0
    var is_favorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        initView()
    }

    private fun initActionBar(titleText: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = titleText

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initView() {

        val modelFactory = UserItemDetailModelFactory(application)
        viewModel = ViewModelProvider(this, modelFactory).get(UserItemDetailViewModel::class.java)

        userItemId = intent.getIntExtra("id", 0)

        viewModel.refresh(userItemId)

        favoriteButton.setOnClickListener { viewModel.updateFavorite(userItemId, !is_favorite) }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.user.observe(this) { user ->
            run {
                name.text = user.login
                type.text = user.type
                loadImage(
                    imageView,
                    user.avatar_url,
                    getProgressDrawable(imageView.context)
                )

                user.login?.let { initActionBar(it) }

                is_favorite = user.is_favorite!!
                changeButtonText()
            }
        }

        viewModel.isUpdated.observe(this) { isUpdated ->
            run {
                if (isUpdated) {
                    is_favorite = !is_favorite
//                    changeButtonText()
                    displayToast()
                }
            }
        }
    }

    fun changeButtonText() {
        if (is_favorite)
            favoriteButton.text = resources.getText(R.string.remote_from_favorite)
        else
            favoriteButton.text = resources.getText(R.string.add_to_favorite)
    }

    private fun displayToast() {
        if (is_favorite)
            Toast.makeText(this, "Item has been added to favorite list successfully", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this, "Item has been removed from favorite list successfully", Toast.LENGTH_LONG).show()

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}