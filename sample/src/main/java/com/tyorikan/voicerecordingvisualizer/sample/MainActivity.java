package com.tyorikan.voicerecordingvisualizer.sample;


import com.tyorikan.voicerecordingvisualizer.RecordingSampler;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;


public class MainActivity extends AppCompatActivity implements
        RecordingSampler.CalculateVolumeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Recording Info
    private RecordingSampler mRecordingSampler;

    // View
    private VisualizerView mVisualizerView;
    private VisualizerView mVisualizerView2;
    private VisualizerView mVisualizerView3;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        {
            mVisualizerView = (VisualizerView) findViewById(R.id.visualizer);
            ViewTreeObserver observer = mVisualizerView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mVisualizerView.setBaseY(mVisualizerView.getHeight());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mVisualizerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mVisualizerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }

        {
            mVisualizerView2 = (VisualizerView) findViewById(R.id.visualizer2);
            ViewTreeObserver observer = mVisualizerView2.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mVisualizerView2.setBaseY(mVisualizerView2.getHeight() / 5);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mVisualizerView2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mVisualizerView2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }

        mVisualizerView3 = (VisualizerView) findViewById(R.id.visualizer3);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        // create AudioRecord
        mRecordingSampler = new RecordingSampler();
        mRecordingSampler.setVolumeListener(this);
        mRecordingSampler.setSamplingInterval(100);
        mRecordingSampler.link(mVisualizerView);
        mRecordingSampler.link(mVisualizerView2);
        mRecordingSampler.link(mVisualizerView3);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecordingSampler.isRecording()) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_mic);
                    mRecordingSampler.stopRecording();
                } else {
                    mFloatingActionButton.setImageResource(R.drawable.ic_mic_off);
                    mRecordingSampler.startRecording();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mRecordingSampler.release();
        super.onDestroy();
    }

    @Override
    public void onCalculateVolume(int volume) {
        // for custom implement
        Log.d(TAG, String.valueOf(volume));
    }
}
