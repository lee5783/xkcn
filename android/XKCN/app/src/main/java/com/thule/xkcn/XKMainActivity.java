package com.thule.xkcn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thule.xkcn.fragment.XKGridFragment;

public class XKMainActivity extends FragmentActivity
{
	FragmentManager _fragmentManager;
    RelativeLayout _headerBar;
    XKGridFragment _fragment;
    ImageButton _refreshBtn, _jumToTopBtn, _menuBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xk_main_activity_layout);
        _headerBar = (RelativeLayout)findViewById(R.id.headerBarView);
        _refreshBtn = (ImageButton)findViewById(R.id.refreshBtn);
        _jumToTopBtn = (ImageButton)findViewById(R.id.jumpToTopBtn);
        _menuBtn = (ImageButton)findViewById(R.id.menuBtn);

		_fragmentManager = getSupportFragmentManager();

		FragmentTransaction transaction = _fragmentManager.beginTransaction();

        _fragment = new XKGridFragment();
		transaction.replace(R.id.centerFragment, _fragment);
		transaction.addToBackStack(null);
		transaction.commit();

        _refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _fragment.refresh();
            }
        });
        _jumToTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _fragment.jumpToTop();
            }
        });
	}

    boolean _animation = false;

    public void hideHeaderBar()
    {
        if (_animation || _headerBar.getVisibility() == View.GONE) return;
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _animation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                _animation = false;
                _headerBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        _headerBar.startAnimation(animation);
    }

    public void showHeaderBar()
    {
        if (_animation || _headerBar.getVisibility() == View.VISIBLE) return;
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _animation = true;
                _headerBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                _animation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        _headerBar.startAnimation(animation);
    }

    long _backPressedTimeStamp;

    @Override
    public void onBackPressed()
    {
        long deltaTime = System.currentTimeMillis() - _backPressedTimeStamp;
        if (deltaTime < 2000)
        {
            finish();
        }
        else
        {
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
            _backPressedTimeStamp = System.currentTimeMillis();
        }
    }
}