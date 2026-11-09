package com.example.e_pemilos;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_pemilos.model.Kandidat;

import java.util.List;

public class HasilActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout containerHasil;
    private Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        dbHelper = new DatabaseHelper(this);
        containerHasil = findViewById(R.id.containerHasil);
        btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> finish());

        displayResults();
    }

    private void displayResults() {
        List<Kandidat> list = dbHelper.getAllKandidat();
        
        // Hitung total suara
        int totalSuara = 0;
        for (Kandidat k : list) {
            totalSuara += k.getJumlahSuara();
        }

        for (Kandidat k : list) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(0, 0, 0, 24);

            TextView tvPaslon = new TextView(this);
            tvPaslon.setText(getString(R.string.paslon_format, k.getNomorUrut(), k.getNamaKetua(), k.getNamaWakil()));
            tvPaslon.setTextColor(0xFFF9B637);
            tvPaslon.setTextSize(18);
            tvPaslon.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvSuara = new TextView(this);
            tvSuara.setText(getString(R.string.vote_count_format, k.getJumlahSuara()));
            tvSuara.setTextColor(0xFFFFFFFF);
            tvSuara.setTextSize(16);

            itemLayout.addView(tvPaslon);
            itemLayout.addView(tvSuara);
            
            containerHasil.addView(itemLayout);
        }
        
        TextView tvTotal = new TextView(this);
        tvTotal.setText(getString(R.string.total_votes_format, totalSuara));
        tvTotal.setTextColor(0xFFF9B637);
        tvTotal.setTextSize(20);
        tvTotal.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTotal.setGravity(Gravity.CENTER);
        tvTotal.setPadding(0, 32, 0, 0);
        
        containerHasil.addView(tvTotal);
    }
}