package com.banhong.APPBox;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

public class AddBoxDialog extends AlertDialog {
	private Builder builder;
	private Context mContext;
	public AddBoxDialog(Context context) {
		super(context);
		mContext = context;
	}

	public AlertDialog creatDialog(final OnClickListener negativelistener,  final OnClickListener positivelistener)
	{
		
		final LayoutInflater la = LayoutInflater.from(mContext);
    	View vi = la.inflate(R.layout.dialog_layout, null);
    	
    	ImageButton select = (ImageButton) vi.findViewById(R.id.select_box_icon);
    	
    	select.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View popupView = la.inflate(R.layout.box_icon_pop_layout, null);
				
				PopupWindowEx popwindow = new PopupWindowEx(popupView , 200, 100, true);
				
				popwindow.setDismissAsDialog(true);
				
				popwindow.showAtLocation(v, Gravity.CENTER, 0, -v.getHeight()/2);   
				//popwindow.showAsDropDown(v, 0, 0);
				popwindow.setTouchable(true);
				popwindow.setFocusable(false);
				popwindow.setOutsideTouchable(true);
				//popwindow.setIgnoreCheekPress();
				
				popwindow.update();
			}

		});
    
		builder = new Builder(mContext);
		builder.setView(vi);
		builder.setNegativeButton(android.R.string.cancel, negativelistener);		
		builder.setPositiveButton(android.R.string.ok, positivelistener);		
		return builder.create();
	}
	
	
}
