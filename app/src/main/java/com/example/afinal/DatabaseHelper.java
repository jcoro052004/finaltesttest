package com.example.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notaria.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "citas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOTARIO = "notario";
    private static final String COLUMN_SALA = "sala";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_DESCRIPCION = "descripcion";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOTARIO + " TEXT, " +
            COLUMN_SALA + " TEXT, " +
            COLUMN_FECHA + " TEXT, " +
            COLUMN_HORA + " TEXT, " +
            COLUMN_DESCRIPCION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Afegir una nova cita
    public boolean addCita(String notario, String sala, String fecha, String hora, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTARIO, notario);
        values.put(COLUMN_SALA, sala);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_HORA, hora);
        values.put(COLUMN_DESCRIPCION, descripcion);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    // Comprovar si la sala està disponible per una cita
    public boolean isSalaDisponible(String sala, String fecha, String hora) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_SALA + " = ? AND " +
                        COLUMN_FECHA + " = ? AND " +
                        COLUMN_HORA + " = ?",
                new String[]{sala, fecha, hora});

        boolean isAvailable = cursor.getCount() == 0; // Si no hi ha resultats, la sala està disponible
        cursor.close();
        db.close();
        return isAvailable;
    }

    // Obtener totes les cites
    public List<Map<String, String>> getAllCitas() {
        List<Map<String, String>> citasList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> cita = new HashMap<>();
                cita.put("id", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                cita.put("notario", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTARIO)));
                cita.put("sala", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SALA)));
                cita.put("fecha", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)));
                cita.put("hora", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)));
                cita.put("descripcion", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)));
                citasList.add(cita);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return citasList;
    }

    // Eliminar una cita per ID
    public boolean deleteCita(int citaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(citaId)});
        db.close();
        return rowsDeleted > 0; // Retorna true si s'ha eliminat alguna fila
    }
}