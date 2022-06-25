package com.example.blog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {

    private ArrayList<SlidePageModule> pages;
    private LayoutInflater layoutInflater;
    private Context context;
    private LinearLayout dots;

    private  int position;


    ImageView imageViewSlider;
    TextView title , description;

    public SliderAdapter(Context context) {
        this.context = context;
        pages = new ArrayList<SlidePageModule>();
        pages.add(new SlidePageModule("Order", "Hi Belen, experts recommend replacing your bike helmet every 3 years. If you can’t remember the last time you replaced yours, then the good news is we’re running a helmet sale with up to 50% off: txt.st/DNG4C ", R.drawable.p1));
        pages.add(new SlidePageModule("pay", "Did someone say dinner? Time to restock or risk being left in the dog house. We’ve got all your favorite goodies just waiting to keep your pooch well-fed, all in an environmentally friendly way: txt.st/DNG4C ", R.drawable.p2));
        pages.add(new SlidePageModule("Delivery", "Hi Belen, we’re all about paying it forward. Bring your friends into our #DoingThings community and enjoy sweating it out together. They get $20 off their first purchase of $100, and you get $20 too. Just send them this link: txt.st/DNG4C ", R.drawable.p3));
        position = 0 ;
    }


    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.position = position;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout ,container , false);

         imageViewSlider = view.findViewById(R.id.slideScreenImageV);
         description = view.findViewById(R.id.description);
         title = view.findViewById(R.id.title);

         imageViewSlider.setImageResource(pages.get(position).getImage());
         title.setText(pages.get(position).getTitle());
         description.setText(pages.get(position).getDescription());

         container.addView(view);
        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    public int getPosition() {
        return position;
    }
}
