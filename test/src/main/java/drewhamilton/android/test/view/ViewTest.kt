package drewhamilton.android.test.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import drewhamilton.android.test.CustomActions
import drewhamilton.android.test.UiTest
import org.junit.Assert.fail

abstract class ViewTest<V : View> : UiTest<ViewTestActivity>(ViewTestActivity::class.java) {

    @Suppress("UNCHECKED_CAST")
    protected fun getView() = activity.getContentView().getChildAt(0) as V

    protected fun setView(view: V) {
        runOnUiThread {
            activity.getContentView().removeAllViews()
            activity.setContentView(view)
        }
        CustomActions.waitForUiThread()
    }

    protected fun setView(@LayoutRes layout: Int) {
        runOnUiThread {
            activity.getContentView().removeAllViews()
            activity.setContentView(layout)
            try {
                getView()
            } catch (castException: ClassCastException) {
                fail("View inflated from layout res <$layout> was not an instance of V")
            }
        }
        CustomActions.waitForUiThread()
    }

    private fun ViewTestActivity.getContentView() = findViewById<ViewGroup>(android.R.id.content)
}
