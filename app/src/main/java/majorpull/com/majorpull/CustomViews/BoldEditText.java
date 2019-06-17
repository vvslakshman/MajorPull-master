package majorpull.com.majorpull.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class BoldEditText extends EditText {
    private Context c;

    public BoldEditText(Context c) {
        super(c);
        this.c = c;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/rubikbold.ttf");
        setTypeface(tfs);
    }

    public BoldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.c = context;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/rubikbold.ttf");
        setTypeface(tfs);
        // TODO Auto-generated constructor stub
    }

    public BoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/rubikbold.ttf");
        setTypeface(tfs);

    }
}
