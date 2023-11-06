package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import ma.ensa.volley.adapter.RoleAdapter;
import ma.ensa.volley.classes.Role;

public class ListRoleActivity extends AppCompatActivity {

    private ListView listView;
    private List<Role> etudiants;
    private RoleAdapter adapter;
    private String url = "http://192.168.1.109:8088/api/v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_role);
        listView = findViewById(R.id.listView);
        retrieveStudentsData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Role selectedEtudiant = etudiants.get(position);

                new AlertDialog.Builder(ListRoleActivity.this)
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
        String loadUrl = this.url + "/roles";

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
                Role etudiant = new Role(jsonObject.getInt("id"), jsonObject.getString("name"));
                etudiants.add(etudiant);
            }

            // Set up an adapter to display the list
            adapter = new RoleAdapter(this, etudiants);
            listView.setAdapter((ListAdapter) adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showEditDialog(final Role etudiant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier Role");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nomInput = new EditText(this);
        nomInput.setHint("Nom");
        nomInput.setText(etudiant.getName());
        layout.addView(nomInput);


        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNom = nomInput.getText().toString();


                Role updatedEtudiant = new Role(etudiant.getId(), newNom);

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

    private void showDeleteConfirmation(final Role etudiant) {
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

    private void sendDeleteRequest(Role etudiant) {
        String deleteUrl = url+"/roles/" + etudiant.getId();

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

    private void sendUpdateRequest(Role etudiant){
        String updateUrl = url+"/roles";
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
                params.put("nom", etudiant.getName());

                return params;
            }
        };

        requestQueue.add(updateRequest);
    }
}