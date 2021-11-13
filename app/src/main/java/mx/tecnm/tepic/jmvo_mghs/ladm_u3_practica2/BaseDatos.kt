package mx.tecnm.tepic.jmvo_mghs.ladm_u3_practica2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) :SQLiteOpenHelper(context, name, factory, version){
    override fun onCreate(p:SQLiteDatabase){
        p.execSQL("CREATE TABLE NOTA(IDNOTA INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, TITULO VARCHAR(254), CONTENIDO TEXT, HORA TIME, FECHA DATE)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}