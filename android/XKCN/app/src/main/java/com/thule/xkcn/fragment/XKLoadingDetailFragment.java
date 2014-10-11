package com.thule.xkcn.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thule.xkcn.R;
import com.thule.xkcn.manager.XKDataManager;

public class XKLoadingDetailFragment extends Fragment
{
//
//    public XKLoadingDetailFragment(Context context, int positison)
//    {
//
//    }
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.xk_loading_detail_fragment_layout, null);
		return v;
	}

	@Override
	public void onAttach(Activity activity)
	{
		XKDataManager.shareInstance().loadNewPage();
		super.onAttach(activity);
	}
}
