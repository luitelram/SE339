package edu.faizuljiastate.mapwithsidemenu;

import android.content.Context;
import android.content.SharedPreferences;


public class LocalStorage {
    SharedPreferences storage;
    public LocalStorage(Context c){
        storage = c.getSharedPreferences("Information",0);
    }
    public void login(User u){
        SharedPreferences.Editor s = storage.edit();
        s.putString("Username",u.getName());
        s.putString("Fullname",u.getFullName());
        s.putString("Phone",u.getPhone());
        s.putString("Birthday",u.getBirthday());
        s.putString("Gender",u.getGender());
        s.putString("Address",u.getAddress());
        s.putString("Email",u.getEmail());
        s.putString("Rating",u.getRating());
        s.putString("Admin",u.getAdmin());
        s.putString("Block",u.getBlock());
        s.putString("Avatar",u.getAvatar());
        s.commit();
    }
    public User getInformation(){
        return new User(storage.getString("Username",""),storage.getString("Fullname",""),"",
                storage.getString("Birthday",""),storage.getString("Gender",""),storage.getString("Address",""),
                storage.getString("Phone",""),storage.getString("Email",""),storage.getString("Rating",""),
                storage.getString("Admin",""),storage.getString("Block",""),storage.getString("Avatar",""));
    }
    public void setAvatar(String navatar){
        SharedPreferences.Editor s = storage.edit();
        s.putString("Avatar",navatar);
        s.commit();
    }
    public void setLogin()
    {
        SharedPreferences.Editor s = storage.edit();
        s.putBoolean("Login",true);
        s.commit();
    }
    public void setLogout(){
        SharedPreferences.Editor s = storage.edit();
        s.putBoolean("Login",false);
        s.commit();
    }
    public boolean getLog(){
        return storage.getBoolean("Login",false);
    }
    public void clear(){
        SharedPreferences.Editor s = storage.edit();
        s.clear();
        s.commit();
    }
}

