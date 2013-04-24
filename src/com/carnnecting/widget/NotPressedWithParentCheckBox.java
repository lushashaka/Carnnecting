package com.carnnecting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

/**
 *	FIXME: Note that somehow the RSVP checkbox is still pressed with the list item view ..  
 */
public class NotPressedWithParentCheckBox extends CheckBox {
	public NotPressedWithParentCheckBox(Context context) {
		super(context);
	}
	
	public NotPressedWithParentCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	public NotPressedWithParentCheckBox(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (pressed && getParent() instanceof View && ((View) getParent()).isPressed()) {
			return;
	    }
		super.setPressed(pressed);
	}
}
