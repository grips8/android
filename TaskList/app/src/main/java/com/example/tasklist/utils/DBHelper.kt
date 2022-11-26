package com.example.tasklist.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tasklist.models.DataTask

class DBHelper(context: Context) : SQLiteOpenHelper(context, "TasksDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE tasks (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT," +
                    "description TEXT," +
                    "date TEXT," +
                    "status TEXT" +
                    ")")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun insertTask(dataTask: DataTask) {
        val db: SQLiteDatabase = writableDatabase
        val contentValues: ContentValues = ContentValues().apply {
            put("name", dataTask.name)
            put("description", dataTask.desc)
            put("date", dataTask.date)
            put("status", dataTask.status)
        }
        db.insert("tasks", null, contentValues)
    }

    fun deleteTask(position: Int) {
        val db: SQLiteDatabase = writableDatabase
        db.delete("tasks","id = ?", arrayOf(position.toString()))
    }

    fun getAllTasks() : MutableList<DataTask> {
        val res: MutableList<DataTask> = mutableListOf()
        val db: SQLiteDatabase = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM tasks", null)
        with(cursor) {
            while (moveToNext()) {
                res.add(
                    DataTask(
                        getInt(getColumnIndexOrThrow("id")),
                        getString(getColumnIndexOrThrow("name")),
                        getString(getColumnIndexOrThrow("description")),
                        getString(getColumnIndexOrThrow("date")),
                        getString(getColumnIndexOrThrow("status"))
                    )
                )
            }
        }
        cursor.close()
        return res
    }
}