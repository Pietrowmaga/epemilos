package com.example.e_pemilos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_pemilos.model.Kandidat;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class KandidatActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int idPemilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kandidat);

        dbHelper = new DatabaseHelper(this);
        idPemilih = getIntent().getIntExtra("ID_PEMILIH", -1);

        MaterialCardView card1 = findViewById(R.id.cardKandidat1);
        MaterialCardView card2 = findViewById(R.id.cardKandidat2);
        MaterialCardView card3 = findViewById(R.id.cardKandidat3);

        TextView tvNama1 = findViewById(R.id.tvNamaKandidat1);
        TextView tvNama2 = findViewById(R.id.tvNamaKandidat2);
        TextView tvNama3 = findViewById(R.id.tvNamaKandidat3);

        // Menghubungkan ImageView mengikut ID dari XML anda
        ImageView iv1 = findViewById(R.id.ivKandidat1);
        ImageView iv2 = findViewById(R.id.ivKandidat2);
        ImageView iv3 = findViewById(R.id.ivKandidat3);

        // Memaparkan gambar dari folder drawable (paslon1, paslon2, paslon3)
        if (iv1 != null) iv1.setImageResource(R.drawable.paslon1);
        if (iv2 != null) iv2.setImageResource(R.drawable.paslon2);
        if (iv3 != null) iv3.setImageResource(R.drawable.paslon3);

        loadKandidatNames(tvNama1, tvNama2, tvNama3);

        if (card1 != null) card1.setOnClickListener(v -> openDetail(1));
        if (card2 != null) card2.setOnClickListener(v -> openDetail(2));
        if (card3 != null) card3.setOnClickListener(v -> openDetail(3));
    }

    private void loadKandidatNames(TextView tv1, TextView tv2, TextView tv3) {
        List<Kandidat> list = dbHelper.getAllKandidat();
        for (Kandidat k : list) {
            if (k.getNomorUrut() == 1 && tv1 != null) {
                tv1.setText(k.getNamaKetua() + " - " + k.getNamaWakil());
            } else if (k.getNomorUrut() == 2 && tv2 != null) {
                tv2.setText(k.getNamaKetua() + " - " + k.getNamaWakil());
            } else if (k.getNomorUrut() == 3 && tv3 != null) {
                tv3.setText(k.getNamaKetua() + " - " + k.getNamaWakil());
            }
        }
    }

    private void openDetail(int nomorUrut) {
        Intent intent = new Intent(this, DetailKandidatActivity.class);
        intent.putExtra("NOMOR_URUT", nomorUrut);
        intent.putExtra("ID_PEMILIH", idPemilih);
        startActivity(intent);
    }
}