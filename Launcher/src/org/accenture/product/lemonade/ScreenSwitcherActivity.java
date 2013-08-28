package org.accenture.product.lemonade;

import java.util.ArrayList;

import org.accenture.product.lemonade.view.HomeScreenLayout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @author shaohu.zhu
 */
public class ScreenSwitcherActivity extends Activity
{
	public static ArrayList<Bitmap> thumbnailList;

	private static Launcher mLauncher;
	ImageView delete;
	ImageView add;
	LinearLayout line1;
	LinearLayout line2;
	LinearLayout line3;

	// private static final int VIEW_WIDTH=96;
	// private static final int VIEW_HEIGHT=110;

	private static final int VIEW_WIDTH = 147;
	private static final int VIEW_HEIGHT = 218;

	private int screenIndex;

	private boolean finishFlag = true;

	public static int numCanNotDelete = 3; // 不能被删除的屏幕数量

	HomeScreenLayout mainLayout;

	private ImageView selectedImage;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.screenswitcher);

		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		line1 = (LinearLayout) findViewById(R.id.line1);
		line2 = (LinearLayout) findViewById(R.id.line2);
		line3 = (LinearLayout) findViewById(R.id.line3);
		delete = (ImageView) findViewById(R.id.delete);
		add = (ImageView) findViewById(R.id.add);

		addButtonListener();

		if (numCanNotDelete == 3)
		{
			fillScreenCase3();
		}
		else
		{
			fillScreen();
		}

	}

	public void deleteScreen()
	{
		int size = thumbnailList.size();
		int frontCount = Launcher.FRONT_SCREEN_COUNT;
		if (size > 5)
		{
			switch (size)
			{
				case 6:
					if ((screenIndex == 0 && frontCount == 1) || (screenIndex == 5 && frontCount == 0))
					{
						mLauncher.removeOneScreen(screenIndex);
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 7:
					if (frontCount == 0)
					{
						if (screenIndex == 5 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 8:
					if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 6 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 9:
					if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 7 || screenIndex == 8)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				default:
					Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreen();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}

	public void deleteScreenCase3()
	{
		int size = thumbnailList.size();
		int frontCount = Launcher.FRONT_SCREEN_COUNT;
		if (size > 3)
		{
			switch (size)
			{
				case 4:
					if ((screenIndex == 0 && frontCount == 1) || (screenIndex == 3 && frontCount == 0))
					{
						mLauncher.removeOneScreen(screenIndex);
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 5:
					if (frontCount == 0)
					{
						if (screenIndex == 3 || screenIndex == 4)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 4)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 6:
					if (frontCount == 0)
					{
						if (screenIndex == 3 || screenIndex == 4 || screenIndex == 5)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 4 || screenIndex == 5)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 5)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 3)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 7:
					if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 4 || screenIndex == 5 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 5 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 3)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 8:
					if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 5 || screenIndex == 6 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 3)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2 || screenIndex == 6 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 9:
					if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2 || screenIndex == 6 || screenIndex == 7
							|| screenIndex == 8)
					{
						mLauncher.removeOneScreen(screenIndex);
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				default:
					Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreenCase3();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	private void addScreen()
	{
		int size = thumbnailList.size();
		if (size < 9)
		{
			switch (size)
			{
				case 5:
					mLauncher.addOneScreen(0);
					break;
				case 6:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
							mLauncher.addOneScreen(0);
							break;
						default:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 7:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 8:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				default:
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreen();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.enough_screen, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}

	private void addScreenCase3()
	{
		int size = thumbnailList.size();
		if (size < 9)
		{
			switch (size)
			{
				case 3:
					mLauncher.addOneScreen(0);
					break;
				case 4:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
							mLauncher.addOneScreen(0);
							break;
						default:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;

				case 5:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 6:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 7:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 1:
						case 2:
							mLauncher.addOneScreen(0);
							break;
						case 3:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 8:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 2:
							mLauncher.addOneScreen(0);
							break;
						case 3:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				default:
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreenCase3();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.enough_screen, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}

	private void addButtonListener()
	{

		add.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (numCanNotDelete == 3)
				{
					addScreenCase3();
				}
				else
				{
					addScreen();
				}
			}
		});
	}

	private void fillScreen()
	{
		if (thumbnailList == null)
			return;

		mainLayout = (HomeScreenLayout) findViewById(R.id.homescreenlayout);

		int size = thumbnailList.size();
		if (size > 4)
		{
			for (int i = 0; i < size; i++)
			{
				final int j = i;
				ImageView imageView = new ImageView(this);
				imageView.setAdjustViewBounds(true);
				LayoutParams layoutParams = new LinearLayout.LayoutParams(VIEW_WIDTH, VIEW_HEIGHT);
				// layoutParams.setMargins(5, 5, 5, 5);

				imageView.setLayoutParams(layoutParams);
				// imageView.setImageBitmap(thumbnailList.get(i));
				setImage(i, imageView);

				// imageView.setPadding(3, 5, 3, 5);

				// imageView.setBackgroundDrawable(resources.getDrawable(R.drawable.preview_background));
				// imageView.setBackgroundResource(R.drawable.preview_background);
				// imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);

				imageView.setOnLongClickListener(new View.OnLongClickListener()
				{

					@Override
					public boolean onLongClick(View v)
					{
						finishFlag = false;
						screenIndex = j;
						mainLayout.setView(v, delete, add);
						mainLayout.startDragging();
						return false;
					}
				});

				imageView.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						if (finishFlag)
						{
							// gotoScreen(j);
							getIntent().putExtra("gotoIndex", j);

							finish();
						}
						else
						{
							finishFlag = true;
						}
					}
				});

				switch (size)
				{
					case 5:
						if (i == 0)
							addLine1View(imageView);
						else if (i == 1 || i == 2 || i == 3)
							addLine2View(imageView);
						else if (i == 4)
							addLine3View(imageView);
						break;
					case 6:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
								addLine2View(imageView);
							else if (i == 4 || i == 5)
								addLine3View(imageView);
						}
						else
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5)
								addLine3View(imageView);
						}
						break;
					case 7:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
								addLine2View(imageView);
							else if (i == 4 || i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6)
								addLine3View(imageView);
						}
						break;
					case 8:
						if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6 || i == 7)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6 || i == 7)
								addLine3View(imageView);
						}
						break;
					case 9:
						if (i == 0 || i == 1 || i == 2)
							addLine1View(imageView);
						else if (i == 3 || i == 4 || i == 5)
							addLine2View(imageView);
						else if (i == 6 || i == 7 || i == 8)
							addLine3View(imageView);
						break;
				}
			}
		}
	}

	private void setImage(int i, ImageView imageView)
	{
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale((float) (1f / 3f), (float) (1f / 3f));

		// matrix.postScale(0.35f, 0.35f);

		Bitmap bitmap = thumbnailList.get(i);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

		// 设置 ImageView的图片为上面转换的图片
		imageView.setImageDrawable(bmd);
		imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);
		// imageView.setBackgroundColor(0xffffffff);
	}

	private void setImageBig(int i, ImageView imageView)
	{
		imageView.setImageBitmap(thumbnailList.get(i));
		imageView.setScaleType(ScaleType.CENTER);

	}

	private void fillScreenCase3()
	{
		if (thumbnailList == null)
			return;

		mainLayout = (HomeScreenLayout) findViewById(R.id.homescreenlayout);

		int size = thumbnailList.size();

		Launcher.FRONT_SCREEN_COUNT = (size - 3) % 2 == 0 ? (size - 3) / 2 : (size - 3) / 2 + 1;

		if (size > 2)
		{
			for (int i = 0; i < size; i++)
			{
				final int j = i;
				ImageView imageView = new ImageView(this);

				imageView.setAdjustViewBounds(false);

				LayoutParams layoutParams = new LinearLayout.LayoutParams(VIEW_WIDTH, VIEW_HEIGHT);
				// layoutParams.setMargins(5, 7, 5, 0);

				imageView.setLayoutParams(layoutParams);
				setImage(i, imageView);
				// imageView.setBackgroundDrawable(resources.getDrawable(R.drawable.preview_background));
				// imageView.setBackgroundResource(R.drawable.preview_background);

				imageView.setOnLongClickListener(new View.OnLongClickListener()
				{

					@Override
					public boolean onLongClick(View v)
					{
						finishFlag = false;
						screenIndex = j;
						mainLayout.setView(v, delete, add);
						mainLayout.startDragging();
						return false;
					}
				});

				imageView.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						if (finishFlag)
						{
							// gotoScreen(j);
							// Intent intent=new
							// Intent(ScreenSwitcherActivity.this,Launcher.class);
							// intent.putExtra("gotoIndex", j);
							// setResult(0, intent);
							//							
							// startActivity(intent);
							// overridePendingTransition(R.anim.zoomout,R.anim.zoomin);
							getIntent().putExtra("gotoIndex", j);
							setResult(0, getIntent());
							mLauncher.switchScreen(j);
							mLauncher.hideApps(j);
							startChange(v, j);
							// finish();

						}
						else
						{
							finishFlag = true;
						}
					}
				});

				switch (size)
				{
					case 3:
						if (i == 0)
						{
							line1.addView(createEmptyImageView());
							line3.addView(createEmptyImageView());
						}
						// imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);
						addLine2View(imageView);
						break;
					case 4:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
							{
								line1.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
							{
								addLine2View(imageView);
							}
							else
							{
								addLine3View(imageView);
							}
						}
						else
						{
							if (i == 0)
							{
								addLine1View(imageView);
								line3.addView(createEmptyImageView());
							}
							else
							{

								addLine2View(imageView);
							}
						}
						break;
					case 5:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
							{
								line1.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
							{
								addLine2View(imageView);
							}
							else if (i == 3 || i == 4)
							{
								addLine3View(imageView);
							}
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0)
							{
								addLine1View(imageView);
							}
							else if (i == 1 || i == 2 || i == 3)
							{
								addLine2View(imageView);
							}
							else if (i == 4)
							{
								addLine3View(imageView);
							}
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0)
							{
								line3.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
							{
								addLine2View(imageView);
							}
						}
						break;
					case 6:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
							{
								line1.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
							{
								addLine2View(imageView);
							}
							else if (i == 3 || i == 4 || i == 5)
							{
								addLine3View(imageView);
							}
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
							{
								addLine2View(imageView);
							}
							else if (i == 4 || i == 5)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
							{
								addLine2View(imageView);
							}
							else if (i == 5)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 3)
						{
							if (i == 0)
							{
								line3.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
							{
								addLine2View(imageView);
							}
						}
						break;
					case 7:
						if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
							{
								addLine2View(imageView);
							}
							else if (i == 4 || i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 3)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6)
								addLine3View(imageView);
						}
						break;
					case 8:
						if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6 || i == 7)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 3)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6 || i == 7)
								addLine3View(imageView);
						}
						break;
					case 9:
						if (i == 0 || i == 1 || i == 2)
							addLine1View(imageView);
						else if (i == 3 || i == 4 || i == 5)
							addLine2View(imageView);
						else if (i == 6 || i == 7 || i == 8)
							addLine3View(imageView);
						break;
				}
			}
		}
	}

	private class MyThread extends Thread
	{
		@Override
		public void run()
		{
			int i = 0;
			while (i < 12)
			{
				try
				{
					// 业务处理
					// 图片放大

					mainLayout.setDrawImage(((BitmapDrawable) selectedImage.getDrawable()).getBitmap());
					mainLayout.postInvalidate();

					Thread.sleep(100);
					i++;
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// finish();
		}
	}

	private ImageView animateImage;

	private void startChange(View v, final int j)
	{
		int location[] = new int[2];
		v.getLocationInWindow(location);
		animateImage = (ImageView) findViewById(R.id.animateImage);

		animateImage.setImageBitmap(thumbnailList.get(j));

		System.out.println("width=" + animateImage.getWidth());
		System.out.println("height=" + animateImage.getHeight());

		LinearLayout.LayoutParams layout = (LayoutParams) animateImage.getLayoutParams();
		// layout.width = 123;
		// layout.height = 170;

		int leftMargin = location[0] + 10;
		// 30
		int topMargin = location[1] - 30;
		layout.setMargins(leftMargin, topMargin, 0, 0);
		animateImage.setLayoutParams(layout);
		animateImage.setVisibility(View.VISIBLE);
		animateImage.setScaleType(ScaleType.FIT_CENTER);
		// animateImage.setBackgroundColor(0xffffffff);
		final int time = 300;
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 4f, 1f, 4f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		scaleAnimation.setDuration(time);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 1f);
		alphaAnimation.setDuration(time);

		// 115/149
		// System.out.println("leftMargin"+leftMargin+"topMargin"+topMargin);
		// TranslateAnimation translateAnimation=new
		// TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0,
		// TranslateAnimation.RELATIVE_TO_PARENT,115,
		// TranslateAnimation.RELATIVE_TO_PARENT, 0,
		// TranslateAnimation.RELATIVE_TO_PARENT, 149);

		TranslateAnimation translateAnimation = new TranslateAnimation(0, 180 - leftMargin, 0, 240 - topMargin);
		translateAnimation.setDuration(time);
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(translateAnimation);
		animationSet.setInterpolator(new AccelerateInterpolator((float) 0.2));
		animateImage.startAnimation(animationSet);
		View homescreenmainlayout = findViewById(R.id.homescreenmainlayout);
		homescreenmainlayout.setVisibility(View.INVISIBLE);
		// homescreenmainlayout.startAnimation(alphaAnimation);
		// animateImage.setBackgroundResource(R.drawable.screenswitcher_panel_background);
		animationSet.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				// TODO Auto-generated method stub

				mLauncher.showApps(j);
				finish();

			}
		});
	}

	// private void addLine2View(ImageView imageView){
	// imageView.setBackgroundResource(R.drawable.screenswitcher_panel_fixed_background);
	// imageView.setPadding(7, 8, 7, 4);

	private void addLine2View(ImageView imageView)
	{
		addThumbtack(imageView);
		line2.addView(imageView);
	}

	private void addThumbtack(ImageView imageView)
	{
		final Bitmap bitmap = Bitmap.createBitmap((int) 115, (int) 180, Bitmap.Config.ARGB_8888);
		final Canvas c = new Canvas(bitmap);
		Drawable drawable = imageView.getDrawable();
		drawable.setBounds(0, 8, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() + 5);
		c.scale(0.95f, 0.95f);
		c.translate(7, 0);
		drawable.draw(c);

		final Bitmap bitmap1 = Bitmap.createBitmap((int) 115, (int) 180, Bitmap.Config.ARGB_8888);
		final Canvas c1 = new Canvas(bitmap1);
		c1.drawBitmap(bitmap, 0, 0, null);
		Drawable drawable2 = getResources().getDrawable(R.drawable.screenswitcher_lock_screen_title);
		drawable2.setBounds(3, 0, 114, drawable2.getIntrinsicHeight());
		c1.translate(5, 0);
		drawable2.draw(c1);
		imageView.setImageBitmap(bitmap1);
	}

	private void formatImageSize(ImageView imageView)
	{

		final Bitmap result = Bitmap.createBitmap((int) 115, (int) 180, Bitmap.Config.ARGB_8888);
		final Canvas c = new Canvas(result);
		Drawable drawable = imageView.getDrawable();
		drawable.setBounds(0, 8, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() + 5);
		c.scale(0.95f, 0.95f);
		c.translate(5, 0);
		drawable.draw(c);
		imageView.setImageBitmap(result);

		// return result;
	}

	private void addLine1View(ImageView imageView)
	{
		// imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);
		formatImageSize(imageView);
		line1.addView(imageView);
	}

	private void addLine3View(ImageView imageView)
	{
		// imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);
		formatImageSize(imageView);
		line3.addView(imageView);
	}

	private ImageView createEmptyImageView()
	{
		ImageView hide = new ImageView(this);
		hide.setBackgroundColor(Color.TRANSPARENT);
		LayoutParams hideLayoutParams = new LinearLayout.LayoutParams(VIEW_WIDTH, VIEW_HEIGHT);
		// hideLayoutParams.setMargins(5, 7, 5, 0);
		hide.setLayoutParams(hideLayoutParams);
		return hide;
	}

	public static void initPic(ArrayList<Bitmap> thumbList)
	{
		if (thumbnailList != null)
		{
			thumbnailList.clear();
		}
		System.gc();
		thumbnailList = thumbList;
	}

	public static void setLauncher(Launcher launcher)
	{
		mLauncher = launcher;
	}

}
