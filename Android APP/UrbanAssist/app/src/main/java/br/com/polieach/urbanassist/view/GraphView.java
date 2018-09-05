package br.com.polieach.urbanassist.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.Thing;

public class GraphView extends View {

    private static final int INVALID_POINTER_ID = -1;
    private static final float RADIUS = 15;
    private static final int SCALE = 30;
    private Drawable mImage;
    private float mPosX, centralX, mPosY, centralY;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private Paint paint;
    private Path path;
    private Thing origin, destination, globalOrigin, globalDestination;
    private Edge nextEdge;
    private int distance;
    private int degree;
    private Point originPoint;
    private Point destinationPoint;
    private boolean showDirection, initialized;
    private Edge edge;
    private ArrayList<Edge> edgeList = new ArrayList<>(), edgeListCopy = new ArrayList<>();
    private Iterator<Edge> edgeIterator;
    private HashMap<Thing, Point> pointMap;
    private long lastTime;

    public GraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        mImage = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {

            }

            @Override
            public void setAlpha(int i) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
        mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
        initialized = true;
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (System.currentTimeMillis() - lastTime > 2000) {
            lastTime = System.currentTimeMillis();
            for (Map.Entry<Thing, Point> entry : pointMap.entrySet()) {
                if (Math.hypot(entry.getValue().x - ev.getX(), entry.getValue().y - ev.getY()) <= RADIUS * mScaleFactor) {
                    Intent intent = new Intent(getContext(), POIDetailsActivity.class);
                    intent.putExtra("pointOfInterest", entry.getKey());
                    getContext().startActivity(intent);
                    return true;
                }
            }
        }

        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                try {

                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);

                    if (!mScaleDetector.isInProgress()) {
                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        mPosX += dx;
                        mPosY += dy;

                        invalidate();
                    }

