package vn.edu.stu.doangk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.stu.doangk.R;
import vn.edu.stu.doangk.model.SinhVien;

public class SinhVienAdapter extends ArrayAdapter<SinhVien> {
    Activity context;
    int resource;
    List<SinhVien> objects;
    public SinhVienAdapter(@NonNull Activity context, int resource, @NonNull List<SinhVien> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=null;
        LayoutInflater inflater=this.context.getLayoutInflater();
        view=inflater.inflate(this.resource,null);
        ImageView imgAnh=view.findViewById(R.id.imgItemStu);
        TextView txtTen=view.findViewById(R.id.txtItemTen);
        TextView txtMa=view.findViewById(R.id.txtItemMa);
        TextView txtLop=view.findViewById(R.id.txtItemLop);
        SinhVien sv=this.objects.get(position);
        Bitmap img = BitmapFactory.decodeByteArray(sv.getAnh(),0,sv.getAnh().length);
        imgAnh.setImageBitmap(img);
        txtTen.setText(sv.getHoten());
        txtMa.setText(sv.getMa());
        txtLop.setText(sv.getLop());

        return view;
    }

}
