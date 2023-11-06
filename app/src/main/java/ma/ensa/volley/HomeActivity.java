package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {


    private Button bn1;
    private Button bn2;
    private Button bn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }
    public void navigateToFiliereActivity(View view) {
        Intent intent = new Intent(this, FiliereActivity.class);
        startActivity(intent);
    }
    public void navigateToEtudiantActivity(View view) {
        Intent intent = new Intent(this, EtudiantActivity.class);
        startActivity(intent);
    }
    public void navigateToRoleActivity(View view) {
        Intent intent = new Intent(this, RoleActivity.class);
        startActivity(intent);
    }
}