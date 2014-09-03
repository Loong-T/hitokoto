package in.nerd_is.hitokoto.net;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.nerd_is.hitokoto.api.Constants;
import in.nerd_is.hitokoto.api.HitokotoReq;
import in.nerd_is.hitokoto.api.HitokotoVo;
import in.nerd_is.hitokoto.provider.HitokotoProvider;
import in.nerd_is.hitokoto.provider.HitokotoProviderTask;
import in.nerd_is.hitokoto.utils.AndroidUtils;
import in.nerd_is.hitokoto.utils.CommonUtils;

/**
 * Created by Zheng Xuqiang on 2014/8/19 0019.
 */
public class HitokotoTask extends AsyncTask<HitokotoReq, Void, HitokotoVo> {

    private final Context mContext;
    private TextView mTvHitokoto;

    public HitokotoTask(Context mContext, TextView mTvHitokoto) {
        this.mContext = mContext;
        this.mTvHitokoto = mTvHitokoto;
    }

    @Override
    protected HitokotoVo doInBackground(HitokotoReq... params) {

        if (!AndroidUtils.Net.isNetworkConnected(mContext)) {
            return new HitokotoVo("网络未连接");
        }

        String request = null;

        if (null == params[0]) {
            request = Constants.API_URL;
        }
        else {
            // TODO: parse params
        }

        URL url;
        HttpURLConnection conn;
        InputStream in;
        HitokotoVo result = null;
        try {
            url = new URL(request);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);

            conn.connect();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return new HitokotoVo(conn.getResponseMessage());
            }

            in = conn.getInputStream();
            String json = CommonUtils.IO.is2String(in);

            JSON.DEFFAULT_DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
            result = JSON.parseObject(json, HitokotoVo.class);

            in.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(HitokotoVo hitokotoVo) {
        if (hitokotoVo != null) {
            mTvHitokoto.setText(hitokotoVo.hitokoto);
            (new HitokotoProviderTask(mContext)).execute(hitokotoVo);
        }

        super.onPostExecute(hitokotoVo);
    }
}
