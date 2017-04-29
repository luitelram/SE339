package edu.faizuljiastate.mapwithsidemenu;

/**
 * Created by pigva on 4/25/2017.
 */

public class Notification {
    private String Id, toUser,fromUser,type,evtID;
    public Notification(String Id, String fromUser,String toUser,String type, String evtID){
        this.Id = Id;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.type = type;
        this.evtID = evtID;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvtID() {
        return evtID;
    }

    public void setEvtID(String evtID) {
        this.evtID = evtID;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
