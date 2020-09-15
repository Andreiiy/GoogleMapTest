package com.atatar.googlemaptest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atatar.googlemaptest.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
        this.context = context;
    }

   private void rendowWindowText(final Marker marker, View view){
       String title = marker.getTitle();
       ImageView imageView = (ImageView)mWindow.findViewById(R.id.iv_info);
       if(title.equals("Staying in these places is prohibited by law"))
       imageView.setBackgroundResource(R.drawable.ic_quest);

       TextView info = (TextView)view.findViewById(R.id.tv_inform);
       if(title!=null)
       info.setText(title);

       Button btn_close = (Button)mWindow.findViewById(R.id.btn_clouse);
       btn_close.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               marker.hideInfoWindow();
           }
       });
   }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);

        return mWindow;
    }
}
