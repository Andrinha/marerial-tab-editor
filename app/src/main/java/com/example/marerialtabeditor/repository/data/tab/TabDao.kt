package com.example.marerialtabeditor.repository.data.tab

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TabDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTab(tab: Tab)

    @Query("SELECT * FROM tab_table")
    fun readAllData(): LiveData<List<Tab>>

//    @Query("DELETE FROM tab_table WHERE id = :tabId")
//    fun deleteItem(name: String)
}