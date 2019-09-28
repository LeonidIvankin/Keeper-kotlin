package ru.leonidivankin.kotlinforandroid.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        viewModel.getViewState().observe(this, Observer<S>{
            if(it == null) return@Observer
            if(it.error != null){
                renderError(it.error)
                return@Observer
            }


            renderData(it.data)
        })
    }

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable){

        error.message?.let{
            showError(it)
        }
    }

    protected fun showError(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}