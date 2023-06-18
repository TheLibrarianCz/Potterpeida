package job.hunt.potteredia.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import job.hunt.potteredia.model.Character

@Database(
    entities = [
        Character::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PotterpediaRoom : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {

        fun buildDatabase(context: Context): PotterpediaRoom {
            return Room.databaseBuilder(
                context,
                PotterpediaRoom::class.java,
                "potterpedia.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
