package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.wind.billypos.data.local.model.LocalFiscalDigitalKey
import io.reactivex.Single

@Dao
interface CompanyDao {

    @Insert
    fun insertFiscalDigitalKey(fiscalDigitalKey: LocalFiscalDigitalKey): Single<Long>
}