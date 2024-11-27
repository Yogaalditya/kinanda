package com.example.project_akhirnov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {
    private Button btnLogout;
    private BottomNavigationView navigasi;
    private TextView tvName, tvEmail;
    private Koneksi database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        navigasi = findViewById(R.id.bottom_navigation);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);
        database = new Koneksi(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String username = prefs.getString("username", "Nama tidak ditemukan");
        String email = prefs.getString("email", "email tidak ditemukan");

        if (email == null || username == null) {
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            finish();
            return;
        }

        tvName.setText(username);
        tvEmail.setText(email);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hapus sesi login
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                // Arahkan ke halaman login
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                finish();
            }
        });

        navigasi.setOnItemSelectedListener(item -> {
            int itemid = item.getItemId();

            if (itemid == R.id.nav_home) {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                return true;
            } else if (itemid == R.id.nav_TopUp) {
                startActivity(new Intent(AccountActivity.this, TopUpActivity.class));
                return true;

            } else if (itemid == R.id.nav_history) {
                startActivity(new Intent(AccountActivity.this, HistoryActivity.class));
                return true;

            } else if (itemid == R.id.nav_account) {
                return true;
            }
            return false;
        });
        navigasi.setSelectedItemId(R.id.nav_account);
    }
}