package com.example.lenovo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MaterialActivity extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    String[] m = {"tv1", "tv2", "tv3", "tv4", "tv5", "tv6", "tv7", "tv8"};
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        localStorage= new LocalStorage(MaterialActivity.this);
//        final String section = getIntent().getStringExtra("section_id");
        String section =localStorage.getSection();
        getSupportActionBar().setTitle("مواد الفرع "+section);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);
        tv7 = (TextView) findViewById(R.id.tv7);
        tv8 = (TextView) findViewById(R.id.tv8);



        sendMaterial();

    }



    private void sendMaterial() {
//        final String section = getIntent().getStringExtra("section_id");

        final String url;
        String section =localStorage.getSection();

//        Toast.makeText(MaterialActivity.this,section,Toast.LENGTH_SHORT).show();
        if(section.equals("علمي")){
            url = getString(R.string.api) + "/materials/sientific";
        }else {
            url = getString(R.string.api) + "/materials/literary";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Http http = new Http(MaterialActivity.this,url);
//                {
//                    "material": [
//                            "",
//                            "",
//                    ]
//                }
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code ==200){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String material1= response.getString("material");
                                JSONArray jsonArray = new JSONArray(material1);

                                String[] material = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    material[i] = jsonArray.getString(i);
                                }
                                tv1.setText(jsonArray.getString(0));
                                tv2.setText(jsonArray.getString(1));
                                tv3.setText(jsonArray.getString(2));
                                tv4.setText(jsonArray.getString(3));
                                tv5.setText(jsonArray.getString(4));
                                tv6.setText(jsonArray.getString(5));
                                tv7.setText(jsonArray.getString(6));
                                tv8.setText(jsonArray.getString(7));

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MaterialActivity.this,"Error"+code,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();
    }
}
