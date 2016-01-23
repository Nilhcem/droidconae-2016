package com.nilhcem.droidcontn.ui.speakers.list;

import com.nilhcem.droidcontn.data.model.Speaker;

import java.util.List;

public interface SpeakersListView {

    void displaySpeakers(List<Speaker> speakers);

    void showSpeakerDetail(Speaker speaker);
}
