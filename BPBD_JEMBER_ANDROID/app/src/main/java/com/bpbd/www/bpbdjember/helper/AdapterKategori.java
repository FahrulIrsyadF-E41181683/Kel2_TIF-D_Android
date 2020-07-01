package com.bpbd.www.bpbdjember.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bpbd.www.bpbdjember.R;

import java.util.List;

public class AdapterKategori extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    List<DataKategori> item;

    public AdapterKategori(Activity activity, List<DataKategori> item){
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null){
            view = inflater.inflate(R.layout.row_spinner, null);
        }

        TextView tv_waktu = view.findViewById(R.id.kategoribcn);

        DataKategori dataKategori;
        dataKategori = item.get(position);

        tv_waktu.setText(dataKategori.getKategori());

        return view;
    }
}

