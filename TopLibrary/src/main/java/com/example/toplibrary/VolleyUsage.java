package com.example.toplibrary;

import android.util.Log;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class VolleyUsage {
    public static ImageLoader.ImageListener usage() {
        return new ContentImageCallback(ContentImageCallback.TYPE_SUSPIRE);
    }

    /**
     * Check whether the onResponse() & onErrorResponse() will keep after proguard !!
     */
    private static class ContentImageCallback implements ImageLoader.ImageListener {
        static final int TYPE_SUSPIRE = 0;

        private int mType = -1;

        ContentImageCallback(int type) {
            mType = type;
        }

        @Override
        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
            if (null != imageContainer && null != imageContainer.getBitmap()) {
                switch (mType) {
                    case TYPE_SUSPIRE:
                        Log.e("Testt", "image of bitmap " + imageContainer.getBitmap());
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("Testt", "ContentImageCallback Error", volleyError.getCause());
        }
    }
}
