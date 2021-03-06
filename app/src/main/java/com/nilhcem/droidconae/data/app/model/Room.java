package com.nilhcem.droidconae.data.app.model;

import android.support.annotation.NonNull;

public enum Room {

    NONE(0, ""),
    MAIN_HALL(1, "Main Hall"),
    BREAKOUT_ROOM(2, "Breakout Room");

    public final int id;
    public final String name;

    Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Room getFromId(int id) {
        for (Room room : Room.values()) {
            if (room.id == id) {
                return room;
            }
        }
        return NONE;
    }

    public static Room getFromName(@NonNull String name) {
        for (Room room : Room.values()) {
            if (name.equals(room.name)) {
                return room;
            }
        }
        return NONE;
    }
}
