package com.wind.billypos.base

import com.wind.billypos.utils.LocaleManager
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    var primaryBaseActivity: Context? = null
    var viewDataBinding: T? = null
    private var mViewModel: V? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { LocaleManager.setLocale(it) })
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
//        try {
//            if (toolbar != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    (toolbar.getChildAt(0) as TextView).typeface = resources.getFont(R.font.font_baumans_regular)
//                } else {
//                    try {
//                        (toolbar.getChildAt(0) as TextView).typeface = Typeface.createFromAsset(this?.assets, "fonts/Baumans-Regular.ttf")
//                    } catch (ex: Exception) {
//                    }
//                }
//            }
//        }catch(ex: Exception){}
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        performDataBinding()
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
        viewDataBinding!!.lifecycleOwner = this
    }
}