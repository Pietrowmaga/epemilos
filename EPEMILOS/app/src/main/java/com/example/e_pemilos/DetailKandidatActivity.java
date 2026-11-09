package com.example.e_pemilos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_pemilos.model.Kandidat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailKandidatActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int nomorUrut;
    private int idPemilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kandidat);

        dbHelper = new DatabaseHelper(this);
        nomorUrut = getIntent().getIntExtra("NOMOR_URUT", 1);
        idPemilih = getIntent().getIntExtra("ID_PEMILIH", -1);

        ImageView ivFoto = findViewById(R.id.ivDetailFoto);
        TextView tvNama = findViewById(R.id.tvDetailNama);
        TextView tvVisi = findViewById(R.id.tvDetailVisi);
        TextView tvMisi = findViewById(R.id.tvDetailMisi);
        TextView tvProker = findViewById(R.id.tvDetailProker);
        Button btnPilih = findViewById(R.id.btnPilih);

        // Ambil data kandidat secara dinamis dari database berdasarkan nomor urut
        Kandidat selected = dbHelper.getKandidatByNomor(nomorUrut);

        if (selected != null) {
            // Samakan format nama dengan yang ada di halaman list
            tvNama.setText("Paslon 0" + selected.getNomorUrut() + "\n" + selected.getNamaKetua() + " - " + selected.getNamaWakil());
            tvVisi.setText(selected.getVisi());
            tvMisi.setText(selected.getMisi());
            tvProker.setText(selected.getProker());

            // Set foto jika ada (saat ini menggunakan placeholder default)
            int resId = getResources().getIdentifier(selected.getFoto(), "drawable", getPackageName());
            if (resId != 0) {
                ivFoto.setImageResource(resId);
            }
        }

        btnPilih.setOnClickListener(v -> {
            if (selected != null) {
                prosesVoting(selected.getIdKandidat());
            } else {
                Toast.makeText(this, "Data kandidat tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prosesVoting(int idKandidat) {
        if (idPemilih == -1) {
            Toast.makeText(this, "Sesi tidak valid, silakan login ulang", Toast.LENGTH_SHORT).show();
            return;
        }

        String waktuSekarang = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        
        boolean sukses = dbHelper.voteKandidat(idPemilih, idKandidat, waktuSekarang);

        if (sukses) {
            Intent intent = new Intent(DetailKandidatActivity.this, SuccessActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Gagal melakukan voting!", Toast.LENGTH_SHORT).show();
        }
    }
}