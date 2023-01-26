package com.example.marerialtabeditor.repository.data.tab

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TabDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTab(tab: Tab)

    @Query("SELECT * FROM tab_table")
    fun readAllData(): LiveData<List<Tab>>

//    @Query("DELETE FROM tab_table WHERE id = :tabId")
//    fun deleteItem(name: String)
}