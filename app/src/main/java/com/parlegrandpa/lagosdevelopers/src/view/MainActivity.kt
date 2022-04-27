package com.parlegrandpa.lagosdevelopers.src.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.parlegrandpa.lagosdevelopers.R
import com.parlegrandpa.lagosdevelopers.src.model.UserItemViewModelFactory
import com.parlegrandpa.lagosdevelopers.src.view.adapter.UserListAdapter
import com.parlegrandpa.lagosdevelopers.src.viewmodel.ListUserViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListUserViewModel
    private val usersAdapter = UserListAdapter(this, arrayListOf())

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    override fun onResume() {
        super.onResume()

        fetchUsers(1, true)
    }

    fun initView() {
        val modelfactory = UserItemViewModelFactory(application)
        viewModel = ViewModelProvider(this, modelfactory).get(ListUserViewModel::class.java)

        usersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }

        favoriteButton.setOnClickListener { startActivity(Intent(this, FavoriteListActivity::class.java)) }

        initSwipeToRefresh()
//        initNestedSV()

        observeViewModel()
    }

    fun initSwipeToRefresh() {
        refreshLayout.setOnRefreshListener {
            fetchUsers(1, true)
        }
    }

    fun fetchUsers(count: Int, showLoader: Boolean) {
        refreshLayout.isRefreshing = false
        viewModel.refresh(count, showLoader)
    }

//    fun initNestedSV() {
//        nestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            // on scroll change we are checking when users scroll as bottom.
//            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//                // in this method we are incrementing page number,
//                // making progress bar visible and calling get data method.
//                count++
//                Log.e("DEDWDW", ""+count)
//                // on below line we are making our progress bar visible.
////                pBLoading.visibility(View.VISIBLE)
//                if (count < 20) {
//                    refreshLayout.isRefreshing = true
//                    // on below line we are again calling
//                    // a method to load data in our array list.
//                    fetchUsers(2, false)
//                }
//            }
//        })
//    }

    fun observeViewModel() {
        viewModel.users.observe(this, Observer { users ->
            run {
                users?.let {
                    if (users.isEmpty()) {
                        listError.visibility = View.VISIBLE
                        usersList.visibility = View.GONE
                    } else {
                        usersList.visibility = View.VISIBLE
                        listError.visibility = View.GONE
                        usersAdapter.updateUsers(it) }
                    }
            }
        })

        viewModel.usersLoadError.observe(this, Observer { isError ->
            run {
                isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            run {
                isLoading?.let {
                    loadingView.visibility = if (it) View.VISIBLE else View.GONE
                    if (it) {
                        listError.visibility = View.GONE
                        usersList.visibility = View.GONE
                    }
                }
            }
        })
    }
}