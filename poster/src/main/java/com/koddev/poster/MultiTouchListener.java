package com.koddev.poster;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MultiTouchListener implements OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
    public boolean isRotateEnabled = true;
    public boolean isTranslateEnabled = true;
    public boolean isScaleEnabled = true;
    public float minimumScale = 0.5f;
    public float maximumScale = 10.0f;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector;

    private ImageView mDeleteBtn;
    private RelativeLayout root;

    private boolean isZoom = false;

    private Selectable selectable;
    private ImageView mImageCurrentView;
    private TextView mTextCurrentView;

    public MultiTouchListener(ImageView deleteBtn, RelativeLayout root, Selectable selectable, View currentView) {
        mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        this.mDeleteBtn = deleteBtn;
        this.root = root;
        this.selectable = selectable;
        if(currentView instanceof ImageView){
            mImageCurrentView = (ImageView) currentView;
        } else {
            mTextCurrentView = (TextView) currentView;
        }
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
    }

    private static void move(View view, TransformInfo info) {
        computeRenderOffset(view, info.pivotX, info.pivotY);
        adjustTranslation(view, info.deltaX, info.deltaY);

        // Assume that scaling still maintains aspect ratio.
        float scale = view.getScaleX() * info.deltaScale;
        scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
        view.setScaleX(scale);
        view.setScaleY(scale);

        float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
        view.setRotation(rotation);
    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
            return;
        }

        float[] prevPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(prevPoint);

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

        float[] currPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(currPoint);

        float offsetX = currPoint[0] - prevPoint[0];
        float offsetY = currPoint[1] - prevPoint[1];

        view.setTranslationX(view.getTranslationX() - offsetX);
        view.setTranslationY(view.getTranslationY() - offsetY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(view, event);

        if (!isTranslateEnabled) {
            return true;
        }

        int action = event.getAction();
        switch (action & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mPrevX = event.getX();
                mPrevY = event.getY();

                if(mImageCurrentView != null) {
                    selectable.getSelect(mImageCurrentView);
                } else {
                    selectable.getSelect(mTextCurrentView);
                }

                // Save the ID of this pointer.
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                mDeleteBtn.setVisibility(View.VISIBLE);
                mDeleteBtn.setImageResource(R.mipmap.ic_delete_icon_black);
                // Find the index of the active pointer and fetch its position.
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex != -1) {
                    float currX = event.getX(pointerIndex);
                    float currY = event.getY(pointerIndex);

                    // Only move if the ScaleGestureDetector isn't processing a
                    // gesture.
                    if (!mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - mPrevX, currY - mPrevY);
                    }
                }

                float dX =  event.getRawX();
                float dY = event.getRawY();

                if (insideButton(dX, dY)){
                    view.setAlpha(0.5f);
                    mDeleteBtn.setImageResource(R.mipmap.ic_delete_icon_red);
                    if (!isZoom) {
                        zoomBtn();
                        isZoom = true;
                    }
                } else {
                    view.setAlpha(1f);
                    if (isZoom) {
                        unZoomBtn();
                        isZoom = false;
                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;

                mDeleteBtn.setVisibility(View.GONE);
                /*imageCurrentView = null;
                textCurrentView = null;*/

                float dX =  event.getRawX();
                float dY = event.getRawY();

                if(insideButton(dX, dY)){
                    root.removeView(view);
                    unZoomBtn();
                    selectable.getSelect(null);
                    selectable.delete();
                }

                break;

            case MotionEvent.ACTION_POINTER_UP: {
                // Extract the index of the pointer that left the touch sensor.
                int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mPrevX = event.getX(newPointerIndex);
                    mPrevY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }

                break;
            }
        }

        return true;
    }

    private void zoomBtn(){
        mDeleteBtn.getLayoutParams().width = mDeleteBtn.getWidth() + 15;
        mDeleteBtn.getLayoutParams().height = mDeleteBtn.getHeight() + 15;
        mDeleteBtn.requestLayout();
    }

    private void unZoomBtn(){
        mDeleteBtn.getLayoutParams().width = mDeleteBtn.getWidth() - 15;
        mDeleteBtn.getLayoutParams().height = mDeleteBtn.getHeight() - 15;
        mDeleteBtn.requestLayout();
    }

    private boolean insideButton(float dX, float dY){

        // Get location of Delete Button
        int [] location = new int[2];
        mDeleteBtn.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        // Check if pointer is inside of Delete Button
        if(dX < (x + mDeleteBtn.getWidth())
                && dX > x
                && dY > y
                && dY < y + mDeleteBtn.getHeight()) {
            return true;
        }
        return false;
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector = new Vector2D();

        @Override
        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            mPivotX = detector.getFocusX();
            mPivotY = detector.getFocusY();
            mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        @Override
        public boolean onScale(View view, ScaleGestureDetector detector) {
            TransformInfo info = new TransformInfo();
            info.deltaScale = isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            info.deltaAngle = isRotateEnabled ? Vector2D.getAngle(mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0f;
            info.deltaX = isTranslateEnabled ? detector.getFocusX() - mPivotX : 0.0f;
            info.deltaY = isTranslateEnabled ? detector.getFocusY() - mPivotY : 0.0f;
            info.pivotX = mPivotX;
            info.pivotY = mPivotY;
            info.minimumScale = minimumScale;
            info.maximumScale = maximumScale;

            move(view, info);
            return false;
        }

    }

    private class TransformInfo {

        public float deltaX;
        public float deltaY;
        public float deltaScale;
        public float deltaAngle;
        public float pivotX;
        public float pivotY;
        public float minimumScale;
        public float maximumScale;
    }
}
