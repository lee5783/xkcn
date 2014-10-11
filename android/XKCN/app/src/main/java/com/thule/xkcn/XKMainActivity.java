package com.thule.xkcn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.thule.xkcn.fragment.XKGridFragment;

public class XKMainActivity extends FragmentActivity
{
	private FragmentManager _fragmentManager;
    RelativeLayout _headerBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xk_main_activity_layout);
        _headerBar = (RelativeLayout)findViewById(R.id.headerBarView);
		_fragmentManager = getSupportFragmentManager();

		FragmentTransaction transaction = _fragmentManager.beginTransaction();
		transaction.replace(R.id.centerFragment, new XKGridFragment());
		transaction.addToBackStack(null);
		transaction.commit();
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
}