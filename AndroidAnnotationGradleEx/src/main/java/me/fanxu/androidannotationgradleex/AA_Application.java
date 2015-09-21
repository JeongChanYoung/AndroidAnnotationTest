package me.fanxu.androidannotationgradleex;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.SystemService;

import java.io.File;

/**
 * Created by Kyde on 2015-09-21.
 */
@EApplication
public class AA_Application extends Application {
    private static final String DISK_CACHE_SUBDIR = "cp_img";
    private static final int MEM_CACHE_SIZE = 1024 * 1024 * 2; // 2MB
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 500; // 500MB
    @Override
    public void onCreate() {
        super.onCreate();
        initSomeStuff();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .considerExifParams(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        File diskCache= new File(getCachePath(getApplicationContext(), DISK_CACHE_SUBDIR));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(10)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(MEM_CACHE_SIZE))
                .memoryCacheSize(MEM_CACHE_SIZE)
                .memoryCacheSizePercentage(13)
                .diskCache(new UnlimitedDiskCache(diskCache))
                .diskCacheSize(DISK_CACHE_SIZE)
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .imageDownloader(new BaseImageDownloader(getApplicationContext()))
                .imageDecoder(new BaseImageDecoder(false))
                .defaultDisplayImageOptions(defaultOptions)
//				.diskCacheExtraOptions(480, 320, null)
//				.writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);

    }

    @SystemService
    NotificationManager notificationManager;


    @Background
    void initSomeStuff() {
        // init some stuff in background
    }

    private String getCachePath(Context context, String dir){
        String cachePath = //getCacheDir().getPath();
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ?
                        getExternalCacheDir(context).getPath() :
                        getCacheDir().getPath();
        return cachePath + File.separator + DISK_CACHE_SUBDIR;
    }

    private boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    private File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    private boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
}
