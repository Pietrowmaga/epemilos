package com.example.e_pemilos.model;

public class Kandidat {
    private int idKandidat;
    private int nomorUrut;
    private String namaKetua;
    private String namaWakil;
    private String visi;
    private String misi;
    private String proker;
    private String foto;
    private int jumlahSuara;

    public Kandidat() {}

    public Kandidat(int idKandidat, int nomorUrut, String namaKetua, String namaWakil,
                    String visi, String misi, String proker, String foto, int jumlahSuara) {
        this.idKandidat = idKandidat;
        this.nomorUrut = nomorUrut;
        this.namaKetua = namaKetua;
        this.namaWakil = namaWakil;
        this.visi = visi;
        this.misi = misi;
        this.proker = proker;
        this.foto = foto;
        this.jumlahSuara = jumlahSuara;
    }

    public int getIdKandidat() { return idKandidat; }
    public void setIdKandidat(int idKandidat) { this.idKandidat = idKandidat; }
    public int getNomorUrut() { return nomorUrut; }
    public void setNomorUrut(int nomorUrut) { this.nomorUrut = nomorUrut; }
    public String getNamaKetua() { return namaKetua; }
    public void setNamaKetua(String namaKetua) { this.namaKetua = namaKetua; }
    public String getNamaWakil() { return namaWakil; }
    public void setNamaWakil(String namaWakil) { this.namaWakil = namaWakil; }
    public String getVisi() { return visi; }
    public void setVisi(String visi) { this.visi = visi; }
    public String getMisi() { return misi; }
    public void setMisi(String misi) { this.misi = misi; }
    public String getProker() { return proker; }
    public void setProker(String proker) { this.proker = proker; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public int getJumlahSuara() { return jumlahSuara; }
    public void setJumlahSuara(int jumlahSuara) { this.jumlahSuara = jumlahSuara; }
}