package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OnBoardActivity extends AppCompatActivity {

    private int currentPos = 0;

    private ViewPager viewPager;
    private Button btn_Next, btn_Skip;
    private LinearLayout dots;
    private SliderAdapter mAdapter;
    private TextView dotsTextv[];
    private ViewPager.OnPageChangeListener mPageOnChangeListiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        dots = (LinearLayout) findViewById(R.id.dots);
        mAdapter = new SliderAdapter(this);
        viewPager.setAdapter(this.mAdapter);
        btn_Skip = findViewById(R.id.Skip_btn);
        btn_Next = findViewById(R.id.btn_next);
        this.mPageOnChangeListiner = new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                DotsFunction(position);
                btnsHide(position);
                currentPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        btn_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_Skip.getText().toString().equals("Skip")) {
                    startActivity(new Intent(getBaseContext() , AuthActivity.class));
                    finish();

                }else  {
                    currentPos -=1;
                    viewPager.setCurrentItem(currentPos);
                }
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_Next.getText().toString().equals("Start")) {
                    startActivity(new Intent(getBaseContext() , AuthActivity.class));
                    finish();
                }
                else {
                    currentPos = (currentPos+1)% mAdapter.getCount();
viewPager.setCurrentItem(currentPos);                }
            }
        });
        viewPager.setOnPageChangeListener(this.mPageOnChangeListiner);

    }

    @SuppressLint("ResourceAsColor")
    public void DotsFunction(int pos) {

        dots.removeAllViews();

        dotsTextv = new TextView[mAdapter.getCount()];
        for (int x = 0; x < mAdapter.getCount(); x++) {

            dotsTextv[x] = new TextView(this);
            dotsTextv[x].setText(Html.fromHtml("&#8226"));
            if (x == pos) {
                dotsTextv[x].setTextColor(getResources().getColor(R.color.red));
                dotsTextv[x].setTextSize(40);

            } else {
                dotsTextv[x].setTextColor(R.color.white);
                dotsTextv[x].setTextSize(35);
            }
            dots.addView(dotsTextv[x]);

        }
    }

    public void btnsHide(int position) {
        if (position != 0) {
            this.btn_Skip.setText("Back");
        } else  {
            this.btn_Skip.setText("Skip");

        }
        if (position == mAdapter.getCount() - 1) {
            this.btn_Next.setText("Start");
        } else {
            this.btn_Next.setText("Next");
        }

    }
}