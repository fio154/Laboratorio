package bdd;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import model.Plato;

@Database(entities = {Plato.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlatoDao platoDao();

    public static AppDatabase INSTANCE;

    static AppDatabase getInstance(final Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class, "database-name")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }
}
