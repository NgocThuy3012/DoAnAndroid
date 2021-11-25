package vn.edu.stu.doangk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import vn.edu.stu.doangk.model.Lop;
import vn.edu.stu.doangk.model.SinhVien;

public class DanhSachLopActivity extends AppCompatActivity {
    ListView lvLop;
    ArrayList<Lop> dsLop;
    ArrayAdapter<Lop> adapterLop;
    int requestCode = 113, resultCode = 115;
    Lop chon;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbSinhVien.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_lop);
        addControls();
        addEvents();
        docDSLop();
    }

    private void docDSLop() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("select * from Lop", null);
        adapterLop.clear();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String ten = cursor.getString(1);
            adapterLop.add(new Lop(id, ten));
        }
        cursor.close();
        database.close();
        adapterLop.notifyDataSetChanged();
    }


    private void addEvents() {
        lvLop.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachLopActivity.this);
                    builder.setMessage(R.string.txtDelet);
                    builder.setPositiveButton(R.string.txtCancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Lop l = adapterLop.getItem(position);
                            SQLiteDatabase database = openOrCreateDatabase(
                                    DB_NAME,
                                    MODE_PRIVATE,
                                    null
                            );
                            int delete = database.delete(
                                    "Lop",
                                    "id=?",
                                    new String[]{l.getId()}
                            );
                            int deleteSV = database.delete(
                                    "Sinhvien",
                                    "lop=?",
                                    new String[]{l.getId()}
                            );
                            database.close();
                            docDSLop();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
                return true;
            }
        });
        lvLop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < adapterLop.getCount()) {
                    Intent intent = new Intent(DanhSachLopActivity.this, ThongTinLopActivity.class);
                    chon = adapterLop.getItem(position);
                    intent.putExtra("CHON", chon);
                    startActivityForResult(intent, requestCode);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (resultCode == this.resultCode) {
                if (data.hasExtra("TRA")) {
                    Lop tra = (Lop) data.getSerializableExtra("TRA");
                    if (chon != null) {
                        chon.setId(tra.getId());
                        chon.setTen(tra.getTen());
                        SQLiteDatabase database = openOrCreateDatabase(
                                DB_NAME,
                                MODE_PRIVATE,
                                null
                        );
                        ContentValues row = new ContentValues();
                        row.put("id", chon.getId());
                        row.put("ten", chon.getTen());
                        int update = database.update(
                                "Lop",
                                row,
                                "id=?",
                                new String[]{chon.getId()}
                        );
                        database.close();
                        docDSLop();
                    }
                } else if (data.hasExtra("THEM")) {
                    Lop l = (Lop) data.getSerializableExtra("THEM");
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    ContentValues row = new ContentValues();
                    row.put("id", l.getId());
                    row.put("ten", l.getTen());
                    long insert = database.insert(
                            "Lop",
                            null,
                            row
                    );
                    database.close();
                    docDSLop();
//                    dsLop.add(l);
//                    adapterLop.notifyDataSetChanged();
                }
            }
        }
        chon = null;
    }


    private void addControls() {
        lvLop = findViewById(R.id.lvLop);
        dsLop = new ArrayList<>();
        adapterLop = new ArrayAdapter<>(
                DanhSachLopActivity.this,
                android.R.layout.simple_list_item_1

        );
        lvLop.setAdapter(adapterLop);
        chon = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAbout:
                Intent intent = new Intent(DanhSachLopActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuExit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}