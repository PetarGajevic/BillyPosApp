package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wind.billypos.data.local.model.LocalFiscalDigitalKey
import com.wind.billypos.data.local.model.LocalLicense
import io.reactivex.Maybe

@Dao
interface LicenceDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(licence: LocalLicense): Maybe<Long>

    @Query ("SELECT * FROM licence")
    fun getLicence(): Maybe<LocalLicense>

    @Query("SELECT * FROM fiscal_digital_key")
    fun getCertificate(): Maybe<LocalFiscalDigitalKey>
}