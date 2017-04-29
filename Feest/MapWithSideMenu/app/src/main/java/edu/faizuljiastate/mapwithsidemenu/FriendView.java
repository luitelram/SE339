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
 * Created by pigva on 3/2/2017.
 */

public class FriendView extends ArrayAdapter<User> {

        private final Context context;
        private final ArrayList<User> itemname;


        public FriendView(Context context, int resource, ArrayList<User> itemname) {
            super(context,resource,itemname);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.itemname=itemname;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView=inflater.inflate(R.layout.activity_friends_adapter,parent,false);

            TextView frusername = (TextView) rowView.findViewById(R.id.frusername);
            ImageView fravatar = (ImageView) rowView.findViewById(R.id.fravatar);
            TextView frfullname = (TextView) rowView.findViewById(R.id.frfullname);

            User u = itemname.get(position);
            frusername.setText(u.getName());
            frfullname.setText(u.getFullName());
            byte[] decodedString = Base64.decode(u.getAvatar(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            fravatar.setImageBitmap(decodedByte);
            return rowView;
        };
}
