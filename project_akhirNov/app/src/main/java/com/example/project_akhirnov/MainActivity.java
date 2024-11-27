package com.example.project_akhirnov;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearBookAlbert, linearBookWw2, linearBookProgrammer;
    private TextView tvBalanceAmount, tvUserName;
    private Koneksi database;
    private Button btnLihatSemua;


    @Override
    protected void onResume() {
        super.onResume();

        float currentBalance = database.getBalance();

        String formatbalance = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(currentBalance);
        tvBalanceAmount.setText("Rp: " + formatbalance);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

         linearBookAlbert = findViewById(R.id.linearBookAlbert);
         linearBookWw2 = findViewById(R.id.linearBookWw2);
         linearBookProgrammer = findViewById(R.id.linearBookProgrammer);
         tvBalanceAmount = findViewById(R.id.tvBalanceAmount);
         tvUserName = findViewById(R.id.tvUserName);
         btnLihatSemua = findViewById(R.id.btnLihatSemua);
         database = new Koneksi(this);
        BottomNavigationView navigasi = findViewById(R.id.bottom_navigation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLihatSemua.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, view_lihat_semua.class));
        });



        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean isRegistered = prefs.getBoolean("isRegistered", false);
        String email = prefs.getString("email", null);

        if (!isRegistered || email == null) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            finish();
            return;
        }

        String username = database.getUserbyEmail(email);

        if (username != null) {
            tvUserName.setText(username);
            editor.putString("username", username);
            editor.apply();
        }



        linearBookAlbert.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, bookAlbertActivity.class));
        });

        linearBookWw2.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, bookWw2Activity.class));
        });

        linearBookProgrammer.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, bookProgrammerActivity.class));
        });


        navigasi.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {

            } else if (itemId == R.id.nav_TopUp) {

                startActivity(new Intent(MainActivity.this, TopUpActivity.class));

            }  else if (itemId == R.id.nav_history) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));

            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));


            }
            return false;
        });
        navigasi.setSelectedItemId(R.id.nav_home);
    }
}