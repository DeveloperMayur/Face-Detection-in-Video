///*
// * Copyright (C) 2013 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.example.android.mediarecorder;
//
//import android.app.Activity;
//import android.content.Context;
//import android.hardware.Camera;
//import android.media.CamcorderProfile;
//import android.media.MediaRecorder;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//
//import com.example.android.common.media.CameraHelper;
//import com.example.android.videocompression.MediaController;
//
//import java.io.File;
//import java.util.List;
//
//import es.claucookie.miniequalizerlibrary.EqualizerView;
//
///**
// * This activity uses the camera/camcorder as the A/V source for the {@link MediaRecorder} API.
// * A {@link TextureView} is used as the camera preview which limits the code to API 14+. This
// * can be easily replaced with a {@link android.view.SurfaceView} to run on older devices.
// */
//public class VideoRecoActivity extends Activity {
//
//    private Camera mCamera = null;
//    private CamcorderProfile camcorderProfile = null;
//    private CameraSourcePreview1 cameraSourcePreview;
//    private GraphicOverlay<OcrGraphic> graphicOverlay;
//
//    private Button captureButton, btnSwitchCamera;
//
//    private MediaRecorder mMediaRecorder = null;
//    private File mOutputFile;
//
//    private boolean isRecording = false;
//    private int CameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
//    /**
//     * Rotation of the device, and thus the associated preview images captured from the device.
//     */
//    private int mRotation;
//    private static final String TAG = "Recorder";
//
//    EqualizerView equalizer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_reco);
//
//        captureButton = (Button) findViewById(R.id.button_capture);
//        btnSwitchCamera = (Button) findViewById(R.id.button_switch_camera);
//        equalizer = (EqualizerView) findViewById(R.id.equalizer_view);
//        cameraSourcePreview = (CameraSourcePreview1) findViewById(R.id.cameraSourcePreview);
//        graphicOverlay = (GraphicOverlay<OcrGraphic>) findViewById(R.id.graphicOverlay);
//        graphicOverlay.handleRotation(getResources().getConfiguration().orientation);
//
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // if we are using MediaRecorder, release it first
//        releaseMediaRecorder();
//        // release the camera immediately on pause event
//        releaseCamera();
//    }
//
//    /**
//     * Restarts the camera.
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//
//    }
//
//
//    private void startRecording(){
//        new MediaPrepareTask().execute(Camera.CameraInfo.CAMERA_FACING_BACK);
//        showEqualizer();
//        captureButton.setBackground(this.getResources().getDrawable(R.drawable.video_camera_stop_icon));
//    }
//
//    private void stopRecording(){
//
//        try {
//            mMediaRecorder.stop();  // stop the recording
//            // compress Vedio file in smaller size so it can be upload to server
//            Log.e("before compression", "File Compression task is starting");
//            new VideoCompressor().execute();
//            toggleEqualizer();
//            captureButton.setBackground(this.getResources().getDrawable(R.drawable.video_camera_icon));
//        } catch (RuntimeException ex) {
//            ex.printStackTrace();
//            // RuntimeException is thrown when stop() is called immediately after start().
//            // In this case the output file is not properly constructed ans should be deleted.
//            Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
//            //noinspection ResultOfMethodCallIgnored
//            mOutputFile.delete();
//        }
//
//        // release the MediaRecorder object
//        releaseMediaRecorder();
//        isRecording = false;
//        // stop recording and release camera
//        releaseCamera();
//
//    }
//
//    /**
//     * The capture button controls all user interaction. When recording, the button click
//     * stops recording, releases {@link android.media.MediaRecorder} and {@link android.hardware.Camera}. When not recording,
//     * it prepares the {@link android.media.MediaRecorder} and starts recording.
//     *
//     * @param view the view generating the event.
//     */
//    public void onCaptureClick(View view) {
//        if (isRecording) {
//            stopRecording();
//        } else {
//            startRecording();
//
//        }
//
//    }
//
//    public void onSwitchClick(View view) {
//        if (isRecording) {
//            // stop recording and release camera
//            try {
//                mMediaRecorder.stop();  // stop the recording
//                toggleEqualizer();
//                captureButton.setBackground(this.getResources().getDrawable(R.drawable.video_camera_icon));
//                mOutputFile.delete();
//            } catch (RuntimeException e) {
//                // RuntimeException is thrown when stop() is called immediately after start().
//                // In this case the output file is not properly constructed ans should be deleted.
//                Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
//                //noinspection ResultOfMethodCallIgnored
//                mOutputFile.delete();
//            }
//            releaseMediaRecorder(); // release the MediaRecorder object
//            mCamera.lock();         // take camera access back from MediaRecorder
//
//            // inform the user that recording has stopped
//            isRecording = false;
//            releaseCamera();
//            // END_INCLUDE(stop_release_media_recorder)
//
//            // BEGIN_INCLUDE(prepare_start_media_recorder)
//            if (CameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
//
//                // BEGIN_INCLUDE(prepare_start_media_recorder)
//                new MediaPrepareTask().execute(Camera.CameraInfo.CAMERA_FACING_FRONT, null, null);
//                showEqualizer();
//                captureButton.setBackground(this.getResources().getDrawable(R.drawable.video_camera_stop_icon));
//
//            } else if (CameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//
//                // BEGIN_INCLUDE(prepare_start_media_recorder)
//                new MediaPrepareTask().execute(Camera.CameraInfo.CAMERA_FACING_BACK, null, null);
//                showEqualizer();
//                captureButton.setBackground(this.getResources().getDrawable(R.drawable.video_camera_stop_icon));
//
//
//            } else {
//
//                // BEGIN_INCLUDE(prepare_start_media_recorder)
//                new MediaPrepareTask().execute(Camera.CameraInfo.CAMERA_FACING_BACK, null, null);
//                showEqualizer();
//                captureButton.setBackground(this.getResources().getDrawable(R.drawable.video_camera_stop_icon));
//
//            }
//
//        }
//    }
//
//    private void releaseMediaRecorder() {
//        try {
//
//            if (mMediaRecorder != null) {
//                // clear recorder configuration
//                mMediaRecorder.reset();
//                // release the recorder object
//                mMediaRecorder.release();
//                mMediaRecorder = null;
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void releaseCamera() {
//        try {
//            if (mCamera != null) {
//
//                // Lock camera for later use i.e taking it back from MediaRecorder.
//                // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
//                mCamera.lock();
//
//                // release the camera for other applications
//                mCamera.release();
//                mCamera = null;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//    class MediaPrepareTask extends AsyncTask<Integer, Void, Boolean> {
//
//        private Context mContext;
//
//        private MediaPrepareTask() {
//        }
//
//        @Override
//        protected Boolean doInBackground(Integer... params) {
//
//            // initialize video camera
//            boolean isCameraCreated = createCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
//
//            if (isCameraCreated && mCamera != null)
//                prepareMedia();
//            return true;
//
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//
//        }
//    }
//
//    private boolean createCamera(int cameraFacing) {
//        try {
//
////            mCamera = CameraHelper.getDefaultCameraInstance();
//            mCamera = CameraHelper.getDefaultFrontFacingCameraInstance();
//
//            if (mCamera == null)
//                return false;
//
//
//            mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
//                @Override
//                public void onFaceDetection(Camera.Face[] faces, Camera camera) {
//
//                    Log.e("FaceDetection", "face detected: " + faces.length);
//
////                    Log.d("FaceDetection", "face detected: " + faces.length +
////                            " Face 1 Location X: " + faces[0].rect.centerX() +
////                            "Y: " + faces[0].rect.centerY());
//
//
//                }
//            });
//            CameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
//            // We need to make sure that our preview and recording video size are supported by the
//            // camera. Query camera to find all the sizes and choose the optimal size given the
//            // dimensions of our preview surface.
//            Camera.Parameters parameters = mCamera.getParameters();
//
//            List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
//            List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
//
//            Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
//                    mSupportedPreviewSizes, cameraSourcePreview.getWidth(), cameraSourcePreview.getHeight());
//
//            // Use the same size for recording profile.
//            camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//            camcorderProfile.videoFrameWidth = optimalSize.width;
//            camcorderProfile.videoFrameHeight = optimalSize.height;
//
//            // likewise for the camera object itself.
//            parameters.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
//
//            setRotation(mCamera, parameters, CameraFacing);
//
//            mCamera.setParameters(parameters);
//
//            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
//            // with {@link SurfaceView}
//            TextureView textureView = cameraSourcePreview.getTextureView(graphicOverlay);
//            if (textureView == null)
//                return false;
//
//            mCamera.setPreviewTexture(textureView.getSurfaceTexture());
//
//            return true;
//        } catch (Exception ex) {
//            releaseCamera();
//            ex.printStackTrace();
//            return false;
//        }
//    }
//
//    private boolean prepareMedia() {
//        try {
//
//            // BEGIN_INCLUDE (configure_media_recorder)
//            mMediaRecorder = new MediaRecorder();
//
//            // Step 1: Unlock and set camera to MediaRecorder
//            mCamera.unlock();
//            mMediaRecorder.setCamera(mCamera);
//
//            // Step 2: Set sources
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//            // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
//            mMediaRecorder.setProfile(camcorderProfile);
//
//            // Step 4: Set output file
//            mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO);
//            if (mOutputFile == null) {
//                return false;
//            }
//            mMediaRecorder.setOutputFile(mOutputFile.getPath());
//            // END_INCLUDE (configure_media_recorder)
//
//            // Step 5: Prepare configured MediaRecorder
//            mMediaRecorder.prepare();
//
//            mMediaRecorder.start();
//            isRecording = true;
//
//            showEqualizer();
//
////            TimeThread timeThread = new TimeThread();
//
//            return true;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//            // release the camera immediately on pause event
//            releaseCamera();
//
//            releaseMediaRecorder();
//            return false;
//        }
//    }
//
//    private void setRendomText() {
//        OcrGraphic graphic;
//        graphic = new OcrGraphic(graphicOverlay, "Hi Zebpay my name is mayur and i want to verify my account throught video please do needfull");
//        graphicOverlay.add(graphic);
//    }
//
//
//    /**
//     * Gets the id for the camera specified by the direction it is facing.  Returns -1 if no such
//     * camera was found.
//     *
//     * @param facing the desired camera (front-facing or rear-facing)
//     */
//    private static int getIdForRequestedCamera(int facing) {
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
//            Camera.getCameraInfo(i, cameraInfo);
//            if (cameraInfo.facing == facing) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * get number of camera which is user to displat switch camera button
//     * camera was found.
//     */
//    private static int getNumberofCameras() {
//        try {
//            int cameraCount = -1;
//            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//            cameraCount = Camera.getNumberOfCameras();
//            return cameraCount;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return -1;
//        }
//    }
//
//
//    /**
//     * Calculates the correct rotation for the given camera id and sets the rotation in the
//     * parameters.  It also sets the camera's display orientation and rotation.
//     *
//     * @param parameters the camera parameters for which to set the rotation
//     * @param cameraId   the camera id to set rotation based on
//     */
//    private void setRotation(Camera camera, Camera.Parameters parameters, int cameraId) throws Exception {
//        WindowManager windowManager = (WindowManager) VideoRecoActivity.this.getSystemService(Context.WINDOW_SERVICE);
//        int degrees = 0;
//        int rotation = windowManager.getDefaultDisplay().getRotation();
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_90:
//                degrees = 90;
//                break;
//            case Surface.ROTATION_180:
//                degrees = 180;
//                break;
//            case Surface.ROTATION_270:
//                degrees = 270;
//                break;
//            default:
//                Log.e(TAG, "Bad rotation value: " + rotation);
//        }
//
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        Camera.getCameraInfo(cameraId, cameraInfo);
//
//        int angle;
//        int displayAngle;
//        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            angle = (cameraInfo.orientation + degrees) % 360;
//            displayAngle = (360 - angle); // compensate for it being mirrored
//        } else {  // back-facing
//            angle = (cameraInfo.orientation - degrees + 360) % 360;
//            displayAngle = angle;
//        }
//
//        // This corresponds to the rotation constants in {@link Frame}.
//        mRotation = angle / 90;
//
//        camera.setDisplayOrientation(displayAngle);
//        parameters.setRotation(angle);
//    }
//
//    private void toggleEqualizer() {
//        if (equalizer.isAnimating()) {
//            equalizer.stopBars();
//        } else {
//            equalizer.animateBars();
//        }
//    }
//
//    private void showEqualizer() {
//        equalizer.animateBars();
//    }
//
//
//    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            if (mOutputFile != null && mOutputFile.isFile() && mOutputFile.exists()) {
//
//                String filePath = mOutputFile.getAbsolutePath();
//                Log.e("Output File:", filePath + "");
//
//                return MediaController.getInstance().convertVideo(filePath);
//
//            } else
//                return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean compressed) {
//            super.onPostExecute(compressed);
//            if (compressed) {
//                Log.e("Compression", "Compression successfully!");
//                Log.e("Compressed File Path", "" + MediaController.cachedFile.getPath());
//            } else {
//                Log.e("Compression", "Compression Failed!");
//            }
//        }
//    }
//
//    private class TimeThread implements Runnable {
//        private long mStartTimeMillis = SystemClock.elapsedRealtime();
//
//        // This lock guards all of the member variables below.
//        private boolean mActive = true;
//
//        TimeThread() {
//
//        }
//
//        /**
//         * As long as the processing thread is active, this executes detection on frames
//         * continuously.  The next pending frame is either immediately available or hasn't been
//         * received yet.  Once it is available, we transfer the frame info to local variables and
//         * run detection on that frame.  It immediately loops back for the next frame without
//         * pausing.
//         * <p/>
//         * If detection takes longer than the time in between new frames from the camera, this will
//         * mean that this loop will run without ever waiting on a frame, avoiding any context
//         * switching or frame acquisition time latency.
//         * <p/>
//         * If you find that this is using more CPU than you'd like, you should probably decrease the
//         * FPS setting above to allow for some idle time in between frames.
//         */
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(2000);
//
//                setRendomText();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//}
