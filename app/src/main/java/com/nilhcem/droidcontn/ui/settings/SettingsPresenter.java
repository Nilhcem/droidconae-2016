package com.nilhcem.droidcontn.ui.settings;

import com.nilhcem.droidcontn.ui.BasePresenter;
import com.nilhcem.droidcontn.utils.AppUtils;

public class SettingsPresenter extends BasePresenter<SettingsView> {

    public SettingsPresenter(SettingsView view) {
        super(view);
    }

    public void onCreate() {
        view.setAppVersion(AppUtils.getVersion());
    }

    public boolean onNotifySessionsChange(boolean checked) {
        view.setNotifySessionsCheckbox(checked);
        return true;
    }
}