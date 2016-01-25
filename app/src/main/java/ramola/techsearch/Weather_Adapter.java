package ramola.techsearch;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Weather_Adapter extends RecyclerView.Adapter<Weather_Adapter.ViewHolder>{
  private   ArrayList<item_weather> weatherArrayList=new ArrayList<>();
    private Context context;
private ImageLoader imageLoader;
    public Weather_Adapter(Context context) {
        this.context = context;
        imageLoader=MySingleton.getInstance(context).getImageLoader();
    }
public void setList(ArrayList<item_weather> list){
    weatherArrayList=list;
    notifyItemRangeChanged(0,list.size());
}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_weather,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      if (!weatherArrayList.get(position).date.isEmpty()&&weatherArrayList.get(position).date.length()!=0){
         holder.date.setText(getTime(weatherArrayList.get(position).date));
      }
        else {
          holder.date.setText("YYYY-MM-DD");
      }
        if (!weatherArrayList.get(position).status.isEmpty()&&weatherArrayList.get(position).status.length()!=0){
            holder.status.setText(weatherArrayList.get(position).status);
        }
        else {
            holder.status.setText("NO status");
        }
        if (!weatherArrayList.get(position).max.isEmpty()&&weatherArrayList.get(position).max.length()!=0){
            holder.max.setText("MAX: "+weatherArrayList.get(position).max);
        }
        else {
            holder.max.setText("0");
        }
        if (!weatherArrayList.get(position).min.isEmpty()&&weatherArrayList.get(position).min.length()!=0){
            holder.min.setText("MIN: "+weatherArrayList.get(position).min);
        }
        else {
            holder.min.setText("0");
        }
        if (!weatherArrayList.get(position).icon.isEmpty()&&weatherArrayList.get(position).icon.length()!=0){
            load_image(weatherArrayList.get(position).icon,holder);
        }
    }

    private String getTime(String date) {
        long time=Long.parseLong(date)*1000L;
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    private void load_image(String url_photo, final ViewHolder viewHolder) {
        imageLoader.get(url_photo,new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                viewHolder.imageView.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
            }
        });
    }
    @Override
    public int getItemCount() {
        return weatherArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView date,status,max,min;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.image_row_weather_status);
            date= (TextView) itemView.findViewById(R.id.date_row_weather);
            status= (TextView) itemView.findViewById(R.id.status_row_weather);
            max= (TextView) itemView.findViewById(R.id.max_temp_row_weather);
            min= (TextView) itemView.findViewById(R.id.min_temp_row_weather);
        }
    }
}
