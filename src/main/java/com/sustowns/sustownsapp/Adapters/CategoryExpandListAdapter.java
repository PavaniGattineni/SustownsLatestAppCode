package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.MakeBookingActivity;

import java.util.HashMap;
import java.util.List;

public class CategoryExpandListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public CategoryExpandListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_row_child, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.textViewChild);

        txtListChild.setText(childText);
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupPosition == 0) {
                    if (childPosition == 0) {
                        Toast.makeText(_context, "0", Toast.LENGTH_SHORT).show();

                    } else if (childPosition == 1) {
                        Toast.makeText(_context, "1", Toast.LENGTH_SHORT).show();

                    }
                    if (childPosition == 2) {
                        Toast.makeText(_context, "2", Toast.LENGTH_SHORT).show();

                    }
                    if (childPosition == 3) {
                        Toast.makeText(_context, "3", Toast.LENGTH_SHORT).show();

                    }
                    if (childPosition == 4) {
                        Toast.makeText(_context, "4", Toast.LENGTH_SHORT).show();

                    }
                }
                    else  if(groupPosition == 1){
                        if(childPosition == 0){
                            Intent i = new Intent(_context, MakeBookingActivity.class);
                            _context.startActivity(i);

                        }else  if(childPosition == 1){
                            Toast.makeText(_context, "1", Toast.LENGTH_SHORT).show();

                        } if(childPosition == 2){
                            Toast.makeText(_context, "2", Toast.LENGTH_SHORT).show();

                        }
                }else
                    if(childPosition == 0){
                        Toast.makeText(_context, "0", Toast.LENGTH_SHORT).show();

                    }else  if(childPosition == 1){
                        Toast.makeText(_context, "1", Toast.LENGTH_SHORT).show();

                    } if(childPosition == 2){
                        Toast.makeText(_context, "2", Toast.LENGTH_SHORT).show();

                    }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_row_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textViewGroup);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}