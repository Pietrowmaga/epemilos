package com.example.e_pemilos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.e_pemilos.model.Kandidat;
import com.example.e_pemilos.model.Pemilih;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_pemilihan_osis.db";
    private static final int DATABASE_VERSION = 12; // Versi 12 untuk mereset database bersih

    private static final String CREATE_TABLE_KANDIDAT =
            "CREATE TABLE kandidat (" +
                    "id_kandidat INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nomor_urut INTEGER, " +
                    "nama_ketua TEXT, " +
                    "nama_wakil TEXT, " +
                    "visi TEXT, " +
                    "misi TEXT, " +
                    "proker TEXT, " +
                    "foto TEXT, " +
                    "jumlah_suara INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_PEMILIH =
            "CREATE TABLE pemilih (" +
                    "id_pemilih INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "token TEXT, " +
                    "nama TEXT, " +
                    "status_peserta TEXT, " +
                    "status_memilih INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_HASIL_VOTING =
            "CREATE TABLE hasil_voting (" +
                    "id_voting INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_pemilih INTEGER, " +
                    "id_kandidat INTEGER, " +
                    "waktu_memilih TEXT, " +
                    "FOREIGN KEY(id_pemilih) REFERENCES pemilih(id_pemilih), " +
                    "FOREIGN KEY(id_kandidat) REFERENCES kandidat(id_kandidat))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KANDIDAT);
        db.execSQL(CREATE_TABLE_PEMILIH);
        db.execSQL(CREATE_TABLE_HASIL_VOTING);
        insertDummyKandidat(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kandidat");
        db.execSQL("DROP TABLE IF EXISTS pemilih");
        db.execSQL("DROP TABLE IF EXISTS hasil_voting");
        onCreate(db);
    }

    private void insertDummyKandidat(SQLiteDatabase db) {
        ContentValues cv1 = new ContentValues();
        cv1.put("nomor_urut", 1);
        cv1.put("nama_ketua", "AHMAD MUZAKI");
        cv1.put("nama_wakil", "SITI AISYAH");
        cv1.put("visi", "Mewujudkan OSIS yang aktif, inovatif, dan berintegritas.");
        cv1.put("misi", "1. Meningkatkan kedisiplinan siswa.\n2. Mengadakan kegiatan seni dan olahraga rutin.");
        cv1.put("proker", "Pentas Seni Tahunan, Lomba Kebersihan Kelas");
        cv1.put("foto", "paslon1");
        db.insert("kandidat", null, cv1);

        ContentValues cv2 = new ContentValues();
        cv2.put("nomor_urut", 2);
        cv2.put("nama_ketua", "BUDI SANTOSO");
        cv2.put("nama_wakil", "LANI ARYANI");
        cv2.put("visi", "Menjadikan sekolah sebagai wadah kreativitas tanpa batas.");
        cv2.put("misi", "1. Mendukung klub robotik dan IT.\n2. Optimalisasi media sosial sekolah.");
        cv2.put("proker", "Workshop Digital, E-Sport Competition");
        cv2.put("foto", "paslon2");
        db.insert("kandidat", null, cv2);

        ContentValues cv3 = new ContentValues();
        cv3.put("nomor_urut", 3);
        cv3.put("nama_ketua", "CITRA LESTARI");
        cv3.put("nama_wakil", "DEDI KURNIAWAN");
        cv3.put("visi", "Sekolah Nyaman, Siswa Berprestasi.");
        cv3.put("misi", "1. Revitalisasi taman sekolah.\n2. Program bimbingan belajar teman sebaya.");
        cv3.put("proker", "Green School Project, Study Club");
        cv3.put("foto", "paslon3");
        db.insert("kandidat", null, cv3);
    }

    // Impor data JSON dengan pengembalian status string pesan diagnostic
    public String importDataFromAssets(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Jika data sudah diimpor, kembalikan jumlahnya
        int totalExisting = getJumlahPemilih();
        if (totalExisting > 0) {
            return "SUCCESS:" + totalExisting;
        }

        int importedCount = 0;
        db.beginTransaction();
        try {
            InputStream is = context.getAssets().open("peserta.json");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            is.close();

            String jsonStr = baos.toString("UTF-8").trim();

            if (jsonStr.isEmpty()) {
                return "ERROR: File peserta.json di folder assets masih KOSONG!";
            }

            JSONArray jsonArray = new JSONArray(jsonStr);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put("nama", obj.getString("nama"));
                cv.put("status_peserta", obj.getString("status"));
                cv.put("token", obj.getString("token"));
                cv.put("status_memilih", 0);

                db.insert("pemilih", null, cv);
                importedCount++;
            }
            db.setTransactionSuccessful();
            return "SUCCESS:" + importedCount;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: Gagal membaca JSON -> " + e.getMessage();
        } finally {
            db.endTransaction();
        }
    }

    public int getJumlahPemilih() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM pemilih", null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public List<Kandidat> getAllKandidat() {
        List<Kandidat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM kandidat", null);
        if (cursor.moveToFirst()) {
            do {
                Kandidat k = new Kandidat(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_kandidat")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("nomor_urut")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nama_ketua")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nama_wakil")),
                        cursor.getString(cursor.getColumnIndexOrThrow("visi")),
                        cursor.getString(cursor.getColumnIndexOrThrow("misi")),
                        cursor.getString(cursor.getColumnIndexOrThrow("proker")),
                        cursor.getString(cursor.getColumnIndexOrThrow("foto")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("jumlah_suara"))
                );
                list.add(k);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public long addKandidat(int nomorUrut, String namaKetua, String namaWakil, String visiMisi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nomor_urut", nomorUrut);
        cv.put("nama_ketua", namaKetua);
        cv.put("nama_wakil", namaWakil);
        cv.put("visi", visiMisi);
        cv.put("misi", "");
        cv.put("proker", "");
        cv.put("foto", "");
        return db.insert("kandidat", null, cv);
    }

    public boolean voteKandidat(int idPemilih, int idKandidat, String waktu) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE kandidat SET jumlah_suara = jumlah_suara + 1 WHERE id_kandidat = " + idKandidat);
        db.execSQL("UPDATE pemilih SET status_memilih = 1 WHERE id_pemilih = " + idPemilih);

        ContentValues cv = new ContentValues();
        cv.put("id_pemilih", idPemilih);
        cv.put("id_kandidat", idKandidat);
        cv.put("waktu_memilih", waktu);
        long res = db.insert("hasil_voting", null, cv);

        return res != -1;
    }

    public Kandidat getKandidatByNomor(int nomor) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("kandidat", null, "nomor_urut = ?", new String[]{String.valueOf(nomor)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Kandidat k = new Kandidat(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_kandidat")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("nomor_urut")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nama_ketua")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nama_wakil")),
                    cursor.getString(cursor.getColumnIndexOrThrow("visi")),
                    cursor.getString(cursor.getColumnIndexOrThrow("misi")),
                    cursor.getString(cursor.getColumnIndexOrThrow("proker")),
                    cursor.getString(cursor.getColumnIndexOrThrow("foto")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("jumlah_suara"))
            );
            cursor.close();
            return k;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public List<Pemilih> getAllPemilih() {
        List<Pemilih> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pemilih", null);
        if (cursor.moveToFirst()) {
            do {
                Pemilih p = new Pemilih(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_pemilih")),
                        cursor.getString(cursor.getColumnIndexOrThrow("token")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nama")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status_peserta")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("status_memilih"))
                );
                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Pemilih checkLogin(String nama, String status, String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM pemilih WHERE LOWER(TRIM(nama)) = LOWER(TRIM(?)) " +
                "AND LOWER(TRIM(status_peserta)) = LOWER(TRIM(?)) " +
                "AND TRIM(token) = TRIM(?)";

        Cursor cursor = db.rawQuery(query, new String[]{nama, status, token});

        if (cursor != null && cursor.moveToFirst()) {
            Pemilih p = new Pemilih(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_pemilih")),
                    cursor.getString(cursor.getColumnIndexOrThrow("token")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nama")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status_peserta")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("status_memilih"))
            );
            cursor.close();
            return p;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public long addPemilih(String token, String nama, String statusPeserta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("token", token);
        cv.put("nama", nama);
        cv.put("status_peserta", statusPeserta);
        cv.put("status_memilih", 0);
        return db.insert("pemilih", null, cv);
    }

    public int updatePemilih(int id, String token, String nama, String statusPeserta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("token", token);
        cv.put("nama", nama);
        cv.put("status_peserta", statusPeserta);
        return db.update("pemilih", cv, "id_pemilih = ?", new String[]{String.valueOf(id)});
    }

    public int deletePemilih(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("pemilih", "id_pemilih = ?", new String[]{String.valueOf(id)});
    }
}