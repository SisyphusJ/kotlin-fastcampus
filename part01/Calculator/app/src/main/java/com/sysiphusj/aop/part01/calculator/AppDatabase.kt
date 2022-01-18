package com.sysiphusj.aop.part01.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sysiphusj.aop.part01.calculator.dao.HistoryDao
import com.sysiphusj.aop.part01.calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}