package com.ansoft.chatapp.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClickEffect {

	public static void Opacity(final View view) {
		view.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(v == view) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) {
						v.setAlpha(.5f);
					} else {
						v.setAlpha(1f);
					}
				}
				return false;
			}
		});
	}
	
	public static void setFont(Context context, TextView textView, String Fontname) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/"+Fontname);
		textView.setTypeface(tf);
	}
	
	public static void setFont(Context context, EditText editText, String Fontname) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/"+Fontname);
		editText.setTypeface(tf);
	}
	
	public static void setFont(Context context, Button button, String Fontname) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/"+Fontname);
		button.setTypeface(tf);
	}
}
