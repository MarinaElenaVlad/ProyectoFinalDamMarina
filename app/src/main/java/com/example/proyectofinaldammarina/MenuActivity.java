package com.example.proyectofinaldammarina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    Button b;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        firebaseAuth = FirebaseAuth.getInstance();

        b = findViewById(R.id.button);

//        Bundle bundle = getIntent().getExtras();
//
//        String uid = bundle.getString("uid"); //asi conseguimos el rol!!
//
//        Toast.makeText(MenuActivity.this, uid, Toast.LENGTH_LONG).show();
        //ASI TAMBIEN FUNCIONA!!
        Toast.makeText(MenuActivity.this, firebaseAuth.getUid(), Toast.LENGTH_LONG).show();


/**
 * HACER ESTO EN UNA RAMA APARTE Y LUEGO JUNTARLA, POR SI TE EQUIVOCAS!!
 */

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}