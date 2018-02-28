package com.example.user.eventest.widget;

import com.example.user.eventest.model.Memo;
import com.example.user.eventest.model.RoomRepository;

import java.util.ArrayList;

/**
 * Created by DR
 * on 28.02.2018.
 */

public class WidgetData implements WidgetPresenter {

    private RoomRepository roomRepository;

    WidgetData(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public ArrayList<Memo> populateListItem() {

        return roomRepository.getAllData();

    }
}
