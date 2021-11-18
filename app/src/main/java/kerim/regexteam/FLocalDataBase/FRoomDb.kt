package kerim.regexteam.FLocalDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FRoomItemEntity::class],version = 2,exportSchema = false)
abstract class FRoomDb: RoomDatabase() {
    abstract fun filmDataDao():FRoomDao

    companion object {

        @Volatile
        private var INSTANCE: FRoomDb? = null
        fun getDatabase(context: Context): FRoomDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized (this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FRoomDb::class.java, "data_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

}