package com.example.user.eventest.widget

import com.example.user.eventest.model.Memo
import com.example.user.eventest.model.RoomRepository
import java.util.*

/**
 * Created by DR
 * on 28.02.2018.
 */

class WidgetData(private val roomRepository: RoomRepository) : WidgetPresenter {

    override fun populateListItem(): ArrayList<Memo> = roomRepository.getAllData()

}
