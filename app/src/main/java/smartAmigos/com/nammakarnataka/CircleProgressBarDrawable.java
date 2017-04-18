package smartAmigos.com.nammakarnataka;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.facebook.drawee.drawable.ProgressBarDrawable;



public class CircleProgressBarDrawable extends ProgressBarDrawable {
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mLevel = 0;
    private int maxLevel = 10000;

    int version;


    public CircleProgressBarDrawable(int version) {
        this.version = version;
    }

    @Override
    protected boolean onLevelChange(int level) {
        mLevel = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (getHideWhenZero() && mLevel == 0) {
            return;
        }


        if(version==1){
            drawBar(canvas, maxLevel, Color.rgb(230,230,230));
            drawBar(canvas, mLevel, Color.BLACK);
        }else {
            drawBar(canvas, maxLevel, Color.WHITE);
            drawBar(canvas, mLevel, Color.DKGRAY);
        }



    }

    private void drawBar(Canvas canvas, int level, int color) {
        Rect bounds = getBounds();


        RectF rectF = new RectF((float) (bounds.right * .2), (float) (bounds.bottom * .2),
                (float) (bounds.right * .8), (float) (bounds.bottom * .8));


        float x = bounds.right / 2 ;
        float y = bounds.bottom / 2 ;
        PointF center = new PointF(x, y);



        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        if(version == 1){
            mPaint.setStrokeWidth(5);
            if (level != 0)
                canvas.drawArc(rectF, 0, (float) (level * 360 / maxLevel), false, mPaint);
        }
        else{
            mPaint.setStrokeWidth(12);
            if (level != 0)
                ArcUtils.drawArc(canvas, center, 120, 0, (float) (level * 360 / maxLevel), mPaint);
        }


    }
}