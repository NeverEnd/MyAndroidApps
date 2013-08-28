
package org.accenture.product.lemonade.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 
 * @author shaohu.zhu
 * 自己定义的Gallery，实现立体滚动效果
 */

public class CoverFlow extends Gallery {

	private Camera mCamera = new Camera();
	//图标大小 
	private int mMaxRotationAngle = 72;
	private int mMaxZoom = -100;
	
	
	
	private int mCoveflowCenter;
	private boolean mAlphaMode = true;
	private boolean mCircleMode = false;
	
	
	int SPEED=400;

	public CoverFlow(Context context) {
		super(context);
		
		//把这个属性设成true的时候每次viewGroup(看Gallery的源码就可以看到它是从ViewGroup间接继承过来的)在重新画它的child的时候都会促发getChildStaticTransformation这个函数
		//,所以我们只需要在这个函数里面去加上旋转和放大的操作就可以了
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	public boolean getCircleMode() {
		return mCircleMode;
	}

	public void setCircleMode(boolean isCircle) {
		mCircleMode = isCircle;
	}

	public boolean getAlphaMode() {
		return mAlphaMode;
	}

	public void setAlphaMode(boolean isAlpha) {
		mAlphaMode = isAlpha;
	}

	public int getMaxZoom() {
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}
	
	//重写Garray方法 ，产生层叠和放大效果
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		final int childCenter = getCenterOfView(child);		
		final int childWidth = child.getWidth();		
		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		if (childCenter == mCoveflowCenter) {
			transformImageBitmap((ImageView) child, t, 0, 0);
		} else {
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
			int d =(int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
			// Log.d("test", "recanglenum:"+Math.floor ((mCoveflowCenter -
			// childCenter) / childWidth));
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
						: mMaxRotationAngle;
			}
			transformImageBitmap((ImageView) child, t, rotationAngle,
					d);
		}
		return true;
	}

	/**
	 * This is called during layout when the size of this view has changed. If
	 * you were just added to the view hierarchy, you're called with the old
	 * values of 0.
	 * 
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * @param oldw
	 *            Old width of this view.
	 * @param oldh
	 *            Old height of this view.
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * Transform the Image Bitmap by the Angle passed
	 * 
	 * @param imageView
	 *            ImageView the ImageView whose bitmap we want to rotate
	 * @param t
	 *            transformation
	 * @param rotationAngle
	 *            the Angle by which to rotate the Bitmap
	 */
	private void transformImageBitmap(ImageView child, Transformation t,
			int rotationAngle, int d) {
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getHeight();
		final int imageWidth = child.getWidth();
		final int rotation = Math.abs(rotationAngle);
		mCamera.translate(0, 0.0f, 50.0f);
//		 As the angle of the view gets less, zoom in
		if (rotation <= mMaxRotationAngle) {
			
			
			if(rotationAngle>0){
				float zoomAmount = (float) (mMaxZoom + (rotation *2.8));
				mCamera.translate(0.0f, 0.0f, zoomAmount);
			}else{
				float zoomAmount = (float) (mMaxZoom + (rotation *0.5 ));
				mCamera.translate(0.0f, 0.0f, zoomAmount);
			}
			
			if (mAlphaMode) {
				((ImageView) (child)).setAlpha((int) (255 - rotation * 2.5));
			}
		}
		if(rotationAngle>0)
		{
			mCamera.translate(d,0, 0);
			mCamera.rotateY(-rotationAngle);
		}else
		{
//			mCamera.translate(d*0.2f,0, 0);
			mCamera.rotateY(-rotationAngle*0.83f);
		}
		
		
		mCamera.getMatrix(imageMatrix);
		
		
		//imageMatrix.preSkew(0,0.1f, 0, 0); 
		
//		imageMatrix.postRotate(-20);

//		if(rotationAngle<0)
//		{
//			imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
//			imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
//		}
//		else
//		{
//			imageMatrix.preTranslate((imageWidth / 2), -(imageHeight / 2));
//			imageMatrix.postTranslate(-(imageWidth / 2), (imageHeight / 2));
//		}
		mCamera.restore();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{

		if (velocityX > SPEED)
		{
			return super.onFling(e1, e2, SPEED, velocityY);
		}
		else if (velocityX < -SPEED)
		{
			return super.onFling(e1, e2, -SPEED, velocityY);
		}
		else
		{
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}
	
}
