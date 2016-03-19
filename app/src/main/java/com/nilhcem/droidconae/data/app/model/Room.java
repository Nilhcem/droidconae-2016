package com.nilhcem.droidconae.data.app.model;

import android.support.annotation.NonNull;

public enum Room {

    NONE(0, ""),
    TRACK_1(1, "Track 1"),
    TRACK_2(2, "Track 2");

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
