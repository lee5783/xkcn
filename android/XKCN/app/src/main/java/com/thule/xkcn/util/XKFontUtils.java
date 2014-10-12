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
    public static final String Font_Lobster = "fonts/Lobster 1.4.otf";
	public static final String Font_Roboto_Regular = "fonts/Roboto-Regular.ttf";
    public static final String Font_Roboto_Light = "fonts/Roboto-Light.ttf";
	public static Typeface CandyTypeface;
    public static Typeface LobsterTypeface;
	public static Typeface RobotoTypeface;
    public static Typeface RobotoLightTypeface;

	public enum FontType
	{
		Candy, Lobster, RobotoRegular, RobotoLight
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
            if (LobsterTypeface == null)
            {
                LobsterTypeface = Typeface.createFromAsset(_context.getAssets(), Font_Lobster);
            }
			if (RobotoTypeface == null)
			{
				RobotoTypeface = Typeface.createFromAsset(_context.getAssets(), Font_Roboto_Regular);
			}
            if (RobotoLightTypeface == null)
            {
                RobotoLightTypeface = Typeface.createFromAsset(_context.getAssets(), Font_Roboto_Light);
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
            case Lobster:
                return LobsterTypeface;
		case RobotoLight:
			return RobotoLightTypeface;
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
        case Lobster:
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
		case RobotoLight:
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
