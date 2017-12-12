package com.example.asidosibuea.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asidosibuea on 04/12/17.
 */

public class KegiatanAdapter extends ArrayAdapter<Kegiatan> {

    public KegiatanAdapter(@NonNull Context context, @NonNull List<Kegiatan> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_list_item_todo, parent, false);
        }

        Kegiatan kegiatan =getItem(position);
        TextView tvKegiatan = (TextView) convertView.findViewById(R.id.kegiatan);
        TextView tvWaktu = (TextView) convertView.findViewById(R.id.waktu);

        tvKegiatan.setText(kegiatan.getKegiatan());
        tvWaktu.setText(kegiatan.getWaktu());

        return convertView;
    }
}
