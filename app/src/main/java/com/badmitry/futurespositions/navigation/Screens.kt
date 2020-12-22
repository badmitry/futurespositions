package com.badmitry.futurespositions.navigation

import android.content.Context
import android.content.Intent
import com.badmitry.futurespositions.mvp.model.entity.Future
import com.badmitry.futurespositions.ui.activity.MainActivity
import com.badmitry.futurespositions.ui.fragments.ChooseFuturesFragment
import com.badmitry.futurespositions.ui.fragments.GraphFragment
import com.badmitry.futurespositions.ui.fragments.NetReconnectionFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class MainActivityScreen(private val isConnection: Boolean) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?) =
            Intent(context, MainActivity::class.java).putExtra("isConnection", isConnection)
    }

    class NetReconnectionScreen : SupportAppScreen() {
        override fun getFragment() = NetReconnectionFragment.newInstance()
    }

    class ChooseFutureScreen : SupportAppScreen() {
        override fun getFragment() = ChooseFuturesFragment.newInstance()
    }

    class GraphFutureScreen(private val future: Future) : SupportAppScreen() {
        override fun getFragment() = GraphFragment.newInstance(future)
    }
}