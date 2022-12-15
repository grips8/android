package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Categories : IntIdTable() {
    val _id = varchar("_id", 48)
    val name = varchar("name", 50) // Column<String>
}


class Category(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Category>(Categories)

    var _id by Categories._id
    var name by Categories.name

    fun toData(): CategoryJson {
        return CategoryJson(_id, name)
    }
}

@Serializable
data class CategoryJson(var _id: String, var name: String) {
    constructor(cat: Category) : this(cat._id, cat.name)
}