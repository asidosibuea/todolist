package com.example.asidosibuea.todolist;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.asidosibuea.todolist.data.KegiatanHelper;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.asidosibuea.todolist.data.KegiatanKontrak;
import com.example.asidosibuea.todolist.data.KegiatanKontrak.KegiatanEntry;

public class MainActivity extends AppCompatActivity {
//    private TextView tvTimeResult;
    String waktu;
    private Button btTimePicker;
    private TimePickerDialog timePickerDialog;
    EditText edt_kegiatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_kegiatan = (EditText) findViewById(R.id.kegiatan);

//        tvTimeResult = (TextView) findViewById(R.id.tv_timeresult);
        btTimePicker = (Button) findViewById(R.id.bt_showtimepicker);
        btTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

    }

    private void showTimeDialog() {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
//                tvTimeResult.setText(hourOfDay+":"+minute);
                waktu=hourOfDay+":"+minute;
            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    public void addToDo(View view){
        EditText edt_kegiatan = (EditText) findViewById(R.id.kegiatan);

        String kegiatan = edt_kegiatan.getText().toString();

//  bentuk objek content values -- menngirim data yang akan dimasukin ke tabel
        ContentValues contentValues = new ContentValues();
        contentValues.put(KegiatanEntry.KOLOM_KEGIATAN, kegiatan);
        contentValues.put(KegiatanEntry.KOLOM_WAKTU, waktu);

        KegiatanHelper kegiatanHelper = new KegiatanHelper(this);
        SQLiteDatabase db = kegiatanHelper.getWritableDatabase();
        long result = db.insert(KegiatanEntry.NAMA_TABEL,null,contentValues);
        if (result == -1){
            Toast.makeText(this, "Can't insert to database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Inserted to database, with id : "+result, Toast.LENGTH_SHORT).show();
        }
//
//        Intent intent = new Intent(this, Main2Activity.class);
//        startActivity(intent);
        tampilData();
    }

    public void tampilData(){
        // ambil data dari database --> cursor
        KegiatanHelper kegiatanHelper = new KegiatanHelper(this);
        SQLiteDatabase sqLiteDatabase = kegiatanHelper.getReadableDatabase();

        String[] namaKolom = {
                KegiatanEntry.KOLOM_KEGIATAN,
                KegiatanEntry.KOLOM_WAKTU
        };

        Cursor cursor = sqLiteDatabase.query(KegiatanEntry.NAMA_TABEL,
                namaKolom,
                null,
                null,
                null,
                null,
                null
        );

        //parsing data --> String arrayList<Person>
        ArrayList<Kegiatan> listKegiatan = new ArrayList<>();
        while (cursor.moveToNext()){
            int idx_kegiatan = cursor.getColumnIndex(KegiatanEntry.KOLOM_KEGIATAN);
            String kegiatan = cursor.getString(idx_kegiatan);

            int idx_waktu = cursor.getColumnIndex(KegiatanEntry.KOLOM_WAKTU);
            String waktu = cursor.getString(idx_waktu);


            // buat object person
            Kegiatan kegiatan1 = new Kegiatan(kegiatan, waktu);

            //masukkan ke dalam array list
            listKegiatan.add(kegiatan1);
        }

        cursor.close();

        //tampilkan data di tempat yang disediakan --> Listview
        ListView listView = (ListView) findViewById(R.id.lv_kegiatan);
        KegiatanAdapter adapter = new KegiatanAdapter(this, listKegiatan);
        listView.setAdapter(adapter);
    }

    public void deleteToDo(View view) {
        View v = (View) view.getParent();
        TextView tvKegiatan = (TextView) v.findViewById(R.id.kegiatan);
        String kegiatan = tvKegiatan.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                KegiatanEntry.NAMA_TABEL, KegiatanEntry.KOLOM_KEGIATAN, kegiatan);


        KegiatanHelper kegiatanHelper = new KegiatanHelper(MainActivity.this);
        SQLiteDatabase sqlDB = kegiatanHelper.getWritableDatabase();
        sqlDB.execSQL(sql);
        tampilData();
    }

    public void editToDo (View view) {
        View u = (View) view.getParent();
        TextView taskUpdateView = (TextView) u.findViewById(R.id.kegiatan);
        final int id = u.getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Data");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String update = edt_kegiatan.getText().toString();

                KegiatanHelper kegiatanHelper = new KegiatanHelper(MainActivity.this);
                SQLiteDatabase db = kegiatanHelper.getWritableDatabase();
                //db.execSQL(sql);
                ContentValues values = new ContentValues();

                values.remove(KegiatanEntry.KOLOM_KEGIATAN);
//                values.put(TaskContract.Columns._ID, id);
                values.put(KegiatanEntry.KOLOM_KEGIATAN, update);
                db.replace(KegiatanEntry.NAMA_TABEL, null, values);
                tampilData();
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }
}
