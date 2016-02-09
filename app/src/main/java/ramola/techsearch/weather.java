package ramola.techsearch;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static ramola.techsearch.Field.Field_weather.APP_ID;
import static ramola.techsearch.Field.Field_weather.ARRAY_NAME_MAIN;
import static ramola.techsearch.Field.Field_weather.BASE_URL_WEATHER;
import static ramola.techsearch.Field.Field_weather.BASE_URL_WEATHER_CURRENT;
import static ramola.techsearch.Field.Field_weather.CURRENT_UNIT;
import static ramola.techsearch.Field.Field_weather.DATE;
import static ramola.techsearch.Field.Field_weather.IMAGE_URL;
import static ramola.techsearch.Field.Field_weather.LOGO;
import static ramola.techsearch.Field.Field_weather.MAX;
import static ramola.techsearch.Field.Field_weather.MIN;
import static ramola.techsearch.Field.Field_weather.STATUS;
import static ramola.techsearch.Field.Field_weather.TEMP;
import static ramola.techsearch.Field.Field_weather.UNIT;
import static ramola.techsearch.Field.Field_weather.WEATHER;
import static ramola.techsearch.Field.Field_weather.WIND;
import static ramola.techsearch.Field.Field_weather.WIND_DEGREE;
import static ramola.techsearch.Field.Field_weather.WIND_SPEED;

/**
 * A simple {@link Fragment} subclass.
 */
public class weather extends android.support.v4.app.Fragment {
 private    onclick onclick_item;
    private static final String TAG_WEATHER ="weather" ;
    private static final String TAG_CURRENT_MAX = "max";
    private static final String TAG_CURRENT_MIN ="min" ;
    private static final String TAG_CURRENT_WIND ="wind" ;
    private static final String TAG_CURRENT_HUMIDITY ="humidity" ;
    private static final String TAG_CURRENT_STATUS ="status";
    private  RecyclerView recyclerView;
private Weather_Adapter adapter;
private ArrayList<item_weather> list;
private TextView CURRENT_MAX,CURRENT_MIN,CURRENT_WIND,CURRENT_HUMIDITY,CURRENT_STATUS;
//private String MAX,MIN,WIND,HUMIDITY,STATUS,url;
private ImageView CURRENT_LOGO;
private ImageLoader imageLoader;
    private  LocationDetector locationDetector;
    public weather() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_wheather, container, false);
        locationDetector=new LocationDetector(getActivity());
        imageLoader=MySingleton.getInstance(getActivity()).getImageLoader();
        recyclerView= (RecyclerView) v.findViewById(R.id.recycler_view_weather);
        CURRENT_MAX= (TextView) v.findViewById(R.id.current_max);
        CURRENT_MIN= (TextView) v.findViewById(R.id.current_min);
        CURRENT_WIND= (TextView) v.findViewById(R.id.current_wind);
        CURRENT_HUMIDITY= (TextView) v.findViewById(R.id.current_humidity);
        CURRENT_STATUS= (TextView) v.findViewById(R.id.current_status);
        CURRENT_LOGO= (ImageView) v.findViewById(R.id.current_image_status);
        setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new Weather_Adapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onclick_item.click(list.get(position).date);
            }
        }));
        if (savedInstanceState!=null){
            list=savedInstanceState.getParcelableArrayList(TAG_WEATHER);
            adapter.setList(list);
            CURRENT_MAX.setText(savedInstanceState.getString(TAG_CURRENT_MAX));
            CURRENT_MIN.setText(savedInstanceState.getString(TAG_CURRENT_MIN));
            CURRENT_HUMIDITY.setText(savedInstanceState.getString(TAG_CURRENT_HUMIDITY));
            CURRENT_WIND.setText(savedInstanceState.getString(TAG_CURRENT_WIND));
            CURRENT_STATUS.setText(savedInstanceState.getString(TAG_CURRENT_STATUS));
        }
        else{
            Sendjson_current();
            sendjson();
        }
        return v ;
    }
