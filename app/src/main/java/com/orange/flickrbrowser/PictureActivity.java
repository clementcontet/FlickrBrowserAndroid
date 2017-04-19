package com.orange.flickrbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        String farm = this.getIntent().getStringExtra("farm");
        String server = this.getIntent().getStringExtra("server");
        String id = this.getIntent().getStringExtra("id");
        String secret = this.getIntent().getStringExtra("secret");
        ImageView imageView = (ImageView) findViewById(R.id.activity_picture);

        Picasso.with(this).load("https://farm"
                +farm
                +  ".staticflickr.com/"
                + server
                +  "/"
                + id
                + "_" + secret
                + "_b.jpg")
                .into(imageView);
    }
}
