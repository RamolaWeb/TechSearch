package ramola.techsearch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static ramola.techsearch.Field.FIELD_WEATHER_DAY.BASE_URL_DAY;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.DATE;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.DAY_HUMIDITY;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.DESCRIPTION;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.ICON;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.MAIN;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.TEMP_MAX;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.TEMP_MIN;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.TIME_CALCULATION;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.UNITS_DAILY;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.WEATHER;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.WIND_DEGREE_HOUR;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.WIND_HOUR;
import static ramola.techsearch.Field.FIELD_WEATHER_DAY.WIND_SPEED_HOUR;
import static ramola.techsearch.Field.Field_weather.APP_ID;
import static ramola.techsearch.Field.Field_weather.IMAGE_URL;

public class hourly_weather extends AppCompatActivity {
private RecyclerView recyclerView;
private Hourly_Adapter adapter;
private ArrayList<item_hourly> list;
    private final String Key_Movie="hour";
    private long time_access;
private  long time_ahead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_weather);
        Toolbar toolbar= (Toolbar) findViewById(R.id.appbar_hourly_weather);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOURLY WEATHER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent i=getIntent();
        if(i!=null)
            time_access=i.getLongExtra("GET",0)-21600L;
        time_ahead=time_access+86400L;
        ;
        recyclerView= (RecyclerView) findViewById(R.id.hourly_weather_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Hourly_Adapter(this);
        recyclerView.setAdapter(adapter);
        if (savedInstanceState!=null){
            list=savedInstanceState.getParcelableArrayList(Key_Movie);
            adapter.setList(list);
        }
        else {
            sendJsonRequest();
        }
    }

    private void sendJsonRequest() {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(getUrl(),null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                list=parseJson(jsonObject);
                adapter.setList(list);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(hourly_weather.this, "" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(hourly_weather.this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private ArrayList<item_hourly> parseJson(JSONObject jsonObject) {
        ArrayList<item_hourly> dumblist=new ArrayList<>();
        try {
            String name_hourly=jsonObject.getJSONObject("city").getString("name");
            JSONArray array=jsonObject.getJSONArray("list");
            for (int i=0;i<array.length();i++){
                String icon_hourly="", description="", time_hourly="", max_hourly="", min_hourly="", wind_hourly="", humidity_hourly="";
                JSONObject result=array.getJSONObject(i);
                if(Long.parseLong(result.getString(TIME_CALCULATION))<time_access){
                    continue;
                }
                else{
                if (!getAccess(Long.parseLong(result.getString(TIME_CALCULATION))))
                    break;
                if(contain(result,MAIN)){
                    JSONObject main=result.getJSONObject(MAIN);
                    if(contain(main,TEMP_MAX)&&contain(main,TEMP_MIN)&&contain(main,DAY_HUMIDITY)){
                  max_hourly=main.getString(TEMP_MAX);
                    min_hourly=main.getString(TEMP_MIN);
                    humidity_hourly=main.getString(DAY_HUMIDITY);}
                    else {
                        max_hourly="NO DATA";
                        min_hourly="NO DATA";
                        humidity_hourly="NO DATA";
                    }
                }
               if (contain(result,DATE)){
                   time_hourly=result.getString(DATE);
               }
                else time_hourly="NO DATA";
                if (contain(result,WIND_HOUR)){
                    JSONObject wind=result.getJSONObject(WIND_HOUR);
                    if(contain(wind,WIND_DEGREE_HOUR)&&contain(wind,WIND_SPEED_HOUR)){
                        wind_hourly="Speed is "+wind.getString(WIND_SPEED_HOUR)+" m/s"+" at Degree "+wind.getString(WIND_DEGREE_HOUR);
                    }

                }
                else wind_hourly="NO DATA";
                if (contain(result,WEATHER)){
                    JSONArray jsonArray=result.getJSONArray(WEATHER);
                    JSONObject weather1=jsonArray.getJSONObject(0);
                    if (contain(weather1,DESCRIPTION)){
                        description=weather1.getString(DESCRIPTION);

                    }
                    else description="NO DATA";
                    if (contain(weather1,ICON)){
                        icon_hourly=IMAGE_URL+weather1.getString(ICON)+".png";
                    }
                    else
                        icon_hourly="NAN";

                }
                dumblist.add(new item_hourly(icon_hourly,description,name_hourly,time_hourly,max_hourly,min_hourly,wind_hourly,humidity_hourly));
            }}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dumblist;

    }

    private String getUrl() {
        return BASE_URL_DAY+"1256237"+UNITS_DAILY+APP_ID;
    }
    private boolean getAccess(long time){
        return (time>=time_access)&&(time<time_ahead) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hourly_weather, menu);
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
        if (id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition (R.anim.open_main, R.anim.close_next);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private Boolean contain(JSONObject object,String name){
        return object.has(name)&&(!object.isNull(name));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition (R.anim.open_main, R.anim.close_next);
    }
}
