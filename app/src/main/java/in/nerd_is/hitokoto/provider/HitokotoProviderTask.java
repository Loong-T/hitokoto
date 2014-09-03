package in.nerd_is.hitokoto.provider;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import in.nerd_is.hitokoto.api.HitokotoVo;

/**
 * Created by Zheng Xuqiang on 2014/9/1 0001.
 */
public class HitokotoProviderTask extends AsyncTask<HitokotoVo, Void, Void> {

    private Context mContext;

    public HitokotoProviderTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(HitokotoVo... params) {
        HitokotoVo vo = params[0];
        if (vo == null) {
            return null;
        }

        mContext.getContentResolver().insert(
                HitokotoProviderMetaData.HitokotoTable.URI_CONTENT,
                vo2ContentValues(vo));

        return null;
    }

    private ContentValues vo2ContentValues(HitokotoVo vo) {
        ContentValues values = new ContentValues();
        values.put(HitokotoProviderMetaData.HitokotoTable._ID, vo.id);
        values.put(HitokotoProviderMetaData.HitokotoTable.AUTHOR, vo.author);
        values.put(HitokotoProviderMetaData.HitokotoTable.CATEGORY, vo.cat);
        values.put(HitokotoProviderMetaData.HitokotoTable.CATEGORY_NAME, vo.catname);
        values.put(HitokotoProviderMetaData.HitokotoTable.DATE, vo.date.getTime());
        values.put(HitokotoProviderMetaData.HitokotoTable.HITOKOTO, vo.hitokoto);
        values.put(HitokotoProviderMetaData.HitokotoTable.LIKE, vo.like);
        values.put(HitokotoProviderMetaData.HitokotoTable.SOURCE, vo.source);
        return values;
    }
}
