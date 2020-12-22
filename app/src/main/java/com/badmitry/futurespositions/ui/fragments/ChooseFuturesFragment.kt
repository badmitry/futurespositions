package com.badmitry.futurespositions.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.badmitry.futurespositions.R
import com.badmitry.futurespositions.databinding.FragmentChooseFutureBinding
import com.badmitry.futurespositions.mvp.presenter.ChooseFuturesPresenter
import com.badmitry.futurespositions.mvp.view.IChooseFuturesView
import com.badmitry.futurespositions.ui.App
import com.badmitry.futurespositions.ui.BackBtnListener
import com.badmitry.futurespositions.ui.adapter.FuturesRVAdapter
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ChooseFuturesFragment : MvpAppCompatFragment(), IChooseFuturesView, BackBtnListener {
    companion object {
        fun newInstance() = ChooseFuturesFragment()
    }

    private val presenter: ChooseFuturesPresenter by moxyPresenter {
        ChooseFuturesPresenter().apply { App.component.inject(this) }
    }

    private var binding: FragmentChooseFutureBinding? = null

    private var adapter: FuturesRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_future, container, false)
        return binding?.root
    }

    override fun init() {
        binding?.rvFuture?.layoutManager = LinearLayoutManager(context)
        adapter = FuturesRVAdapter(presenter.futureListPresenter)
        binding?.rvFuture?.adapter = adapter
    }

    override fun backPressed(): Boolean = presenter.backPressed()

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}