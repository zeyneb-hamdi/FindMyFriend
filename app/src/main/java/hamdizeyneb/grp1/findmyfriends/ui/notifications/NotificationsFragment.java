package hamdizeyneb.grp1.findmyfriends.ui.notifications;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.HashMap;

import hamdizeyneb.grp1.findmyfriends.Config;
import hamdizeyneb.grp1.findmyfriends.JSONParser;
import hamdizeyneb.grp1.findmyfriends.databinding.FragmentNotificationsBinding;
import hamdizeyneb.grp1.findmyfriends.ui.home.HomeFragment;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.btnaddAdd.setOnClickListener(v -> {
            String pseudo = binding.edpseudoAdd.getText().toString().trim();
            String numero = binding.ednumAdd.getText().toString().trim();
            String longitude = binding.edlongAdd.getText().toString().trim();
            String latitude = binding.edlatAdd.getText().toString().trim();

            if (TextUtils.isEmpty(pseudo) || TextUtils.isEmpty(numero)
                    || TextUtils.isEmpty(longitude) || TextUtils.isEmpty(latitude)) {
                Toast.makeText(requireActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérification basique : numero doit être numérique si ton PHP attend un integer
            // Tu peux enlever cette vérif si numero peut être string côté serveur
            try {
                Integer.parseInt(numero);
            } catch (NumberFormatException ex) {
                Toast.makeText(requireActivity(), "Le champ numéro doit être un entier", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lancer l'AsyncTask qui utilise JSONParser
            new Addition(pseudo, numero, longitude, latitude).execute();
        });





        return root;
    }
    class Addition extends AsyncTask
    {
        private final String pseudo, numero, longitude, latitude;
        private AlertDialog alert;

        Addition(String pseudo, String numero, String longitude, String latitude) {
            this.pseudo = pseudo;
            this.numero = numero;
            this.longitude = longitude;
            this.latitude = latitude;
        }


        @Override
        protected void onPreExecute() {

            AlertDialog.Builder dialogue=new AlertDialog.Builder(NotificationsFragment.this.getActivity());
            dialogue.setTitle("Adding");
            dialogue.setMessage("Please wait");
            alert= dialogue.create();;
            alert.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            JSONParser parser = new JSONParser();

            HashMap<String, String> params = new HashMap<>();
            params.put("pseudo", pseudo);
            params.put("numero", numero);     // si PHP attend integer, assure-toi qu'il s'agit d'un entier
            params.put("longitude", longitude);
            params.put("latitude", latitude);

            // utilise la méthode makeHttpRequest(url, method, params)
            JSONObject response = parser.makeHttpRequest(Config.URL_Add_Location, "POST", params);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Object o) {

            alert.dismiss();


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




