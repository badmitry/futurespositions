package com.badmitry.futurespositions.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.badmitry.futurespositions.R
import com.badmitry.futurespositions.mvp.presenter.list.IFutureListPresenter
import com.badmitry.futurespositions.mvp.view.list.IFutureItemView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_future.view.*

class FuturesRVAdapter(private val presenter: IFutureListPresenter) :
    RecyclerView.Adapter<FuturesRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_future, parent, false))

    override fun getItemCount() = presenter.getCount()
    override fun onBindViewHolder(holder: FuturesRVAdapter.ViewHolder, position: Int) {
        holder.pos = position
        holder.containerView.setOnClickListener {
            presenter.itemClickListener?.invoke(holder)
        }
        presenter.bindView(holder)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer, IFutureItemView {
        override var pos = -1
        override fun setName(name: String) = with(containerView) {
            tv_future_name.text = name
        }
    }
}