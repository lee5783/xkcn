/**
 * 
 */
package com.thule.xkcn.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * @author thule
 * 
 */
public class XKBlurUtils
{
	public static Bitmap blurBitmap(Context context, Bitmap bitmap, float opacity)
	{
		final RenderScript rs = RenderScript.create(context);
		final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_SCRIPT);
		final Allocation allocation = Allocation.createTyped(rs, input.getType());

		// Bitmap outBitmap = bitmap.copy(bitmap.getConfig(), true);

		Bitmap outBitmap = Bitmap.createBitmap((int) (bitmap.getWidth()), (int) (bitmap.getHeight()),
				Bitmap.Config.ARGB_8888);
        //Bitmap outBitmap = Bitmap.createBitmap((int) (bitmap.getWidth()), (int) (bitmap.getHeight()),
        //        bitmap.getConfig());
		// Blur the image
		// final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
		// Element.U8_4(rs));
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, allocation.getElement());
		int radius = opacityToBlurRadius(opacity);
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(allocation);
		allocation.copyTo(outBitmap);

		// //Make the image greyscale
		// final ScriptIntrinsicColorMatrix scriptColor =
		// ScriptIntrinsicColorMatrix.create(rs, Element.U8_4(rs));
		// scriptColor.setGreyscale();
		// scriptColor.forEach(input, output);
		// output.copyTo(grayBitmap);
		//
		// //Show the results
		// mNormalImage.setImageBitmap(inBitmap);
		// mBlurImage.setImageBitmap(outBitmap);
		// mColorImage.setImageBitmap(grayBitmap);

		// We don't need RenderScript anymore
		rs.destroy();
		return outBitmap;
	}

	public static int opacityToBlurRadius(float _opacity)
	{
		int radius = 1;
		if (_opacity <= .25f)
		{
			radius = 19;
		}
		else if (_opacity > .25f && _opacity <= .5f)
		{
			radius = 21;
		}
		else if (_opacity > .5f && _opacity <= .75f)
		{
			radius = 23;
		}
		else
		{
			radius = 25;
		}

		return radius;
	}
}
