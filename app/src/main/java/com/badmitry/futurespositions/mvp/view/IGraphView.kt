package com.badmitry.futurespositions.mvp.view

import com.badmitry.futurespositions.mvp.model.entity.Price
import com.badmitry.futurespositions.mvp.model.entity.responses.FuturePosition
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IGraphView : MvpView {
    fun paintGraphStockPrice(listPrices: List<Price>)
    fun paintGraphsOfPositions(listFutures: List<FuturePosition>)
    fun setFutureName(name: String)
    fun displayGraphStockPrices()
    fun displayGraphFuturesPositions()
}