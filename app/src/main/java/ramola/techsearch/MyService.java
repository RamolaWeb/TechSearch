package ramola.techsearch;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static ramola.techsearch.Field.BASE_URL;
import static ramola.techsearch.Field.CATEGORY;
import static ramola.techsearch.Field.FEED;
import static ramola.techsearch.Field.LONG_DESCRIPTION;
import static ramola.techsearch.Field.PHOTO;
import static ramola.techsearch.Field.SHORT_DESCRIPTION;
import static ramola.techsearch.Field.TITLE;
import static ramola.techsearch.Field.URL_FETCH_DATA;

public class MyService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if(new Connection(this).isInternet()){
        new Task(this).execute(jobParameters);}
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
    private static class Task extends AsyncTask<JobParameters,Void,JobParameters>{
        private MyService myService;
        private DBhelper dBhelper;
         Task(MyService myService) {
             this.myService=myService;
             dBhelper=MyApplication.getDataBase();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameterses) {
            try {
                dBhelper.insertMovies(parseJson(sendJsonRequest()),true);
            }
           catch (Exception c){

           }
            return jobParameterses[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            myService.jobFinished(jobParameters,false);

        }
        private JSONObject sendJsonRequest(){
            JSONObject object=null;
            RequestFuture<JSONObject> requestFuture=RequestFuture.newFuture();
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(getUrl(),null,requestFuture,requestFuture);
            int socketoutms=30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketoutms, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            MySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest);
            try {
                object=requestFuture.get(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (TimeoutException e) {
            }


            return object;
        }

        private ArrayList<Feed> parseJson(JSONObject jsonObject){

            if(jsonObject==null)
                return null;
            ArrayList<Feed> feedArrayList=new ArrayList<>();
            try {
                JSONArray jsonArray=jsonObject.getJSONArray(FEED);
                for (int i=0;i<jsonArray.length();i++){
                    String category,title,short_description,long_description,photo_url;
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    if (contain(jsonObject1,CATEGORY)){
                        category=jsonObject1.getString(CATEGORY);
                    }
                    else{
                        category="NaN";
                    }
                    if (contain(jsonObject1,TITLE)){
                        title=jsonObject1.getString(TITLE);
                    }
                    else{
                        title="NaN";
                    }
                    if (contain(jsonObject1,SHORT_DESCRIPTION)){
                        short_description=jsonObject1.getString(SHORT_DESCRIPTION);
                    }
                    else{
                        short_description="NaN";
                    }
                    if (contain(jsonObject1,LONG_DESCRIPTION)){
                        long_description=jsonObject1.getString(LONG_DESCRIPTION);
                    }
                    else{
                        long_description="NaN";
                    }
                    if (contain(jsonObject1,PHOTO)){
                        photo_url=jsonObject1.getString(PHOTO);
                    }
                    else{
                        photo_url="NaN";
                    }
                    feedArrayList.add(new Feed(title,category,short_description,long_description,photo_url));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return feedArrayList;
        }
        private Boolean contain(JSONObject object,String name){
            return object.has(name)&&(!object.isNull(name));
        }
        private String getUrl(){
            return BASE_URL+URL_FETCH_DATA;
        }
    }
}
