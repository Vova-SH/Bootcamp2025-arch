package ru.sicampus.bootcamp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.sicampus.bootcamp.databinding.ItemUserBinding
import ru.sicampus.bootcamp.domain.list.UserEntity

class UserAdapter : ListAdapter<UserEntity, UserAdapter.ViewHolder>(UserDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserEntity) {
            binding.title.text = item.name
            binding.description.text = item.email
            Picasso.get().load(item.photoUrl)
                .resize(64, 64)
                .centerCrop()
                .into(binding.photo)
        }

    }

    object UserDiff : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }
}
