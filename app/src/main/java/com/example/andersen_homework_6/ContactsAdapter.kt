package com.example.andersen_homework_6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andersen_homework_6.databinding.ItemUserBinding

class ContactsAdapter(
    private val onItemClicked: (user: User, position: Int) -> Unit,
    private val onItemLongClicked: (position: Int) -> Unit
) :
    ListAdapter<User, ContactsAdapter.Holder>(ContactDiffUtilCallback()) {
    private val differ = AsyncListDiffer(this, ContactDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return Holder(binding, onItemClicked, onItemLongClicked)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = differ.currentList[position]
        holder.bind(user, position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    class Holder(
        private val binding: ItemUserBinding,
        val onItemClicked: (user: User, position: Int) -> Unit,
        val onItemLongClicked: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, position: Int) {
            binding.nameUserTextView.text = user.name
            binding.surnameUserTextView.text = user.surName
            binding.phoneUserTextView.text = user.phone
            Glide.with(itemView)
                .load(user.picture)
                .into(binding.userImage)
            itemView.setOnClickListener {
                onItemClicked(user, position)
            }
            itemView.setOnLongClickListener {
                onItemLongClicked(position)
                true
            }
        }
    }

    fun updateUsers(newList: List<User>) {
        differ.submitList(newList)
    }

    class ContactDiffUtilCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.picture == newItem.picture
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}