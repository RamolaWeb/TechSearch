package ramola.techsearch;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
    public static void SaveImage(Bitmap bitmap,String filename) {
        File folder= Environment.getExternalStoragePublicDirectory("DiskImage");
        if(!folder.exists()) {
            folder.mkdir();
        }

        File file=new File(folder,filename+".png");
        if (file.exists()){
            return;
        }
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.d("FileSave","File "+file.getName()+" has saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  public static Bitmap GetImage(String filename){

      File file=new File(Environment.getExternalStoragePublicDirectory("DiskImage"),filename+".png");
      Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
      Log.d("SuccessFile","Success "+file.getName());
      return bitmap;
  }
    public static boolean check(String filename){
        File file=new File(Environment.getExternalStoragePublicDirectory("DiskImage"),filename+".png");
        if(file.exists())
            return true;
        return false;
    }
}
