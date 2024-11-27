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

public class bookATG extends AppCompatActivity {
    private ImageButton backButton;
    private Koneksi database;
    private static final float BOOK_PRICE = 25000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_atg);
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

        if (database.isBookPurchased("bookATG")) {
            Toast.makeText(this, "Selamat Membaca", Toast.LENGTH_SHORT).show();
        } else {
            PurchaseDialog();
        }


    }

    private void PurchaseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Beli buku");

        String formatPrice = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(BOOK_PRICE);
        builder.setMessage("Tolong Beli e-book nya terlebih dahulu!! Dengan Harga Rp." + formatPrice);
        builder.setCancelable(false);
        builder.setPositiveButton("Beli", (dialogInterface, i) -> {
            if (purchaseBook()) {
                Toast.makeText(this, "buku berhasil dibeli", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "saldo tidak cukup, pembelian gagal", Toast.LENGTH_SHORT).show();
                PurchaseDialog();
            }
        });
        builder.setNegativeButton("Batal", (dialogInterface, i) -> finish());
        builder.show();
    }

    private boolean purchaseBook() {
        float currentBalance = database.getBalance();

        if (currentBalance >= BOOK_PRICE) {
            database.UpdateBalance(currentBalance - BOOK_PRICE);
            database.markPurchased("bookATG");
            Toast.makeText(this, "Buku berhasil dibeli", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saldo Tidak cukup", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}