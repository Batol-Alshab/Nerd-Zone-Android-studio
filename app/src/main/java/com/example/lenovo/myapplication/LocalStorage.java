package com.example.lenovo.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LocalStorage {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    String token;
    String section;
    public  LocalStorage(Context context){
        this.context= context;
        sharedPreferences=context.getSharedPreferences("LOGIN_API",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public String getToken(){
        token=sharedPreferences.getString("token","token");
        return token;
    }
    public void setToken(String token){
        editor.putString("token",token);
        editor.commit();
        this.token=token;
    }

    public void setSection(String section){
        editor.putString("section",section);
        editor.commit();
        this.section=section;
    }
    public String getSection(){
        section=sharedPreferences.getString("section","section");
        return section;
    }
}
