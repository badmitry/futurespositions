package com.badmitry.futurespositions.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.badmitry.futurespositions.R
import com.badmitry.futurespositions.databinding.FragmentGraphFutureBinding
import com.badmitry.futurespositions.mvp.model.entity.Future
import com.badmitry.futurespositions.mvp.model.entity.Price
import com.badmitry.futurespositions.mvp.model.entity.responses.FuturePosition
import com.badmitry.futurespositions.mvp.presenter.GraphFuturesPresenter
import com.badmitry.futurespositions.mvp.view.IGraphView
import com.badmitry.futurespositions.ui.App
import com.badmitry.futurespositions.ui.BackBtnListener
import com.github.mikephil.charting.data.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


class GraphFragment : MvpAppCompatFragment(), IGraphView, BackBtnListener {
    companion object {
        private const val FUTURE_ARG = "future"
        fun newInstance(future: Future) = GraphFragment().apply {
            arguments = Bundle().apply {
                putParcelable(FUTURE_ARG, future)
            }
        }
    }

    private val presenter: GraphFuturesPresenter by moxyPresenter {
        val future = arguments?.getParcelable<Future>(FUTURE_ARG) as Future
        GraphFuturesPresenter(future).apply { App.component.inject(this) }
    }

    private var binding: FragmentGraphFutureBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_graph_future, container, false)
        return binding?.root
    }

    override fun backPressed(): Boolean = presenter.backPressed()

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun paintGraphStockPrice(listPrices: List<Price>) {
        val listEntry = mutableListOf<Entry>()
        for (i in listPrices.indices) {
            listEntry.add(Entry(i.toFloat(), listPrices[i].price.toFloat()))
        }
        val dataSet = LineDataSet(listEntry, "График цены за год")
        val colorLime = context?.let { ContextCompat.getColor(it, R.color.lime) }
        colorLime?.let { dataSet.color = colorLime }
        dataSet.setDrawCircles(false)
        val lineData = LineData(dataSet)
        binding?.chartStockPrices?.let {
            val xAxis = it.xAxis
            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            it.data = lineData
            it.invalidate()
        }
        presenter.displayGraphStockPrices()
    }

    override fun paintGraphsOfPositions(listFutures: List<FuturePosition>) {
        val colorRed = context?.let { ContextCompat.getColor(it, R.color.red) }
        val listEntryJuridicalPositionShort = mutableListOf<BarEntry>()
        for (i in listFutures.indices) {
            listEntryJuridicalPositionShort.add(
                BarEntry(
                    i.toFloat(),
                    -listFutures[i].JuridicalShort.filter { !it.isWhitespace() }.toFloat()
                )
            )
        }
        val dataSetJuridicalPositionShort =
            BarDataSet(listEntryJuridicalPositionShort, "Шортовые позиции юридических лиц")
        dataSetJuridicalPositionShort.setDrawValues(false)
        colorRed?.let { dataSetJuridicalPositionShort.setColor(colorRed) }

        val listEntryJuridicalPositionLong = mutableListOf<BarEntry>()
        for (i in listFutures.indices) {
            listEntryJuridicalPositionLong.add(
                BarEntry(
                    i.toFloat(),
                    listFutures[i].JuridicalLong.filter { !it.isWhitespace() }.toFloat()
                )
            )
        }
        val dataSetJuridicalPositionLong =
            BarDataSet(listEntryJuridicalPositionLong, "Лонговые позиции юридических лиц")
        dataSetJuridicalPositionLong.setDrawValues(false)

        val dataJuridicalPosition = BarData(
            dataSetJuridicalPositionShort,
            dataSetJuridicalPositionLong
        )

        val listEntryPhysicalPositionLong = mutableListOf<BarEntry>()
        for (i in listFutures.indices) {
            listEntryPhysicalPositionLong.add(
                BarEntry(
                    i.toFloat(),
                    listFutures[i].PhysicalLong.filter { !it.isWhitespace() }.toFloat()
                )
            )
        }
        val dataSetPhysicalPositionLong =
            BarDataSet(listEntryPhysicalPositionLong, "Шортовые позиции физических лиц")
        dataSetPhysicalPositionLong.setDrawValues(false)

        val listEntryPhysicalPositionShort = mutableListOf<BarEntry>()
        for (i in listFutures.indices) {
            listEntryPhysicalPositionShort.add(
                BarEntry(
                    i.toFloat(),
                    -listFutures[i].PhysicalShort.filter { !it.isWhitespace() }.toFloat()
                )
            )
        }
        val dataSetPhysicalPositionShort =
            BarDataSet(listEntryPhysicalPositionShort, "Лонговые позиции физических лиц")
        dataSetPhysicalPositionShort.setDrawValues(false)
        colorRed?.let { dataSetPhysicalPositionShort.setColor(colorRed) }

        val dataPhysicalPosition = BarData(
            dataSetPhysicalPositionShort,
            dataSetPhysicalPositionLong
        )

        binding?.physicalBarChart?.let {
            val xAxis = it.xAxis
            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            it.data = dataPhysicalPosition
            it.invalidate()
        }

        binding?.juridicalBarChart?.let {
            val xAxis = it.xAxis
            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            it.data = dataJuridicalPosition
            it.invalidate()
        }
        presenter.displayGraphFuturesPositions()
    }

    override fun setFutureName(name: String) {
        binding?.tvFutureName?.text = name
    }

    override fun displayGraphStockPrices() {
        binding?.chartStockPrices?.visibility = View.VISIBLE
        binding?.progressPrices?.visibility = View.GONE
    }

    override fun displayGraphFuturesPositions() {
        binding?.juridicalBarChart?.visibility = View.VISIBLE
        binding?.physicalBarChart?.visibility = View.VISIBLE
        binding?.progressJuridical?.visibility = View.GONE
        binding?.progressPhysical?.visibility = View.GONE
    }
}