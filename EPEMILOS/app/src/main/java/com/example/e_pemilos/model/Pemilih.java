package com.example.e_pemilos.model;

public class Pemilih {
    private int idPemilih;
    private String token;
    private String nama;
    private String statusPeserta;
    private int statusMemilih;

    public Pemilih() {}

    public Pemilih(int idPemilih, String token, String nama, String statusPeserta, int statusMemilih) {
        this.idPemilih = idPemilih;
        this.token = token;
        this.nama = nama;
        this.statusPeserta = statusPeserta;
        this.statusMemilih = statusMemilih;
    }

    public int getIdPemilih() { return idPemilih; }
    public void setIdPemilih(int idPemilih) { this.idPemilih = idPemilih; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getStatusPeserta() { return statusPeserta; }
    public void setStatusPeserta(String statusPeserta) { this.statusPeserta = statusPeserta; }
    public int getStatusMemilih() { return statusMemilih; }
    public void setStatusMemilih(int statusMemilih) { this.statusMemilih = statusMemilih; }
}