package com.parlegrandpa.lagosdevelopers.src.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parlegrandpa.lagosdevelopers.R
import com.parlegrandpa.lagosdevelopers.src.model.UserItem
import com.parlegrandpa.lagosdevelopers.src.util.loadImage
import com.parlegrandpa.lagosdevelopers.src.util.getProgressDrawable
import com.parlegrandpa.lagosdevelopers.src.view.ItemDetailActivity
import kotlinx.android.synthetic.main.item_user.view.*

class UserListAdapter(var context: Context, var users: ArrayList<UserItem>): RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    fun updateUsers(newUsers: List<UserItem>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(context, users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val userName = view.name
        val usertype = view.usertype
        val imageView = view.imageView
        val linearLayout = view.linearLayout

        fun bind(context: Context, userItem: UserItem) {
            userName.text = userItem.login
            usertype.text = userItem.type
            loadImage(
                imageView,
                userItem.avatar_url,
                getProgressDrawable(imageView.context)
            )

            linearLayout.setOnClickListener {
                context.startActivity(Intent(context, ItemDetailActivity::class.java).putExtra("id", userItem.id))
            }
        }
    }
}