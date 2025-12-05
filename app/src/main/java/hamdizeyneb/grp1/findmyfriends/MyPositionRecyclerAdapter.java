package hamdizeyneb.grp1.findmyfriends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyPositionRecyclerAdapter extends RecyclerView.Adapter<MyPositionRecyclerAdapter.MyViewHolder> {

    Context con;
    ArrayList<Position> data;

    public MyPositionRecyclerAdapter(Context con, ArrayList<Position> data) {
        this.con = con;
        this.data = data;
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvpseudo,tvnumero,tvLongitude,tvLatitude;
        Button btndelete,btnedit,btnmap;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvpseudo=itemView.findViewById(R.id.tvpseudo);
            tvnumero=itemView.findViewById(R.id.tvnum);
            tvLatitude=itemView.findViewById(R.id.tvLatitude);
            tvLongitude=itemView.findViewById(R.id.tvLongitude);
            btndelete=itemView.findViewById(R.id.btnDelete);
            btnedit=itemView.findViewById(R.id.btnUpdate);
            btnmap=itemView.findViewById(R.id.btnViewOnMap);

            btnmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(con,MapsActivity.class);
                    i.putExtra("pseudo",tvpseudo.getText().toString());
                    i.putExtra("longitude",tvLongitude.getText().toString());
                    i.putExtra("latitude",tvLatitude.getText().toString());
                    con.startActivity(i);

                }
            });

        }

    }
    @NonNull
    @Override
    public MyPositionRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout l=null;
        //creation d'une nouvelle view
        LayoutInflater inf=LayoutInflater.from(con);
        l=(LinearLayout) inf.inflate(R.layout.poistion_view,null);
        return new MyViewHolder(l);

    }

    @Override
    public void onBindViewHolder(@NonNull MyPositionRecyclerAdapter.MyViewHolder holder, int position) {
        Position p=data.get(position);
        holder.tvpseudo.setText(p.pseudo);
        holder.tvnumero.setText(p.numero);
        holder.tvLatitude.setText(p.latitude);
        holder.tvLongitude.setText(p.longitude);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
