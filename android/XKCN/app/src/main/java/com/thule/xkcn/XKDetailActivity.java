package com.thule.xkcn;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.thule.xkcn.fragment.XKDetailFragment;
import com.thule.xkcn.fragment.XKLoadingDetailFragment;
import com.thule.xkcn.manager.XKDataManager;
import com.thule.xkcn.manager.XKDataManager.XKDataNotifier;
import com.thule.xkcn.model.XKPage;

import java.util.Timer;
import java.util.TimerTask;

public class XKDetailActivity extends FragmentActivity implements XKDataNotifier, ViewPager.OnPageChangeListener
{
	private ViewPager _pager;
	private ScreenSlidePagerAdapter _adapter;
	private int _currentPage;
    private boolean _showBar;

    private RelativeLayout _headerBar, _footerBar;

    Timer _hideMenuTimer;
    TimerTask _hideMenuTask;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("page"))
		{
			_currentPage = extras.getInt("page");
			extras.remove("page");
		}
		else
		{
			_currentPage = 0;
		}

		setContentView(R.layout.xk_detail_activity_layout);

		_pager = (ViewPager) findViewById(R.id.viewPager);
        _headerBar = (RelativeLayout)findViewById(R.id.headerBarView);
        _footerBar = (RelativeLayout)findViewById(R.id.footerBarView);

		_adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		_pager.setAdapter(_adapter);
        _pager.setOnPageChangeListener(this);

        _showBar = true;
	}

	@Override
	protected void onStart()
	{
		_pager.setCurrentItem(_currentPage, false);
		XKDataManager.shareInstance().setDataNotifier(this);
		super.onStart();
	}

    @Override
    protected void onResume() {
        super.onResume();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideHeaderFooterBar();
                }
            }, 2000);
    }

    @Override
	protected void onStop()
	{
		XKDataManager.shareInstance().removeDataNotifier(this);
		super.onStop();
	}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        Log.e(">>",">>>>>>>>>>>>>>>>>>> position " + position + " possition offset " + positionOffset+  " pixel " + positionOffsetPixels);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        if(state == ViewPager.SCROLL_STATE_SETTLING)
        {
            Log.e("","Scroll state SCROLL_STATE_SETTLING" );
        }
        else if (state == ViewPager.SCROLL_STATE_DRAGGING)
        {
            Log.e("","Scroll state SCROLL_STATE_DRAGGING" );

        }
        else if(state == ViewPager.SCROLL_STATE_IDLE)
        {
            Log.e("","Scroll state SCROLL_STATE_IDLE" );
        }
    }

    @Override
    public void onPageSelected(int position)
    {
        Log.e("","Select position " + position );
    }

    @Override
	public void successLoadNewPage(XKPage page)
	{
		int count = XKDataManager.shareInstance().allItems().size();
		if (_adapter != null)
		{
			_adapter.setItemCount(count);
		}
	}

    public void showHeaderFooterBar()
    {
        if(_showBar)
        {
            return;
        }
        _showBar = true;

        Animation _headerAnimation =  new TranslateAnimation(0, 0, -_headerBar.getHeight(), 0);
        _headerAnimation.setDuration(500);
        _headerAnimation.setFillAfter(true);

        _headerBar.startAnimation(_headerAnimation);

        Animation _footerAnimation =  new TranslateAnimation(0, 0, _footerBar.getHeight(), 0);
        _footerAnimation.setDuration(500);
        _footerAnimation.setFillAfter(true);

        _footerAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        _footerBar.startAnimation(_footerAnimation);


    }
    void hideHeaderFooterBar()
    {
        if(!_showBar)
        {
            return;
        }
        _showBar = false;
        Animation _headerAnimation =  new TranslateAnimation(0, 0, 0, - _headerBar.getHeight());
        _headerAnimation.setDuration(500);
        _headerAnimation.setFillAfter(true);

        _headerBar.startAnimation(_headerAnimation);

        Animation _footerAnimation =  new TranslateAnimation(0, 0, 0, _footerBar.getHeight());
        _footerAnimation.setDuration(500);
        _footerAnimation.setFillAfter(true);

        _footerBar.startAnimation(_footerAnimation);;
    }

    void startTimer()
    {
        stopTimer();

        _hideMenuTimer = new Timer();

        _hideMenuTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                XKDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        hideHeaderFooterBar();
                    }
                });
                Log.i("Timer", "Hide footer bar");
            }
        }, 3000);
    }

    void stopTimer()
    {
        if (_hideMenuTimer != null)
        {
            _hideMenuTimer.cancel();
            _hideMenuTimer = null;
        }
    }

	protected class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
	{

		int count = 0;

		public ScreenSlidePagerAdapter(FragmentManager fm)
		{
			super(fm);
			count = XKDataManager.shareInstance().allItems().size() + 1;
		}

		public void setItemCount(int count)
		{
			this.count = count + 1;
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position)
		{
			if (position == count - 1)
			{
				return new XKLoadingDetailFragment();
			}
			else
			{
				return new XKDetailFragment(XKDetailActivity.this, position);
			}
		}

		@Override
		public int getCount()
		{
			return count;
		}
		
		@Override
		public int getItemPosition(Object object)
		{
			// TODO Auto-generated method stub
			//return super.getItemPosition(object);
			return POSITION_NONE;
		}

	}
}
