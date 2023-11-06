package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.Filiere;
import ma.ensa.volley.R;

public class FiliereAdapter extends ArrayAdapter<Filiere> {

    private List<Filiere> filieres;
    private Context context;

    public FiliereAdapter(Context context, List<Filiere> filieres) {
        super(context, 0, filieres);
        this.filieres = filieres;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Filiere filiere = filieres.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }


        TextView nom = convertView.findViewById(R.id.code);
        TextView prenom = convertView.findViewById(R.id.libelle);


        nom.setText("Code: " + filiere.getCode());
        prenom.setText("Libelle: " + filiere.getName());



        return convertView;
    }
}
