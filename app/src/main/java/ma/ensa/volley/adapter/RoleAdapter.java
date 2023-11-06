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
import ma.ensa.volley.classes.Role;

public class RoleAdapter extends ArrayAdapter<Role> {
    private List<Role> filieres;
    private Context context;

    public RoleAdapter(Context context, List<Role> filieres) {
        super(context, 0, filieres);
        this.filieres = filieres;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Role filiere = filieres.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item2, parent, false);
        }


        TextView prenom = convertView.findViewById(R.id.name);


        prenom.setText("Role: " + filiere.getName());



        return convertView;
    }
}
