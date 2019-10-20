package ru.leonidivankin.kotlinforandroid.ui.splash

import android.os.Handler
import org.koin.android.viewmodel.ext.android.viewModel
import ru.leonidivankin.kotlinforandroid.ui.base.BaseActivity
import ru.leonidivankin.kotlinforandroid.ui.main.MainActivity

class SplashActivity : BaseActivity<Boolean?>() {

    companion object {
        private const val START_DELAY = 1000L
    }

    override val viewModel: SplashViewModel by viewModel()

    override val layoutRes: Int? = null


    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf {
            it
        }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}
