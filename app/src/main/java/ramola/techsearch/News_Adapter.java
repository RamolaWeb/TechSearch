package ramola.techsearch;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import static ramola.techsearch.Field.BASE_URL;
public class News_Adapter extends RecyclerView.Adapter<News_Adapter.ViewHolder> {
    private Context context;
    private ImageLoader imageLoader;
    private int previous=0;
    public News_Adapter(Context context) {
        this.context = context;
        imageLoader=MySingleton.getInstance(context).getImageLoader();
    }

    private ArrayList<Feed> list=new ArrayList();
    private int size_list=0;
    public void setList(ArrayList<Feed> list){
        this.list=list;
        notifyItemRangeChanged(size_list,list.size());
        size_list=list.size();
    }
public void refresh(ArrayList<Feed> list){
    this.list=list;
    notifyItemInserted(list.size());
}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.title.setText(list.get(i).TITLE);
        try {
            viewHolder.short_description.setText(URLDecoder.decode(list.get(i).SHORT_DESCRIPTION, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.d("error", "error");
        }
        if (Utils.check(list.get(i).URL_PHOTO.substring(9))) {
            new Waiting.GetImage(list.get(i).URL_PHOTO.substring(9), viewHolder.imageView).execute();
        } else
            load_image(list.get(i).URL_PHOTO, viewHolder);
            if (i > previous) {
                Animation.AnimateRecyclerItem(viewHolder, true);
            } else {
                Animation.AnimateRecyclerItem(viewHolder, false);
            }
            previous = i;


    }

    private void load_image(  final String url_photo, final ViewHolder viewHolder) {
        imageLoader.get(BASE_URL + url_photo, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap != null) {
                    viewHolder.imageView.setImageBitmap(bitmap);
                    new Waiting.SaveImage(bitmap, url_photo.substring(9)).execute();
                } else
                    Log.d("errsdfg", "errrrorrr for" + url_photo);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                viewHolder.imageView.setImageResource(R.drawable.mohit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,short_description;
       ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title_row_news);
            short_description= (TextView) itemView.findViewById(R.id.short_description_row_news);
            imageView= (ImageView) itemView.findViewById(R.id.imageView_row_news);
            title.setTypeface(TypeFaceManager.getBold());
            short_description.setTypeface(TypeFaceManager.getLight());
        }
    }

}
