package com.robot.mr.makanduluyuk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mNamaKegaiatan = new ArrayList<>();
    private ArrayList<String> mJamAwal = new ArrayList<>();
    private ArrayList<String> mJamAkhir = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mNamaKegaiatan, ArrayList<String> mJamAwal, ArrayList<String> mJamAkhir) {
        this.mNamaKegaiatan = mNamaKegaiatan;
        this.mJamAwal = mJamAwal;
        this.mJamAkhir = mJamAkhir;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listkegiatan, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.namaKegiatan.setText(mNamaKegaiatan.get(position));
        holder.jamAwal.setText(mJamAwal.get(position));
        holder.jamAkhir.setText(mJamAkhir.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mNamaKegaiatan.get(position));

                Toast.makeText(mContext, mNamaKegaiatan.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNamaKegaiatan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView namaKegiatan;
        TextView jamAwal;
        TextView jamAkhir;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaKegiatan = itemView.findViewById(R.id.nama_kegiatan);
            jamAwal = itemView.findViewById(R.id.jam_awal);
            jamAkhir = itemView.findViewById(R.id.jam_akhir);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
