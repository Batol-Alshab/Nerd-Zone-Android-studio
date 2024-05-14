package com.example.lenovo.myapplication;
// <color name="colorAccent">#FF4081</color>
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
//import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar()!= null) {
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("NERD ZONE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }, 1500);
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//
//            }
//        })





//        tv1 =(TextView)findViewById(R.id.textView);
//        e = (EditText)findViewById(R.id.editText);
//        b=(Button)findViewById(R.id.button);
//
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast t= Toast.makeText(MainActivity.this,"Hi",Toast.LENGTH_LONG);
//                t.setGravity(Gravity.CENTER,200,200);
//                t.show();


//                NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                Context context= MainActivity.this;
//                Intent intent = new Intent(context, MainActivity.class);
//                PendingIntent p = PendingIntent.getActivity(context,0,intent,0);
//                NotificationCompat.Builder mbuilder=new NotificationCompat.Builder(MainActivity.this);
//                mbuilder.setTicker("ON");
//                mbuilder.setContentTitle("HI");
//                mbuilder.setContentText("hello");
//                mbuilder.setContentIntent(p);
//                Notification notification=mbuilder.build();
//                nm.notify(0,notification);


//                String name= e.getText().toString();
//            Intent i=new Intent(MainActivity.this,MainActivity2.class);
//            .putExtra("name",e1.getText().toString());
//            startActivities(i);
//                String s = e1.getText().toString();
//                tv1.setText(name+":");
//            }
//        });

    }
}




