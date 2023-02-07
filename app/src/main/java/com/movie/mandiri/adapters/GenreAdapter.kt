package com.movie.mandiri.adapters

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movie.mandiri.databinding.ItemGenreBinding
import com.movie.mandiri.models.Genre

class GenreAdapter(private var data: MutableList<Genre>?, private val evt: GenreView) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return mViewHolder(inflater, this.evt)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val datum = data?.get(position)
        val hola = holder as mViewHolder
        hola.Bind(datum)
    }

    fun remove(res: Genre?) {
        val position = data!!.indexOf(res)
        if (position > -1) {
            data!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addAll(genre: MutableList<Genre>) {
        this.data = genre
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Genre {
        return data!![position]
    }

    fun clearAll() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
        notifyItemRangeRemoved(0, itemCount)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    private inner class mViewHolder(val binding: ItemGenreBinding, val evt: GenreView) :
        RecyclerView.ViewHolder(binding.root) {

        private var mLastClickTime: Long = 0

        fun Bind(data: Genre?) {
            binding.tvGenre.text = data?.name.toString()

            binding.root.setOnClickListener(View.OnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@OnClickListener
                }

                mLastClickTime = SystemClock.elapsedRealtime()
                data?.id?.let { it1 -> evt.clickGenre(it1) }
            })
        }
    }

    interface GenreView {
        fun clickGenre(id: Int)
    }
}