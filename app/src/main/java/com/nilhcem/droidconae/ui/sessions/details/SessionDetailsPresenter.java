package com.nilhcem.droidconae.ui.sessions.details;

import android.os.Bundle;

import com.nilhcem.droidconae.R;
import com.nilhcem.droidconae.data.app.model.Session;
import com.nilhcem.droidconae.data.database.dao.SessionsDao;
import com.nilhcem.droidconae.receiver.reminder.SessionsReminder;
import com.nilhcem.droidconae.ui.BaseActivityPresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SessionDetailsPresenter extends BaseActivityPresenter<SessionDetailsView> {

    private final Session session;
    private final SessionsDao sessionsDao;
    private final SessionsReminder sessionsReminder;

    public SessionDetailsPresenter(SessionDetailsView view, Session session, SessionsDao sessionsDao, SessionsReminder sessionsReminder) {
        super(view);
        this.session = session;
        this.sessionsDao = sessionsDao;
        this.sessionsReminder = sessionsReminder;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        view.bindSessionDetails(session);
        sessionsDao.isSelected(session)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSelected -> view.updateFabButton(isSelected, false),
                        throwable -> Timber.e(throwable, "Error getting selected session state"));
    }

    public void onFloatingActionButtonClicked() {
        sessionsDao.getSelectedSessions(session.getFromTime())
                .map(sessions -> {
                    boolean shouldInsert = true;
                    for (Session session : sessions) {
                        sessionsReminder.removeSessionReminder(session);
                        if (session.getId() == this.session.getId()) {
                            shouldInsert = false;
                        }
                    }

                    if (shouldInsert) {
                        sessionsReminder.addSessionReminder(session);
                    }
                    sessionsDao.toggleSelectedSessionState(this.session, shouldInsert);
                    return shouldInsert;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shouldInsert -> {
                    if (shouldInsert) {
                        view.showSnackbarMessage(R.string.session_details_added);
                    } else {
                        view.showSnackbarMessage(R.string.session_details_removed);
                    }
                    view.updateFabButton(shouldInsert, true);
                }, throwable -> Timber.e(throwable, "Error getting selected session state"));
    }
}
