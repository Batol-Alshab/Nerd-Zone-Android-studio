package com.example.lenovo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {
    TextView tvFName,tvLName,tvEmail,tvSection;
    Button btnLogout, btnMaterial;
    String section_id=null;
    LocalStorage localStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().setTitle("الملف الشخصي");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvFName =(TextView) findViewById(R.id.tvFName);
        tvLName =(TextView) findViewById(R.id.tvLName);
        tvEmail =(TextView) findViewById(R.id.tvEmail);
        tvSection =(TextView) findViewById(R.id.tvSection);
        btnLogout =(Button) findViewById(R.id.btnLogout);
        btnMaterial=(Button) findViewById(R.id.btnMaterial);
        localStorage= new LocalStorage(UserActivity.this);

        getUser();

        btnMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localStorage.setSection(section_id);
                Intent intent = new Intent(UserActivity.this,MaterialActivity.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout() {
        final String url =getString(R.string.api)+"/logout";
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Http http = new Http(UserActivity.this,url);
                http.setMethod("post");
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code ==200){
                                Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();

                        }else {
                            Toast.makeText(UserActivity.this,"Error"+code,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();

    }

    private void getUser() {
        final String url =getString(R.string.api)+"/user/index";
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Http http = new Http(UserActivity.this,url);
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code ==200){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
//                                [
//                                'fname'=>$user->fname,
//                                        'lname'=>$user->lname,
//                                        'email'=>$user->email,
//                                        'section'=>$s
//                                ]
                                String fname= response.getString("fname");
                                String lname= response.getString("lname");
                                String email= response.getString("email");
                                String section = response.getString("section");
//                                Toast.makeText(UserActivity.this,section,Toast.LENGTH_LONG).show();
                                section_id=section;
                                tvFName.setText(fname);
                                tvLName.setText(lname);
                                tvEmail.setText(email);
                                tvSection.setText(section);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(UserActivity.this,"Error"+code,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();
    }

}
