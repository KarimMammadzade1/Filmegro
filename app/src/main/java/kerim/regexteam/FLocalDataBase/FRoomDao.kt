package kerim.regexteam.FLocalDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FRoomDao {
   @Insert()
   suspend fun insertData(rateData:FRoomItemEntity)

   @Query("SELECT * FROM RatedFilms WHERE ratedFilmId = :filmId ")
   suspend fun getData(filmId:String):List<FRoomItemEntity>
}