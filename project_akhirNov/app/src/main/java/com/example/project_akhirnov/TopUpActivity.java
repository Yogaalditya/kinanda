package com.example.project_akhirnov;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.Locale;

public class TopUpActivity extends AppCompatActivity {
    private Button btnTopUp;
    private CardView cardGopay, cardDana, cardTelkom;
    private TextView tvHargaGopay, tvHargaDana, tvHargaTelkom, tvBalance;
    private EditText etAmount;
    private Koneksi database;

    private float adminFeeGopay = 0.10f;
    private float adminFeeDana = 0.11f;
    private float adminFeeTelkom = 0.15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top_up);

        btnTopUp = findViewById(R.id.btnTopUp);
        cardGopay = findViewById(R.id.cardGopay);
        cardDana = findViewById(R.id.cardDana);
        cardTelkom = findViewById(R.id.cardTelkom);
        tvHargaGopay = findViewById(R.id.tvHargaGopay);
        tvHargaDana = findViewById(R.id.tvHargaDana);
        tvHargaTelkom = findViewById(R.id.tvHargaTelkom);
        tvBalance = findViewById(R.id.tvBalance);
        etAmount = findViewById(R.id.etAmount);
        database = new Koneksi(this);


        BottomNavigationView navigasi = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        updateBalance();
        
        btnTopUp.setOnClickListener(view -> {
            String amountText = etAmount.getText().toString();
            String selectedMethod = getSelectedMethod();

            if (!TextUtils.isEmpty(amountText)) {
                float amount = Float.parseFloat(amountText);

                if (amount <= 10000) {
                    Toast.makeText(this, "minimal Top-Up Rp 10.000!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                float CurrentBalance = database.getBalance();

                float updateBalance = CurrentBalance + amount;

                database.UpdateBalance(updateBalance);

                database.addHistory(amount, selectedMethod);

                startActivity(new Intent(TopUpActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Lengkapi jumlah saldo dan metode pembayaran", Toast.LENGTH_SHORT).show();
            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String inputAmount = charSequence.toString().trim();
                if (!TextUtils.isEmpty(inputAmount)) {
                    float updateAmount = Float.parseFloat(inputAmount);
                    if (updateAmount >= 10000) {
                        updateMethodPrice(updateAmount);
                    } else {
                        resetPrices();
                    }

                } else {
                    resetPrices();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cardGopay.setOnClickListener(view -> selectPayment(cardGopay));
        cardDana.setOnClickListener(view -> selectPayment(cardDana));
        cardTelkom.setOnClickListener(view -> selectPayment(cardTelkom));

        defaultColor();


        navigasi.setOnItemSelectedListener(item -> {
            int itemid = item.getItemId();

            if (itemid == R.id.nav_home) {
                startActivity(new Intent(TopUpActivity.this, MainActivity.class));

            } else if (itemid == R.id.nav_TopUp) {

            } else if (itemid == R.id.nav_history) {
                startActivity(new Intent(TopUpActivity.this, HistoryActivity.class));

            } else if (itemid == R.id.nav_account) {
                startActivity(new Intent(TopUpActivity.this, AccountActivity.class));


            }
            return false;
        });
        navigasi.setSelectedItemId(R.id.nav_TopUp);

        defaultColor();

    }

    private void updateBalance() {
        float currentBalance = database.getBalance();


        String formatBalance = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(currentBalance);
        tvBalance.setText("Balance: Rp" + formatBalance);
    }

    private String getSelectedMethod() {
        if (cardGopay.getCardBackgroundColor().getDefaultColor() ==
                ContextCompat.getColor(this, R.color.selected_card_color)) {
            return "Gopay";
        } else if (cardDana.getCardBackgroundColor().getDefaultColor() ==
                ContextCompat.getColor(this, R.color.selected_card_color)) {
            return "Dana";
        } else if (cardTelkom.getCardBackgroundColor().getDefaultColor() ==
                ContextCompat.getColor(this, R.color.selected_card_color)) {
            return "Telkomsel";
        }
        return null;
    }

    private void resetPrices() {
        // Reset harga jika input kosong
        tvHargaGopay.setText("Rp. -");
        tvHargaDana.setText("Rp. -");
        tvHargaTelkom.setText("Rp. -");
        btnTopUp.setVisibility(View.GONE);

        // Sembunyikan tombol "Top Up" jika input kosong
        btnTopUp.setVisibility(View.GONE);
    }

    private void updateMethodPrice(float updateAmount) {
        // Update harga dengan biaya admin
        String gopayPrice = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(updateAmount + (updateAmount * adminFeeGopay));
        String danaPrice = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(updateAmount + (updateAmount * adminFeeDana));
        String telkomPrice = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(updateAmount + (updateAmount * adminFeeTelkom));


        tvHargaGopay.setText("Rp: " + gopayPrice);
        tvHargaDana.setText("Rp: " +danaPrice);
        tvHargaTelkom.setText("Rp: " + telkomPrice);

        // Menampilkan tombol "Top Up" jika ada jumlah yang dimasukkan
        btnTopUp.setVisibility(View.VISIBLE);
    }


    private void defaultColor() {
        cardDana.setCardBackgroundColor(ContextCompat.getColor(this,R.color.default_card_color));
        cardGopay.setCardBackgroundColor(ContextCompat.getColor(this,R.color.default_card_color));
        cardTelkom.setCardBackgroundColor(ContextCompat.getColor(this,R.color.default_card_color));
    }// kenapa harus ada thisnya

    private void selectPayment(CardView selectCard) {
        defaultColor();

        selectCard.setCardBackgroundColor(getResources().getColor(R.color.selected_card_color));

        btnTopUp.setVisibility(View.VISIBLE);


    }

}
