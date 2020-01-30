package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.ProductsActivity;

import java.util.HashMap;
import java.util.List;

public class FilterCategoryExpaListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listChildData;

    public FilterCategoryExpaListAdapter(Context context, List<String> listDataHeader,
                                         HashMap<String, List<String>> listChildData) {
        this._context = context;
        this.listDataHeader = listDataHeader;
        this.listChildData = listChildData;
    }
    public FilterCategoryExpaListAdapter(){
        //Empty Constructor
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listChildData.get(this.listDataHeader.get(groupPosition))
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
                ((ProductsActivity)_context).openSubSubCatList(childText);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildData.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
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