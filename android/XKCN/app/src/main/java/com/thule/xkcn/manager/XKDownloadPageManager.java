package com.thule.xkcn.manager;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

import com.thule.xkcn.model.XKItem;
import com.thule.xkcn.model.XKPage;
import com.thule.xkcn.util.XKUtils;

public class XKDownloadPageManager extends AsyncTask<Integer, String, XKPage>
{

	DownloadPageListener _downloadPageListener;
	int _page;

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
	}

	@Override
	protected XKPage doInBackground(Integer... params)
	{
		_page = params[0];
		XKPage page = new XKPage(params[0]);
		try
		{
			Document document = Jsoup.connect(page.url).get();

			Log.i("", "Parse URL " + page.url);

			Elements elements = document.select("div.gridItem");

			for (int i = 0; i < elements.size(); i++)
			{
				Element e = elements.get(i);

				XKItem item = new XKItem();
				item.pageIndex = page.pageIndex;
				item.id = e.attr("id");
				item.dataPermalink = e.attr("data-permalink");
				item.dataNoteURL = e.attr("data-notes-url");
				item.dataHeight = XKUtils.parseInt(e.attr("data-height"));
				item.dataHeight500 = XKUtils.parseInt(e.attr("data-height-500"));
				item.dataWidth500 = XKUtils.parseInt(e.attr("data-width-500"));
				item.dataPhoto100 = e.attr("data-photo-100");
				item.dataPhoto250 = e.attr("data-photo-250");
				item.dataPhotoHighRes = e.attr("data-photo-high");
				item.dataWidthHighRes = XKUtils.parseInt(e.attr("data-width-high-res"));
				item.dataHeightHighRes = XKUtils.parseInt(e.attr("data-height-high-res"));
				item.dataIndentify = e.attr("data-identifier");
				item.title = e.attr("data-title");
                item.dataType = e.attr("data-type");

                if(item.dataType.equals("photo"))
                {
                    page.data.add(item);
                }
			}
			return page;

		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(XKPage result)
	{
		super.onPostExecute(result);

		if (result != null)
		{
			if (_downloadPageListener != null)
			{
				_downloadPageListener.finishDownloadPage(result);
			}
		}
		else
		{
			if (_downloadPageListener != null)
			{
				_downloadPageListener.failDownloadPage(_page, "This is dummy message");
			}
		}
	}

	public void setDownloadListener(DownloadPageListener listener)
	{
		this._downloadPageListener = listener;
	}

	public interface DownloadPageListener
	{
		void finishDownloadPage(XKPage page);

		void failDownloadPage(int pageIndex, String message);
	}
}
