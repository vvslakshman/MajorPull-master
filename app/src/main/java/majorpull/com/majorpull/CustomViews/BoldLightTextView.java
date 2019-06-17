package majorpull.com.majorpull.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class BoldLightTextView extends TextView {
    private Context c;

    public BoldLightTextView(Context c) {
        super(c);
        this.c = c;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/rubikmedium.ttf");
        setTypeface(tfs);

    }

    public BoldLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.c = context;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/rubikmedium.ttf");
        setTypeface(tfs);
        // TODO Auto-generated constructor stub
    }

    public BoldLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/rubikmedium.ttf");
        setTypeface(tfs);

    }
}
