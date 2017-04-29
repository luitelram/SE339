package edu.faizuljiastate.mapwithsidemenu;

/**
 * Created by pigva on 2/22/2017.
 */

public class User{
    private String username;
    private String fullname;
    private String password;
    private String birthday;
    private String phone;
    private String email;
    private String rating;
    private String gender;
    private String address;
    private String admin;
    private String block;
    private String avatar;


    public User(String username, String fullname, String password, String birthday,String gender,
                        String address, String phone,String email,String rating,String admin,String block,String avatar) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.rating = rating;
        this.gender = gender;
        this.address = address;
        this.admin = admin;
        this.block = block;
        this.avatar = avatar;
    }
    public void setFullName(String nfullname){
        fullname = nfullname;
    }
    public void setBirthday(String nbirthday){
        birthday = nbirthday;
    }
    public void setPhone(String nphone){
        phone = nphone;
    }
    public void setPassword(String npassword)
    {
        password = npassword;
    }
    public void setEmail(String nemail) {
        email=nemail;
    }
    public void setRating(String nrating){rating=nrating;}
    public void setAddress(String naddress){address=naddress;}
    public void setGender(String ngender){gender=ngender;}
    public void setAdmin(String admin) {
        this.admin = admin;
    }
    public void setBlock(String block) {
        this.block = block;
    }
    public void setName(String username) {
        this.username = username;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAdmin() {
        return admin;
    }
    public String getBlock() {
        return block;
    }
    public String getFullName(){
        return fullname;
    }
    public String getPassword()
    {
        return password;
    }
    public String getPhone(){
        return phone;
    }
    public String getBirthday(){
        return  birthday;
    }
    public String getEmail(){return email;}
    public String getRating(){return rating;}
    public String getGender(){return gender;}
    public String getAddress() {return address;}
    public String getName() {
        return username;
    }
    public String getAvatar() {
        return avatar;
    }


}
