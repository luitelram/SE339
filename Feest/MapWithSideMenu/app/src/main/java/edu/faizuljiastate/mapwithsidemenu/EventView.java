package edu.faizuljiastate.mapwithsidemenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pigva on 3/3/2017.
 */

public class EventView extends ArrayAdapter<Event> {
    private final Context context;
    private final ArrayList<Event> itemname;


    public EventView(Context context, int resource, ArrayList<Event> itemname) {
        super(context,resource,itemname);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.itemname=itemname;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.activity_events_adapter,parent,false);

        TextView evtname = (TextView) rowView.findViewById(R.id.evtname);
        TextView evthost = (TextView) rowView.findViewById(R.id.evthost);
        TextView evtaddress = (TextView) rowView.findViewById(R.id.evtaddress);
        TextView evtrestriction = (TextView) rowView.findViewById(R.id.evtrestriction);
        TextView evtdate= (TextView) rowView.findViewById(R.id.evtdate);
        TextView evttime = (TextView) rowView.findViewById(R.id.evttime);
        TextView evtdescription = (TextView) rowView.findViewById(R.id.evtdescription);
        ImageView evtimage = (ImageView) rowView.findViewById(R.id.evtimage);

        Event e = itemname.get(position);
        evtname.setText("Event: "+e.getName());
        evthost.setText("Hosted by: "+e.getHost());
        evtaddress.setText("Address: "+e.getAddress());
        evtrestriction.setText("Restriction: "+e.getRestriction());
        evtdate.setText("Date: "+e.getDate());
        evttime.setText("Time: "+e.getTime());
        evtdescription.setText("Description: "+e.getDescription());
        evtimage.setImageResource(R.drawable.ic_events_list);
        return rowView;

    };
}
