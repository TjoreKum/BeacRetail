package com.example.beacretail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CouponDeleteAdapter extends ArrayAdapter<Coupon> {


Context context;
int layoutResourceId;
ArrayList<Coupon> data = null;

public CouponDeleteAdapter(Activity context, int layoutResourceId, ArrayList<Coupon> data) {
    super(context, layoutResourceId, data);
    this.layoutResourceId = layoutResourceId;
    this.context = context;
    this.data = data;
}

@Override
public View getView(final int position, View convertView, ViewGroup parent) {
    View row = convertView;
    final CouponHolder holder;
    
    if(row == null)
    {
    	holder = new CouponHolder();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        
//        holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
        holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle1);
        holder.imgBtn = (ImageButton)row.findViewById(R.id.delete);
        holder.imgBtn
        .setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });
        row.setTag(holder);
        holder.imgBtn.setTag(data.get(position));
    }
    else
    {
    	holder = (CouponHolder) row.getTag();
    	holder.imgBtn.setTag(data.get(position));
    }
    
    Coupon coupon = data.get(position);
    holder.txtTitle.setText(coupon.getCouponDesc());
//    Bitmap bitmap = BitmapFactory.decodeFile(coupon.getCouponImg());
//    holder.imgIcon.setImageBitmap(bitmap);
    //holder.checkbox.setChecked(data.get(position).isSelected());
    
    return row;
}

static class CouponHolder
{
    ImageView imgIcon;
    TextView txtTitle;
    ImageButton imgBtn;
}
}