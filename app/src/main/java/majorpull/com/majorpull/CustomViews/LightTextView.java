package majorpull.com.majorpull.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class LightTextView extends TextView {
	private Context c;

	public LightTextView(Context c) {
		super(c);
		this.c = c;
		Typeface tfs = Typeface.createFromAsset(c.getAssets(),
				"fonts/rubiklight.ttf");
		setTypeface(tfs);

	}

	public LightTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.c = context;
		Typeface tfs = Typeface.createFromAsset(c.getAssets(),
				"fonts/rubiklight.ttf");
		setTypeface(tfs);
		// TODO Auto-generated constructor stub
	}

	public LightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.c = context;
		Typeface tfs = Typeface.createFromAsset(c.getAssets(), "fonts/rubiklight.ttf");
		setTypeface(tfs);

	}
}
