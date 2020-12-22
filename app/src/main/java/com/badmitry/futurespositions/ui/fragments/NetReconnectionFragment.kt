package com.badmitry.futurespositions.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.badmitry.futurespositions.R
import com.badmitry.futurespositions.databinding.FragmentNetReconnectionLayoutBinding
import com.badmitry.futurespositions.mvp.presenter.NetReconnectionPresenter
import com.badmitry.futurespositions.mvp.view.INetReconnectView
import com.badmitry.futurespositions.ui.App
import com.badmitry.futurespositions.ui.BackBtnListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class NetReconnectionFragment : MvpAppCompatFragment(), INetReconnectView, BackBtnListener {
    companion object {
        fun newInstance() = NetReconnectionFragment()
    }

    private var binding: FragmentNetReconnectionLayoutBinding? = null

    private val presenter: NetReconnectionPresenter by moxyPresenter {
        NetReconnectionPresenter().apply { App.component.inject(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_net_reconnection_layout, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.bnReloadData?.setOnClickListener {
            presenter.reloadFutures()
        }
    }

    override fun backPressed(): Boolean = presenter.backPressed()

}