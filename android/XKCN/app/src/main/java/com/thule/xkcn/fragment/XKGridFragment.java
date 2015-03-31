package com.thule.xkcn.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.thule.xkcn.util.XKDrawUtils;
import com.thule.xkcn.util.XKUtils;
import com.thule.xkcn.view.XKGridViewLoadMoreListener;

public class XKGridFragment extends Fragment implements XKDataNotifier, OnItemClickListener
{

	private ArrayList<XKItem> _listData;
	public GridView _gridView;

    private RelativeLayout _loadingView;
	private XKGridItemAdapter _adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.xk_grid_fragment_layout, null);

        _loadingView = (RelativeLayout)view.findViewById(R.id.loadingView);

		_gridView = (GridView) view.findViewById(R.id.gridview);

		_gridView.setOnScrollListener(new XKGridViewLoadMoreListener()
		{
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                loadMorePage();
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

        _listData = XKDataManager.shareInstance().allItems();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (_listData.size() == 0)
                {
                    loadMorePage();
                }
            }
        }, 500);


		return view;
	}

    public void jumpToTop()
    {
        _gridView.smoothScrollToPosition(0);
    }

    public void refresh()
    {
        XKDataManager.shareInstance().cleanup();
        loadMorePage();
    }

	@Override
	public void onAttach(Activity activity)
	{
		XKDataManager.shareInstance().setDataNotifier(this);
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

    boolean _animation = false;

    void loadMorePage()
    {
        if(_animation)
            return;
        Animation animation = new TranslateAnimation(0, 0, XKUtils.convertDpToPixel(getActivity(), 50), 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _loadingView.setVisibility(View.VISIBLE);
                _animation = true;
                XKDataManager.shareInstance().loadNewPage();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
_animation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        _loadingView.startAnimation(animation);


    }

    @Override
	public void successLoadNewPage(final XKPage page)
	{
        if(_animation)
            return;
        Animation animation = new TranslateAnimation(0, 0, 0, XKUtils.convertDpToPixel(getActivity(), 50));
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _animation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                _loadingView.setVisibility(View.GONE);
                _animation = false;

                _listData = XKDataManager.shareInstance().allItems();
                if (_adapter == null)
                {
                    _adapter = new XKGridItemAdapter(getActivity(), _listData);
                    _gridView.setAdapter(_adapter);
                }
                else
                {
                    _adapter.setData(_listData);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        _loadingView.startAnimation(animation);
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        _loadingView.setVisibility(View.GONE);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            _gridView.setNumColumns(2);
        }
        else
        {
            _gridView.setNumColumns(3);

        }
    }

    public static class XKGridItemAdapter extends BaseAdapter
	{
        private Context _context;
		private ArrayList<XKItem> data;

		public XKGridItemAdapter(Context context, ArrayList<XKItem> data)
		{
			this.data = data;
            _context = context;
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
		public View getView(int position, View view, ViewGroup viewGroup)
		{
			ViewHolder holder = null;
			if (view == null)
			{
				view = LayoutInflater.from(_context).inflate(R.layout.xk_gridview_item_layout, null);
				holder = new ViewHolder();
				holder.image = (ImageView) view.findViewById(R.id.img_image);
				holder.image.setClickable(false);
				holder.image.setFocusableInTouchMode(false);
				view.setTag(holder);
			}

			holder = (ViewHolder) view.getTag();
            if (holder.image!=null && holder.image.getDrawingCache()!=null)
            {
                holder.image.getDrawingCache().recycle();
                holder.image.setImageBitmap(null);
            }

			XKItem item = (XKItem) getItem(position);
			ImageLoader.getInstance().displayImage(item.dataPhoto250, holder.image, XKDrawUtils.defaultImageOption());
			return view;
		}

	}

	static class ViewHolder
	{
		ImageView image;
	}
}
