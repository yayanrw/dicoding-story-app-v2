package com.heyproject.storyapp.domain.model

import com.heyproject.storyapp.data.local.entity.StoryEntity
import com.heyproject.storyapp.data.remote.dto.StoryDto

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 14:59
Github : https://github.com/yayanrw
 **/

data class Story(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Double?,
    val id: String,
    val lat: Double?
)

fun StoryDto.toEntity(): StoryEntity {
    return StoryEntity(
        photoUrl = photoUrl,
        createdAt = createdAt,
        name = name,
        description = description,
        lon = lon,
        id = id,
        lat = lat
    )
}