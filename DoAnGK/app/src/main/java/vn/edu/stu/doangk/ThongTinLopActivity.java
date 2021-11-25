package vn.edu.stu.doangk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vn.edu.stu.doangk.adapter.SinhVienAdapter;
import vn.edu.stu.doangk.model.Lop;
import vn.edu.stu.doangk.model.SinhVien;

public class ThongTinLopActivity extends AppCompatActivity {
    EditText txtTenLop, txtMaLop;
    Lop chon;
    Button btnLuu, btnThem;
    String ma, ten;
    ListView lvSv;
    ArrayList<SinhVien> dsSV;
    SinhVienAdapter adapter;
    int requestCode = 113, resultCode = 115;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbSinhVien.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_lop);


        addControls();
        getData();
        addEvents();
        docDSSv();
    }


    private void docDSSv() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("select * from Sinhvien where lop=?", new String[]{chon.getId()});
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
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chon == null) {
                    chon = new Lop();
                }
                if (chon.getId().equals(txtMaLop.getText().toString()) != true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinLopActivity.this);
                    builder.setMessage(R.string.txtChange);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    chon.setTen(txtTenLop.getText().toString());
                    chon.setId(txtMaLop.getText().toString());

                        Intent intent = getIntent();
                        intent.putExtra("TRA", chon);
                        setResult(resultCode, intent);
                        finish();

                }
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chon.setTen(txtTenLop.getText().toString());
                chon.setId(txtMaLop.getText().toString());
            if(checkID(chon.getId())){
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ThongTinLopActivity.this);
                builder.setMessage(R.string.txtCheckID);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                });
                android.app.AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }else {
                Intent intent = getIntent();
                intent.putExtra("THEM", chon);
                setResult(resultCode, intent);
                finish();
            }
            }
        });
    }
    private Boolean checkID(String ma){
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
                return true;
        }
        return false;
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent.hasExtra("CHON")) {
            chon = (Lop) intent.getSerializableExtra("CHON");
            if (chon != null) {
                txtTenLop.setText(chon.getTen());
                txtMaLop.setText(chon.getId());
            } else {
                resetView();
            }
        } else {
            resetView();
        }
    }

    private void resetView() {
        txtMaLop.setText("");
        txtTenLop.setText("");
    }

    private void addControls() {
        txtMaLop = findViewById(R.id.txtMaLop);
        txtTenLop = findViewById(R.id.txtTenLop);
        btnLuu = findViewById(R.id.btnLuu);
        btnThem = findViewById(R.id.btnThem);
        ma = txtMaLop.getText().toString();
        ten = txtTenLop.getText().toString();

        lvSv = findViewById(R.id.lvDSsv);
        dsSV = new ArrayList<>();
        adapter = new SinhVienAdapter(
                ThongTinLopActivity.this,
                R.layout.item_student,
                dsSV
        );

        lvSv.setAdapter(adapter);
    }
}