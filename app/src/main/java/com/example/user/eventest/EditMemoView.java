package com.example.user.eventest;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by DR
 * on 07.03.2018.
 */

public class EditMemoView extends AppCompatEditText {
    public EditMemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getActivity() != null)
                getActivity().setEditViewsGone();
        }
        return false;
    }

    private MainActivity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof MainActivity) {
                return (MainActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
