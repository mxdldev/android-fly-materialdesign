package com.yesway.calendardemo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yesway.calendardemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wl
 * @since 2016/08/26 13:36
 */
public class EventAdapter extends BaseAdapter {
    private List<String> details = new ArrayList<>();
    private String typeName;

    public void setDetails(String type, String[] data) {
        typeName = type;
        details.clear();
        details.addAll(Arrays.asList(data));
    }

    public void clear() {
        typeName = null;
        details.clear();
    }

    @Override
    public int getCount() {
        return details.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textview;
        if(position == 0) { //show header
            textview = (TextView) View.inflate(parent.getContext(), R.layout.item_event_header, null);
            if(typeName != null) {
                textview.setText(typeName);
            }
        } else { //show details
            textview = (TextView) View.inflate(parent.getContext(), R.layout.item_event, null);
            textview.setText(details.get(position - 1));
        }
        return textview;
    }
}
