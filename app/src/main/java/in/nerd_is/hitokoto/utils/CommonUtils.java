package in.nerd_is.hitokoto.utils;

import org.apache.http.NameValuePair;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Zheng Xuqiang on 2014/8/4 0004.
 * 常规常用方法
 */
public class CommonUtils {

    public static class Net {

        /**
         * 将参数列表转换为HttpUrlConnection可用的参数字符串<br>
         * 参见http://stackoverflow.com/a/13486223/2369039
         *
         * @param method 进行Http连接的方式，值为GET或POST
         * @param params 请求的参数列表
         * @return 转换后的String
         * @throws java.io.UnsupportedEncodingException
         */
        public static String getRequestQuery(String method, List<NameValuePair> params)
                throws UnsupportedEncodingException {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first) {
                    first = false;
                    if ("GET".equalsIgnoreCase(method))
                        result.append("?");
                }
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        } // end static method getRequestQuery
    } // end static class Net

    public static class IO {

        /**
         * 将InputStream中的数据读取为String<br>
         * 参见http://stackoverflow.com/a/309718/2369039
         *
         * @param is InputStream
         * @param bufferSize buffer大小
         * @param encoding String编码
         * @return 读取出来的String
         * @throws java.io.IOException
         */
        public static String is2String(InputStream is, int bufferSize,String encoding)
                throws IOException {
            Reader reader = new InputStreamReader(is, encoding);
            char[] buffer = new char[bufferSize];
            StringBuilder out = new StringBuilder();

            int rsz;
            while ((rsz = reader.read(buffer, 0, bufferSize)) >= 0) {
                out.append(buffer, 0, rsz);
            }

            return out.toString();
        }

        /**
         * 以UTF-8编码读取InputStream为String
         * @param is InputStream
         * @return 读取出来的String
         * @throws java.io.IOException
         */
        public static String is2String(InputStream is) throws IOException {
            return is2String(is, 50, "UTF-8");
        }

        public static void close(Closeable closeable) {
            if (closeable == null)
                return;

            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
