package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wind.billypos.data.local.model.LocalConfiguration
import com.wind.billypos.view.model.Configuration
import io.reactivex.Maybe
import io.reactivex.Observable


@Dao
interface FiscalisationDao {

//    @Insert
//    fun insertCompany(company: LocalConfiguration.Company): Observable<Long>

//    @Query("SELECT * FROM company LIMIT 1")
//    fun getCompany(): Maybe<Configuration.Company>

}