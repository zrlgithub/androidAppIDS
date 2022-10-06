package com.example.ids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context ctx;
    private List<String> bibl;

    public CustomAdapter(@NonNull Context context,  List<String> list)
    {
        this.ctx=context;
        this.bibl=list;
    }

    @Override
    public int getCount() {
        return bibl.size();
    }

    @Override
    public Object getItem(int i) {
        return bibl.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.activity_custom_adapter,parent,false);
        TextView tv1 = v.findViewById(R.id.text1);
        TextView tv2 = v.findViewById(R.id.text2);
        TextView tv3 = v.findViewById(R.id.text3);
        TextView tv4 = v.findViewById(R.id.text4);
        TextView tv5 = v.findViewById(R.id.text5);

        String b = (String) getItem(position);
        tv1.setText(b);

        return v;
    }

    public void remove(int i) {
        bibl.remove(i);
    }

}