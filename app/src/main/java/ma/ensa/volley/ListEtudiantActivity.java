package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensa.volley.adapter.EtudiantAdapter;
import ma.ensa.volley.adapter.RoleAdapter;
import ma.ensa.volley.classes.Etudiant;
import ma.ensa.volley.classes.Role;

public class ListEtudiantActivity extends AppCompatActivity {

    private ListView listView;
    private List<Etudiant> etudiants;
    private EtudiantAdapter adapter;
    private String url = "http://192.168.1.109:8088/api/v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);
        listView = findViewById(R.id.listView);
        retrieveStudentsData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Etudiant selectedEtudiant = etudiants.get(position);

                new AlertDialog.Builder(ListEtudiantActivity.this)
                        .setTitle("Modifier ou supprimer?")
                        .setItems(new CharSequence[]{"Modifier", "Supprimer"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    showEditDialog(selectedEtudiant);
                                } else {
                                    showDeleteConfirmation(selectedEtudiant);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void retrieveStudentsData() {
        String loadUrl = this.url + "/etudiants";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the JSON response and display data in ListView
                        Log.d("response", response+"");
                        handleJsonResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void handleJsonResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            etudiants = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Etudiant etudiant = new Etudiant(

                        jsonObject.getString("FirstName"),
                        jsonObject.getString("LastName"),
                        jsonObject.getString("telephone")
            );
                etudiants.add(etudiant);
            }

            // Set up an adapter to display the list
            adapter = new EtudiantAdapter(this, etudiants);
            listView.setAdapter((ListAdapter) adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showEditDialog(final Etudiant etudiant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier Etudiant");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText prenomInput = new EditText(this);
        prenomInput.setHint("FirstName");
        prenomInput.setText(etudiant.getFirstName());
        layout.addView(prenomInput);
        final EditText nomInput = new EditText(this);
        nomInput.setHint("LastName");
        nomInput.setText(etudiant.getLastName());
        layout.addView(nomInput);
        final EditText telephone = new EditText(this);
        telephone.setHint("telephone");
        telephone.setText(etudiant.getTelephone());
        layout.addView(telephone);



        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNom = nomInput.getText().toString();
                String newPreNom = prenomInput.getText().toString();
                String newTel = telephone.getText().toString();
                Etudiant updatedEtudiant = new Etudiant( newNom, newPreNom, newTel);

                int position = etudiants.indexOf(etudiant);

                etudiants.set(position, updatedEtudiant);

                adapter.notifyDataSetChanged();
                sendUpdateRequest(updatedEtudiant);
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showDeleteConfirmation(final Etudiant etudiant) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez vous vraiment supprimer cet étudiant?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        etudiants.remove(etudiant);
                        adapter.notifyDataSetChanged();
                        sendDeleteRequest(etudiant);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void sendDeleteRequest(Etudiant etudiant) {
        String deleteUrl = url+"/etudiants/" + etudiant.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error+"");
            }
        });

        requestQueue.add(request);
    }

    private void sendUpdateRequest(Etudiant etudiant){
        String updateUrl = url+"/etudiants";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest updateRequest = new StringRequest(Request.Method.POST, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "ok updating!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error+"");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(etudiant.getId()));
                params.put("FirstName", etudiant.getFirstName());
                params.put("LastName", etudiant.getLastName());
                params.put("telephone", etudiant.getTelephone());
                return params;
            }
        };

        requestQueue.add(updateRequest);
    }
}