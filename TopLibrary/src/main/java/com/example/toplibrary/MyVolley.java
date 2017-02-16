package com.example.toplibrary;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;

import java.io.File;

public class MyVolley {

    private static final int DEFAULT_DISK_USAGE_BYTES = 100 * 1024 * 1024;
    private static MyVolley sInstance = null;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Handler mMainHandler;
    private Cache mDiskCache;

    private static int sCacheSize = 3 * 1024 * 1024;

    private MyVolley(Context ctx) {
        if (ctx == null) {
            throw new RuntimeException("App does not init!!");
        }
        init(ctx);
    }

    public static MyVolley getInstance(Context ctx) {
        if (null == sInstance) {
            synchronized (MyVolley.class) {
                if (null == sInstance) {
                    sInstance = new MyVolley(ctx);
                }
            }
        }
        return sInstance;
    }

    private synchronized void init(Context context) {
        String pictureParentDir = Environment.getExternalStorageDirectory().getPath();
        File cacheDir;
        if (!TextUtils.isEmpty(pictureParentDir)) {
            cacheDir = new File(pictureParentDir);
        } else {
            cacheDir = getCacheDir(context);
            if (null == cacheDir) {
                return;
            }
        }
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (Throwable e) {
        }

        HttpStack stack = new HurlStack();

        Network network = new BasicNetwork(stack);

        if (mDiskCache == null) {
            mDiskCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
        }

        mRequestQueue = new RequestQueue(mDiskCache, network);
        mRequestQueue.start();

        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        int minSize = 1024 * 1024 * memClass / 8;

        sCacheSize = (sCacheSize == 0 || sCacheSize > minSize) ? minSize : sCacheSize;

        mImageLoader = new ImageLoader(mRequestQueue, null);
    }

    private void release() {
        if (mRequestQueue != null) {
            mRequestQueue.stop();
            mRequestQueue = null;
        }

        if (mImageLoader != null) {
            mImageLoader = null;
        }

        sInstance = null;
    }

    private File getCacheDir(Context context) {
        File dir = context.getCacheDir();
        if (null == dir) return null;

        File cacheDir = new File(dir.getAbsolutePath() + "/cm_image_cache/");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public synchronized DiskBasedCache getDiskBasedCache() {
        return (DiskBasedCache) mRequestQueue.getCache();
    }

    public synchronized boolean isCached(String url) {
        if (!TextUtils.isEmpty(url) && null != getDiskBasedCache()) {
            File file = getDiskBasedCache().getFileForKey(url);
            return file.exists();
        }
        return false;
    }

    public synchronized String getFilePahtByUrl(String url) {
        if (!TextUtils.isEmpty(url) && null != getDiskBasedCache()) {
            File file = getDiskBasedCache().getFileForKey(url);
            if (file != null && file.exists()) {
                return file.getAbsolutePath();
            }
        }
        return "";
    }

    public synchronized ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    private ImageLoader.ImageListener mDefaultImageListener = new ImageLoader.ImageListener() {
        @Override
        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
        }
    };

    public synchronized void preLoadImageIntoDiskOnly(final String url, final ImageLoader.ImageListener listener) {
        if (null == url) return;

        if (isMainThread()) {
            mImageLoader.get(url, listener == null ? mDefaultImageListener : listener);
        } else {
            if (mMainHandler == null) {
                mMainHandler = new Handler(Looper.getMainLooper());
            }
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mImageLoader.get(url, listener == null ? mDefaultImageListener : listener);
                }
            });
        }
    }

    /**
     * 等同于调用preLoadImageIntoDiskOnly方法，只是有些sdk还是调用此方法，所以留着，建议用等同于调用preLoadImageIntoDiskOnly方法
     */
    public synchronized void preLoadImage(final String url) {
        preLoadImageIntoDiskOnly(url, mDefaultImageListener);
    }

    /**
     * 等同于调用preLoadImageIntoDiskOnly方法，只是有些sdk还是调用此方法，所以留着，建议用等同于调用preLoadImageIntoDiskOnly方法
     */
    public synchronized void preLoadImage(final String url, final ImageLoader.ImageListener listener) {
        preLoadImageIntoDiskOnly(url, listener);
    }


    public synchronized void preLoadImageIntoDiskOnly(final String url) {
        preLoadImageIntoDiskOnly(url, mDefaultImageListener);
    }

    public synchronized void loadImage(final ImageView image, String icon) {
        if (null == icon || null == image) {
            return;
        }
        getImageLoader().get(icon, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                image.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, image.getWidth(), image.getHeight());
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
