package com.orange.flickrbrowser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("FlickrBrowserAndroid");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FlickrService service = retrofit.create(FlickrService.class);

        service.getPictures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        FlickrAnswer -> {
                            mAdapter = new PictureAdapter(FlickrAnswer.photos.photo);
                            recyclerView.setAdapter(mAdapter);
                        },
                        throwable -> Log.e("TAG", "Error", throwable));
    }

    public interface FlickrService {
        @GET("/services/rest/?method=flickr.interestingness.getList&api_key=0c731f4470260b5ff4ccc3d519d07697&format=json&nojsoncallback=1&per_page=500")
        Observable<FlickrAnswer> getPictures();
    }

    public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
        private FlickrAnswer.FlickrPic[] mPictures;

        public PictureAdapter(FlickrAnswer.FlickrPic[] pictures) {
            mPictures = pictures;
        }

        @Override
        public PictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.element_picture, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mPictures[position].title);

            Picasso.with(MainActivity.this).load("https://farm"
                    + mPictures[position].farm
                    +  ".staticflickr.com/"
                    + mPictures[position].server
                    +  "/"
                    + mPictures[position].id
                    + "_" + mPictures[position].secret
                    + "_q.jpg")
                    .into(holder.mImageView);

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                intent.putExtra("farm",  mPictures[position].farm);
                intent.putExtra("server",  mPictures[position].server);
                intent.putExtra("id",  mPictures[position].id);
                intent.putExtra("secret",  mPictures[position].secret);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return mPictures.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImageView;
            TextView mTextView;
            ViewHolder(View v) {
                super(v);
                mImageView = (ImageView) v.findViewById(R.id.image_view);
                mTextView = (TextView) v.findViewById(R.id.text_view);
            }
        }
    }
}