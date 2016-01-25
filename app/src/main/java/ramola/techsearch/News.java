package ramola.techsearch;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class News extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Feed> list;
    private News_Adapter adapter;
    private load_news loadNews;
    private String Key_Movie = "feed";
    private DBhelper dBhelper;
    private int showData = 20;
    private int visibleItemCount, totalItemCount, firstVisibleItem;
    private boolean loading = true;
    private int previousTotal;

    public News() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dBhelper = MyApplication.getDataBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_news);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new News_Adapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + 1)) {
                    // End has been reached

                    Log.i("Yaeye!", "end called");
                    new News.load(true).execute();
                    // Do something

                    loading = true;
                }
            }
        });
        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList(Key_Movie);
            adapter.setList(list);
        } else {
            new News.load(false).execute();

        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadNews.getId(list.get(position).TITLE);
            }
        }));

        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            //  list=dBhelper.getData();
            //   adapter.refresh(list);
            return true;
        }
        if (item.getItemId() == R.id.action_upload) {
            startActivity(new Intent(getActivity(), UploadActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Key_Movie, list);
    }

    public interface load_news {
        public void getId(String id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loadNews = (load_news) context;
    }

    private class load extends AsyncTask<Void, Void, Void> {

        private boolean load;

        public load(boolean load) {
            this.load = load;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (load) {
                list.addAll(dBhelper.getData(20, showData));
                showData = showData + 20;
            } else
                list = dBhelper.getData(20, 0);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.setList(list);
        }
    }
}
