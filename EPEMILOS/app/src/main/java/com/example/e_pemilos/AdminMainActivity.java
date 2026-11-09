package com.example.e_pemilos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class AdminMainActivity extends AppCompatActivity {

    private TextInputEditText etAdminUser, etAdminPass;
    private Button btnAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        etAdminUser = findViewById(R.id.etAdminUser);
        etAdminPass = findViewById(R.id.etAdminPass);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        btnAdminLogin.setOnClickListener(v -> {
            String username = etAdminUser.getText().toString().trim();
            String password = etAdminPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password harus diisi!", Toast.LENGTH_SHORT).show();
            } else {
                if (username.equals("admin") && password.equals("admin123")) {
                    Toast.makeText(this, "Login Admin Berhasil!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminMainActivity.this, admin.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Username atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}