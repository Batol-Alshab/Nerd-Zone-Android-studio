package com.example.lenovo.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail,etPassword;
    Button btnLogin,btnRegister;
    String email,password;
    LocalStorage localStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        localStorage= new LocalStorage(LoginActivity.this);
        etEmail=(EditText)findViewById(R.id.etEmail1);
        etPassword=(EditText)findViewById(R.id.etPassword1);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button)findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RgisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void checkLogin(){
        email=etEmail.getText().toString();
        password=etPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            alertFaild("جميع الحقول مطلوبة");
        }
        else {
            sendLogin();
        }
    }

    private void sendLogin() {
        JSONObject params = new JSONObject();
        try {
            // الوسيط الاول اسم العمود
            params.put("email",email);
            params.put("password",password);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String data = params.toString();
        final String url = getString(R.string.api)+"/login";

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Http http= new Http(LoginActivity.this,url);
                http.setMethod("post");
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code ==200){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
//                                [
//                                'token'=>$token
//                                ]
                                final String token= response.getString("token");
                                Toast.makeText(LoginActivity.this,"OK",Toast.LENGTH_SHORT).show();
                                localStorage.setToken(token);
//                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                                startActivity(intent);

                                NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Context c=LoginActivity.this;
                                Intent i = new Intent(c , UserActivity.class);
                                PendingIntent pi= PendingIntent.getActivity(c,0,i,0);
                                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                int notificationIcon = R.drawable.logo;
                                NotificationCompat.Builder nb= new NotificationCompat.Builder(LoginActivity.this);
//                nb.setSmallIcon(android.R.drawable.stat_notify_more);
                                nb.setSmallIcon(notificationIcon);
                                nb.setContentTitle("اهلا بك في موقعنا المميز");
                                nb.setContentText("نيرد زون ...حلق في عالم المعرفة");
                                nb.setSound(soundUri);
                                nb.setContentIntent(pi);
                                Notification notification= nb.build();
                                nm.notify(0,notification);

                                Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                                startActivity(intent);
                                finish();
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(code ==404 ){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFaild(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this,"Error"+code,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();


//        Intent intent = new Intent(LoginActivity.this,UserActivity.class);
//        startActivity(intent);
//        finish();
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
