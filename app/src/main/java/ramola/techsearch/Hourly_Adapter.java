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

import java.util.ArrayList;

public class Hourly_Adapter extends RecyclerView.Adapter<Hourly_Adapter.ViewHolder> {
    private ArrayList<item_hourly> list=new ArrayList<>();
    private ImageLoader imageLoader;
    private int previous=0;

    public Hourly_Adapter(Context context) {
        imageLoader=MySingleton.getInstance(context).getImageLoader();
    }
public void setList(ArrayList<item_hourly> list){
    this.list=list;
    notifyItemRangeChanged(0,list.size());
}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hourly,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      if(!list.get(position).name_hourly.isEmpty()&&list.get(position).name_hourly.length()!=0){
          holder.name.setText(list.get(position).name_hourly);
      }
        else {
          holder.name.setText("NO DATA");
      }
        if(!list.get(position).time_hourly.isEmpty()&&list.get(position).time_hourly.length()!=0){
            holder.time.setText(list.get(position).time_hourly);
        }
        else {
            holder.time.setText("NO DATA");
        }
        if(!list.get(position).description.isEmpty()&&list.get(position).description.length()!=0){
            holder.description.setText(list.get(position).description);
        }
        else {
            holder.description.setText("NO DATA");
        }
        if(!list.get(position).max_hourly.isEmpty()&&list.get(position).max_hourly.length()!=0&&!list.get(position).min_hourly.isEmpty()&&list.get(position).min_hourly.length()!=0){
            holder.max_min.setText("MAX: "+list.get(position).max_hourly+"/"+" MIN: "+list.get(position).min_hourly);
        }
        else {
            holder.max_min.setText("NO DATA");
        }
        if(!list.get(position).wind_hourly.isEmpty()&&list.get(position).wind_hourly.length()!=0){
            holder.wind.setText(list.get(position).wind_hourly);
        }
        else {
            holder.wind.setText("NO DATA");
        }
        if(!list.get(position).humidity_hourly.isEmpty()&&list.get(position).humidity_hourly.length()!=0){holder.humidity.setText("HUMIDITY IS "+list.get(position).humidity_hourly+" %");}
        else {
            holder.humidity.setText("NO DATA");
        }
        if(!list.get(position).icon_hourly.isEmpty()&&list.get(position).icon_hourly.length()!=0){
            load_image(list.get(position).icon_hourly,holder);
        }
        if(position>previous)
        {
            Animation.AnimateRecyclerItem(holder,true);
        }
        else {
            Animation.AnimateRecyclerItem(holder,false);
        }
        previous=position;
    }

    private void load_image(String url_photo, final ViewHolder viewHolder) {
        imageLoader.get(url_photo,new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                viewHolder.icon.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                viewHolder.icon.setImageResource(R.drawable.ic_launcher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,time,description,max_min,wind,humidity;
        ImageView icon;
        public ViewHolder(View itemView) {
            super(itemView);
        name= (TextView) itemView.findViewById(R.id.name_hourly);
        time= (TextView) itemView.findViewById(R.id.time_hourly);
        description= (TextView) itemView.findViewById(R.id.description_hourly);
        max_min= (TextView) itemView.findViewById(R.id.temperature_hourly);
        wind= (TextView) itemView.findViewById(R.id.wind_hourly);
        humidity= (TextView) itemView.findViewById(R.id.humidity_hourly);
        icon= (ImageView) itemView.findViewById(R.id.image_hourly);
        }
    }
}