                    mLastTouchX = x;
                    mLastTouchY = y;
                } catch (Exception e) {
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {

                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        restartEdgeList();

        if (edgeList.size() > 0) {

            if (initialized) {
                mPosX = centralX = canvas.getWidth() / 2;
                mPosY = centralY = canvas.getHeight() / 2;
                initialized = false;
            }

            Point initialPoint = new Point((int) mPosX, (int) mPosY);
            drawNode(initialPoint, edgeList.get(0).getOrigin(), canvas);

            while (edgeIterator.hasNext()) {

                edge = edgeIterator.next();
                origin = edge.getOrigin();
                destination = edge.getDestination();
                distance = edge.getDistance();
                degree = edge.getDegree();
                originPoint = null;
                destinationPoint = null;

                if (pointMap.containsKey(origin.getID())) {
                    originPoint = pointMap.get(origin.getID());
                    if (pointMap.containsKey(destination.getID())) {
                        destinationPoint = pointMap.get(destination.getID());
                    } else {
                        destinationPoint = defineSecondPosition(originPoint, distance, degree);
                        drawNode(destinationPoint, edge.getDestination(), canvas);
                    }
                    drawEdge(originPoint, destinationPoint, nextEdge.getDestination().getID() == edge.getDestination().getID(), canvas);
                    edgeIterator.remove();

                } else if (pointMap.containsKey(destination.getID())) {

                    destinationPoint = pointMap.get(destination.getID());
                    originPoint = defineSecondPosition(destinationPoint, distance, degree - 180);

                    drawNode(originPoint, edge.getOrigin(), canvas);

                    drawEdge(originPoint, destinationPoint, nextEdge.getDestination().getID() == edge.getDestination().getID(), canvas);


                    edgeIterator.remove();
                }
            }
        }

        float pivotX, pivotY;
        pivotX = mImage.getIntrinsicWidth() / 2;
        pivotY = mImage.getIntrinsicHeight() / 2;

        canvas.save();

        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor, pivotX, pivotY);
        mImage.draw(canvas);
        canvas.restore();
    }

    public void updateNextDirection(Edge nextEdge) {
        this.nextEdge = nextEdge;
        invalidate();
    }

    private void restartEdgeList() {

        edgeList.addAll(edgeListCopy);
        edgeIterator = edgeList.iterator();

        showDirection = false;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(10);
        path = new Path();

        pointMap = new HashMap<>();
    }

    private Point defineSecondPosition(Point firstPosition, int distance, int degree) {

        Point secondPosition = new Point((int) (firstPosition.x + (Math.cos(Math.toRadians(degree)) * distance * SCALE * mScaleFactor)), (int) (firstPosition.y + (Math.sin(Math.toRadians(degree)) * distance * SCALE * mScaleFactor)));
        return secondPosition;
    }

    private void drawNode(Point point, Thing thing, Canvas canvas) {

        if (globalDestination != null && thing.getID() == globalDestination.getID()) {
            paint.setColor(Color.RED);
        } else if (nextEdge.getOrigin().getID() == thing.getID()) {
            paint.setColor(Color.GREEN);
        } else if (thing.getID() == globalOrigin.getID()) {
            paint.setColor(Color.BLUE);
        } else {
            paint.setColor(Color.BLACK);
        }

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.x, point.y, RADIUS * mScaleFactor, paint);

        pointMap.put(thing, point);
    }

    private void drawEdge(Point originPoint, Point destinationPoint, boolean isNext, Canvas canvas) {

        if (isNext) {
            paint.setColor(Color.GREEN);
        } else {
            paint.setColor(Color.BLACK);
        }

        int arrowHeadAngle = 45;
        int arrowHeadLenght = (int) (1 * SCALE * mScaleFactor);

        int cos = (int) (Math.cos(Math.toRadians(degree)) * (distance + RADIUS) * mScaleFactor);
        int sin = (int) (Math.sin(Math.toRadians(degree)) * (distance + RADIUS) * mScaleFactor);

        int originEdgeX = originPoint.x + cos;
        int originEdgeY = originPoint.y + sin;
        int destinationEdgeX = destinationPoint.x - cos;
        int destinationEdgeY = destinationPoint.y - sin;

        double angle = Math.atan2(destinationEdgeY - originEdgeY, destinationEdgeX - originEdgeX) * 180 / Math.PI + arrowHeadAngle;

        path.reset();
        path.moveTo(originEdgeX, originEdgeY);
        path.lineTo(destinationEdgeX, destinationEdgeY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, paint);

        float[] linePts = new float[]{destinationEdgeX - arrowHeadLenght, destinationEdgeY, destinationEdgeX, destinationEdgeY};
        float[] linePts2 = new float[]{destinationEdgeX, destinationEdgeY, destinationEdgeX, destinationEdgeY + arrowHeadLenght};
        Matrix rotateMat = new Matrix();

        //get the center of the line
        float centerX = destinationEdgeX;
        float centerY = destinationEdgeY;

        //set the angle

        //rotate the matrix around the center
        rotateMat.setRotate((float) angle, centerX, centerY);
        rotateMat.mapPoints(linePts);
        rotateMat.mapPoints(linePts2);

        path.reset();
        path.moveTo(linePts[0], linePts[1]);
        path.lineTo(linePts[2], linePts[3]);
        path.lineTo(linePts2[0], linePts2[1]);
        path.lineTo(linePts2[2], linePts2[3]);
        path.lineTo(linePts[0], linePts[1]);
        path.close();

        canvas.drawPath(path, paint);
    }

    public void centralize() {

        mPosX = centralX;
        mPosY = centralY;
        mScaleFactor = 1.f;

        invalidate();
    }

    public void updateEdgeList(ArrayList<Edge> edgeList, Thing origin, Edge nextEdge, Thing destination) {

        mScaleFactor = 1.f;
        initialized = true;
        edgeListCopy = new ArrayList<>();
        edgeListCopy.addAll(edgeList);

        globalOrigin = origin;
        globalDestination = destination;
        this.nextEdge = nextEdge;

        invalidate();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }
}
