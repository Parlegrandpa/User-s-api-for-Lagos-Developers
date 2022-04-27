package com.parlegrandpa.lagosdevelopers.src.view.screens

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.parlegrandpa.lagosdevelopers.R
import com.parlegrandpa.lagosdevelopers.src.data.factory.UserItemDetailModelFactory
import com.parlegrandpa.lagosdevelopers.src.util.getProgressDrawable
import com.parlegrandpa.lagosdevelopers.src.util.loadImage
import com.parlegrandpa.lagosdevelopers.src.viewmodel.UserItemDetailViewModel
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {

    lateinit var viewModel: UserItemDetailViewModel

    var userItemId: Int = 0
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        initView()
    }

    private fun initActionBar(titleText: String) {
        supportActionBar?.hide()
        toolbar_title.text = titleText
        backArrow.setOnClickListener { finish() }
    }

    private fun initView() {

        val modelFactory = UserItemDetailModelFactory(application)
        viewModel = ViewModelProvider(this, modelFactory).get(UserItemDetailViewModel::class.java)

        userItemId = intent.getIntExtra("id", 0)

        viewModel.refresh(userItemId)

        favoriteButton.setOnClickListener { viewModel.updateFavorite(userItemId, !isFavorite) }

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

                isFavorite = user.is_favorite
                changeButtonText()
            }
        }

        viewModel.isUpdated.observe(this) { isUpdated ->
            run {
                if (isUpdated) {
                    isFavorite = !isFavorite
//                    changeButtonText()
                    displayToast()
                }
            }
        }
    }

    private fun changeButtonText() {
        if (isFavorite)
            favoriteButton.text = resources.getText(R.string.remote_from_favorite)
        else
            favoriteButton.text = resources.getText(R.string.add_to_favorite)
    }

    private fun displayToast() {
        if (isFavorite)
            Toast.makeText(
                this,
                "Item has been added to favorite list successfully",
                Toast.LENGTH_LONG
            ).show()
        else
            Toast.makeText(
                this,
                "Item has been removed from favorite list successfully",
                Toast.LENGTH_LONG
            ).show()

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}