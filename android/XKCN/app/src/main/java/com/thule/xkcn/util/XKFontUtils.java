package com.thule.xkcn.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author thule
 * 
 */
public class XKFontUtils
{
	public static final String Font_Candy = "fonts/CANDY.TTF";
	public static final String Font_Roboto_Regular = "fonts/RobotoCondensed-Bold.ttf";
    public static final String Font_Roboto_Bold = "fonts/RobotoCondensed-Regular.ttf";
	public static Typeface CandyTypeface;
	public static Typeface RobotoTypeface;
    public static Typeface RobotoBoldTypeface;

	public enum FontType
	{
		Candy, RobotoRegular, RobotoBold
	}

	/**
	 * @param _context
	 * @param type
	 * @return
	 */
	static Typeface getTypeface(Context _context, FontType type)
	{
		try
		{
			if (CandyTypeface == null)
			{
				CandyTypeface = Typeface.createFromAsset(_context.getAssets(), Font_Candy);
			}
			if (RobotoTypeface == null)
			{
				RobotoTypeface = Typeface.createFromAsset(_context.getAssets(), Font_Roboto_Regular);
			}
            if (RobotoBoldTypeface == null)
            {
                RobotoBoldTypeface = Typeface.createFromAsset(_context.getAssets(), Font_Roboto_Bold);
            }

		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}

		switch (type)
		{
		case Candy:
			return CandyTypeface;
		case RobotoBold:
			return RobotoBoldTypeface;
		default:
			return RobotoTypeface;
		}
	}

	/**
	 * @param _context
	 * @param obj
	 * @param type
	 */
	public static void setCustomFont(Context _context, Object obj, FontType type)
	{

		Typeface tf = getTypeface(_context, type);
		if (tf == null)
			return;
		switch (type)
		{

		case Candy:
			if (obj instanceof TextView)
			{
				((TextView) obj).setTypeface(tf, Typeface.NORMAL);
			}
			else if (obj instanceof EditText)
			{
				((EditText) obj).setTypeface(tf, Typeface.NORMAL);
			}
			else if (obj instanceof Button)
			{
				((Button) obj).setTypeface(tf, Typeface.NORMAL);
			}
			else if (obj instanceof RadioButton)
			{
				((RadioButton) obj).setTypeface(tf, Typeface.NORMAL);
			}
			break;
		case RobotoBold:
            if (obj instanceof TextView)
            {
                ((TextView) obj).setTypeface(tf, Typeface.NORMAL);
            }
            else if (obj instanceof EditText)
            {
                ((EditText) obj).setTypeface(tf, Typeface.NORMAL);
            }
            else if (obj instanceof Button)
            {
                ((Button) obj).setTypeface(tf, Typeface.NORMAL);
            }
            else if (obj instanceof RadioButton)
            {
                ((RadioButton) obj).setTypeface(tf, Typeface.NORMAL);
            }
            break;
		case RobotoRegular:
            if (obj instanceof TextView)
            {
                ((TextView) obj).setTypeface(tf, Typeface.NORMAL);
            }
            else if (obj instanceof EditText)
            {
                ((EditText) obj).setTypeface(tf, Typeface.NORMAL);
            }
            else if (obj instanceof Button)
            {
                ((Button) obj).setTypeface(tf, Typeface.NORMAL);
            }
            else if (obj instanceof RadioButton)
            {
                ((RadioButton) obj).setTypeface(tf, Typeface.NORMAL);
            }
            break;
		default:
			break;
		}
	}
}
