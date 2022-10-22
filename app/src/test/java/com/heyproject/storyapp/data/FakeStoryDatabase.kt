package com.heyproject.storyapp.data

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.heyproject.storyapp.data.datasource.local.dao.RemoteKeysDao
import com.heyproject.storyapp.data.datasource.local.dao.StoryDao
import com.heyproject.storyapp.data.datasource.local.database.StoryDatabase
import org.mockito.Mockito

/**
Written by Yayan Rahmat Wijaya on 10/22/2022 12:16
Github : https://github.com/yayanrw
 **/

class FakeStoryDatabase : StoryDatabase() {
    override fun storyDao(): StoryDao = FakeStoryDao()

    override fun remoteKeysDao(): RemoteKeysDao = FakeRemoteKeysDao()

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        return Mockito.mock(SupportSQLiteOpenHelper::class.java)
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return Mockito.mock(InvalidationTracker::class.java)
    }

    override fun clearAllTables() {}
}