package com.banhong.locatefriendcn;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class FriendListPopView {
	// 声明PopupWindow对象的引用
	// 这里可以尝试其它效果方式,
	// 如popupWindow.showAsDropDown(v,(screenWidth-dialgoWidth)/2,0);
	// popupWindow.showAtLocation(findViewById(R.id.layout),Gravity.CENTER, 0,
	// 0);
    
	private PopupWindow popupWindow = null;
	private View popupWindow_view = null;
	private LinearLayout layout=null;
	private View parent = null;
	Context context = null;
	FriendListPopView(Context c, int width, int height, boolean focusable) {
	    
	    context = c;
		LayoutInflater layoutinf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 获取自定义布局文件pop.xml的视图
		//layoutinf.
		popupWindow_view = layoutinf.inflate(R.layout.list_pop, null, false);
		layout = (LinearLayout) popupWindow_view.findViewById(R.id.listLayout);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		//layout= new LinearLayout(c);
		//layout.setLayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		//layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

		//ViewGroup.LayoutParams
		popupWindow = new PopupWindow(popupWindow_view, width, height, focusable);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		popupWindow_view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}

				return false;
			}
		});

	}

	public void showList(ListView list, View v)
	{
	    
	    layout.addView(list);
	    popupWindow.showAsDropDown(v);
	    
	}
	

	

}