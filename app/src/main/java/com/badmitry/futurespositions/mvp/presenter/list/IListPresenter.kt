package com.badmitry.futurespositions.mvp.presenter.list

import com.badmitry.futurespositions.mvp.view.list.IItemView

interface IListPresenter <V: IItemView> {
    var itemClickListener: ((V) -> Unit)?
    fun bindView(view: V)
    fun getCount(): Int
}