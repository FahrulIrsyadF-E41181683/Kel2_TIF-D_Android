package com.bpbd.www.bpbdjember;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bpbd.www.bpbdjember.response.BeritaItem;
import com.squareup.picasso.Picasso;

import java.util.List;

class AdapterBerita extends RecyclerView.Adapter<AdapterBerita.MyViewHolder> {
    // Buat Global variable untuk manampung context
    Context context;
    List<BeritaItem> berita;
    public AdapterBerita(Context context, List<BeritaItem> data_berita) {
        // Inisialisasi
        this.context = context;
        this.berita = data_berita;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.berita_item, parent, false);

        // Hubungkan dengan MyViewHolder
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Set widget
        holder.tvJudul.setText(berita.get(position).getJUDUL());
        holder.tvTglTerbit.setText(berita.get(position).getTANGGAL());
        holder.tvPenulis.setText("Ditulis oleh : " + berita.get(position).getNAMA());
        holder.tvIdBerita.setText(berita.get(position).getIDBRT());

        // Dapatkan url gambar
        final String urlGambarBerita = "http://192.168.0.107/Kel2_TIF-D/BPBD_JEMBER_WEB/assets/img/berita_gambar/" + berita.get(position).getGAMBARBRT();
        // Set image ke widget dengna menggunakan Library Piccasso
        // krena imagenya dari internet
        Picasso.with(context).load(urlGambarBerita).into(holder.ivGambarBerita);

        // Event klik ketika item list nya di klik
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mulai activity Detail
                Intent varIntent = new Intent(context, DetailActivity.class);
                // sisipkan data ke intent
                varIntent.putExtra("ID_BERITA", berita.get(position).getIDBRT());
                varIntent.putExtra("JDL_BERITA", berita.get(position).getJUDUL());
                varIntent.putExtra("TGL_BERITA", berita.get(position).getTANGGAL());
                varIntent.putExtra("PNS_BERITA", berita.get(position).getNAMA());
                varIntent.putExtra("FTO_BERITA", urlGambarBerita);
                varIntent.putExtra("ISI_BERITA", berita.get(position).getISIBERITA());

                // method startActivity cma bisa di pake di activity/fragment
                // jadi harus masuk ke context dulu
                context.startActivity(varIntent);
            }
        });
    }
    // Menentukan Jumlah item yang tampil
    @Override
    public int getItemCount() {
        return berita.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Deklarasi widget
        ImageView ivGambarBerita;
        TextView tvJudul, tvTglTerbit, tvPenulis, tvIdBerita;
        public MyViewHolder(View itemView) {
            super(itemView);
            // inisialisasi widget
            ivGambarBerita = (ImageView) itemView.findViewById(R.id.ivPosterBerita);
            tvJudul = (TextView) itemView.findViewById(R.id.tvJudulBerita);
            tvTglTerbit = (TextView) itemView.findViewById(R.id.tvTglTerbit);
            tvPenulis = (TextView) itemView.findViewById(R.id.tvPenulis);
            tvIdBerita = (TextView) itemView.findViewById(R.id.tvId);
        }
    }
}
