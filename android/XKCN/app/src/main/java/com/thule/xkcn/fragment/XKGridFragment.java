package com.thule.xkcn.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.thule.xkcn.R;
import com.thule.xkcn.XKDetailActivity;
import com.thule.xkcn.XKMainActivity;
import com.thule.xkcn.manager.XKDataManager;
import com.thule.xkcn.manager.XKDataManager.XKDataNotifier;
import com.thule.xkcn.model.XKItem;
import com.thule.xkcn.model.XKPage;
import com.thule.xkcn.view.XKGridViewLoadMoreListener;

public class XKGridFragment extends Fragment implements XKDataNotifier, OnItemClickListener
{

	private ArrayList<XKItem> _listData;
	public GridView _gridView;
	private DisplayImageOptions options;
	private XKGridItemAdapter _adapter;



	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.xk_grid_fragment_layout, null);

		_gridView = (GridView) view.findViewById(R.id.gridview);
		_listData = XKDataManager.shareInstance().allItems();
		_gridView.setOnScrollListener(new XKGridViewLoadMoreListener()
		{
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                XKDataManager.shareInstance().loadNewPage();
            }

            @Override
            public void onScrollDown() {
                XKMainActivity activity = (XKMainActivity) getActivity();
                activity.hideHeaderBar();
            }

            @Override
            public void onScrollUp() {
                XKMainActivity activity = (XKMainActivity) getActivity();
                activity.showHeaderBar();
            }
		});
		_gridView.setOnItemClickListener(this);

		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();

		return view;
	}

	@Override
	public void onAttach(Activity activity)
	{
		XKDataManager.shareInstance().setDataNotifier(this);
		XKDataManager.shareInstance().loadNewPage();
		super.onAttach(activity);
	}

	@Override
	public void onDetach()
	{
		XKDataManager.shareInstance().removeDataNotifier(this);
		super.onDetach();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), XKDetailActivity.class);
		intent.putExtra("page", Integer.valueOf(arg2));

		getActivity().startActivity(intent);
	}
    @Override
	public void successLoadNewPage(XKPage page)
	{
		_listData = XKDataManager.shareInstance().allItems();
		if (_adapter == null)
		{
			_adapter = new XKGridItemAdapter(_listData);
			_gridView.setAdapter(_adapter);
		}
		else
		{
			_adapter.setData(_listData);
		}
	}

	public class XKGridItemAdapter extends BaseAdapter
	{

		private ArrayList<XKItem> data;

		public XKGridItemAdapter(ArrayList<XKItem> data)
		{
			this.data = data;
		}

		public void setData(ArrayList<XKItem> listData)
		{
			data = listData;
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			return data.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2)
		{
			ViewHolder holder = null;
			if (arg1 == null)
			{
				arg1 = getActivity().getLayoutInflater().inflate(R.layout.xk_gridview_item_layout, null);
				holder = new ViewHolder();
				holder.image = (ImageView) arg1.findViewById(R.id.img_image);
				holder.image.setClickable(false);
				holder.image.setFocusableInTouchMode(false);
				arg1.setTag(holder);
			}

			holder = (ViewHolder) arg1.getTag();

			XKItem item = (XKItem) getItem(arg0);

			ImageLoader.getInstance().displayImage(item.dataPhoto250, holder.image, options);

			return arg1;
		}

	}

	private class ViewHolder
	{
		ImageView image;
	}
}
