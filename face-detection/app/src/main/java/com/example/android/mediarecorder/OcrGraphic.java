/*
 * Copyright (C) The Android Open Source Project
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
 */
package com.example.android.mediarecorder;


/*
 * Copyright (C) The Android Open Source Project
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
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class OcrGraphic extends GraphicOverlay.Graphic {

    private int mId;

    private static final int TEXT_COLOR = Color.WHITE;

    private static Paint sTextPaint;
//    private final TextBlock mText;
    private String strPanNumber;

//    public OcrGraphic(GraphicOverlay overlay, TextBlock text, String strPanNumber) {
//        super(overlay);
//
//        mText = text;
//        this.strPanNumber = strPanNumber;
//
//        if (sTextPaint == null) {
//            sTextPaint = new Paint();
//            sTextPaint.setColor(TEXT_COLOR);
//            sTextPaint.setTextSize(54.0f);
//        }
//        // Redraw the overlay, as this graphic has been added.
//        postInvalidate();
//    }

    public OcrGraphic(GraphicOverlay overlay, String strPanNumber) {
        super(overlay);

        this.strPanNumber = strPanNumber;

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(54.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     *
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
       return false;
//        RectF rect = new RectF(text.getBoundingBox());
//        rect.left = translateX(rect.left);
//        rect.top = translateY(rect.top);
//        rect.right = translateX(rect.right);
//        rect.bottom = translateY(rect.bottom);
//        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
//        TextBlock text = mText;
//        if (text == null) {
//            return;
//        }

        // Break the text into multiple lines and draw each one according to its own bounding box.
//        List<? extends Text> textComponents = text.getComponents();
//        Text currentText = textComponents.get(0);
//        float left = translateX(currentText.getBoundingBox().left);
//        float bottom = translateY(currentText.getBoundingBox().bottom);
        canvas.drawText("this is test text please ignore", 10, 10, sTextPaint);

    }
}
