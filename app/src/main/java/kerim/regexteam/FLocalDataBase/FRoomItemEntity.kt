package kerim.regexteam.FLocalDataBase

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RatedFilms")
data class FRoomItemEntity(
    @PrimaryKey(autoGenerate = true)
    val rateId:Int=0,
    val ratedFilmId: String,
    val ratedUserName:String,
    val userRate: Float
)