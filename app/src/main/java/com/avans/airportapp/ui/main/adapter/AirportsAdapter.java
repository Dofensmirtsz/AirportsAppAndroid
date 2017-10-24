package com.avans.airportapp.ui.main.adapter;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avans.airportapp.R;
import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.ui.detail.DetailActivity;

public class AirportsAdapter extends RecyclerView.Adapter<AirportsAdapter.ViewHolder> {

    private Cursor data;

    public AirportsAdapter(Cursor data) {
        data.moveToFirst();
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_airport_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        data.moveToPosition(position);

        holder.name.setText(data.getString(data.getColumnIndex(AirportDBHelper.NAME)));
        holder.icao.setText(data.getString(data.getColumnIndex(AirportDBHelper.ICAO)));

        holder.itemView.setOnClickListener(view -> {
            data.moveToPosition(position);
            holder.itemView.getContext().startActivity(
                    DetailActivity.getStartIntent(holder.itemView.getContext(),
                            data.getString(data.getColumnIndex(AirportDBHelper.ICAO))));
        });
    }

    @Override
    public int getItemCount() {
        return data.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView icao;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.airport_name);
            icao = (TextView) itemView.findViewById(R.id.airport_icao);
        }
    }
}
