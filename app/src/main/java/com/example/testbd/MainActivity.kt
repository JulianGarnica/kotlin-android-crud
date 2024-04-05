package com.example.testbd

import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtCodigo = findViewById<EditText>(R.id.txtCodigo)
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnInsertar = findViewById<Button>(R.id.btnInsertar)
        val btnEditar = findViewById<Button>(R.id.btnEditar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        val listItems = findViewById<ListView>(R.id.listItems)



        val admin = OpenHelper(this, "bdEstudiante", null, 1)



        listItems.adapter = buscarTabla(admin)

        /*
        Insertar listener
         */

        btnInsertar.setOnClickListener {
            val baseDeDatos = admin.writableDatabase

            val codigo = txtCodigo.text.toString()
            val nombre = txtNombre.text.toString()
            val telefono = txtTelefono.text.toString()

            if(!codigo.isEmpty() && !nombre.isEmpty() && !telefono.isEmpty()){
                val registro = ContentValues()
                registro.put("codigo", codigo)
                registro.put("nombre", nombre)
                registro.put("telefono", telefono)

                baseDeDatos.insert("estudiante", null, registro)
                baseDeDatos.close()
                Toast.makeText(applicationContext, "¡Creado con exito!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Hay datos sin registrar!", Toast.LENGTH_SHORT).show()
            }

            listItems.adapter = buscarTabla(admin)
        }

        /*
        Buscar listener
         */

        btnBuscar.setOnClickListener {
            val baseDeDatos = admin.writableDatabase

            val codigo = txtCodigo.text.toString()
    //:
            if(!codigo.isEmpty()){
                val dato: Cursor
                try {
                    dato = baseDeDatos.rawQuery(
                        "SELECT nombre, telefono FROM estudiante WHERE codigo = '$codigo'",
                        null
                    )

                    if (dato.moveToFirst()) {
                        txtNombre.setText(dato.getString(0))
                        txtTelefono.setText(dato.getString(1))
                        baseDeDatos.close()


                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Dato no existe unu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } finally {
                    listItems.adapter = buscarTabla(admin, codigo)
                }
            }else{
                Toast.makeText(applicationContext, "Ingresa un registro!", Toast.LENGTH_SHORT).show()
                txtNombre.setText("")
                txtTelefono.setText("")
                listItems.adapter = buscarTabla(admin)
            }
        }

        /*
        Eliminar listener
         */

        btnEliminar.setOnClickListener {
            val baseDeDatos = admin.writableDatabase

            val codigo = txtCodigo.text.toString()
            if(!codigo.isEmpty()){
                try {
                    val op = baseDeDatos.delete(
                        "estudiante",
                        "codigo = ${codigo}",
                        null
                    )
                    if(op > 0) {
                        Toast.makeText(
                            applicationContext,
                            "¡Eliminado con exito!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Error Eliminando, Dato no existe unu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch(e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Error Eliminando, Dato no existe unu!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(applicationContext, "Ingresa un registro!", Toast.LENGTH_SHORT).show()
            }
            listItems.adapter = buscarTabla(admin)
        }

        /*
        Editar listener
         */

        btnEditar.setOnClickListener {
            val baseDeDatos = admin.writableDatabase

            val codigo = txtCodigo.text.toString()
            val nombre = txtNombre.text.toString()
            val telefono = txtTelefono.text.toString()
            //:
            if(!codigo.isEmpty()){
                val registro = ContentValues()
                registro.put("nombre", nombre)
                registro.put("telefono", telefono)
                try {
                    val op = baseDeDatos.update(
                        "estudiante",
                        registro,
                        "codigo = ${codigo}",
                        null
                    )
                    if(op > 0) {
                        Toast.makeText(
                            applicationContext,
                            "¡Editado con exito!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Error editando, Dato no existe unu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch(e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Error editando, Dato no existe unu!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(applicationContext, "Ingresa un registro!", Toast.LENGTH_SHORT).show()
            }
            listItems.adapter = buscarTabla(admin)
        }
    }

    fun buscarTabla(admin: OpenHelper, datoQuery: String = ""): ListAdapter? {
        val dato: Cursor
        val baseDeDatos = admin.writableDatabase
        var users = arrayOf<String>()

        if(datoQuery.isNullOrBlank()){
            dato = baseDeDatos.rawQuery(
                "SELECT codigo, nombre, telefono FROM estudiante",
                null
            )
            while (dato.moveToNext()) {
                users += "${dato.getString(0)} - ${dato.getString(1)} - ${dato.getString(2)}"
            }
        }else{
            dato = baseDeDatos.rawQuery(
                "SELECT codigo, nombre, telefono FROM estudiante WHERE codigo LIKE '%${datoQuery}%' OR nombre LIKE '%${datoQuery}%' OR telefono LIKE '%${datoQuery}%'",
                null
            )
            while (dato.moveToNext()) {
                users += "${dato.getString(0)} - ${dato.getString(1)} - ${dato.getString(2)}"
            }
        }

        if(users.isEmpty()){
            users += "No hay datos registrados."
        }

        baseDeDatos.close()

        return ArrayAdapter(this,
            android.R.layout.simple_list_item_1, users)

    }
}