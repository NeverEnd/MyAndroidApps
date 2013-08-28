package com.banhong.APPBox;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

public class PopupWindowEx extends PopupWindow {
	private View mContentView;
	private Context mContext;
	public PopupWindowEx(View contentView, int width, int height, boolean focusable)
	{
		super( contentView,  width,  height,  focusable);
		if(contentView != null)
		{
			mContentView = contentView;
			mContext = contentView.getContext();
		}
	};
	
	public void setDismissAsDialog(boolean b)
	{
		if(b == true)
		{
			mContentView.setFocusableInTouchMode(false);
			mContentView.setOnKeyListener(new View.OnKeyListener() {
				
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			
			mContentView.setOnTouchListener(new View.OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
		
	};
//	
//	private boolean isOutOfBounds(MotionEvent event) {
//        final int x = (int) event.getX();
//        final int y = (int) event.getY();
//        final int slop = ViewConfiguration.get(mContext).getScaledWindowTouchSlop();
//        final View decorView = getWindow().getDecorView();
//        return (x < -slop) || (y < -slop)
//                || (x > (decorView.getWidth()+slop))
//                || (y > (decorView.getHeight()+slop));
//    }
}
