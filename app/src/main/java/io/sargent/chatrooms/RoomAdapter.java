package io.sargent.chatrooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends ArrayAdapter<RoomInfo> {

    public RoomAdapter(Context ctx, int res, List<RoomInfo> obj) {
        super(ctx, res, obj);
    }

    @Override
    // Called once for each row of the ListView //
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get TodoItem for position
        final RoomInfo item = getItem(position);

        // Check is an existing view is being reused
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.drawer_row, parent, false);
        }

        TextView text = (TextView)convertView.findViewById(R.id.rowText);
        text.setText(item.roomName);

        // Return completed view
        return convertView;
    }
}
