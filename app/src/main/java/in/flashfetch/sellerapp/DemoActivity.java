package in.flashfetch.sellerapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import in.flashfetch.sellerapp.Adapters.DemoViewPagerAdapter;

/**
 * Created by KRANTHI on 10-07-2016.
 */
public class DemoActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Demo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DemoActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        DemoViewPagerAdapter demoViewPagerAdapter  = new DemoViewPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.demo_viewPager);
        viewPager.setAdapter(demoViewPagerAdapter);
    }
}
