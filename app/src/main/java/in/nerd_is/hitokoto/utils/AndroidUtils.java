package in.nerd_is.hitokoto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Zheng Xuqiang on 2014/7/30 0030.
 * Android常用方法
 */
public class AndroidUtils {

    public static class Net {

        /**
         * 网络是否可用
         * @param context
         * @return
         */
        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        /**
         * Avoiding Bugs In Earlier Releases<br>
         * Prior to Android 2.2 (Froyo, API level 8), this class had some frustrating bugs. <br>
         * In particular, calling close() on a readable InputStream could poison the connection pool.
         */
        public static void disableConnectionReuseIfNecessary() {
            // HTTP connection reuse which was buggy pre-froyo
            if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
                System.setProperty("http.keepAlive", "false");
            }
        } // end static method disableConnectionReuseIfNecessary

    } // end static class Net

    public static class FileIO {

        /**
         * 将Bitmap保存为文件<br>
         * 参见http://my.oschina.net/ryanhoo/blog/93406
         */
        public static boolean saveBitmap2File(File file, Bitmap bitmap) throws IOException {
            if(file == null || bitmap == null)
                return false;

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return result;
        }

        public static Bitmap loadBitmapFile(File file){
            return BitmapFactory.decodeFile(file.getPath());
        }

        /* Checks if external storage is available for read and write */
        public static boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        /* Checks if external storage is available to at least read */
        public static boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
        }

    }
}
