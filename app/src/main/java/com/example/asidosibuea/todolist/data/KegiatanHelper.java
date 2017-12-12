package com.example.asidosibuea.todolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asidosibuea.todolist.data.KegiatanKontrak.KegiatanEntry;

/**
 * Created by asidosibuea on 04/12/17.
 */

public class KegiatanHelper extends SQLiteOpenHelper {

    private static final String NAMA_DB = "todo.db";
    private static final int VERSION_DB = 2;

    public KegiatanHelper(Context context) {
        super(context, NAMA_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_SYNTAX = "CREATE TABLE "+KegiatanEntry.NAMA_TABEL+" (\n" +
                KegiatanEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                KegiatanEntry.KOLOM_KEGIATAN+" TEXT NOT NULL,\n" +
                KegiatanEntry.KOLOM_WAKTU+" TEXT NOT NULL\n" +
                ");";
        db.execSQL(CREATE_SYNTAX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP TABLE IF EXISTS "+KegiatanEntry.NAMA_TABEL+";";
        db.execSQL(drop);
        onCreate(db);
    }
}
