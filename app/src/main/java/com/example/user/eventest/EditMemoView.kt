package com.example.user.eventest

import android.content.Context
import android.content.ContextWrapper
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Created by DR
 * on 07.03.2018.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class EditMemoView(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    var editState: Boolean = false


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getActivity() != null) {
                getActivity()?.setEditViewsGone()
            }
        }
        return true
    }

    private fun getActivity(): MainActivity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is MainActivity) {
                return context
            }
            context = (context).baseContext
        }
        return null
    }
}
