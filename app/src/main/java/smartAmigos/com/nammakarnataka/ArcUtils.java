package smartAmigos.com.nammakarnataka;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;


import org.jetbrains.annotations.NotNull;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static java.lang.StrictMath.sin;

public final class ArcUtils {
    private static final double FULL_CIRCLE_RADIANS = toRadians(360d);

    private ArcUtils() { }


    public static void drawArc(@NotNull Canvas canvas, PointF circleCenter, float circleRadius,
                               float startAngle, float sweepAngle, @NotNull Paint paint)
    {
        drawArc(canvas, circleCenter, circleRadius, startAngle, sweepAngle, paint, 8, false);
    }


    public static void drawArc(@NotNull Canvas canvas, PointF circleCenter, float circleRadius,
                               float startAngle, float sweepAngle, @NotNull Paint paint,
                               int arcsPointsOnCircle, boolean arcsOverlayPoints)
    {
        if (sweepAngle == 0f)
        {
            final PointF p = pointFromAngleDegrees(circleCenter, circleRadius, startAngle);
            canvas.drawPoint(p.x, p.y, paint);
        }
        else
        {
            canvas.drawPath(createBezierArcDegrees(
                    circleCenter, circleRadius, startAngle, sweepAngle,
                    arcsPointsOnCircle, arcsOverlayPoints, null), paint);
        }
    }


    public static double normalizeRadians(double radians)
    {
        radians %= FULL_CIRCLE_RADIANS;
        if (radians < 0d) { radians += FULL_CIRCLE_RADIANS; }
        if (radians == FULL_CIRCLE_RADIANS) { radians = 0d; }
        return radians;
    }



    @NotNull
    public static PointF pointFromAngleRadians(@NotNull PointF center, float radius, double angleRadians)
    {
        return new PointF((float)(center.x + radius * cos(angleRadians)),
                (float)(center.y + radius * sin(angleRadians)));
    }


    @NotNull
    public static PointF pointFromAngleDegrees(@NotNull PointF center, float radius, float angleDegrees)
    {
        return pointFromAngleRadians(center, radius, toRadians(angleDegrees));
    }


    public static void addBezierArcToPath(@NotNull Path path, @NotNull PointF center,
                                          @NotNull PointF start, @NotNull PointF end, boolean moveToStart)
    {
        if (moveToStart) { path.moveTo(start.x, start.y); }
        if (start.equals(end)) { return; }

        final double ax = start.x - center.x;
        final double ay = start.y - center.y;
        final double bx = end.x - center.x;
        final double by = end.y - center.y;
        final double q1 = ax * ax + ay * ay;
        final double q2 = q1 + ax * bx + ay * by;
        final double k2 = 4d / 3d * (sqrt(2d * q1 * q2) - q2) / (ax * by - ay * bx);
        final float x2 = (float)(center.x + ax - k2 * ay);
        final float y2 = (float)(center.y + ay + k2 * ax);
        final float x3 = (float)(center.x + bx + k2 * by);
        final float y3 = (float)(center.y + by - k2 * bx);

        path.cubicTo(x2, y2, x3, y3, end.x, end.y);
    }


    @NotNull
    public static Path createBezierArcRadians(@NotNull PointF center, float radius, double startAngleRadians,
                                              double sweepAngleRadians, int pointsOnCircle, boolean overlapPoints,
                                              @Nullable Path addToPath)
    {
        final Path path = addToPath != null ? addToPath : new Path();
        if (sweepAngleRadians == 0d) { return path; }

        if (pointsOnCircle >= 1)
        {
            final double threshold = FULL_CIRCLE_RADIANS / pointsOnCircle;
            if (abs(sweepAngleRadians) > threshold)
            {
                double angle = normalizeRadians(startAngleRadians);
                PointF end, start = pointFromAngleRadians(center, radius, angle);
                path.moveTo(start.x, start.y);
                if (overlapPoints)
                {
                    final boolean cw = sweepAngleRadians > 0; // clockwise?
                    final double angleEnd = angle + sweepAngleRadians;
                    while (true)
                    {
                        double next = (cw ? ceil(angle / threshold) : floor(angle / threshold)) * threshold;
                        if (angle == next) { next += threshold * (cw ? 1d : -1d); }
                        final boolean isEnd = cw ? angleEnd <= next : angleEnd >= next;
                        end = pointFromAngleRadians(center, radius, isEnd ? angleEnd : next);
                        addBezierArcToPath(path, center, start, end, false);
                        if (isEnd) { break; }
                        angle = next;
                        start = end;
                    }
                }
                else
                {
                    final int n = abs((int)ceil(sweepAngleRadians / threshold));
                    final double sweep = sweepAngleRadians / n;
                    for (int i = 0;
                         i < n;
                         i++, start = end)
                    {
                        angle += sweep;
                        end = pointFromAngleRadians(center, radius, angle);
                        addBezierArcToPath(path, center, start, end, false);
                    }
                }
                return path;
            }
        }

        final PointF start = pointFromAngleRadians(center, radius, startAngleRadians);
        final PointF end = pointFromAngleRadians(center, radius, startAngleRadians + sweepAngleRadians);
        addBezierArcToPath(path, center, start, end, true);
        return path;
    }

    @NotNull
    public static Path createBezierArcDegrees(@NotNull PointF center, float radius, float startAngleDegrees,
                                              float sweepAngleDegrees, int pointsOnCircle, boolean overlapPoints,
                                              @Nullable Path addToPath)
    {
        return createBezierArcRadians(center, radius, toRadians(startAngleDegrees), toRadians(sweepAngleDegrees),
                pointsOnCircle, overlapPoints, addToPath);
    }
}