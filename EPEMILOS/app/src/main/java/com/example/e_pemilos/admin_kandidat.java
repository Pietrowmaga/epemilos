package com.example.e_pemilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class admin_kandidat extends AppCompatActivity {

    private TextInputEditText etNoPaslon, etNamaKetua, etNamaWakil, etVisiMisi;
    private Button btnSimpanKandidat, btnLogout;
    private View menuPeserta, menuHasil, btnToggleSidebar, sidebar;
    private DatabaseHelper dbHelper;
    private boolean isSidebarVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kandidat);

        dbHelper = new DatabaseHelper(this);

        sidebar = findViewById(R.id.sidebar);
        btnToggleSidebar = findViewById(R.id.btnToggleSidebar);
        etNoPaslon = findViewById(R.id.etNoPaslon);
        etNamaKetua = findViewById(R.id.etNamaKetua);
        etNamaWakil = findViewById(R.id.etNamaWakil);
        etVisiMisi = findViewById(R.id.etVisiMisi);
        btnSimpanKandidat = findViewById(R.id.btnSimpanKandidat);
        btnLogout = findViewById(R.id.btnLogout);
        menuPeserta = findViewById(R.id.menuPeserta);
        menuHasil = findViewById(R.id.menuHasil);

        if (btnToggleSidebar != null && sidebar != null) {
            btnToggleSidebar.setOnClickListener(v -> {
                if (isSidebarVisible) {
                    sidebar.setVisibility(View.GONE);
                } else {
                    sidebar.setVisibility(View.VISIBLE);
                }
                isSidebarVisible = !isSidebarVisible;
            });
        }

        btnSimpanKandidat.setOnClickListener(v -> {
            String noStr = etNoPaslon.getText().toString();
            String ketua = etNamaKetua.getText().toString();
            String wakil = etNamaWakil.getText().toString();
            String visiMisi = etVisiMisi.getText().toString();

            if (noStr.isEmpty() || ketua.isEmpty() || wakil.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi data!", Toast.LENGTH_SHORT).show();
                return;
            }

            int no = Integer.parseInt(noStr);
            long res = dbHelper.addKandidat(no, ketua, wakil, visiMisi);

            if (res != -1) {
                Toast.makeText(this, "Kandidat berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                etNoPaslon.setText("");
                etNamaKetua.setText("");
                etNamaWakil.setText("");
                etVisiMisi.setText("");
            } else {
                Toast.makeText(this, "Gagal menambahkan kandidat!", Toast.LENGTH_SHORT).show();
            }
        });

        if (menuPeserta != null) {
            menuPeserta.setOnClickListener(v -> {
                finish(); // Kembali ke activity admin (peserta)
            });
        }

        if (menuHasil != null) {
            menuHasil.setOnClickListener(v -> {
                Intent intent = new Intent(admin_kandidat.this, HasilActivity.class);
                startActivity(intent);
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(admin_kandidat.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}