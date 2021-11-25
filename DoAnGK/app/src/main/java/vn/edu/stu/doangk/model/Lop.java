package vn.edu.stu.doangk.model;

import java.io.Serializable;
import java.util.ArrayList;

import vn.edu.stu.doangk.R;

public class Lop implements Serializable {
    private  String id;
    private String ten;


    public Lop(String id, String ten) {
        this.id = id;
        this.ten = ten;

    }

    public Lop(String ten) {
        this.ten = ten;
    }

    public Lop() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }



    @Override
    public String toString() {

        return  id +"\n"+ ten;
    }
}
