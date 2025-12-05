package hamdizeyneb.grp1.findmyfriends.ui.home;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hamdizeyneb.grp1.findmyfriends.Config;
import hamdizeyneb.grp1.findmyfriends.JSONParser;
import hamdizeyneb.grp1.findmyfriends.MyPositionRecyclerAdapter;
import hamdizeyneb.grp1.findmyfriends.Position;
import hamdizeyneb.grp1.findmyfriends.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    ArrayList<Position> data=new ArrayList<>();
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        binding.btndownHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download download=new Download();
                download.execute();

            }
        });




        return root;
    }
class Download extends AsyncTask {

        AlertDialog alert;

    @Override
    protected void onPreExecute() {
        //ui thread: acces à l'interface graphique
        AlertDialog.Builder dialogue=new AlertDialog.Builder(HomeFragment.this.getActivity());
        dialogue.setTitle("Downloading");
        dialogue.setMessage("Please wait");
        alert= dialogue.create();;
        alert.show();

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //traitement en 2nd thread
        JSONParser parser=new JSONParser();
        JSONObject response=parser.makeRequest(Config.URL_GetAll_Locations);
        Log.e("response",response.toString());

        try {
            int success=response.getInt("success");
            if(success==1)
            {
                JSONArray array=response.getJSONArray("positions");
                data.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject ligne=array.getJSONObject(i);
                    int idposition=ligne.getInt("idposition");
                    String pseudo= ligne.getString("pseudo");
                    String numero= ligne.getString("numero");
                    String longitude= ligne.getString("longitude");
                    String latitude= ligne.getString("latitude");
                    data.add(new Position(idposition,pseudo,numero,longitude,latitude));

                }

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    @Override
    protected void onPostExecute(Object o) {

        //ui thread : acces à ui
        //ArrayAdapter adapter=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data);
        MyPositionRecyclerAdapter adapter=new MyPositionRecyclerAdapter(getActivity(),data);
        binding.rvHome.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        binding.rvHome.setLayoutManager(linearLayoutManager);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1, GridLayoutManager.VERTICAL,false);
        binding.rvHome.setLayoutManager(gridLayoutManager);

        alert.dismiss();

    }
}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}