package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Products : IntIdTable() {
//    val id = integer("id").autoIncrement() // Column<Int>
    val title = varchar("title", 50) // Column<String>
    val price = integer("price") // Column<Int>
    val categoryID = integer("categoryID").references(Categories.id) // Column<Int>
//
//    override val primaryKey = PrimaryKey(id, name = "PK_Products_ID")
}

class Product(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)

    var title by Products.title
    var price by Products.price
    var categoryID by Products.categoryID

    fun toData(): ProductJson {
        return ProductJson(title, price, categoryID)
    }
}

@Serializable
data class ProductJson(var title: String, var price: Int, var categoryID: Int) {
    constructor(prod: Product) : this(prod.title, prod.price, prod.categoryID)
}