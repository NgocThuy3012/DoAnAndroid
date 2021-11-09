package vn.edu.stu.doangk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
EditText txtUser, txtPass;
Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String ten=txtUser.getText().toString();
                    String pass=txtPass.getText().toString();
                    if(ten.equals("thuy")&&pass.equals("123")) {
                        Intent intent = new Intent(
                                MainActivity.this,
                                MenuActivity.class
                        );
                        startActivity(intent);
                    }else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(R.string.txtAlertLogin);
                        builder.setPositiveButton(R.string.txtExit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCancelable(true);
                            }
                        });
                       AlertDialog dialog=builder.create();
                       dialog.setCanceledOnTouchOutside(true);
                       dialog.show();
                    }
            }
        });
    }

    private void addControls() {
        txtUser=findViewById(R.id.txtUser);
        txtPass=findViewById(R.id.txtPass);
        btnLogin=findViewById(R.id.btnLogin);
    }

}