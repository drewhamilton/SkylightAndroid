package dev.drewhamilton.skylight.android.demo.rx.ui

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class RxActivity : AppCompatActivity() {

    private val subscriptions = CompositeDisposable()

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }

    protected fun Disposable.untilDestroy() {
        subscriptions.add(this)
    }
}
