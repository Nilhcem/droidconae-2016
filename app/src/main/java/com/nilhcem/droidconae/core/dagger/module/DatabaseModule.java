package com.nilhcem.droidconae.core.dagger.module;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.nilhcem.droidconae.data.database.DbOpenHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class DatabaseModule {

    static final String TAG = "database";

    @Provides @Singleton SQLiteOpenHelper provideSQLiteOpenHelper(Application application) {
        return new DbOpenHelper(application);
    }

    @Provides @Singleton SqlBrite provideSqlBrite() {
        return SqlBrite.create(Timber.tag(TAG)::v);
    }

    @Provides @Singleton BriteDatabase provideBriteDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        return sqlBrite.wrapDatabaseHelper(helper);
    }
}
