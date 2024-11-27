package com.example.project_akhirnov;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class view_lihat_semua extends AppCompatActivity {
    private LinearLayout linearBookAlbert, linearBookWw2, linearBookProgrammer, linearBookATG;
    private ImageButton backButton;
    private Koneksi database;
    private static final float BOOK_PRICE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_lihat_semua);
        linearBookAlbert = findViewById(R.id.linearBookAlbert);
        linearBookWw2 = findViewById(R.id.linearBookWw2);
        linearBookProgrammer = findViewById(R.id.linearBookProgrammer);
        linearBookATG = findViewById(R.id.linearBookATG);
        backButton = findViewById(R.id.backButton);
        database = new Koneksi(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backButton.setOnClickListener(view -> {
            finish();
        });

        linearBookAlbert.setOnClickListener(view -> {
            Intent intent = new Intent(view_lihat_semua.this, bookAlbertActivity.class);
            startActivity(intent);

        });

        linearBookWw2.setOnClickListener(view -> {
            Intent intent = new Intent(view_lihat_semua.this, bookWw2Activity.class);
            startActivity(intent);

        });

        linearBookProgrammer.setOnClickListener(view -> {
            Intent intent = new Intent(view_lihat_semua.this, bookProgrammerActivity.class);
            startActivity(intent);

        });

        linearBookATG.setOnClickListener(view -> {
            startActivity(new Intent(view_lihat_semua.this, bookATG.class));

        });
    }
}