package com.parlegrandpa.lagosdevelopers.src.view.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.parlegrandpa.lagosdevelopers.R
import com.parlegrandpa.lagosdevelopers.src.data.factory.UserItemViewModelFactory
import com.parlegrandpa.lagosdevelopers.src.view.adapter.UserListAdapter
import com.parlegrandpa.lagosdevelopers.src.viewmodel.ListUserViewModel
import kotlinx.android.synthetic.main.activity_favorite_list.*

class FavoriteListActivity : AppCompatActivity() {

    lateinit var viewModel: ListUserViewModel
    private val usersAdapter = UserListAdapter(this, arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_list)

        supportActionBar?.hide()

        initView()
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchFavoriteList()
    }

    fun initView() {
        val modelfactory = UserItemViewModelFactory(application)
        viewModel = ViewModelProvider(this, modelfactory).get(ListUserViewModel::class.java)

        itemList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }

        backArrow.setOnClickListener { finish() }
        removeAllButton.setOnClickListener { showDeleteWarning() }

        observeViewModel()
    }

    private fun showDeleteWarning() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.removeAllFavoriteList()

            finish()
            startActivity(intent)
        }

        builder.setNegativeButton("No") { _, _ ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun observeViewModel() {
        viewModel.users.observe(this) { users ->
            run {
                users?.let {
                    if (users.isEmpty()) {
                        listError.visibility = View.VISIBLE
                        itemList.visibility = View.GONE
                    } else {
                        itemList.visibility = View.VISIBLE
                        listError.visibility = View.GONE
                        usersAdapter.updateUsers(it)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}