package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FiliereActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filiere);
    }
    public void AjoutFiliere(View view) {
        Intent intent = new Intent(this, AjoutFiliereActivity.class);
        startActivity(intent);
    }
    public void ListFilieres(View view) {
        Intent intent = new Intent(this, ListFiliereActivity.class);
        startActivity(intent);
    }
}