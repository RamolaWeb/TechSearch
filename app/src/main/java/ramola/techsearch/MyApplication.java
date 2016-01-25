package ramola.techsearch;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {
    private static MyApplication myApplication=null;
   private static DBhelper dBhelper=null;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
    }
    public static Context getAppContext(){
        return myApplication.getApplicationContext();
    }
    public  static synchronized DBhelper getDataBase(){
        if (dBhelper==null)
            dBhelper=new DBhelper(getAppContext());
        return dBhelper;
    }
}
