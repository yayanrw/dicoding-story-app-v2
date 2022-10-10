package com.heyproject.storyapp.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.heyproject.storyapp.data.datasource.local.dao.RemoteKeysDao
import com.heyproject.storyapp.data.datasource.local.dao.StoryDao
import com.heyproject.storyapp.data.datasource.local.entity.RemoteKeysEntity
import com.heyproject.storyapp.data.datasource.local.entity.StoryEntity

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 14:30
Github : https://github.com/yayanrw
 **/

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}