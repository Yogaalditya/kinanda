package com.example.project_akhirnov;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView lvHistory;
    private Koneksi database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        lvHistory = findViewById(R.id.lvHistory);
        database = new Koneksi(this);
        BottomNavigationView navigasi = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navigasi.setOnItemSelectedListener(item -> {
            int itemid = item.getItemId();

            if (itemid == R.id.nav_home) {
                startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                return true;
            } else if (itemid == R.id.nav_TopUp) {
                startActivity(new Intent(HistoryActivity.this, TopUpActivity.class));
                return true;
            } else if (itemid == R.id.nav_history) {
                return true;
            } else if (itemid == R.id.nav_account) {
                startActivity(new Intent(HistoryActivity.this, AccountActivity.class));
                return true;

            }
            return false;
        });
        navigasi.setSelectedItemId(R.id.nav_history);

        List<String> historyList = database.getAllHistory();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, historyList
        );
        lvHistory.setAdapter(adapter);
    }
}