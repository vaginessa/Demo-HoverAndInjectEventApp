package com.example.hoversample.ui;

import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.hoversample.R;

public class FloatingAuthenticationDialog extends BaseFloatingDialog
{
	private EditText et_password;
	private ImageButton btn_close;

	@Override
	public void createAndAttachView(int id, FrameLayout frame)
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		View customView = inflater.inflate(
				R.layout.view_floating_layer, frame, true);
		
		

	}

	@Override
	public int getFlags(int id)
	{
		return super.getFlags(id)
				& (~StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE);
	}

	@Override
	public StandOutLayoutParams getParams(int id, Window window)
	{
		// TODO Auto-generated method stub
		return null;
	}

	// -------------------------------------------------------

}
