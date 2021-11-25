package vn.edu.stu.doangk.model;

import java.io.Serializable;
import java.util.Date;

public class SinhVien implements Serializable {
    private String hoten;
    private String ma;
    private int namSinh;
    private String que;
    private String gioiTinh;
    private byte[] anh;
    private String lop;

    public SinhVien(String hoten, String ma, int namSinh, String que, String gioiTinh, byte[] anh, String lop) {
        this.hoten = hoten;
        this.ma = ma;
        this.namSinh = namSinh;
        this.que = que;
        this.gioiTinh = gioiTinh;
        this.anh = anh;
        this.lop = lop;
    }

    public SinhVien() {
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }
}
