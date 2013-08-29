package com.banhong.APPBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import com.banhong.APPBox.DataAdapter.Box;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class AppBox extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "APPBOX";
	
	private List<ResolveInfo> applist;
	private PackageManager pm;
	private DataAdapter da;
	private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        pm = getPackageManager();
        GridView gridview = (GridView) findViewById(R.id.AppBoxGridView);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>(); 
        
        da = new DataAdapter(this);
        ArrayList<ContentValues> allbox = da.getAllBox();
        if(allbox!=null && allbox.size()> 0)
        {
        	for(int i =0; i < allbox.size();i++)
        	{
        		HashMap<String, Object> map = new HashMap<String, Object>();  
        		Box box = new Box(allbox.get(i).getAsInteger(DataAdapter.BOX_PIC_ID), allbox.get(i).getAsString(DataAdapter.BOX_TITLE));
        		map.put("Item", box);    
        		lstImageItem.add(map);
        	}
        }
        
        
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        applist = pm.queryIntentActivities(intent, 0);
        //applist = getPackageManager().getInstalledApplications(android.content.pm.PackageManager.GET_META_DATA);
        // 生成动态数组，并且转入数据   
        Collections.sort(applist, new ResolveInfo.DisplayNameComparator(getPackageManager()));

        
         
        for (int i = 0; i < applist.size(); i++) {  
        	
	        HashMap<String, Object> map = new HashMap<String, Object>();  
	        
	        map.put("Item", applist.get(i));// 添加图像资源的ID  applist.get(i).icon
	          
	        lstImageItem.add(map);  
        }  
        // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
        
       lstImageItem.add(addBoxIcon());
       SimpleAdapter saImageItems = new SimpleAdapter(this, 
        												lstImageItem,// 数据来源  
        												R.layout.image_and_text,// image_and_text的XML实现  
        
        												// 动态数组与ImageItem对应的子项  
        												new String[] { "Item", "Item" },  
        
        												// ImageItem的XML文件里面的一个ImageView,两个TextView ID  
        												new int[] { R.id.ItemImage, R.id.ItemText });  
        
        saImageItems.setViewBinder(new ViewBinder() {
			
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				// TODO Auto-generated method stub
				if(view instanceof TextView)
				{
					if(data instanceof ResolveInfo)
					{
						String title = ((ResolveInfo) data).loadLabel(pm).toString();
						((TextView) view).setText(title);
					}
					else if(data instanceof Box)
					{
						((TextView) view).setText(((Box)data).mtitle);
					}
					
				}
				else if(view instanceof ImageView)
				{
					Bitmap bitmap = null;
			    	if(data instanceof ResolveInfo)
					{			    		
			    		bitmap = createIconBitmap(((ResolveInfo)data).loadIcon(pm));
					}
			    	else if(data instanceof Box)
			    	{
			    		bitmap = createIconBitmap(BitmapFactory.decodeResource(getResources(), ((Box)data).mpicID));
			    	}
			    	
			    	((ImageView)view).setImageBitmap(bitmap);
				}
				return true;
			}
		});
        
        // 添加并且显示  
        gridview.setAdapter(saImageItems);  
        // 添加消息处理  
        gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object o = parent.getItemAtPosition(position);
				if(o instanceof HashMap<?, ?>)
				{
					Object data = ((HashMap<String, Object>)o).get("Item");
					if(data instanceof Box)
					{

				    	Dialog dialog = new AddBoxDialog(mContext).creatDialog(new OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						}, new OnClickListener(){
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}						
						});
				    				    	
				    	dialog.show();

					}
					else if(data instanceof ResolveInfo)
					{
						ResolveInfo info = (ResolveInfo) data;
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						intent.setComponent(new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						startActivitySafely(intent, data);
						
					}
				}
			}
		});                
    }
    
    
    @Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}


	private Bitmap createIconBitmap (Drawable data)
    {
    	
		final Resources resources = mContext.getResources();
		int width;
		int height;
		height = width = (int) resources.getDimension(android.R.dimen.app_icon_size);
		
		Drawable pic = data;
		if (pic instanceof PaintDrawable) {
			PaintDrawable painter = (PaintDrawable) pic;
			painter.setIntrinsicWidth(width);
			painter.setIntrinsicHeight(height);
		} else if (pic instanceof BitmapDrawable) {
			// Ensure the bitmap has a density.
			BitmapDrawable bitmapDrawable = (BitmapDrawable) pic;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if (bitmap.getDensity() == Bitmap.DENSITY_NONE) {
				bitmapDrawable.setTargetDensity(getApplicationContext()
						.getResources().getDisplayMetrics());
			}
		}

		int sourceWidth = pic.getIntrinsicWidth();
		int sourceHeight = pic.getIntrinsicHeight();

		if (sourceWidth > 0 && sourceWidth > 0) {
			// There are intrinsic sizes.
			if (width < sourceWidth || height < sourceHeight) {
				// It's too big, scale it down.
				final float ratio = (float) sourceWidth / sourceHeight;
				if (sourceWidth > sourceHeight) {
					height = (int) (width / ratio);
				} else if (sourceHeight > sourceWidth) {
					width = (int) (height * ratio);
				}
			} else if (sourceWidth < width && sourceHeight < height) {
				// It's small, use the size they gave us.
				width = sourceWidth;
				height = sourceHeight;
			}
		}
		int textureWidth = height + 2;
		int textureHeight = height + 2;

		final Bitmap bitmap = Bitmap.createBitmap(textureWidth, textureHeight,
				Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);

		final int left = (textureWidth - width) / 2;
		final int top = (textureHeight - height) / 2;
		Rect sOldBounds = new Rect();
		sOldBounds.set(pic.getBounds());
		pic.setBounds(left, top, left + width, top + height);
		pic.draw(canvas);
		pic.setBounds(sOldBounds);
		//view.setImageBitmap(bitmap);
		return bitmap;
    }
    
    private Bitmap createIconBitmap (Bitmap data)
    {
		final Resources resources = mContext.getResources();
		int width;
		int height;
		height = width = (int) resources.getDimension(android.R.dimen.app_icon_size);
		return Bitmap.createScaledBitmap(data, width, height, false);
    }
    
    private HashMap<String, Object> addBoxIcon()
    {
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	Box box = new Box(R.drawable.add_icon, getResources().getString(R.string.add_box));
    	map.put("Item", box);
    	
    	return map;
    	
    }
    
    void startActivitySafely(Intent intent, Object tag) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Unable to launch. tag=" + tag + " intent=" + intent, e);
        } catch (SecurityException e) {
            Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Launcher does not have the permission to launch " + intent +
                    ". Make sure to create a MAIN intent-filter for the corresponding activity " +
                    "or use the exported attribute for this activity. "
                    + "tag="+ tag + " intent=" + intent, e);
        }
    }
}