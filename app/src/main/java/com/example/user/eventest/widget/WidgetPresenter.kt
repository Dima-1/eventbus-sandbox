package com.example.user.eventest.widget

import com.example.user.eventest.model.Memo
import java.util.*

/**
 * Created by DR
 * on 28.02.2018.
 */

interface WidgetPresenter {
    fun populateListItem(): ArrayList<Memo>
}
