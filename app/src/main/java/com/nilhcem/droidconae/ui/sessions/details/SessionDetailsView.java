package com.nilhcem.droidconae.ui.sessions.details;

import android.support.annotation.StringRes;

import com.nilhcem.droidconae.data.app.model.Session;

public interface SessionDetailsView {

    void bindSessionDetails(Session session);

    void updateFabButton(boolean isSelected, boolean animate);

    void showSnackbarMessage(@StringRes int resId);
}
