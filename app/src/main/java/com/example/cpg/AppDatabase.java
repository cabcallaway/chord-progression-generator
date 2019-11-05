package com.example.cpg;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cpg.dao.ProgressionDao;
import com.example.cpg.helpers.Converters;
import com.example.cpg.model.Progression;
import com.example.cpg.model.User;
import com.example.cpg.dao.UserDao;

@Database(entities = {User.class, Progression.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao getUserDao();

    public abstract ProgressionDao getProgressionDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-database.db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
