package majorpull.com.majorpull.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by user103 on 28/12/17.
 */

public class TextViewBold extends android.support.v7.widget.AppCompatTextView
{
    private Context context;

    public TextViewBold(Context context) {
        super(context);
        this.context = context;
        Typeface tfs = Typeface.createFromAsset(context.getAssets(), "fonts/rubikbold.ttf");
        setTypeface(tfs);
    }

    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Typeface tfs = Typeface.createFromAsset(context.getAssets(),
                "fonts/rubikbold.ttf");
        setTypeface(tfs);
    }

    public TextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Typeface tfs = Typeface.createFromAsset(context.getAssets(),
                "fonts/rubikbold.ttf");
        setTypeface(tfs);
    }
}
