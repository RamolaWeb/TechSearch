package ramola.techsearch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;


public class LoadMoreFeed extends AppCompatActivity{
    private static final String KEY_ID ="id" ;
    private static final String KEY_CATEGORY ="category" ;
    private static final String KEY_TITLE ="title" ;
    private static final String KEY_DESCRIPTION ="description" ;
    private static final String KEY_IMAGE ="image" ;
    private   TextView category,title,description;
    private ImageView image;
  private  String id;
  private   ImageLoader imageLoader;
    private ArrayList<Feed> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader=MySingleton.getInstance(MyApplication.getAppContext()).getImageLoader();
        setContentView(R.layout.activity_load_more_feed);
        category= (TextView) findViewById(R.id.category_load_news);
        title= (TextView) findViewById(R.id.title_load_news);
        description= (TextView) findViewById(R.id.description_load_news);
        image= (ImageView) findViewById(R.id.image_load_news);
        category.setTypeface(TypeFaceManager.getRegular());
        title.setTypeface(TypeFaceManager.getBold());
        description.setTypeface(TypeFaceManager.getLight());

        Intent i=getIntent();
        if(i!=null){
            id=i.getStringExtra(KEY_ID);
            list=MyApplication.getDataBase().getSingle(id);
        }
        if(savedInstanceState==null){
            if(list.size()>0){
            title.setText(list.get(0).TITLE);
            category.setText(list.get(0).CATEGORY);
            description.setText(list.get(0).LONG_DESCRIPTION);
            if(Utils.check(list.get(0).URL_PHOTO.substring(9)))
            new Waiting.GetImage(list.get(0).URL_PHOTO.substring(9),image).execute();
            else
                image.setImageResource(R.drawable.mohit);
            }
        }
        else {
            title.setText(savedInstanceState.getString(KEY_TITLE));
            category.setText(savedInstanceState.getString(KEY_CATEGORY));
            description.setText(savedInstanceState.getString(KEY_DESCRIPTION));
            new Waiting.GetImage(savedInstanceState.getString(KEY_IMAGE).substring(9),image).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_more_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CATEGORY, list.get(0).CATEGORY);
        outState.putString(KEY_TITLE, list.get(0).TITLE);
        outState.putString(KEY_DESCRIPTION,list.get(0).LONG_DESCRIPTION);
        outState.putString(KEY_IMAGE,list.get(0).URL_PHOTO);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition (R.anim.open_main, R.anim.close_next);
    }
}
