package com.nilhcem.droidcontn.core.dagger;

import com.nilhcem.droidcontn.ui.drawer.DrawerActivity;
import com.nilhcem.droidcontn.ui.settings.SettingsFragment;
import com.nilhcem.droidcontn.ui.speakers.list.SpeakersListFragment;

/**
 * A common interface implemented by both the internal and production flavored components.
 */
public interface AppGraph {

    void inject(DrawerActivity activity);

    void inject(SpeakersListFragment fragments);

    void inject(SettingsFragment fragment);
}
