package ajaysrinivas.putmeintouch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ajay Srinivas on 4/8/2017.
 */

public class PostIdHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "postIdManager.db";
    private static final String TABLE_POSTID = "postid";

    private static final String KEY_ID = "id";
    private static final String KEY_POSTID = "post_id";

    public PostIdHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTID);

        String CREATE_POSTID_TABLE = "CREATE TABLE " + TABLE_POSTID + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_POSTID + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_POSTID_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTID);
        onCreate(sqLiteDatabase);
    }

    public void addId(PostID postID) {
        SQLiteDatabase db = this.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(KEY_ID, postID.getId());
//        values.put(KEY_POSTID, postID.getPostid());
//        db.insert(TABLE_NAME, null, values);
//        db.close();

        db.execSQL("INSERT INTO " + TABLE_POSTID + " VALUES(" + "\'" + postID.getId() + "\'," + "\'" + postID.getPostid() + "\');");
        db.close();
    }

    public String getPostId(int position) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_POSTID + " WHERE id=" + position, null);
        cursor.moveToFirst();
        db.close();
        return cursor.getString(1);
    }

}
