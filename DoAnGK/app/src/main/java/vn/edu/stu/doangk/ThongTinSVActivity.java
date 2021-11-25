package vn.edu.stu.doangk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


import vn.edu.stu.doangk.model.Lop;
import vn.edu.stu.doangk.model.SinhVien;

public class ThongTinSVActivity extends AppCompatActivity {
    EditText txtTen;
    EditText txtMa;
    EditText txtNamSinh;
    EditText txtLop;
    EditText txtQue;
    EditText txtGt;
    ImageView imgSV;
    SinhVien chon;
    Spinner spinner;
    ArrayList<String> dslop;
    Button btnLuu, btnThem, btnAdd;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbSinhVien.sqlite";
    private int REQUEST_IMAGE = 10;
    int requestCode = 113, resultCode = 115;
    String ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_svactivity);
        addControls();
        readDataLop();
        getData();

        addEvents();

    }

    private void TakePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);

    }

    private byte[] chuyenImgViewSangByteArray(ImageView imageView) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytesData = stream.toByteArray();
            stream.close();
            return bytesData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                Uri imgUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgSV.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readDataLop() {
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from Lop", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String ten = cursor.getString(1);
            dslop.add(ten);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dslop);
        spinner.setAdapter(adapter);
    }

    private int setSpiner(String ten) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(ten)) {
                return i;
            }
        }
        return 0;
    }

    private Boolean checkID(String ma){
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor1 = database.rawQuery("select * from Sinhvien", null);
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            String masv = cursor1.getString(0);
            if (masv.equals(ma))
                return true;
        }
        return false;
    }

    private String selectMaLop(String ten) {
        String ma = "";
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor1 = database.rawQuery("select * from Lop", null);
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            String tenlop = cursor1.getString(1);
            if (tenlop.equals(ten))
                ma = cursor1.getString(0);
        }
        return ma;
    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chon == null) {
                    chon = new SinhVien();
                }
                if (chon.getMa().equals(txtMa.getText().toString()) != true) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ThongTinSVActivity.this);
                    builder.setMessage(R.string.txtChange);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    android.app.AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    chon.setHoten(txtTen.getText().toString());
                    chon.setMa(txtMa.getText().toString());
                    String text = spinner.getSelectedItem().toString();
                    String malop = selectMaLop(text) ;
                    chon.setLop(malop);
                    byte[] anh = chuyenImgViewSangByteArray(imgSV);
                    chon.setAnh(anh);
                    chon.setGioiTinh(txtGt.getText().toString());
                    chon.setQue(txtQue.getText().toString());
                    chon.setNamSinh(Integer.parseInt(txtNamSinh.getText().toString()));

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

                chon.setHoten(txtTen.getText().toString());
                chon.setMa(txtMa.getText().toString());
                String text = spinner.getSelectedItem().toString();
                String malop = selectMaLop(text) ;
                chon.setLop(malop);
                byte[] anh = chuyenImgViewSangByteArray(imgSV);
                chon.setAnh(anh);
                chon.setGioiTinh(txtGt.getText().toString());
                chon.setQue(txtQue.getText().toString());
                chon.setNamSinh(Integer.parseInt(txtNamSinh.getText().toString()));
                if(checkID(chon.getMa())){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ThongTinSVActivity.this);
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


    private void getData() {
        Intent intent = getIntent();
        if (intent.hasExtra("CHON")) {
            chon = (SinhVien) intent.getSerializableExtra("CHON");
            if (chon != null) {
                txtTen.setText(chon.getHoten());
                txtMa.setText(chon.getMa());
//                txtLop.setText(chon.getLop());
                String lop = chon.getLop();
                spinner.setSelection(setSpiner(lop));
                txtQue.setText(chon.getQue());
                txtGt.setText(chon.getGioiTinh());
                txtNamSinh.setText(chon.getNamSinh() + "");
                Bitmap img = BitmapFactory.decodeByteArray(chon.getAnh(), 0, chon.getAnh().length);
                imgSV.setImageBitmap(img);
            } else {
                resetView();
            }
        } else {
            resetView();
        }
    }

    private void resetView() {
        txtTen.setText("");
        txtMa.setText("");
        txtLop.setText("");
        txtQue.setText("");
        txtGt.setText("");
        txtNamSinh.setText("");
        imgSV.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

    private void addControls() {
        imgSV = findViewById(R.id.imgSinhVien);
        txtTen = findViewById(R.id.txtTenSV);
        txtMa = findViewById(R.id.txtMssv);
//        txtLop = findViewById(R.id.txtLop);
        txtNamSinh = findViewById(R.id.txtNamSinh);
        txtQue = findViewById(R.id.txtQue);
        txtGt = findViewById(R.id.txtGioiTinh);
        btnLuu = findViewById(R.id.btnLuuSv);
        btnThem = findViewById(R.id.btnThemSv);
        btnAdd = findViewById(R.id.btnAdd);
        spinner = findViewById(R.id.spinnerLop);
        dslop = new ArrayList<>();
        ma = txtMa.getText().toString();
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
                Intent intent = new Intent(ThongTinSVActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuExit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}