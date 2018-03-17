package com.example.user.eventest

import android.content.Context
import android.content.ContextWrapper
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent

/**
 * Created by DR
 * on 07.03.2018.
 */

class EditMemoView(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    var isEditState: Boolean = false


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
