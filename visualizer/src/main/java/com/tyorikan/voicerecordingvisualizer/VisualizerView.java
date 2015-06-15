/*
 * Copyright (C) 2015 tyorikan
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tyorikan.voicerecordingvisualizer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * A class that draws visualizations of data received from {@link RecordingSampler}
 *
 * Created by tyorikan on 2015/06/08.
 */
public class VisualizerView extends FrameLayout {

    private static final int DEFAULT_NUM_COLUMNS = 20;
    private static final int RENDAR_RANGE_TOP = 0;
    private static final int RENDAR_RANGE_BOTTOM = 1;
    private static final int RENDAR_RANGE_TOP_BOTTOM = 2;

    private int mNumColumns;
    private int mRenderColor;
    private int mType;
    private int mRenderRange;

    private int mBaseY;

    private Canvas mCanvas;
    private Bitmap mCanvasBitmap;
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mPaint.setColor(mRenderColor);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.visualizerView);
        mNumColumns = args.getInteger(R.styleable.visualizerView_numColumns, DEFAULT_NUM_COLUMNS);
        mRenderColor = args.getColor(R.styleable.visualizerView_renderColor, Color.WHITE);
        mType = args.getInt(R.styleable.visualizerView_renderType, Type.BAR.getFlag());
        mRenderRange = args.getInteger(R.styleable.visualizerView_renderRange, RENDAR_RANGE_TOP);
        args.recycle();
    }

    /**
     * @param baseY center Y position of visualizer
     */

    public void setBaseY(int baseY) {
        mBaseY = baseY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create canvas once we're ready to draw
        mRect.set(0, 0, getWidth(), getHeight());

        if (mCanvasBitmap == null) {
            mCanvasBitmap = Bitmap.createBitmap(
                    canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        }

        if (mCanvas == null) {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }

    /**
     * receive volume from {@link RecordingSampler}
     *
     * @param volume volume from mic input
     */
    protected void receive(final int volume) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                if ((mType & Type.BAR.getFlag()) != 0) {
                    drawBar(volume);
                }
                invalidate();
            }
        });
    }

    private void drawBar(int volume) {
        if (mNumColumns > getWidth()) {
            mNumColumns = DEFAULT_NUM_COLUMNS;
        }

        float columnWidth = (float) getWidth() / (float) mNumColumns;
        float space = columnWidth / 8f;

        for (int i = 0; i < mNumColumns; i++) {
            double randomVolume = Math.random() * volume + 1;
            float height = ((float) getHeight() / 60f) * (float) randomVolume;

            RectF rect =
                    createRectF(i * columnWidth + space, (i + 1) * columnWidth - space, height);
            mCanvas.drawRect(rect, mPaint);
        }
    }

    private RectF createRectF(float left, float right, float height) {
        if (mBaseY == 0) {
            mBaseY = getHeight() / 2;
        }
        switch (mRenderRange) {
            case RENDAR_RANGE_TOP:
                return new RectF(left, mBaseY - (height / 2), right, mBaseY);
            case RENDAR_RANGE_BOTTOM:
                return new RectF(left, mBaseY, right, mBaseY + (height / 2));
            case RENDAR_RANGE_TOP_BOTTOM:
                return new RectF(left, mBaseY - (height / 2), right, mBaseY + (height / 2));
            default:
                return new RectF(left, mBaseY - (height / 2), right, mBaseY);
        }
    }

    /**
     * visualizer type
     */
    public enum Type {
        BAR(0x1);

        private int mFlag;

        Type(int flag) {
            mFlag = flag;
        }

        public int getFlag() {
            return mFlag;
        }
    }
}
