package com.example.testbd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OpenHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    :SQLiteOpenHelper(context, name, factory, version)
{
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE estudiante(codigo int PRIMARY KEY, nombre varchar, telefono int)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}