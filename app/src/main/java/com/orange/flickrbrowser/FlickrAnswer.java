package com.orange.flickrbrowser;

import java.util.ArrayList;

/**
 * Created by clementcontet on 4/19/17.
 */

public class FlickrAnswer {
        public FlickrPics photos;

    public class FlickrPics {
        public FlickrPic[] photo;
    }

    public class FlickrPic {
        public String id;
        public String secret;
        public String server;
        public String farm;
        public String title;
    }
}