public void sendjson(){
    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(getUrl(false),null,new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            list=parseJson(jsonObject);
            adapter.setList(list);
        }
    },new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    });
    MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
}

    private ArrayList<item_weather> parseJson(JSONObject jsonObject) {
        if (jsonObject.length()==0)
            return null;
        ArrayList<item_weather> dumArrayList=new ArrayList<>();
        try {
            JSONArray array=jsonObject.getJSONArray(ARRAY_NAME_MAIN);
            for (int i=0;i<array.length();i++){
                String max="",min="",status="",date="",icon="";
                JSONObject jsonObject1=array.getJSONObject(i);
                if(contain(jsonObject1,DATE)){
                    date=jsonObject1.getString(DATE);
                }
                else date="YYYY-MM-DD";
                if(contain(jsonObject1,TEMP)){
                    JSONObject temp=jsonObject1.getJSONObject(TEMP);
                    if(contain(temp,MAX)){
                        max=temp.getString(MAX);
                    }
                    else {
                        Log.d("error","error");
                        max="NO DATA";}
                    if(contain(temp,MIN)){
                        min=temp.getString(MIN);
                    }
                    else {min="NO DATA";}
                }
                if (contain(jsonObject1,WEATHER)){
                    JSONArray jsonArray=jsonObject1.getJSONArray(WEATHER);
                        JSONObject weather1=jsonArray.getJSONObject(0);
                        if (contain(weather1,STATUS)){
                            status=weather1.getString(STATUS);

                        }
                        else status="NO DATA";
                      if (contain(weather1,LOGO)){
                          icon=IMAGE_URL+weather1.getString(LOGO)+".png";
                      }
                        else
                        icon="NAN";

                }
                dumArrayList.add(new item_weather(date,status,max,min,icon));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dumArrayList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG_WEATHER, list);
       outState.putString(TAG_CURRENT_MAX,CURRENT_MAX.getText().toString());
        outState.putString(TAG_CURRENT_MIN,CURRENT_MIN.getText().toString());
        outState.putString(TAG_CURRENT_WIND,CURRENT_WIND.getText().toString());
        outState.putString(TAG_CURRENT_HUMIDITY,CURRENT_HUMIDITY.getText().toString());
        outState.putString(TAG_CURRENT_STATUS,CURRENT_STATUS.getText().toString());
    }
    private String getUrl(boolean Current){
        if(Current)
            return BASE_URL_WEATHER_CURRENT+"lat="+locationDetector.getLatitude()+"&lon="+locationDetector.getLongitude()+CURRENT_UNIT+APP_ID;
            else
        return BASE_URL_WEATHER+"lat="+locationDetector.getLatitude()+"&lon="+locationDetector.getLongitude()+UNIT+APP_ID;

    }
    private Boolean contain(JSONObject object,String name){
        return object.has(name)&&(!object.isNull(name));
    }
    private void Sendjson_current(){
        JsonObjectRequest jsonObjectRequest_current=new JsonObjectRequest(getUrl(true),null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                parseJson_current(jsonObject);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest_current);
    }

    private void parseJson_current(JSONObject jsonObject) {
        if(contain(jsonObject,"main")){
            try {
                JSONObject weather=jsonObject.getJSONObject("main");
               CURRENT_MAX.setText("MAX: "+weather.getString("temp_max"));
                CURRENT_MIN.setText("MIN: "+weather.getString("temp_min"));
                CURRENT_HUMIDITY.setText("HUMIDITY: "+weather.getString("humidity")+" %");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            CURRENT_MAX.setText("NO DATA");
            CURRENT_MIN.setText("NO DATA");
            CURRENT_HUMIDITY.setText("NO DATA");
        }
        if (contain(jsonObject,WIND)){
            try {
                JSONObject wind=jsonObject.getJSONObject(WIND);
                CURRENT_WIND.setText("SPEED OF WIND IS "+wind.getString(WIND_SPEED)+" m/s"+" at degree "+wind.getString(WIND_DEGREE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else CURRENT_WIND.setText("NO DATA");
        if (contain(jsonObject,WEATHER)){
            JSONArray jsonArray= null;
            try {
                jsonArray = jsonObject.getJSONArray(WEATHER);
                JSONObject weather1=jsonArray.getJSONObject(0);
                if (contain(weather1,STATUS)){
                    CURRENT_STATUS.setText(weather1.getString(STATUS)+"\n"+weather1.getString("description"));

                }
                else CURRENT_STATUS.setText("NO DATA ");
                if (contain(weather1,LOGO)){
                    load_image(IMAGE_URL + weather1.getString(LOGO) + ".png");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private void load_image(String url_photo) {
        imageLoader.get(url_photo,new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                CURRENT_LOGO.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
             CURRENT_LOGO.setImageResource(R.drawable.ic_launcher);
            }
        });
    }
    public  interface onclick{
        public void click(String time);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onclick_item= (onclick) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_weather,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_share){
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,"Today weather is "+CURRENT_STATUS.getText().toString());
            startActivity(Intent.createChooser(i,"Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
