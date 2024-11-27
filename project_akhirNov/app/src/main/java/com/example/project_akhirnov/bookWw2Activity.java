package com.example.project_akhirnov;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class bookWw2Activity extends AppCompatActivity {
    private ImageButton backButton;
    private Koneksi database;
    private static final float BOOK_PRICE = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_ww2);
        backButton = findViewById(R.id.backButton);
        database = new Koneksi(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton.setOnClickListener(view -> finish());


        if (database.isBookPurchased("bookWw2")) {
            Toast.makeText(this, "Selamat Membaca", Toast.LENGTH_SHORT).show();
        } else {
            purchaseDialog();
        }
    }

    private void purchaseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String formatPrice = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(BOOK_PRICE);
        builder.setTitle("Beli Buku")
                .setMessage("Tolong beli e-book nya terlebih dahulu!! dengan harga Rp. " + formatPrice)
                .setCancelable(false)
                .setPositiveButton("Beli",(dialogInterface, i) -> purchaseBook())
                .setNegativeButton("Batal", (dialogInterface, i) -> finish());
        builder.show();
    }

    private void purchaseBook() {
        float currentBalance = database.getBalance();

        if (currentBalance >= BOOK_PRICE) {
            database.UpdateBalance(currentBalance - BOOK_PRICE);
            database.markPurchased("bookWw2");
            Toast.makeText(this, "Buku berhasil dibeli", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
        }
    }
}