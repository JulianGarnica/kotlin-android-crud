package com.example.testbd

import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        val admin = OpenHelper(this, "bdEstudiante", null, 1)
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
        }

        btnBuscar.setOnClickListener {
            val baseDeDatos = admin.writableDatabase

            val codigo = txtCodigo.text.toString()
    //:
            if(!codigo.isEmpty()){
                val dato: Cursor
                dato = baseDeDatos.rawQuery(
                    "SELECT nombre, telefono FROM estudiante WHERE codigo = $codigo",
                    null
                )

                if(dato.moveToFirst()){
                    txtNombre.setText(dato.getString(0))
                    txtTelefono.setText(dato.getString(1))
                    baseDeDatos.close()
                }else{
                    Toast.makeText(applicationContext, "Dato no existe unu!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext, "Ingresa un registro!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}