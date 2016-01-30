package com.nilhcem.droidcontn.data.app;

import com.nilhcem.droidcontn.data.app.model.Schedule;
import com.nilhcem.droidcontn.data.app.model.Speaker;
import com.nilhcem.droidcontn.data.database.dao.SelectedSessionsDao;
import com.nilhcem.droidcontn.data.network.DroidconService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DataProvider {

    private final DroidconService service;
    private final SelectedSessionsDao selectedSessionsDao;

    @Inject
    public DataProvider(DroidconService service, SelectedSessionsDao selectedSessionsDao) {
        this.service = service;
        this.selectedSessionsDao = selectedSessionsDao;
    }

    public Observable<Schedule> getSchedule() {
        selectedSessionsDao.init();
        return Observable.combineLatest(getSpeakers(), getScheduleDays(), (speakers, scheduleDays) ->
                AppMapper.mapSchedule(scheduleDays, AppMapper.speakersToMap(speakers)));
    }

    public Observable<List<Speaker>> getSpeakerList() {
        return getSpeakers().map(AppMapper::mapSpeakers);
    }

    private Observable<List<com.nilhcem.droidcontn.data.network.model.Speaker>> getSpeakers() {
        // Get from db || file

        // Get from network
        return service.loadSpeakers()
                .flatMap(Observable::<com.nilhcem.droidcontn.data.network.model.Speaker>from)
                .toList();
    }

    private Observable<List<com.nilhcem.droidcontn.data.network.model.ScheduleDay>> getScheduleDays() {
        // Get from db || file

        // Get from network
        return service.loadSchedule()
                .flatMap(Observable::<com.nilhcem.droidcontn.data.network.model.ScheduleDay>from)
                .toList();
    }
}
