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
import java.util.Random;

import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.CheckBox;

public class FacturaActivity extends AppCompatActivity {
    TextView TextNumFactura,TextModelo,TextMarca,TextPrecio;
    EditText TextPlaca,txtFecha;
    CheckBox FacActiva;
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
        FacActiva=findViewById(R.id.CheckActiva);

        Random r = new Random();
        int randomNumber = r.nextInt(100);
        TextNumFactura.setText(String.valueOf(randomNumber));
    }
     public void Guardar(View view) {
          Factura = TextNumFactura.getText().toString();
          fecha = txtFecha.getText().toString();
          placa = TextPlaca.getText().toString();
          if (placa.isEmpty()&& fecha.isEmpty()){
              Toast.makeText(this, "La placa y la fecha son necesarias", Toast.LENGTH_SHORT).show();
              TextPlaca.requestFocus();
          } else {
              SQLiteDatabase db = admin.getWritableDatabase();
              ContentValues registro = new ContentValues();
              registro.put("nro_factura", Factura);
              registro.put("fecha", fecha);
              registro.put("placa", placa);
              resp = db.insert("TblFactura", null, registro);
              if (resp > 0) {
                  Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                  Limpiar_campos();
              } else {
                  Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
              }

              db.close();
          }
      }

    public void Buscar (View view){
        placa=TextPlaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida para consultar", Toast.LENGTH_SHORT).show();
            TextPlaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("Select * from TblVehiculo where placa='" + placa + "'",null);
            if (fila.moveToNext()){
                Cursor activo=db.rawQuery("Select * from TblVehiculo where placa='" + placa + "'and activo = 'si'",null);
                if (activo.moveToNext()){
                    TextMarca.setText(fila.getString(1));
                    TextModelo.setText(fila.getString(2));
                    TextPrecio.setText(fila.getString(3));

                }
                else{
                    Toast.makeText(this, "El auto se encuentra inactivo", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();
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
            Cursor fila=db.rawQuery("Select * from TblFactura where placa='" + placa + "'",null);
            if (fila.moveToNext()){
                Cursor activo=db.rawQuery("Select * from TblFactura where placa='" + placa + "'and activo = 'si'",null);
                if (activo.moveToNext()){
                    TextNumFactura.setText(fila.getString(0));
                    txtFecha.setText(fila.getString(1));
                    TextPlaca.setText(fila.getString(2));
                    if (fila.getString(3).equals("si")){
                        FacActiva.setChecked(true);}
                    else{
                        FacActiva.setChecked(false);}
                    sw=1;
                    Buscar(view);
                }
                else{
                    Toast.makeText(this, "La factura esta inactiva", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();}
            db.close();
        }
    }

    public void Anular(View view){
        if (sw == 1){
            placa=TextPlaca.getText().toString();
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=db.update("TblFactura",registro,"placa='" + placa + "'",null);
            if (resp > 0){
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
            db.close();
        }
        else
            Toast.makeText(this, "Debe primero consultar", Toast.LENGTH_SHORT).show();
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
