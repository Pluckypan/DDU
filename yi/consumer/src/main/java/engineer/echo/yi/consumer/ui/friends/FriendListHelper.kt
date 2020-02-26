package engineer.echo.yi.consumer.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import engineer.echo.yi.common.cpmts.glide.EasyPicture
import engineer.echo.yi.consumer.R
import engineer.echo.yi.consumer.cmpts.weibo.bean.Account
import engineer.echo.yi.consumer.cmpts.weibo.bean.UserList
import engineer.echo.yi.consumer.databinding.ConsumerItemFriendBinding

object FriendListHelper {

    @BindingAdapter("userList")
    @JvmStatic
    fun bindData(recyclerView: RecyclerView, userList: UserList?) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = FriendAdapter()
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        }
        if (userList != null) {
            recyclerView.adapter?.let {
                if (it is FriendAdapter) {
                    if (userList.previousCursor == 0) {
                        it.setData(userList.users)
                    } else {
                        it.appendData(userList.users)
                    }
                }
            }
        }
    }

    @BindingAdapter("userList")
    @JvmStatic
    fun bindRefreshLayout(layout: SmartRefreshLayout, userList: UserList?) {
        if (userList != null) {
            if (userList.previousCursor == 0) {
                layout.finishRefresh()
            } else {
                layout.finishLoadMore()
            }
            if (userList.nextCursor == 0) {
                layout.setNoMoreData(true)
            }
        }
    }

    private class FriendAdapter : RecyclerView.Adapter<FriendViewHolder>() {

        private val dataList = arrayListOf<Account>()

        fun setData(list: List<Account>?) {
            dataList.clear()
            if (list != null && list.isNotEmpty()) {
                dataList.addAll(list)
            }
            notifyDataSetChanged()
        }

        fun appendData(list: List<Account>) {
            if (list.isNotEmpty()) {
                val start = dataList.size
                dataList.addAll(list)
                notifyItemRangeInserted(start, list.size)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
            return LayoutInflater.from(parent.context)
                .inflate(R.layout.consumer_item_friend, parent, false).let {
                    FriendViewHolder(it)
                }
        }

        override fun getItemCount(): Int = dataList.size

        override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
            holder.bindData(dataList[position])
        }
    }

    private class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ConsumerItemFriendBinding.bind(itemView)

        fun bindData(account: Account) {
            binding.account = account
        }
    }

    @BindingAdapter("accountAvatar")
    @JvmStatic
    fun bindAvatar(imageView: ImageView, account: Account?) {
        EasyPicture.with(imageView)
            .load(account?.avatar ?: "")
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    @BindingAdapter("followState")
    @JvmStatic
    fun bindFollow(textView: TextView, account: Account?) {
        val txt =
            if (account?.following == true) R.string.consumer_followed else R.string.consumer_follow
        textView.setText(txt)
    }
}