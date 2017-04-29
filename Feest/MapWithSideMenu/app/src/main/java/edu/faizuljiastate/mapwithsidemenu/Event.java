package edu.faizuljiastate.mapwithsidemenu;

/**
 * Created by pigva on 3/3/2017.
 */

public class Event {
    private String name,description,address,time,date,host,restriction;
    public Event(String name,String host,String address,String time,String date ,String restriction,String description){
        this.name = name;
        this.description = description;
        this.date = date;
        this.address = address;
        this.time = time;
        this.host = host;
        this.restriction = restriction;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }
}
