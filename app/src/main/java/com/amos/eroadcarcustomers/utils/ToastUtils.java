package com.amos.eroadcarcustomers.utils;


import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.amos.eroadcarcustomers.MyApplication;

/**
 * Created by amos on 14-4-19.
 */
public class ToastUtils {
	private ToastUtils() {
	}

	private static void show(Context context, int resId, int duration) {
		Toast.makeText(context, resId, duration).show();
	}

	private static void show(Context context, String message, int duration) {
		Toast.makeText(context, message, duration).show();
	}

	public static void showShort(int resId) {
		Toast toast = Toast.makeText(MyApplication.getContext(), resId,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		// Toast.makeText(MyApplication.getContext(), resId, Toast.LENGTH_SHORT)
		// .show();
	}

	public static void showShort(String message) {
		Toast toast = Toast.makeText(MyApplication.getContext(), message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		// Toast.makeText(MyApplication.getContext(), message,
		// Toast.LENGTH_SHORT)
		// .show();
	}

	public static void showLong(int resId) {
		Toast toast = Toast.makeText(MyApplication.getContext(), resId,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		// Toast.makeText(MyApplication.getContext(), resId, Toast.LENGTH_LONG)
		// .show();
	}

	public static void showLong(String message) {
		Toast toast = Toast.makeText(MyApplication.getContext(), message,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		// Toast.makeText(MyApplication.getContext(), message,
		// Toast.LENGTH_LONG)
		// .show();
	}
}

