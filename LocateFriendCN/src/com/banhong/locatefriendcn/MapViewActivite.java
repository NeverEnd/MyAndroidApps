package com.banhong.locatefriendcn;

import java.io.IOException;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;



import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MapViewActivite extends Activity {

    private MapView mapview=null;
    private MapController mapcontrol=null;
    private Button friendlistButton = null;
    private ProgressDialog progress = null;
    private MyOverlay overlay = null;
    
    /**
     *  用MapController完成地图控制 
     */
    private MapController mMapController = null;
    /**
     *  MKMapViewListener 用于处理地图事件回调
     */
    MKMapViewListener mMapListener = null;
    
    private FriendListPopView popView;
    
    class Coordinates{
        public int x=0;
        public int y=0;        
    }
    
    List<Button> button_list = new ArrayList<Button>();
    private List<Coordinates> FriendCoordinatesList = new ArrayList<Coordinates>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    	getLayoutInflater().inflate(R.layout.list_pop, null, false);
        super.onCreate(savedInstanceState);
        
        MapApp mapApp= (MapApp)getApplication();
        if(mapApp.mBMapManager == null)
        {
            mapApp.initEngineManager(this);
        }
        
        setContentView(R.layout.map_view_activite);
        friendlistButton = (Button)findViewById(R.id.FirendListPopButton);
        friendlistButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //new HttpConnectionTask().execute("http://locatefriend.sinaapp.com/GetFriendList");
                new HttpConnectionTask().execute("http://locate0friend.duapp.com/GetFriendList");
//                popView = new FriendListPopView(MapViewActivite.this, 200, 150, true);                
//                popView.show(v);
                progress = new ProgressDialog(MapViewActivite.this);
                progress.setCanceledOnTouchOutside(false);
                progress.setMessage("loading friends list");
                progress.show();
            }
        });
        mapview = (MapView)findViewById(R.id.bmapView);
        mapcontrol = mapview.getController();
        /**
         * 获取地图控制器
         */
        mMapController = mapview.getController();
        /**
         *  设置地图是否响应点击事件  .
         */
        mMapController.enableClick(true);
        /**
         * 设置地图缩放级别
         */
        mMapController.setZoom(16);
       
        /**
         * 将地图移动至指定点
         * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index.html查询地理坐标
         * 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
         */
        overlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_gcoding), mapview);
        mapview.getOverlays().add(overlay);
        GeoPoint p ;
        double cLat = 39.89056;//39.945 ;
        double cLon = 116.468947;//116.404 ;
        Intent  intent = getIntent();
        if ( intent.hasExtra("x") && intent.hasExtra("y") ){
            //当用intent参数时，设置中心点为指定点
            Bundle b = intent.getExtras();
            p = new GeoPoint(b.getInt("y"), b.getInt("x"));
        }else{
             p = new GeoPoint((int)(cLat * 1E6), (int)(cLon * 1E6));
        }
        
        mMapController.setCenter(p);
        
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mMapListener = new MKMapViewListener() {
            @Override
            public void onMapMoveFinish() {
                /**
                 * 在此处理地图移动完成回调
                 * 缩放，平移等操作完成后，此回调被触发
                 */
            }
            
            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo) {
                /**
                 * 在此处理底图poi点击事件
                 * 显示底图poi名称并移动至该点
                 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
                 * 
                 */
                String title = "";
                if (mapPoiInfo != null){
                    title = mapPoiInfo.strText;
                    Toast.makeText(MapViewActivite.this,title,Toast.LENGTH_SHORT).show();
                    mMapController.animateTo(mapPoiInfo.geoPt);
                }
            }

            @Override
            public void onGetCurrentMap(Bitmap b) {
                /**
                 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
                 *  可在此保存截图至存储设备
                 */
            }

            @Override
            public void onMapAnimationFinish() {
                /**
                 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
                 */
            }
            /**
             * 在此处理地图载完成事件 
             */
            @Override
            public void onMapLoadFinish() {
                Toast.makeText(MapViewActivite.this, 
                               "地图加载完成", 
                               Toast.LENGTH_SHORT).show();
                
            }
        };
        mapview.regMapViewListener(MapApp.getInstance().mBMapManager, mMapListener);
    }
    
    @Override
    protected void onPause() {
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mapview.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        mapview.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        /**
         *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
         */
        mapview.destroy();
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
        
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mapview.onRestoreInstanceState(savedInstanceState);
    }
    
    private class HttpConnectionTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            Log.w("banhong", "doInBackground start");
            HttpClient httpclient = new DefaultHttpClient();
            String a = params[0];
            HttpGet httpget = new HttpGet(params[0]);

            try{
                HttpResponse response = httpclient.execute(httpget);
                if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
                {                    
                    String result =EntityUtils.toString(response.getEntity());
                    Log.w("banhong", "the result json string="+result);
                  

                    return result;
                }
                
                Log.w("banhong", "the response status code="+response.getStatusLine().getStatusCode());
                            
            }catch(IOException e)
            {
                e.printStackTrace();
                
            } 
            //publishProgress(name);
            return null;
        }
        
        protected void onPostExecute(String result) {
            if(result != null)
            {
                String[] namelist = null;
                String name = null;
                
                try{
                    JSONArray json=new JSONArray(result);
                    
                    namelist = new String[json.length()];
                    FriendCoordinatesList.clear();
                    for(int i=0; i < json.length(); i++)
                    {
                        JSONObject obj = json.getJSONObject(i);
                        name=obj.getString("name");        
                        Log.w("banhong", "the result name="+name);
                        namelist[i]=name;
                        Coordinates coord= new Coordinates();
                        coord.x= obj.getInt("x");
                        coord.y= obj.getInt("y");
                        FriendCoordinatesList.add(coord);
                    }
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }


                creatFriendlist(namelist);       
            }
        };
        
        public List<Button> creatFriendlist(String[] name)
        {
            if(name.length > 0)
            {
                button_list.clear();
                for(int i= 0; i < name.length; i++)
                {
                    Button button = new Button(MapViewActivite.this);
                    button.setText(name[i]);
                    button.setId(i);
                    button.setVisibility(View.VISIBLE);
                    //button.setClickable(false);
                    button.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            OnButtonItemClick(v.getId());
                        }
                    });
                    button_list.add(i, button);
                }

                ListView list = new ListView(MapViewActivite.this);
                ListAdapter adapter = new ListAdapter() {
                    
                    @Override
                    public void unregisterDataSetObserver(DataSetObserver observer) {
                        // TODO Auto-generated method stub
                       
                    }
                    
                    @Override
                    public void registerDataSetObserver(DataSetObserver observer) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public boolean isEmpty() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public boolean hasStableIds() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public int getViewTypeCount() {
                        // TODO Auto-generated method stub
                        return 1;
                    }
                    
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // TODO Auto-generated method stub
                        return button_list.get(position);
                    }
                    
                    @Override
                    public int getItemViewType(int position) {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public long getItemId(int position) {
                        // TODO Auto-generated method stub
                        return position;
                    }
                    
                    @Override
                    public Object getItem(int position) {
                        // TODO Auto-generated method stub
                        return button_list.get(position);                         
                    }
                    
                    @Override
                    public int getCount() {
                        // TODO Auto-generated method stub
                        return button_list.size();
                    }
                    
                    @Override
                    public boolean isEnabled(int position) {
                        // TODO Auto-generated method stub
                        return true;
                    }
                    
                    @Override
                    public boolean areAllItemsEnabled() {
                        // TODO Auto-generated method stub
                        return true;
                    }
                };
                list.setAdapter(adapter);

                popView = new FriendListPopView(MapViewActivite.this, 200, 150, true);
              
                progress.dismiss();
                popView.showList(list, friendlistButton);
            }
            return button_list;
                
        }
        
        private void OnButtonItemClick(int id)
        {
            if(button_list.size() >0)
            {
                Button me = button_list.get(id);
                System.out.println("获得 "+me.getText()+"的位置");
                // 对话框消失
                //popupWindow.dismiss();
                GeoPoint p = new GeoPoint(FriendCoordinatesList.get(id).y, FriendCoordinatesList.get(id).x);
                OverlayItem mark= new OverlayItem(p, me.getText().toString(), "");
                mark.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
                overlay.removeAll();
                overlay.addItem(mark);
                
                mapview.refresh();
                mMapController.animateTo(p);
                

            }
        }
    }
}
