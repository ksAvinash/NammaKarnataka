package smartAmigos.com.nammakarnataka;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



@SuppressLint("AppCompatCustomView")
public class readingFont extends TextView {

    public readingFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public readingFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public readingFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Verdana.ttf" );
        setTypeface(tf);
    }

}