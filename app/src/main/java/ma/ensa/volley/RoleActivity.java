package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
    }
    public void AjoutRole(View view) {
        Intent intent = new Intent(this, AjoutRoleActivity.class);
        startActivity(intent);
    }
    public void ListRoles(View view) {
        Intent intent = new Intent(this, ListRoleActivity.class);
        startActivity(intent);
    }
}