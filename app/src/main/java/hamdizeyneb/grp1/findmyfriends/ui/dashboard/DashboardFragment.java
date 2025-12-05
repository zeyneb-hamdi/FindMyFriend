package hamdizeyneb.grp1.findmyfriends.ui.dashboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import hamdizeyneb.grp1.findmyfriends.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static final int SMS_PERMISSION_CODE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Listener du bouton envoyer SMS
      binding.btnsendDash.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String numero = binding.ednumDash.getText().toString().trim();

              if (numero.isEmpty()) {
                  binding.ednumDash.setError("Entrez un numéro de téléphone");
                  return;
              }

              // Vérifier si la permission est accordée
              if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                      != PackageManager.PERMISSION_GRANTED) {
                  requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
              } else {

                  sendSms(numero);
              }
          }
      });

        // Affichage texte du ViewModel (optionnel)



        return root;
    }

    // Méthode pour envoyer le SMS
    private void sendSms(String numero) {
        try {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(numero, null, "Find Friend : envoyer moi votre position", null, null);
            Toast.makeText(getContext(), "SMS envoyé avec succès", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erreur lors de l'envoi du SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // Gestion du résultat de la permission


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String numero = binding.ednumDash.getText().toString().trim();
                if (!numero.isEmpty()) sendSms(numero);
            } else {
                Toast.makeText(getContext(), "Permission SMS refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
