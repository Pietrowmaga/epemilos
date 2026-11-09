package com.example.e_pemilos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_pemilos.model.Pemilih;

import java.util.List;

public class admin extends AppCompatActivity {

    private Button btnLogout;
    private View menuKandidat, menuHasil, btnTambah, btnHapus, btnUbah, btnToggleSidebar, sidebar;
    private TableLayout tableDataPemilih;
    private DatabaseHelper dbHelper;
    private int selectedPemilihId = -1;
    private Pemilih selectedPemilih = null;
    private boolean isSidebarVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_peserta);

        dbHelper = new DatabaseHelper(this);

        sidebar = findViewById(R.id.sidebar);
        btnToggleSidebar = findViewById(R.id.btnToggleSidebar);
        btnLogout = findViewById(R.id.btnLogout);
        menuKandidat = findViewById(R.id.menuKandidat);
        menuHasil = findViewById(R.id.menuHasil);
        btnTambah = findViewById(R.id.btnTambahPeserta);
        btnHapus = findViewById(R.id.btnHapusPeserta);
        btnUbah = findViewById(R.id.btnUbahPeserta);
        tableDataPemilih = findViewById(R.id.tableDataPemilih);

        loadDataPemilih();

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

        if (menuKandidat != null) {
            menuKandidat.setOnClickListener(v -> {
                Intent intent = new Intent(admin.this, admin_kandidat.class);
                startActivity(intent);
            });
        }

        if (menuHasil != null) {
            menuHasil.setOnClickListener(v -> {
                Intent intent = new Intent(admin.this, HasilActivity.class);
                startActivity(intent);
            });
        }

        if (btnTambah != null) {
            btnTambah.setOnClickListener(v -> showAddDialog());
        }

        if (btnHapus != null) {
            btnHapus.setOnClickListener(v -> {
                if (selectedPemilihId != -1) {
                    new AlertDialog.Builder(this)
                            .setTitle("Hapus Peserta")
                            .setMessage("Apakah Anda yakin ingin menghapus peserta ini?")
                            .setPositiveButton("Ya", (dialog, which) -> {
                                dbHelper.deletePemilih(selectedPemilihId);
                                loadDataPemilih();
                                selectedPemilihId = -1;
                                selectedPemilih = null;
                                Toast.makeText(this, "Peserta dihapus", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Tidak", null)
                            .show();
                } else {
                    Toast.makeText(this, "Pilih peserta terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnUbah != null) {
            btnUbah.setOnClickListener(v -> {
                if (selectedPemilihId != -1 && selectedPemilih != null) {
                    showEditDialog();
                } else {
                    Toast.makeText(this, "Pilih peserta terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(admin.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    private void loadDataPemilih() {
        tableDataPemilih.removeAllViews();
        List<Pemilih> list = dbHelper.getAllPemilih();

        for (Pemilih p : list) {
            TableRow row = new TableRow(this);
            row.setPadding(12, 12, 12, 12);
            row.setClickable(true);
            row.setFocusable(true);
            row.setOnClickListener(v -> {
                for (int i = 0; i < tableDataPemilih.getChildCount(); i++) {
                    tableDataPemilih.getChildAt(i).setBackgroundColor(0);
                }
                v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                selectedPemilihId = p.getIdPemilih();
                selectedPemilih = p;
            });

            TextView tvId = new TextView(this);
            tvId.setText(String.valueOf(p.getIdPemilih()));
            tvId.setTextColor(0xFFFFFFFF);
            tvId.setGravity(android.view.Gravity.CENTER);

            TextView tvToken = new TextView(this);
            tvToken.setText(p.getToken());
            tvToken.setTextColor(0xFFFFFFFF);
            tvToken.setGravity(android.view.Gravity.CENTER);

            TextView tvNama = new TextView(this);
            tvNama.setText(p.getNama());
            tvNama.setTextColor(0xFFFFFFFF);
            tvNama.setGravity(android.view.Gravity.CENTER);

            TextView tvStatusPeserta = new TextView(this);
            tvStatusPeserta.setText(p.getStatusPeserta());
            tvStatusPeserta.setTextColor(0xFFFFFFFF);
            tvStatusPeserta.setGravity(android.view.Gravity.CENTER);

            TextView tvVoted = new TextView(this);
            tvVoted.setText(p.getStatusMemilih() == 1 ? "Voted" : "Active");
            tvVoted.setTextColor(p.getStatusMemilih() == 1 ? 0xFFFF9800 : 0xFF4CAF50);
            tvVoted.setGravity(android.view.Gravity.CENTER);

            row.addView(tvId);
            row.addView(tvToken);
            row.addView(tvNama);
            row.addView(tvStatusPeserta);
            row.addView(tvVoted);

            tableDataPemilih.addView(row);
        }
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Peserta");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_peserta, null);
        EditText etToken = view.findViewById(R.id.etToken);
        EditText etNama = view.findViewById(R.id.etNama);
        EditText etStatusPeserta = view.findViewById(R.id.etStatusPeserta);
        builder.setView(view);
        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String token = etToken.getText().toString();
            String nama = etNama.getText().toString();
            String status = etStatusPeserta.getText().toString();
            if (!token.isEmpty() && !nama.isEmpty() && !status.isEmpty()) {
                dbHelper.addPemilih(token, nama, status);
                loadDataPemilih();
                Toast.makeText(this, "Peserta berhasil ditambah", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ubah Peserta");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_peserta, null);
        EditText etToken = view.findViewById(R.id.etToken);
        EditText etNama = view.findViewById(R.id.etNama);
        EditText etStatusPeserta = view.findViewById(R.id.etStatusPeserta);

        etToken.setText(selectedPemilih.getToken());
        etNama.setText(selectedPemilih.getNama());
        etStatusPeserta.setText(selectedPemilih.getStatusPeserta());

        builder.setView(view);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String token = etToken.getText().toString();
            String nama = etNama.getText().toString();
            String status = etStatusPeserta.getText().toString();
            if (!token.isEmpty() && !nama.isEmpty() && !status.isEmpty()) {
                dbHelper.updatePemilih(selectedPemilihId, token, nama, status);
                loadDataPemilih();
                selectedPemilihId = -1;
                selectedPemilih = null;
                Toast.makeText(this, "Peserta berhasil diubah", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Batal", null);
        builder.show();
    }
}