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

import ma.ensa.volley.adapter.FiliereAdapter;

public class ListFiliereActivity extends AppCompatActivity {

    private ListView listView;
    private List<Filiere> filieres;
    private FiliereAdapter adapter;
    private String url = "http://192.168.1.109:8088/api/v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_filiere);
        listView = findViewById(R.id.listView);
        retrieveStudentsData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Filiere selectedFiliere = filieres.get(position);

                new AlertDialog.Builder(ListFiliereActivity.this)
                        .setTitle("Modifier ou supprimer?")
                        .setItems(new CharSequence[]{"Modifier", "Supprimer"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               if (which == 0) {
                                    showEditDialog(selectedFiliere);
                                } else {
                                    showDeleteConfirmation(selectedFiliere);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void retrieveStudentsData() {
        String loadUrl = this.url;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the JSON response and display data in ListView
                        Log.d("response", response + "");
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
            filieres = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Filiere filiere = new Filiere(
                        jsonObject.getInt("id"),
                        jsonObject.getString("code"),
                        jsonObject.getString("name")

                );
                filieres.add(filiere);
            }

            // Set up an adapter to display the list
            adapter = new FiliereAdapter(this, filieres);
            listView.setAdapter((ListAdapter) adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showEditDialog(final Filiere filiere) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier Filiere");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText code = new EditText(this);
        code.setHint("Code");
        code.setText(filiere.getCode());
        layout.addView(code);

        final EditText name = new EditText(this);
        name.setHint("Name");
        name.setText(filiere.getName());
        layout.addView(name);


        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String NewCode = code.getText().toString();
                String NewLibelle = name.getText().toString();

                Filiere updatedFiliere = new Filiere(
                        filiere.getId(),
                        NewCode,
                        NewLibelle

                );

                int position = filieres.indexOf(filiere);

                filieres.set(position, updatedFiliere);

                adapter.notifyDataSetChanged();
                sendUpdateRequest(updatedFiliere);
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

    private void showDeleteConfirmation(final Filiere fil) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez vous vraiment supprimer cet étudiant?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        filieres.remove(fil);
                        adapter.notifyDataSetChanged();
                        sendDeleteRequest(fil);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void sendDeleteRequest(Filiere fil) {
        String deleteUrl = url + "/filieres" + fil.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error + "");
            }
        });

        requestQueue.add(request);
    }

    private void sendUpdateRequest(Filiere filiere) {
        String updateUrl = url + "/filieres";
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
                        Log.d("error", error + "");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(filiere.getId()));
                params.put("code", filiere.getCode());
                params.put("name", filiere.getName());

                return params;
            }
        };

        requestQueue.add(updateRequest);

    }
}