package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Categories : IntIdTable() {
    //    val id = integer("id").autoIncrement() // Column<Int>
    val name = varchar("name", 50) // Column<String>
    val tax = integer("tax") // Column<Int>
    val exclusiveness = bool("exclusiveness") // Column<Boolean>
//
//    override val primaryKey = PrimaryKey(id, name = "PK_Products_ID")
}


class Category(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Category>(Categories)

    var name by Categories.name
    var tax by Categories.tax
    var exclusiveness by Categories.exclusiveness

    fun toData(): CategoryJson {
        return CategoryJson(name, tax, exclusiveness)
    }
}

@Serializable
data class CategoryJson(var name: String, var tax: Int, var exclusiveness: Boolean) {
    constructor(cat: Category) : this(cat.name, cat.tax, cat.exclusiveness)
}