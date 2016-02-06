package ramola.techsearch;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements weather.onclick,News.load_news{
private Toolbar toolbar;
private TabLayout tabLayout;
private ViewPager viewPager;
private FragmentPagerAdapter pagerAdapter;
private JobScheduler jobScheduler;
    private static final String KEY_ID ="id" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jobScheduler= (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        toolbar= (Toolbar) findViewById(R.id.include_app_bar);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Title_app);
        pagerAdapter.add(new News(), "NEWS");
        pagerAdapter.add(new weather(),"WEATHER");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                constructJob();
            }
        }, 30000);

    }
private void constructJob(){
    JobInfo.Builder builder=new JobInfo.Builder(100,new ComponentName(this,MyService.class))
            .setPeriodic(1000)
            .setPersisted(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
    jobScheduler.schedule(builder.build());
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void click(String time) {
        viewPager.setCurrentItem(1);
        Intent i=new Intent(this,hourly_weather.class);
        i.putExtra("GET",Long.parseLong(time));
        startActivity(i);
        overridePendingTransition (R.anim.open_next, R.anim.close_main);
    }

    @Override
    public void getId(String id) {
        viewPager.setCurrentItem(0);
        Intent i=new Intent(this,LoadMoreFeed.class);
        i.putExtra(KEY_ID,id);
        startActivity(i);
        overridePendingTransition (R.anim.open_next, R.anim.close_main);
    }
}
