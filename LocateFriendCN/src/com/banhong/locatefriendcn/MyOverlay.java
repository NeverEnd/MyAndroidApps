package com.banhong.locatefriendcn;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MyOverlay extends ItemizedOverlay{

    public MyOverlay(Drawable defaultMarker, MapView mapView) {
        super(defaultMarker, mapView);
    }
    

    @Override
    public boolean onTap(int index){
        OverlayItem item = getItem(index);
        //mCurItem = item ;
//        if (index == 3){
//            button.setText("这是一个系统控件");
//            GeoPoint pt = new GeoPoint((int) (mLat4 * 1E6),
//                    (int) (mLon4 * 1E6));
//            // 弹出自定义View
//            pop.showPopup(button, pt, 32);
//        }
//        else{
//           popupText.setText(getItem(index).getTitle());
//           Bitmap[] bitMaps={
//                BMapUtil.getBitmapFromView(popupLeft),      
//                BMapUtil.getBitmapFromView(popupInfo),      
//                BMapUtil.getBitmapFromView(popupRight)      
//            };
//            pop.showPopup(bitMaps,item.getPoint(),32);
//        }
        return true;
    }
    
    @Override
    public boolean onTap(GeoPoint pt , MapView mMapView){
//        if (pop != null){
//            pop.hidePop();
//            mMapView.removeView(button);
//        }
        return false;
    }
    
}