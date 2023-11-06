package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.classes.Etudiant;
import ma.ensa.volley.classes.Role;


    public class EtudiantAdapter extends ArrayAdapter<Etudiant> {
        private List<Etudiant> filieres;
        private Context context;

        public EtudiantAdapter(Context context, List<Etudiant> filieres) {
            super(context, 0, filieres);
            this.filieres = filieres;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Etudiant filiere = filieres.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item3, parent, false);
            }


            TextView prenom = convertView.findViewById(R.id.FirstName);
            TextView nom = convertView.findViewById(R.id.LastName);
            TextView tel = convertView.findViewById(R.id.telephone);

            prenom.setText("Prénom: " + filiere.getFirstName());
            nom.setText("Nom: " + filiere.getLastName());
            tel.setText("Telephone: " + filiere.getTelephone());


            return convertView;
        }
    }


