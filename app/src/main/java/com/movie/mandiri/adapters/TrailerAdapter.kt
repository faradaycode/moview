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
import com.movie.mandiri.databinding.ItemTrailerBinding
import com.movie.mandiri.models.TrailerDatum


class TrailerAdapter(
    private var data: MutableList<TrailerDatum>?,
    private val evt: TrailerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // flag for footer ProgressBar (i.e. last item of list)

    private var isLoadingAdded = false

    fun getData(): List<TrailerDatum>? {
        return data
    }

    fun setData(data: MutableList<TrailerDatum>?) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> viewHolder = _ViewHolder(
                ItemTrailerBinding.inflate(inflater, parent, false),
                this.evt,
                parent.context
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
    fun add(spt: TrailerDatum?) {
        if (spt != null) {
            data!!.add(spt)
        }

        notifyItemInserted(data!!.size - 1)
    }

    fun addAll(spts: List<TrailerDatum?>?) {
        if (spts != null) {
            for (sptr in spts) {
                add(sptr)
            }
        }
    }

    fun remove(res: TrailerDatum?) {
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
        add(TrailerDatum())
    }

    fun removeLoadingFooter() {
        if (data?.size!! > 0) {
            isLoadingAdded = false
            val position = data!!.size - 1
            val item: TrailerDatum = getItem(position)

            data!!.removeAt(position)
            notifyItemRemoved(position)
        }

//        Log.d("isiloadingfooter", item.toString())
    }

    fun getItem(position: Int): TrailerDatum {
        return data!![position]
    }

    //VIEWHOLDER
    private inner class _ViewHolder(
        val binding: ItemTrailerBinding,
        val evt: TrailerView,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var mLastClickTime: Long = 0

        fun Bind(data: TrailerDatum?) {
            val drawable = CircularProgressDrawable(context)
            drawable.centerRadius = 30f
            drawable.strokeWidth = 5f
            // set all other properties as you would see fit and start it
            // set all other properties as you would see fit and start it
            drawable.start()

            binding.tvGenre.text = data?.name
            Glide.with(this.context)
//                .load("https://www.youtube.com/watch?v=${data?.}")
                .load("https://img.youtube.com/vi/${data?.key}/mqdefault.jpg")
                .placeholder(drawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.imposter)

            binding.root.setOnClickListener(View.OnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@OnClickListener
                }

                mLastClickTime = SystemClock.elapsedRealtime()
                data?.key?.let { it1 -> evt.clickFilm(it1) }
            })
        }
    }

    private inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }

    interface TrailerView {
        fun clickFilm(id: String)
    }
}