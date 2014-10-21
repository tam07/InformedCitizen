package sbl.com.informedcitizen;
import android.app.Application;

import com.activeandroid.ActiveAndroid;


//public class InformedCitizenApp extends Application {
  public class InformedCitizenApp extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        // Create global configuration and initialize ImageLoader with this configuration
        /*DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();*/
        /*DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).
                                             cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);*/
    }
}
