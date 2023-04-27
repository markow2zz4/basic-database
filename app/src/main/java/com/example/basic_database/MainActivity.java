package com.example.basic_database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonAdd, buttonDelete, buttonClear, buttonRead, buttonUpdate;
    EditText eID, eName, eMail; DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.add_btn);
        buttonAdd.setOnClickListener(this);

        buttonRead = (Button) findViewById(R.id.read_btn);
        buttonRead.setOnClickListener(this);

        buttonClear = (Button) findViewById(R.id.clear_btn);
        buttonClear.setOnClickListener(this);

        buttonUpdate = (Button) findViewById(R.id.update_btn);
        buttonUpdate.setOnClickListener(this);

        buttonDelete = (Button) findViewById(R.id.delete_btn);
        buttonDelete.setOnClickListener(this);

        eID = (EditText) findViewById(R.id.id_entry);
        eName = (EditText) findViewById(R.id.name_entry);
        eMail = (EditText) findViewById(R.id.mail_entry);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        String ID = eID.getText().toString();
        String name = eName.getText().toString();
        String email = eMail.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); // класс для добавления новых строк в таблицу

        switch (v.getId()) {
            case R.id.add_btn:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, email);
                database.insert(DBHelper.TABLE_PERSONS, null, contentValues);
                break;

            case R.id.read_btn:
                Cursor cursor = database.query(DBHelper.TABLE_PERSONS, null, null, null,
                        null, null, null); // все поля без сортировки и группировки

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                    do {
                        Log.d("mLog", "ID =" + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", email = " + cursor.getString(emailIndex));

                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog", "0 rows");

                cursor.close(); // освобождение памяти
                break;

            case R.id.clear_btn:
                database.delete(DBHelper.TABLE_PERSONS, null, null);
                break;

            case R.id.delete_btn:
                if (ID.equalsIgnoreCase("")) {
                    break;
                }
                int delCount = database.delete(DBHelper.TABLE_PERSONS, DBHelper.KEY_ID + "= " + ID, null);
                Log.d("mLog", "Удалено строк = " + delCount);

            case R.id.update_btn:
                if (ID.equalsIgnoreCase("")) {
                    break;
                }
                contentValues.put(DBHelper.KEY_MAIL, email);
                contentValues.put(DBHelper.KEY_NAME, name);
                int updCount = database.update(DBHelper.TABLE_PERSONS, contentValues, DBHelper.KEY_ID + "= ?", new String[]{ID});
                Log.d("mLog", "Обновлено строк = " + updCount);
        }
        dbHelper.close(); // закрываем соединение с БД
    }

}