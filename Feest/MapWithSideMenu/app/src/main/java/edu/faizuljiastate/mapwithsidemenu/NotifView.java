package edu.faizuljiastate.mapwithsidemenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pigva on 4/22/2017.
 */

public class NotifView extends ArrayAdapter<Notification> {

    private final Context context;
    private final ArrayList<Notification> itemname;


    public NotifView(Context context, int resource, ArrayList<Notification> itemname) {
        super(context,resource,itemname);
        this.context=context;
        this.itemname=itemname;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.activity_notifs_adapter,parent,false);

        TextView content = (TextView) rowView.findViewById(R.id.notifcontent);

        Notification n = itemname.get(position);
        if(n.getType().equals("F"))
        {
            content.setText("'"+n.getFromUser()+"' send you a friend request");
        }
        else
        {
            content.setText("'"+n.getFromUser()+"' invite you to an event");
        }
        return rowView;
    }
}
