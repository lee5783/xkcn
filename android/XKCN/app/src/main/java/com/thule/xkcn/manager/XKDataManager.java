package com.thule.xkcn.manager;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.thule.xkcn.manager.XKDownloadPageManager.DownloadPageListener;
import com.thule.xkcn.model.XKItem;
import com.thule.xkcn.model.XKPage;

public class XKDataManager
{
	private static XKDataManager _manager;

	public static XKDataManager shareInstance()
	{
		if (_manager == null)
		{
			_manager = new XKDataManager();
		}
		return _manager;
	}

	private ArrayList<XKPage> _pages;
	private ArrayList<XKItem> _allItems;
	private ArrayList<XKDataNotifier> _notifiers;
	private HashMap<Integer, XKDownloadPageManager> _hashMap;

	XKDataManager()
	{
		_pages = new ArrayList<XKPage>();
		_allItems = new ArrayList<XKItem>();
		_notifiers = new ArrayList<XKDataNotifier>();

		_hashMap = new HashMap<Integer, XKDownloadPageManager>();
	}

	public ArrayList<XKPage> getPages()
	{
		return _pages;
	}

	public int currentPage()
	{
		return _pages.size() - 1;
	}

	public ArrayList<XKItem> allItems()
	{
		return _allItems;
	}

	public void loadNewPage()
	{
		if (_hashMap.containsKey(Integer.valueOf(currentPage())))
		{
			return;
		}

		XKDownloadPageManager downloadPageManager = new XKDownloadPageManager();
		downloadPageManager.setDownloadListener(new DownloadPageListener()
		{

			@Override
			public void finishDownloadPage(XKPage page)
			{
				_pages.add(page.pageIndex, page);
				_allItems.addAll(page.data);

				if (_hashMap.containsKey(Integer.valueOf(page.pageIndex)))
				{
					_hashMap.remove(Integer.valueOf(page.pageIndex));
				}

				fireSuccessDownloadPage(page);
			}

			@Override
			public void failDownloadPage(int page, String message)
			{
				Log.e("Download page fail", "Download page fail");
				if (_hashMap.containsKey(Integer.valueOf(page)))
				{
					_hashMap.remove(Integer.valueOf(page));
				}
			}
		});

		_hashMap.put(Integer.valueOf(currentPage()), downloadPageManager);

		downloadPageManager.execute(new Integer[]
		{ currentPage() + 1 });
	}

	public void fireSuccessDownloadPage(XKPage page)
	{
		for (XKDataNotifier notifier : _notifiers)
		{
			if (notifier != null)
			{
				notifier.successLoadNewPage(page);
			}
		}
	}

	public void setDataNotifier(XKDataNotifier notifier)
	{
		if (!_notifiers.contains(notifier))
		{
			_notifiers.add(notifier);
		}
	}

	public void removeDataNotifier(XKDataNotifier notifier)
	{
		if (_notifiers.contains(notifier))
		{
			_notifiers.remove(notifier);
		}
	}

	public interface XKDataNotifier
	{
		void successLoadNewPage(XKPage page);
	}
}
