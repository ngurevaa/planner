package ru.kpfu.itis.gureva.feature.home.impl.data.mapper

import ru.kpfu.itis.gureva.core.database.entity.GroupEntity
import ru.kpfu.itis.gureva.feature.home.api.model.Group

fun List<GroupEntity>.mapList(): List<Group> = map {
    it.toGroup()
}

fun GroupEntity.toGroup(): Group = Group(
    id = id,
    name = name
)
