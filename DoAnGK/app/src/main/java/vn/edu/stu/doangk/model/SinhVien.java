package vn.edu.stu.doangk.model;

import java.util.Date;

public class SinhVien {
    private String hoten;
    private String ma;
    private String lop;
    private Date ngaySinh;
    private String que;
    private String gioiTinh;
    private  int anh;

    public SinhVien(String hoten, String ma, String lop, Date ngaySinh, String que, String gioiTinh, int anh) {
        this.hoten = hoten;
        this.ma = ma;
        this.lop = lop;
        this.ngaySinh = ngaySinh;
        this.que = que;
        this.gioiTinh = gioiTinh;
        this.anh = anh;
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

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
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

    public int getAnh() {
        return anh;
    }

    public void setAnh(int anh) {
        this.anh = anh;
    }
}
