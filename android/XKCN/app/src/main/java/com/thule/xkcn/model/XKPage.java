package com.thule.xkcn.model;

import java.util.ArrayList;

public class XKPage
{
	public int pageIndex;
	public String url;
	public ArrayList<XKItem> data;

	public XKPage()
	{
		data = new ArrayList<XKItem>();
	}

	public XKPage(int page)
	{
		pageIndex = page;
		data = new ArrayList<XKItem>();
		url = "http://xkcn.info/page/" + (page+1);
	}
}
