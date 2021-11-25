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
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import vn.edu.stu.doangk.adapter.SinhVienAdapter;
import vn.edu.stu.doangk.model.Lop;
import vn.edu.stu.doangk.model.SinhVien;

public class DanhSachTatCaSVActivity extends AppCompatActivity {
    ListView lvSv;
    ArrayList<SinhVien> dsSV;
    SinhVienAdapter adapter;
    SinhVien chon;
    int requestCode = 113, resultCode = 115;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbSinhVien.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_tat_ca_svactivity);
        addControls();
        addEvents();

        docDSSv();
    }

    private void docDSSv() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("select * from Sinhvien", null);

        adapter.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String hoten = cursor.getString(1);
            int namsinh = cursor.getInt(2);
            String que = cursor.getString(3);
            String gioitinh = cursor.getString(4);
            byte[] anh = cursor.getBlob(5);
            String lop = selectTenLop(cursor.getString(6));

            dsSV.add(new SinhVien(hoten, ma, namsinh, que, gioitinh, anh, lop));

        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    private String selectTenLop(String ma) {
        String ten = "";
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor1 = database.rawQuery("select * from Lop", null);
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            String malop = cursor1.getString(0);
            if (malop.equals(ma))
                ten = cursor1.getString(1);
        }
        return ten;
    }


    private void addEvents() {
        lvSv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachTatCaSVActivity.this);
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
                            SinhVien sv = dsSV.get(position);
                            SQLiteDatabase database = openOrCreateDatabase(
                                    DB_NAME,
                                    MODE_PRIVATE,
                                    null
                            );
                            int delete = database.delete(
                                    "Sinhvien",
                                    "ma=?",
                                    new String[]{sv.getMa()}
                            );
                            database.close();
                            docDSSv();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
                return true;
            }
        });
        lvSv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < dsSV.size()) {
                    Intent intent = new Intent(DanhSachTatCaSVActivity.this, ThongTinSVActivity.class);
                    chon = dsSV.get(position);
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
                    SinhVien sv = (SinhVien) data.getSerializableExtra("TRA");
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    ContentValues row = new ContentValues();
                    row.put("ma", sv.getMa());
                    row.put("hoten", sv.getHoten());
                    row.put("namsinh", sv.getNamSinh());
                    row.put("que", sv.getQue());
                    row.put("gioitinh", sv.getGioiTinh());
                    row.put("anh", sv.getAnh());
                    row.put("lop", sv.getLop());

                    long update = database.update(
                            "Sinhvien",
                            row,
                            "ma=?",
                            new String[]{sv.getMa()}
                    );
                    database.close();
                    docDSSv();
                } else if (data.hasExtra("THEM")) {
                    SinhVien sv = (SinhVien) data.getSerializableExtra("THEM");
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    ContentValues row = new ContentValues();
                    row.put("ma", sv.getMa());
                    row.put("hoten", sv.getHoten());
                    row.put("namsinh", sv.getNamSinh());
                    row.put("que", sv.getQue());
                    row.put("gioitinh", sv.getGioiTinh());
                    row.put("anh", sv.getAnh());
                    row.put("lop", sv.getLop());
                    long insert = database.insert(
                            "Sinhvien",
                            null,
                            row
                    );
                    database.close();
                    docDSSv();
                }
            }
        }
    }
//        chon = null;
//    }

    private void addControls() {
        lvSv = findViewById(R.id.lvSinhVien);
        dsSV = new ArrayList<>();
        adapter = new SinhVienAdapter(
                DanhSachTatCaSVActivity.this,
                R.layout.item_student,
                dsSV
        );

        lvSv.setAdapter(adapter);
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
                Intent intent = new Intent(DanhSachTatCaSVActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuExit:
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}