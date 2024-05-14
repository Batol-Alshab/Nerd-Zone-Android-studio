package com.example.lenovo.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RgisterActivity extends AppCompatActivity {
    EditText etFName,etLName,etEmail,etPassword,etConfirmation;
    Button btnRegister;
    RadioGroup section ,sex ;
    String fname,lname,email,password,confirmation;
    String selectedSex=null , selectedSection=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("إنشاء حساب");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgister);

        etFName=(EditText) findViewById(R.id.etFNmae);
        etLName=(EditText) findViewById(R.id.etLNmae);
        etEmail=(EditText) findViewById(R.id.etEmail);
        etPassword=(EditText) findViewById(R.id.etPassword);
        etConfirmation=(EditText) findViewById(R.id.etConfirmation);
        btnRegister=(Button) findViewById(R.id.btnRegister);
        section=(RadioGroup) findViewById(R.id.section);
        sex =(RadioGroup) findViewById(R.id.sex);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegister();
            }
        });

    }

    private void checkRegister() {
        fname=etFName.getText().toString();
        lname=etLName.getText().toString();
        email=etEmail.getText().toString();
        password=etPassword.getText().toString();
        confirmation=etConfirmation.getText().toString();
        int selected_sex = sex.getCheckedRadioButtonId();
        if (selected_sex == R.id.female) {
            selectedSex = "0";
        } else if (selected_sex == R.id.male) {
            selectedSex = "1";
        }
        int selected_section = section.getCheckedRadioButtonId();
        if (selected_section == R.id.literary) {
            selectedSection = "2";
        } else if (selected_section == R.id.sientific) {
            selectedSection = "1";
        }
//        Toast.makeText(RgisterActivity.this,selectedSex+" rr",Toast.LENGTH_LONG).show();
        if(fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty() || selectedSection==null || selectedSex==null){
            alertFaild("جميع الحقول مطلوبة");
            }
        else if(! password.equals(confirmation)){
                alertFaild("كلمة المرور غير متطابقة مع تأكيدها");
            }
        else {
                sendRegister("تم تسجيل الحساب");
            }
    }

    private void sendRegister(String s) {
        JSONObject param = new JSONObject();
        try {
            param.put("fname",fname);
            param.put("lname",lname);
            param.put("email",email);
            param.put("password",password);
            param.put("password_confirmation",confirmation);
            param.put("sex",selectedSex);
            param.put("section_id",selectedSection);

        }catch (JSONException e){
            e.printStackTrace();
        }

        final String data = param.toString();
        final String url = getString(R.string.api)+"/register";

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Http http= new Http(RgisterActivity.this,url);
                http.setMethod("post");
                http.setData(data);
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code ==200 || code==201){

                            alertSuccess("Register Successfuly");
                        }
                        else if(code ==422 ){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFaild(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RgisterActivity.this,"Error"+code,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();

    }


    private void alertSuccess(String s) {
        new AlertDialog.Builder(this)
                .setTitle("تم")
                .setIcon(R.drawable.ic_check)
                .setMessage(s)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .show();
    }

    private void alertFaild(String s) {
        new AlertDialog.Builder(this)
                .setTitle("faild")
                .setIcon(R.drawable.ic_warning)
                .setMessage(s)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
