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
    private static final String DATABASE_NAME = "postIdManager";
    private static final String TABLE_NAME = "postid";

    private static final String KEY_POSITION = "position";
    private static final String KEY_ID = "id";
    private static final String KEY_POSTID = "post_id";

    public PostIdHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        String CREATE_POSTID_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " TEXT PRIMARY KEY, "+ KEY_POSTID + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_POSTID_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addId(PostID postID) {
        SQLiteDatabase db = this.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(KEY_ID, postID.getId());
//        values.put(KEY_POSTID, postID.getPostid());
//        db.insert(TABLE_NAME, null, values);
//        db.close();

        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" + "\'" + postID.getId() + "\'," + "\'" + postID.getPostid() + "\');");
    }

    public String getPostId(String position){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id='" + position + "'", null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

}
