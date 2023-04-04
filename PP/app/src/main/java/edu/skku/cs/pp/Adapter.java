package edu.skku.cs.pp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class Log{
    public int id;
    public int log;

    public Log(int id, int log){
        this.id = id;
        this.log = log;
    }
}

public class Adapter extends BaseAdapter {
    private ArrayList<Log> items;
    private Context mContext;

    public static String LOG = "log";

    Adapter(ArrayList<Log> items, Context mContext){
        this.mContext = mContext;
        this.items = items;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int i){
        return items.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listview, viewGroup, false);
        }

        ImageView imageview = view.findViewById(R.id.log);

        imageview.setImageResource(items.get(i).log);

        return view;
    }
}
