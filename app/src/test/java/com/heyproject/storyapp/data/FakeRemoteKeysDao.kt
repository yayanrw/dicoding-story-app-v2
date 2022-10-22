package com.heyproject.storyapp.data

import com.heyproject.storyapp.data.datasource.local.dao.RemoteKeysDao
import com.heyproject.storyapp.data.datasource.local.entity.RemoteKeysEntity

/**
Written by Yayan Rahmat Wijaya on 10/22/2022 12:17
Github : https://github.com/yayanrw
 **/

class FakeRemoteKeysDao : RemoteKeysDao {
    private val remoteKeys = mutableListOf<RemoteKeysEntity>()

    override suspend fun insertAll(remoteKey: List<RemoteKeysEntity>) {
        remoteKeys.addAll(remoteKey)
    }

    override suspend fun getRemoteKeysId(id: String): RemoteKeysEntity? {
        return remoteKeys.firstOrNull { it.id == id }
    }

    override suspend fun deleteRemoteKeys() {
        remoteKeys.clear()
    }
}