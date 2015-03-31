package com.thule.xkcn.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.thule.xkcn.R;
import com.thule.xkcn.XKDetailActivity;
import com.thule.xkcn.manager.XKDataManager;
import com.thule.xkcn.model.XKItem;
import com.thule.xkcn.util.XKBlurUtils;
import com.thule.xkcn.util.XKDrawUtils;
import com.thule.xkcn.view.photoview.PhotoView;
import com.thule.xkcn.view.photoview.PhotoViewAttacher;
import com.thule.xkcn.view.progress.ProgressWheel;

public class XKDetailFragment extends Fragment {

    PhotoView _mainImageView;
    ImageView _blurImageView;
    int _position;
    XKItem _item;
    Context _context;
    private ProgressWheel _progressBar;
    private boolean _loadData;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            _position = savedInstanceState.getInt("position");
            _item = XKDataManager.shareInstance().allItems().get(_position);
            _context = getActivity();
        }
    }


    public XKDetailFragment(Context context, int position) {
        super();
        _context = context;
        _position = position;
        _item = XKDataManager.shareInstance().allItems().get(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.xk_detail_fragment_layout, null);
        _mainImageView = (PhotoView) v.findViewById(R.id.mainImageView);
        _blurImageView = (ImageView) v.findViewById(R.id.blurImageView);
        _progressBar = (ProgressWheel) v.findViewById(R.id.progressBar);
        _progressBar.setVisibility(View.GONE);

        _mainImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if(_context != null)
                {
                    XKDetailActivity activity = (XKDetailActivity)_context;
                    activity.showHeaderFooterBar();
                }
            }
        });

        loadImage();

        ImageLoader.getInstance().loadImage(_item.dataPhoto250, XKDrawUtils.defaultImageOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmap == null)
                {
                    Log.i("Error", "Bitmap null with url: " + s);
                    Log.i("Error", "Bitmap null with url: " + _item.dataPhotoHighRes);
                    Log.i("Error", "Bitmap null with type: " + _item.dataType);
                    return;
                }
                if(_context != null)
                {
                    Bitmap blur = XKBlurUtils.blurBitmap(_context, bitmap, 0.9f);
                    if (blur != null)
                    {
                        _blurImageView.setVisibility(View.VISIBLE);
                        _blurImageView.setImageBitmap(blur);
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        _loadData = false;

        return v;
    }

    @Override
    public void onDestroyView() {
        if (_mainImageView != null && _mainImageView.getDrawingCache()!=null)
        {
            _mainImageView.getDrawingCache().recycle();
            _mainImageView.setImageBitmap(null);
        }
        if (_blurImageView != null && _blurImageView.getDrawingCache()!=null)
        {
            _blurImageView.getDrawingCache().recycle();
            _blurImageView.setImageBitmap(null);
        }
        super.onDestroyView();
    }

    void loadImage() {
        if (!_loadData) {
            ImageLoader.getInstance().loadImage(_item.dataPhotoHighRes, new ImageSize(0,0), XKDrawUtils.defaultImageOption(),new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                    _progressBar.setVisibility(View.VISIBLE);
                    _progressBar.setProgress(0);
                    _progressBar.setText("0%");
                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    // TODO : pop error
                }

                @Override
                public void onLoadingComplete(String url, View arg1, Bitmap bitmap) {
                    _progressBar.setVisibility(View.GONE);
                    _progressBar.setProgress(360);
                    _progressBar.setText("100%");
                    _loadData = true;

                    if (bitmap != null && !bitmap.isRecycled() && _context != null);
                    {
                        Bitmap roundedCornerBitmap = XKDrawUtils.getRoundedCornerBitmap(_context, bitmap);
                        if(roundedCornerBitmap != null)
                        {
                            _mainImageView.setImageBitmap(roundedCornerBitmap);
                           bitmap.recycle();
                           bitmap = null;
                        }
                        else
                        {
                            _mainImageView.setImageBitmap(bitmap);
                        }
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            }, new ImageLoadingProgressListener() {

                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    float progress = (float) current / total;
                    _progressBar.setProgress(Math.round(progress * 360));
                    _progressBar.setText(Math.round(progress * 100) + "%");
                }
            });
        }
    }
}
