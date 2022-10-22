package com.heyproject.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.heyproject.storyapp.data.datasource.local.dao.StoryDao
import com.heyproject.storyapp.data.datasource.local.entity.StoryEntity

/**
Written by Yayan Rahmat Wijaya on 10/22/2022 11:31
Github : https://github.com/yayanrw
 **/

class FakeStoryDao : StoryDao {
    private var stories = mutableListOf<StoryEntity>()

    override suspend fun insertStory(storyEntity: StoryEntity) {
        stories.add(storyEntity)
    }

    override fun getStories(): PagingSource<Int, StoryEntity> {
        return FakePagingSource(stories)
    }

    override fun deleteAll() {
        stories.clear()
    }

    class FakePagingSource(private val data: MutableList<StoryEntity>) :
        PagingSource<Int, StoryEntity>() {
        @Suppress("SameReturnValue")
        override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int = 0

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
            return LoadResult.Page(data, 0, 1)
        }

    }
}