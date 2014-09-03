package in.nerd_is.hitokoto.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Zheng Xuqiang on 2014/8/20 0020.
 */
public class HitokotoProvider extends ContentProvider {

    private static final String TAG = "HitokotoProvider";

    private static final int MATCH_ALL = 1;
    private static final int MATCH_ID = 2;
    private static final int MATCH_CAT = 3;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(HitokotoProviderMetaData.AUTHORITY, "hitokoto", MATCH_ALL);
        sUriMatcher.addURI(HitokotoProviderMetaData.AUTHORITY, "hitokoto/#", MATCH_ID);
        sUriMatcher.addURI(HitokotoProviderMetaData.AUTHORITY, "hitokoto/*", MATCH_CAT);
    }

    private HitokotoDatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new HitokotoDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        String sort;
        if (TextUtils.isEmpty(sortOrder)) {
            sort = HitokotoProviderMetaData.HitokotoTable.DEFAULT_SORT_ORDER;
        } else {
            sort = sortOrder;
        }

        switch (sUriMatcher.match(uri)) {
            case MATCH_ALL:
                cursor = db.query(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        projection, null, null, null, null, sort);
                break;
            case MATCH_ID:
                long id = ContentUris.parseId(uri);
                Log.d(TAG, "query MATCH_ID; id: " + id);

                cursor = db.query(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        projection,
                        HitokotoProviderMetaData.HitokotoTable._ID + " = " + id,
                        null, null, null, sort);
                break;
            case MATCH_CAT:
                String select = HitokotoProviderMetaData.HitokotoTable.CATEGORY
                        + " = '" + uri.getPathSegments().get(1) + "'";
                Log.d(TAG, "query MATCH; select: " + select);

                cursor = db.query(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        projection, select, null, null, null, sort);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_ALL:
            case MATCH_CAT:
                return HitokotoProviderMetaData.HitokotoTable.TYPE_CONTENT;
            case MATCH_ID:
                return HitokotoProviderMetaData.HitokotoTable.TYPE_CONTENT_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (sUriMatcher.match(uri) != MATCH_ALL) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        }
        else {
            contentValues = new ContentValues();
        }

        contentValues.put(HitokotoProviderMetaData.HitokotoTable.MODIFIED, System.currentTimeMillis());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insert(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                null, contentValues);

        if (rowId > 0) {
            Uri result = ContentUris.withAppendedId(
                    HitokotoProviderMetaData.HitokotoTable.URI_CONTENT,
                    contentValues.getAsLong(HitokotoProviderMetaData.HitokotoTable._ID));
            getContext().getContentResolver().notifyChange(result, null);

            return result;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        String select;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MATCH_ID:
                select = HitokotoProviderMetaData.HitokotoTable._ID + " = "
                        + uri.getPathSegments().get(1);
                count = db.delete(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        select, selectionArgs);
                break;
            case MATCH_CAT:
                select = HitokotoProviderMetaData.HitokotoTable.CATEGORY + " = "
                        + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(selection)) {
                    select = selection + " AND (" + selection + " )";
                }
                count = db.delete(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        select, selectionArgs);
                break;
            case MATCH_ALL:
                if (!TextUtils.isEmpty(selection)) {
                    select = selection;
                }
                else {
                    select = null;
                }
                count = db.delete((HitokotoProviderMetaData.HitokotoTable.TABLE_NAME),
                        select, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI " + uri);
        } // end switch

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        String select;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MATCH_ID:
                select = HitokotoProviderMetaData.HitokotoTable._ID + " = "
                        + uri.getPathSegments().get(1);
                count = db.update(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        values, select, selectionArgs);
                break;
            case MATCH_CAT:
                select = HitokotoProviderMetaData.HitokotoTable.CATEGORY + " = "
                        + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(selection)) {
                    select = selection + " AND (" + selection + " )";
                }
                count = db.update(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        values, select, selectionArgs);
                break;
            case MATCH_ALL:
                if (!TextUtils.isEmpty(selection)) {
                    select = selection;
                }
                else {
                    select = null;
                }
                count = db.update(HitokotoProviderMetaData.HitokotoTable.TABLE_NAME,
                        values, select, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private static class HitokotoDatabaseHelper extends SQLiteOpenHelper {

        public HitokotoDatabaseHelper(Context context) {
            super(context, HitokotoProviderMetaData.DATABASE_NAME, null,
                    HitokotoProviderMetaData.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE " + HitokotoProviderMetaData.HitokotoTable.TABLE_NAME + "("
                    + HitokotoProviderMetaData.HitokotoTable._ID + " INTEGER PRIMARY KEY, "
                    + HitokotoProviderMetaData.HitokotoTable.HITOKOTO + " TEXT, "
                    + HitokotoProviderMetaData.HitokotoTable.AUTHOR + " TEXT, "
                    + HitokotoProviderMetaData.HitokotoTable.CATEGORY + " TEXT, "
                    + HitokotoProviderMetaData.HitokotoTable.CATEGORY_NAME + " TEXT, "
                    + HitokotoProviderMetaData.HitokotoTable.SOURCE + " TEXT, "
                    + HitokotoProviderMetaData.HitokotoTable.DATE + " INTEGER, "
                    + HitokotoProviderMetaData.HitokotoTable.LIKE + " INTEGER, "
                    + HitokotoProviderMetaData.HitokotoTable.MODIFIED + " INTEGER"
                    + ");"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HitokotoProviderMetaData.HitokotoTable.TABLE_NAME);
            onCreate(db);
        }
    }
}
