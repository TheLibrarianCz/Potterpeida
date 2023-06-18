package job.hunt.potteredia.storage

import androidx.room.TypeConverter
import job.hunt.potteredia.model.Wand
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun stringToWand(value: String?): Wand? {
        return if (value == null) {
            null
        } else {
            Json.decodeFromString<Wand>(value)
        }
    }

    @TypeConverter
    fun wandToString(date: Wand?): String? {
        return if (date == null) {
            null
        } else {
            Json.encodeToString(date)
        }
    }

    @TypeConverter
    fun stringToList(string: String?): List<String>? {
        return if (string == null) {
            null
        } else {
            if (string.isNotBlank()) {
                string.split(";")
            } else {
                emptyList()
            }
        }
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return if (list == null) {
            null
        } else {
            list.joinToString(";")
        }
    }
}
