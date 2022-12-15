package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction


object Products : IntIdTable() {
//    val id = integer("id").autoIncrement() // Column<Int>
    val _id = varchar("_id", 48)
    val name = varchar("title", 256) // Column<String>
    val price = double("price") // Column<Int>
    val description = varchar("description", 1024)
    val categoryID = integer("categoryID").references(Categories.id) // Column<Int>
//
//    override val primaryKey = PrimaryKey(id, name = "PK_Products_ID")
}

class Product(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)

    var _id by Products._id
    var name by Products.name
    var price by Products.price
    var description by Products.description
    var categoryID by Products.categoryID
}

@Serializable
data class ProductJson(var _id: String, var name: String, var price: Double, var description: String, var category: CategoryJson?) {
    constructor(prod: Product) : this(
        prod._id,
        prod.name,
        prod.price,
        prod.description,
        CategoryJson( transaction { Category.findById(prod.categoryID)!! })     // dzikie 8)
    )
}