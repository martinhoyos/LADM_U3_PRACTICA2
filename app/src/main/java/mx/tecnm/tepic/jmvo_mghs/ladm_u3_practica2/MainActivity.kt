package mx.tecnm.tepic.jmvo_mghs.ladm_u3_practica2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var baseDatos = FirebaseFirestore.getInstance()
    var idNotas = ArrayList<Int>()
    var chosen = 0
    var actualizar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mostrarNotas()

        btnagregar.setOnClickListener{
            val notita = Nota(this)

            notita.nombre = editTitulo.text.toString()
            notita.contenido = editContenido.text.toString()

            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            val fechaCompleta = sdf.format(Date())
            val puraHora = fechaCompleta.subSequence(11,19).toString()
            val puraFecha = fechaCompleta.subSequence(0,10).toString()

            notita.fecha = puraFecha
            notita.hora = puraHora

            if(!actualizar){
                val resultadoAgregar = notita.insertar()
                if(resultadoAgregar){
                    Toast.makeText(this,"SE HA AGREGADO LA NOTA", Toast.LENGTH_LONG).show()
                    borrarCampos()
                    mostrarNotas()
                } else {
                    Toast.makeText(this,"NO SE PUDO AGREGAR LA NOTA", Toast.LENGTH_LONG).show()
                }
            }else {
                Toast.makeText(this, "ACTUALMENTE SE ESTÁ EDITANDO UNA NOTA\nPORFAVOR PRESIONE EL BOTON DE EDITAR", Toast.LENGTH_LONG).show()
            }
            mostrarNotas()
        }

        btneditar.setOnClickListener{

            val notita = Nota(this)

            notita.nombre = editTitulo.text.toString()
            notita.contenido = editContenido.text.toString()

            val sdf = SimpleDateFormat("yyyy/mm/dd hh:mm:ss")
            val fechaCompleta = sdf.format(Date())
            val puraHora = fechaCompleta.subSequence(11,19).toString()
            val puraFecha = fechaCompleta.subSequence(0,10).toString()

            notita.fecha = puraFecha
            notita.hora = puraHora

            if(actualizar){
                val resActualizar = notita.actualizar(chosen.toString())
                actualizar = false

                if(resActualizar){
                    Toast.makeText(this,"SE ACTUALIZÓ LA NOTA CON ÉXITO",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"NO SE PUDO ACTUALIZAR LA NOTA",Toast.LENGTH_LONG).show()
                }
                borrarCampos()
            } else {
                Toast.makeText(this, "NO SE ESTÁ EDITANDO NINGUNA NOTA\nPORFAVOR PRESIONE EL BOTON DE GUARDAR", Toast.LENGTH_LONG).show()
            }
            mostrarNotas()
        }

        btnborrar.setOnClickListener{
            borrarCampos()
        }

        btnSincronizar.setOnClickListener{
            insertarFireBase()
        }

    }

    private fun insertarFireBase() {

        idNotas.clear()
        idNotas = Nota(this).obtenerIDS()

        for(i in 0 .. (idNotas.size-1)){
            val notitas = Nota(this).consultar(idNotas[i].toString())
            var datosAInsertar = hashMapOf(
                "TITULO" to notitas.nombre,
                "CONTENIDO" to notitas.contenido,
                "HORA" to notitas.hora,
                "FECHA" to notitas.fecha
            )

            baseDatos.collection("Nota").add(datosAInsertar as Any)
        }

        Toast.makeText(this, "SE HAN SUBIDO LOS DATOS A LA NUBE", Toast.LENGTH_LONG).show()

    }

    fun borrarCampos(){
        editTitulo.setText("")
        editContenido.setText("")
        chosen = 0
        actualizar = false
    }

    fun activarEvento(ListaNotas : ListView){
        ListaNotas.setOnItemClickListener{adapterView, view, i, l->
            chosen = idNotas[i]
            AlertDialog.Builder(this)
                .setTitle("ATENCIÓN")
                .setMessage("¿QUÉ DESEAS HACER CON LA NOTA?")
                .setPositiveButton("EDITAR"){d,i->editarNota()}
                .setNegativeButton("ELIMINAR"){d,i->borrarNota()}
                .setNeutralButton("CANCELAR"){d,i->d.cancel()}
                .show()
        }
    }

    fun mostrarNotas(){
        val arrayNotas = Nota(this).consultar()
        listaNotas.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayNotas)

        idNotas.clear()
        idNotas = Nota(this).obtenerIDS()
        activarEvento(listaNotas)
    }

    fun borrarNota(){
        AlertDialog.Builder(this)
            .setTitle("ATENCIÓN")
            .setMessage("¿ESTÁ SEGURO DE BORRAR LA NOTA ${chosen}?")
            .setPositiveButton("SI"){d,i->
                val resultado = Nota(this).eliminar(chosen)

                if(resultado){
                    Toast.makeText(this,"SE ELIMINÓ LA NOTA",Toast.LENGTH_LONG).show()
                    mostrarNotas()
                } else {
                    Toast.makeText(this,"NO SE PUDO ELIMINAR LA NOTA", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("NO"){d,i->d.cancel()}
            .show()
    }

    fun editarNota(){
        actualizar = true

        val notaLoca = Nota(this).consultar(chosen.toString())

        editTitulo.setText(notaLoca.nombre)
        editContenido.setText(notaLoca.contenido)

    }

}