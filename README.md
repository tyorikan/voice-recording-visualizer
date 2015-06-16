
# Voice Recording Visualizer

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/tyorikan/maven/voice-recording-visualizer/images/download.svg)](https://bintray.com/tyorikan/maven/voice-recording-visualizer/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Voice%20Recording%20Visualizer-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1984)

Simple Visualizer from mic input for Android.

## Usage
```
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        VisualizerView visualizerView = (VisualizerView) findViewById(R.id.visualizer);

        RecordingSampler recordingSampler = new RecordingSampler();
        recordingSampler.setVolumeListener(this);  // for custom implements
        recordingSampler.setSamplingInterval(100); // voice sampling interval
        recordingSampler.link(visualizerView);     // link to visualizer

        recordingSampler.startRecording();
    }
    
    @Override
    protected void onPause() {
        mRecordingSampler.stopRecording();
        super.onPause();
    }
    
    @Override 
    protected void onDestroy() { 
        mRecordingSampler.release();
        super.onDestroy(); 
    } 
```

## VisualizerView
```
<com.tyorikan.voicerecordingvisualizer.VisualizerView
                android:id="@+id/visualizer"
                android:layout_width="100dp"
                android:layout_height="100dp"
                android:background="@android:color/black"
                app:numColumns="4"
                app:renderColor="@color/renderColor"
                app:renderRange="top" />
```

### VisualizerView attrs
| Params        | format | value |
|:--------------|:------------:|:------------:|
| numColumn     | integer | num of visualizer column (ex. `5`, `20`, `100`) |
| renderColor   | color |  visualizer color (ex. `#EFEFEF`, `@color/light_blue`) |
| renderRange   | enum | render direction `top` `bottom` `both` |
| (renderType)  | flag | render type `bar` |

## demo
[![IMAGE demo](http://img.youtube.com/vi/fJTl1bgQ3j4/0.jpg)](http://www.youtube.com/watch?v=fJTl1bgQ3j4)

## Samples
<a href="https://play.google.com/store/apps/details?id=com.tyorikan.voicerecordingvisualizer.sample"><img src="http://www.android.com/images/brand/get_it_on_play_logo_large.png"/></a>

## Gradle
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.tyorikan:voice-recording-visualizer:1.0.0@aar'
}
```

## License
    Copyright 2015 tyorikan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
