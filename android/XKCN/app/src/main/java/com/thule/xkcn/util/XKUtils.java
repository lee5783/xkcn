package com.thule.xkcn.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class XKUtils
{

	public static int parseInt(String string)
	{
        if (TextUtils.isEmpty(string))
        {
            return 0;
        }
		try
		{
			int value = Integer.parseInt(string);
			return value;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(XKUtils.class.getName(), "Could not parse int with value : " + string + "\nReturn 0 value");
			return 0;
		}
	}

	public static void setReferenceState(String key, int state, Context context)
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, state);
		editor.commit();
	}

	public static int getReferenceState(String key, Context context)
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		int val = settings.getInt(key, 0);
		return val;
	}

	public static void setReferenceString(String key, String value, Context context)
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getReferenceString(String key, Context context)
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String val = settings.getString(key, "");
		return val;
	}

	public static Rect getRectInScreen(View view)
	{
		Rect rect = new Rect();
		int[] loc = new int[2];
		view.getLocationOnScreen(loc);
		rect.left = loc[0];
		rect.top = loc[1];
		rect.right = loc[0] + view.getWidth();
		rect.bottom = loc[1] + view.getHeight();

		return rect;
	}

	public static boolean deleteDirectory(File path)
	{
		if (path.exists())
		{
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]);
				}
				else
				{
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * Helper class for handling ISO 8601 strings of the following format:
	 * "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
	 */
	/** Transform Date to ISO 8601 string. */

	public static String convertDateToString(Date date)
	{
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	public static Date convertStringToDate(String iso8601string) throws ParseException
	{
		String s = iso8601string.replace("Z", "+00:00");
		try
		{
			s = s.substring(0, 22) + s.substring(23); // to get rid of the ":"
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new ParseException("Invalid length", 0);
		}
		Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
		return date;
	}

	public static String getFileSize(float filesize)
	{
		float ret = filesize;
		String fileSize = "";
		DecimalFormat df = new DecimalFormat("#0.#");
		if (ret < (1024 * 1024)) // KB
		{
			ret = ret / (float) (1024);
			String size = df.format(ret);
			fileSize = size + " KB";
		}
		else if (ret < (1024 * 1024 * 1024)) // MB
		{
			ret = ret / (float) (1024 * 1024);
			String size = df.format(ret);
			fileSize = size + " MB";
		}
		else if (ret < (1024 * 1024 * 1024 * 1024)) // GB
		{
			ret = ret / (float) (1024 * 1024 * 1024);
			String size = df.format(ret);
			fileSize = size + " GB";
		}
		return fileSize;
	}

	public static int convertDpToPixel(Context context, float dp)
	{
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return (int) px;
	}

	public static String getFileSize(String fileName)
	{
		float ret = getFileSizeInBytes(fileName);
		String fileSize = "";
		DecimalFormat df = new DecimalFormat("#0.#");
		if (ret < (1024 * 1024)) // KB
		{
			ret = ret / (float) (1024);
			String size = df.format(ret);
			fileSize = size + " KB";
		}
		else if (ret < (1024 * 1024 * 1024)) // MB
		{
			ret = ret / (float) (1024 * 1024);
			String size = df.format(ret);
			fileSize = size + " MB";
		}
		else if (ret < (1024 * 1024 * 1024 * 1024)) // GB
		{
			ret = ret / (float) (1024 * 1024 * 1024);
			String size = df.format(ret);
			fileSize = size + " GB";
		}

		return fileSize;
	}

	public static long getFileSizeInBytes(String fileName)
	{
		long ret = 0;
		File f = new File(fileName);
		if (f.isFile())
		{
			return f.length();
		}
		else if (f.isDirectory())
		{
			File[] contents = f.listFiles();
			for (int i = 0; i < contents.length; i++)
			{
				if (contents[i].isFile())
				{
					ret += contents[i].length();
				}
				else if (contents[i].isDirectory())
					ret += getFileSizeInBytes(contents[i].getPath());
			}
		}
		return ret;
	}

	public static Bitmap scaleToActualAspectRatio(Bitmap bitmap, int deviceWidth, int deviceHeight)
	{
		if (bitmap == null)
			return null;

		int bitmapHeight = bitmap.getHeight(); // 563
		int bitmapWidth = bitmap.getWidth(); // 900

		int scaleWidth, scaleHeight;

		if (deviceWidth / deviceHeight >= bitmapWidth / bitmapHeight)
		{
			// resize according height
			scaleHeight = deviceHeight;
			scaleWidth = scaleHeight * bitmapWidth / bitmapHeight;
		}
		else
		{
			// resize according width
			scaleWidth = deviceWidth;
			scaleHeight = scaleWidth * bitmapHeight / bitmapWidth;
		}
		Bitmap bitmapCopy = null;
		try
		{
			bitmapCopy = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true);

			if (bitmap != null && bitmap.getWidth() != bitmapCopy.getWidth() && bitmap.getHeight() != bitmapCopy.getHeight())
			{
				bitmap.recycle();
				bitmap = null;
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return bitmapCopy;
	}
}
