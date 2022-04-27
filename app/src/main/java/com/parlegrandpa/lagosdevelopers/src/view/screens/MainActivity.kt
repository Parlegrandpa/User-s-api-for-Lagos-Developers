package com.parlegrandpa.lagosdevelopers.src.view.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.parlegrandpa.lagosdevelopers.R
import com.parlegrandpa.lagosdevelopers.src.data.factory.UserItemViewModelFactory
import com.parlegrandpa.lagosdevelopers.src.view.adapter.UserListAdapter
import com.parlegrandpa.lagosdevelopers.src.viewmodel.ListUserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListUserViewModel
    private val usersAdapter = UserListAdapter(this, arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        initView()
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            fetchUsers(false)
        }
    }

    private fun initView() {
        val modelFactory = UserItemViewModelFactory(application)
        viewModel = ViewModelProvider(this, modelFactory).get(ListUserViewModel::class.java)

        usersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }

        favoriteButton.setOnClickListener { startActivity(Intent(this, FavoriteListActivity::class.java)) }

        initSwipeToRefresh()

        observeViewModel()
    }

    private fun initSwipeToRefresh() {
        refreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                fetchUsers(true)
            }
        }
    }

    private fun fetchUsers(forceLoadFromRemote: Boolean) {
        refreshLayout.isRefreshing = false
        viewModel.refresh(forceLoadFromRemote)
    }

    private fun observeViewModel() {
        viewModel.users.observe(this) { users ->
            run {
                users?.let {
                    if (users.isEmpty()) {
                        listError.visibility = View.VISIBLE
                        usersList.visibility = View.GONE
                    } else {
                        usersList.visibility = View.VISIBLE
                        listError.visibility = View.GONE
                        usersAdapter.updateUsers(it)
                    }
                }
            }
        }

        viewModel.usersLoadError.observe(this) { isError ->
            run {
                isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            run {
                isLoading?.let {
                    loadingView.visibility = if (it) View.VISIBLE else View.GONE
                    if (it) {
                        listError.visibility = View.GONE
                        usersList.visibility = View.GONE
                    }
                }
            }
        }
    }
}