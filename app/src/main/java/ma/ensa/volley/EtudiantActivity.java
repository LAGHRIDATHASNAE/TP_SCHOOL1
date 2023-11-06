package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EtudiantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);
    }
    public void AjoutEtudiant(View view) {
        Intent intent = new Intent(this, AjoutEtudiantActivity.class);
        startActivity(intent);
    }
    public void ListEtudiants(View view) {
        Intent intent = new Intent(this, ListEtudiantActivity.class);
        startActivity(intent);
    }
}