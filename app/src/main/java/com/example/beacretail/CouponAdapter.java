package com.example.beacretail;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CouponAdapter extends ArrayAdapter<Coupon> {


Context context; 
int layoutResourceId;    
ArrayList<Coupon> data = null;

public CouponAdapter(Activity context, int layoutResourceId,ArrayList<Coupon> data) {
    super(context, layoutResourceId, data);
    this.layoutResourceId = layoutResourceId;
    this.context = context;
    this.data = data;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
    View row = convertView;
    final CouponHolder holder;
    
    if(row == null)
    {
    	holder = new CouponHolder();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        
//        holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
        holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
        holder.checkbox = (CheckBox)row.findViewById(R.id.check);
        holder.checkbox
        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

        	
          @Override
          public void onCheckedChanged(CompoundButton buttonView,
              boolean isChecked) {
            Coupon element = (Coupon) holder.checkbox.getTag();
            element.setSelected(buttonView.isChecked());

          }
        });
        row.setTag(holder);
        holder.checkbox.setTag(data.get(position));
    }
    else
    {
    	holder = (CouponHolder) row.getTag();
    	holder.checkbox.setTag(data.get(position));
    }
    
    Coupon coupon = data.get(position);
    holder.txtTitle.setText(coupon.getCouponDesc());
//    Bitmap bitmap = BitmapFactory.decodeFile(coupon.getCouponImg());
//    holder.imgIcon.setImageBitmap(bitmap);
    holder.checkbox.setChecked(data.get(position).isSelected());
    
    return row;
}

static class CouponHolder
{
    ImageView imgIcon;
    TextView txtTitle;
    CheckBox checkbox;
}
}