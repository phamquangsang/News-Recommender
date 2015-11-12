package com.thefour.newsrecommender.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.thefour.newsrecommender.app.data.NewsContract.NewsEntry;
import com.thefour.newsrecommender.app.data.NewsContract.CategoryEntry;
import com.thefour.newsrecommender.app.data.NewsContract.NewsSourceEntry;

import com.thefour.newsrecommender.app.data.NewsContract;

/**
 * Created by Quang Quang on 8/5/2015.
 */
public class ListNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int NEWS_LOADER=0;

    private static final String[] NEWS_COLUMN = {
            NewsContract.NewsEntry.TABLE_NAME+"."+ NewsContract.NewsEntry._ID,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsEntry.COLUMN_CATEGORY_ID,
            NewsEntry.COLUMN_SOURCE_ID,
            NewsEntry.COLUMN_DESC,
            NewsEntry.COLUMN_CONTENT_URL,
            NewsEntry.COLUMN_IMAGE_URL,
            NewsEntry.COLUMN_IMAGE_PATH,
            NewsEntry.COLUMN_TIME,
            NewsEntry.COLUMN_RATING,
            NewsSourceEntry.COLUMN_SOURCE_NAME,
            NewsSourceEntry.COLUMN_LOGO_FILE_PATH
    };

    static final int COL_NEWS_ID=0;
    static final int COL_TITLE=1;
    static final int COL_CATEGORY_ID=2;
    static final int COL_SOURCE_ID=3;
    static final int COL_DESC=4;
    static final int COL_CONTENT_URI = 5;
    static final int COL_IMAGE_URL =6;
    static final int COL_IMAGE_PATH =7;
    static final int COL_TIME=8;
    static final int COL_RATING =9;
    static final int COL_SOURCE_NAME=10;
    static final int COL_LOGO_FILE_PATH =11;
    private static final String LOG_TAG = ListNewsFragment.class.getSimpleName();

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_CATEGORY_ID = "section_category_id";

    private ListView mListView;
    private NewsAdapter mAdapter;
    private int mPosition;
    public ListNewsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListNewsFragment newInstance(int sectionNumber, int categoryId) {
        ListNewsFragment fragment = new ListNewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_SECTION_CATEGORY_ID,categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(NEWS_LOADER,null,this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAdapter = new NewsAdapter(getActivity(),null,0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int categoryId = getArguments().getInt(ARG_SECTION_CATEGORY_ID);
        Uri newsUri;
        if(categoryId==0)//speacial category this is highlight section
        {
            newsUri = NewsEntry.CONTENT_URI;
        }else {
            newsUri = NewsEntry.buildNewsCategoryId(categoryId);
        }
        //Uri newsUri = NewsEntry.CONTENT_URI;
        String sortOrder = NewsEntry.COLUMN_RATING+" DESC";
        Log.i(LOG_TAG,"News in category Uri: "+newsUri);

        return new CursorLoader(getActivity(),newsUri,NEWS_COLUMN,null,null,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG,"onLoadFinished(): "+data.getCount()+" records loaded");
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        mListView = (ListView)rootView.findViewById(R.id.listView_news);
        mListView.setAdapter(mAdapter);
        //onItemClickListener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(cursor.getString(COL_CONTENT_URI)));
                startActivity(intent);
                mPosition= position;
                mListView.smoothScrollToPosition(mPosition);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
