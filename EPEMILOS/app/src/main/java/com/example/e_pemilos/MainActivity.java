package com.example.e_pemilos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.e_pemilos.model.Pemilih;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etNama, etStatus, etToken;
    private Button btnMasuk;
    private ImageView imgKotakSuara;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Panggil fungsi impor dan dapatkan laporan statusnya
        String hasilImpor = dbHelper.importDataFromAssets(this);

        if (hasilImpor.startsWith("SUCCESS:")) {
            String jumlah = hasilImpor.replace("SUCCESS:", "");
            Toast.makeText(this, "Berhasil terhubung! Total Data: " + jumlah, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, hasilImpor, Toast.LENGTH_LONG).show();
        }

        etNama = findViewById(R.id.etNama);
        etStatus = findViewById(R.id.etStatus);
        etToken = findViewById(R.id.etToken);
        btnMasuk = findViewById(R.id.btnMasuk);
        imgKotakSuara = findViewById(R.id.imgKotakSuara);

        btnMasuk.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String status = etStatus.getText().toString().trim();
            String token = etToken.getText().toString().trim();

            if (nama.isEmpty()) {
                etNama.setError("Nama harus diisi!");
                etNama.requestFocus();
            } else if (status.isEmpty()) {
                etStatus.setError("Status harus diisi!");
                etStatus.requestFocus();
            } else if (token.isEmpty()) {
                etToken.setError("Token harus diisi!");
                etToken.requestFocus();
            } else {
                Pemilih p = dbHelper.checkLogin(nama, status, token);
                if (p != null) {
                    if (p.getStatusMemilih() == 1) {
                        Toast.makeText(MainActivity.this, "Anda sudah menggunakan hak suara!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Selamat Datang, " + nama, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, KandidatActivity.class);
                        intent.putExtra("ID_PEMILIH", p.getIdPemilih());
                        intent.putExtra("NAMA", p.getNama());
                        intent.putExtra("STATUS", p.getStatusPeserta());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Data tidak ditemukan. Silakan cek kembali!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (imgKotakSuara != null) {
            imgKotakSuara.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AdminMainActivity.class);
                startActivity(intent);
            });
        }
    }
}