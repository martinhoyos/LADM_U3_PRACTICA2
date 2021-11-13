package mx.tecnm.tepic.jmvo_mghs.ladm_u3_practica2

import android.content.ContentValues
import android.content.Context

class Nota(p: Context) {
    var nombre = ""
    var contenido = ""
    var fecha = ""
    val pnt = p
    var hora = ""

    fun insertar() : Boolean{
        val tablaNota = BaseDatos(pnt, "NOTASU3",null,1).writableDatabase
        val datos = ContentValues()

        datos.put("TITULO",nombre)
        datos.put("CONTENIDO",contenido)
        datos.put("HORA",hora)
        datos.put("Fecha",fecha)

        val resultado = tablaNota.insert("NOTA", null, datos)
        if(resultado == -1L){
            return false
        }
        return true
    }

    fun consultar() : ArrayList<String>{
        val tablaNota = BaseDatos(pnt,"NOTASU3",null,1).readableDatabase
        val resultado = ArrayList<String>()
        val cursor = tablaNota.query("NOTA",arrayOf("*"),null,null,null,null,null)

        if(cursor.moveToFirst()){
            do{
                var dato = "TITULO: "+cursor.getString(1)+"\nCONTENIDO: "+cursor.getString(2) +
                "\nHORA: "+cursor.getString(3)+"\nFECHA: "+cursor.getString(4)
                resultado.add(dato)
            }while (cursor.moveToNext())
        } else {
            resultado.add("NO SE ENCONTRARON NOTAS TODAVÍA")
        }
        return resultado
    }

    fun consultar(idABuscar:String) : Nota{
        val tablaNota = BaseDatos(pnt,"NOTASU3",null,1).readableDatabase
        val cursor = tablaNota.query("NOTA",arrayOf("*"),"IDNOTA=?", arrayOf(idABuscar),null,null,null,null)
        val nota = Nota(MainActivity())

        if(cursor.moveToFirst()){
            nota.nombre = cursor.getString(1)
            nota.contenido = cursor.getString(2)
            nota.fecha = cursor.getString(4)
            nota.hora = cursor.getString(3)
        }
        return nota
    }

    fun eliminar(idEliminar:Int):Boolean{
        val tablaNota = BaseDatos(pnt,"NOTASU3", null, 1).writableDatabase
        val resultado = tablaNota.delete("NOTA","IDNOTA=?", arrayOf(idEliminar.toString()))

        if(resultado == 0) return false

        return true
    }

    fun actualizar(idActualizar:String):Boolean{
        val tablaNota = BaseDatos(pnt, "NOTASU3",null,1).readableDatabase
        val datos = ContentValues()

        datos.put("TITULO",nombre)
        datos.put("CONTENIDO",contenido)
        datos.put("FECHA",fecha)
        datos.put("HORA",hora)

        val resultado = tablaNota.update("NOTA",datos,"IDNOTA=?", arrayOf(idActualizar))

        if(resultado == 0) return false

        return true
    }

    fun obtenerIDS(): ArrayList<Int> {
        val tablaNota = BaseDatos(pnt,"NOTASU3",null,1).readableDatabase
        val resultado = ArrayList<Int>()
        val cursor = tablaNota.query("NOTA", arrayOf("*"), null,null,null,null,null)

        if(cursor.moveToFirst()){
            do{
                resultado.add(cursor.getInt(0))
            }while(cursor.moveToNext())
        }
        return resultado
    }
}