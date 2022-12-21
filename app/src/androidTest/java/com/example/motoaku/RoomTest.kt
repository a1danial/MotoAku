package com.example.motoaku

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.motoaku.database.AppDatabase
import com.example.motoaku.database.fix.FixDao;
import com.example.motoaku.database.motorcycle.MotorcycleDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var fixDao: FixDao
    private lateinit var motoDao: MotorcycleDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java)
//            .allowMainThreadQueries()
            .build()
        fixDao = db.getFixDao()
        motoDao = db.getMotoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {

    }

}
