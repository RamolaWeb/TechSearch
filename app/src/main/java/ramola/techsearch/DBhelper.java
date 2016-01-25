package ramola.techsearch;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Search";
    private static final String TABLE_NAME="feed";
    private static final int DATABASE_VERSION=1;
    private static final String ID="id";
    private static final String TITLE="title";
    private static final String CATEGORY="category";
    private static final String SHORT_DESCRIPTION="short_description";
    private static final String LONG_DESCRIPTION="long_description";
    private static final String URL_PHOTO="url_photo";
    public DBhelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     String query="create table "+TABLE_NAME+"( "+ID+" integer primary key autoincrement , "+TITLE+" text, "+CATEGORY+" text, "+SHORT_DESCRIPTION+" text , "+LONG_DESCRIPTION+" text , "+URL_PHOTO+" text );";
        Log.d("Table","Table");
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    sqLiteDatabase.execSQL("drop table if exist "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public synchronized ArrayList<Feed> getData(int show ,int skip){
        ArrayList<Feed> dumbList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String coloumn[]={TITLE,CATEGORY,SHORT_DESCRIPTION,LONG_DESCRIPTION,URL_PHOTO};
        Cursor cursor=db.query(false,TABLE_NAME,coloumn,null,null,null,null,null,skip+","+show);
        if(cursor.moveToFirst()){
            do {
                dumbList.add(new Feed(cursor.getString(cursor.getColumnIndex(TITLE)),cursor.getString(cursor.getColumnIndex(CATEGORY)),cursor.getString(cursor.getColumnIndex(SHORT_DESCRIPTION)),cursor.getString(cursor.getColumnIndex(LONG_DESCRIPTION)),cursor.getString(cursor.getColumnIndex(URL_PHOTO))));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  dumbList;
    }
    public void deleteAll(){
        Log.d("delete","delete");
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }
    public synchronized void insertMovies( ArrayList<Feed> listMovies, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }

        SQLiteDatabase db=this.getWritableDatabase();
        String sql = "INSERT INTO " +TABLE_NAME+ " VALUES (?,?,?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            Feed currentMovie = listMovies.get(i);
            statement.clearBindings();
            statement.bindString(2,currentMovie.TITLE);
            statement.bindString(3,currentMovie.CATEGORY);
            statement.bindString(4,currentMovie.SHORT_DESCRIPTION);
            statement.bindString(5,currentMovie.LONG_DESCRIPTION);
            statement.bindString(6,currentMovie.URL_PHOTO);

            statement.execute();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public synchronized ArrayList<Feed> getSingle(String id){

        ArrayList<Feed> singleList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        String coloumn[]={TITLE,CATEGORY,SHORT_DESCRIPTION,LONG_DESCRIPTION,URL_PHOTO};
        Cursor cursor=database.query(TABLE_NAME,coloumn,TITLE+"='"+id+"'",null,null,null,null);
       if(cursor!=null){
           if(cursor.moveToFirst())
           {
            singleList.add(new Feed(cursor.getString(cursor.getColumnIndex(TITLE)),cursor.getString(cursor.getColumnIndex(CATEGORY)),cursor.getString(cursor.getColumnIndex(SHORT_DESCRIPTION)),cursor.getString(cursor.getColumnIndex(LONG_DESCRIPTION)),cursor.getString(cursor.getColumnIndex(URL_PHOTO))));
           }
           else {
               Log.d("ERRORWHILERETRIVINGDATA","error");
           }
       }
        else {
           Log.d("errorwhilegettingerror","error");
       }
        cursor.close();
        database.close();
        return singleList;
    }
}
