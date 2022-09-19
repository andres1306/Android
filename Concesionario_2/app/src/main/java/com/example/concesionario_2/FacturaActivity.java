package com.example.concesionario_2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import java.lang.String;
import android.widget.Toast;
import android.widget.TextView;

public class FacturaActivity extends AppCompatActivity {
    TextView TextNumFactura,txtFecha,TextModelo,TextMarca,TextPrecio,TextPlaca;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);
    String placa,marca,modelo,valor,fecha,Factura;
    byte sw;
    long resp;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        getSupportActionBar().hide();
        TextNumFactura=findViewById(R.id.TextNumFactura);
        txtFecha=findViewById(R.id.txtFecha);
        TextModelo=findViewById(R.id.TextModelo);
        TextMarca=findViewById(R.id.TextMarca);
        TextPrecio=findViewById(R.id.TextPrecio);
        TextPlaca=findViewById(R.id.TextPlaca);
    }
    public void Guardar(View view) {
        placa = TextPlaca.getText().toString();
        marca = TextMarca.getText().toString();
        modelo = TextModelo.getText().toString();
        valor = TextPrecio.getText().toString();
        fecha = txtFecha.getText().toString();
        Factura = TextNumFactura.getText().toString();
        if (placa.isEmpty()) {
            Toast.makeText(this, "La placa es necesaria ", Toast.LENGTH_SHORT).show();
            TextPlaca.requestFocus();
        } else {
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("placa", placa);
            registro.put("marca", marca);
            registro.put("modelo", modelo);+
            registro.put("fecha", fecha);
            registro.put("valor", Integer.parseInt(valor));
            resp = db.insert("TblFactura", null, registro);
            if (resp > 0) {
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            } else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            db.close();
        }


    }
    public void Consultar(View view){
        placa=TextPlaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida para consultar", Toast.LENGTH_SHORT).show();
            TextPlaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("Select * from TblVehiculo where placa='" + placa + "'",null);
            if (fila.moveToNext()){
                sw=1;
                TextMarca.setText(fila.getString(1));
                TextModelo.setText(fila.getString(2));
                TextPrecio.setText(fila.getString(3));
            }
            else
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();
            db.close();
        }

    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }
    private void Limpiar_campos(){
        TextNumFactura.setText("");
        txtFecha.setText("");
        TextModelo.setText("");
        TextPrecio.setText("");
        txtFecha.setText("");
        TextNumFactura.requestFocus();
    }
    }
