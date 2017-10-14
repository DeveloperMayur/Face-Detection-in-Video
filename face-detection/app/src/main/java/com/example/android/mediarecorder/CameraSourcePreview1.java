///*
// * Copyright (C) The Android Open Source Project
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
//package com.example.android.mediarecorder;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.res.Configuration;
//import android.hardware.Camera;
//import android.support.annotation.RequiresPermission;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.TextureView;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.io.IOException;
//
//public class CameraSourcePreview1 extends ViewGroup {
//    private static final String TAG = "CameraSourcePreview";
//
//    private Context mContext;
//    private TextureView textureView;
//    private boolean mStartRequested;
//
//    private GraphicOverlay mOverlay;
//
//    public CameraSourcePreview1(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//        mStartRequested = false;
//        textureView = new TextureView(context);
//        addView(textureView);
//
//    }
//
//    @RequiresPermission(Manifest.permission.CAMERA)
//    public TextureView getTextureView(GraphicOverlay overlay) throws IOException, SecurityException {
//        try {
//            if (overlay != null) {
//
//                int min = Math.min(getWidth(), getHeight());
//                int max = Math.max(getWidth(), getHeight());
//
//                if (isPortraitMode()) {
//                    // Swap width and height sizes when in portrait, since it will be rotated by
//                    // 90 degrees
//                    overlay.setCameraInfo(min, max);
//                } else {
//                    overlay.setCameraInfo(max, min);
//                }
//                overlay.clear();
//            }
//            return textureView;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//
//
//    // ------------------------------------------------------------------------
//    // LAYOUT METHODS
//    // ------------------------------------------------------------------------
//
//    // TODO - document
//    // Need a better explanation of why onMeasure is needed and how width/height are determined.
//    // Must the camera be set first via setCamera? What if mSupportedPreviewSizes == null?
//    // Why do we startPreview within this method if the surface is valid?
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        // We purposely disregard child measurements because act as a
//        // wrapper to a SurfaceView that centers the camera preview instead
//        // of stretching it.
//        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//
//
//        setMeasuredDimension(width, height);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//
//        int previewWidth = 320;
//        int previewHeight = 240;
//
//        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
//        if (isPortraitMode()) {
//            int tmp = previewWidth;
//            previewWidth = previewHeight;
//            previewHeight = tmp;
//        }
//
//        final int viewWidth = right - left;
//        final int viewHeight = bottom - top;
//
//        int childWidth;
//        int childHeight;
//        int childXOffset = 0;
//        int childYOffset = 0;
//        float widthRatio = (float) viewWidth / (float) previewWidth;
//        float heightRatio = (float) viewHeight / (float) previewHeight;
//
//
//        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
//        // it is usually necessary to slightly oversize the child and to crop off portions along one
//        // of the dimensions.  We scale up based on the dimension requiring the most correction, and
//        // compute a crop offset for the other dimension.
//        if (widthRatio > heightRatio) {
//            childWidth = viewWidth;
//            childHeight = (int) ((float) previewHeight * widthRatio);
//            childYOffset = (childHeight - viewHeight) / 2;
//        } else {
//            childWidth = (int) ((float) previewWidth * heightRatio);
//            childHeight = viewHeight;
//            childXOffset = (childWidth - viewWidth) / 2;
//        }
//        View viewChild;
//        for (int i = 0; i < getChildCount(); ++i) {
//            // One dimension will be cropped.  We shift child over or up by this offset and adjust
//            // the size to maintain the proper aspect ratio.
//            viewChild = getChildAt(i);
//            if (getChildAt(i).getId() == R.id.graphicOverlay) {
//                viewChild.layout(left, top, right, bottom);
//            } else {
//                viewChild.layout(-1 * childXOffset, -1 * childYOffset, childWidth - childXOffset, childHeight - childYOffset);
//            }
//        }
//
//    }
//
//    private boolean isPortraitMode() {
//        int orientation = mContext.getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            return false;
//        }
//        return orientation == Configuration.ORIENTATION_PORTRAIT;
//
//
//    }
//}
