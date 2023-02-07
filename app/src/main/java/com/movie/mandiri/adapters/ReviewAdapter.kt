package com.movie.mandiri.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.movie.mandiri.R
import com.movie.mandiri.databinding.ItemReviewBinding
import com.movie.mandiri.models.ResultReview
import java.text.SimpleDateFormat
import java.util.*


class ReviewAdapter(
    private var data: MutableList<ResultReview>?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // flag for footer ProgressBar (i.e. last item of list)

    private var isLoadingAdded = false

    fun getData(): List<ResultReview>? {
        return data
    }

    fun setData(data: MutableList<ResultReview>?) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> viewHolder = _ViewHolder(
                ItemReviewBinding.inflate(inflater, parent, false)
            )
            LOADING -> {
                val v2 = inflater.inflate(R.layout.pagination_loading_view, parent, false)
                viewHolder = LoadingViewHolder(v2)
            }
        }
        return viewHolder!!
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val satum = data!![position]
        when (getItemViewType(position)) {
            ITEM -> {
                val viewHolder = holder as _ViewHolder
                viewHolder.Bind(satum)
            }
            LOADING -> {
                //nothing
            }
        }
    }

    override fun getItemCount(): Int {
        return if (data == null) 0 else data!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == data!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    // HELPER
    fun add(spt: ResultReview?) {
        if (spt != null) {
            data!!.add(spt)
        }

        notifyItemInserted(data!!.size - 1)
    }

    fun addAll(spts: List<ResultReview?>?) {
        if (spts != null) {
            for (sptr in spts) {
                add(sptr)
            }
        }
    }

    fun remove(res: ResultReview?) {
        val position = data!!.indexOf(res)
        if (position > -1) {
            data!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun clearAll() {
        data!!.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(ResultReview())
    }

    fun removeLoadingFooter() {
        if (data?.size!! > 0) {
            isLoadingAdded = false
            val position = data!!.size - 1
            val item: ResultReview = getItem(position)

            data!!.removeAt(position)
            notifyItemRemoved(position)
        }

//        Log.d("isiloadingfooter", item.toString())
    }

    fun getItem(position: Int): ResultReview {
        return data!![position]
    }

    //VIEWHOLDER
    private inner class _ViewHolder(
        val binding: ItemReviewBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun Bind(data: ResultReview?) {
            binding.tvAuthor.text = data?.author
            binding.tvDate.text = timeForcemat(data?.createdAt.toString())
            binding.tvContent.text = data?.content
        }
    }

    private inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }

    ////////////////
    private fun timeForcemat(mtime: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val dd = format.parse(mtime)
        val format2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format2.format(dd)
    }
}