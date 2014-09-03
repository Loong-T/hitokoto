package in.nerd_is.hitokoto.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Zheng Xuqiang on 2014/8/20 0020.
 */
public class HitokotoProviderMetaData {

    private HitokotoProviderMetaData() {}

    public static final String DATABASE_NAME = "hitokoto.db";
    public static final int DATABASE_VERSION = 1;

    public static final String AUTHORITY = "in.nerd_is.hitokoto.provider.HitokotoProvider";

    public static final class HitokotoTable implements BaseColumns {

        private HitokotoTable() {}

        public static final String TABLE_NAME = "hitokoto";
        public static final String DEFAULT_SORT_ORDER = "modified_date DESC";

        public static final Uri URI_CONTENT = Uri.parse("content://" + AUTHORITY + "/hitokoto");

        public static final String TYPE_CONTENT = "vnd.android.cursor.dir/vnd.hitokoto.hitokoto";
        public static final String TYPE_CONTENT_ITEM = "vnd.android.cursor.item/vnd.hitokoto.hitokoto";

        public static final String HITOKOTO = "hitokoto";
        public static final String CATEGORY = "cat";
        public static final String CATEGORY_NAME = "catname";
        public static final String AUTHOR = "author";
        public static final String SOURCE = "source";
        public static final String LIKE = "like";
        public static final String DATE = "date";
        public static final String MODIFIED = "modified_date";
    }
}
