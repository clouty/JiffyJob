package com.jiffyjob.nimblylabs.browseCategories;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browsepage.BrowsePageView;

import java.util.ArrayList;
import java.util.List;

public class BrowseCategories extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_browse_categories, container, false);
        context = view.getContext();
        init();
        generateCategoriesImageBtn();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //commit changes here
    }

    private enum btnEnum {fnbBtn, officeBtn, hotelBtn, mediaBtn, entertainmentBtn, serviceBtn, saleBtn, healthCareBtn}

    private void init() {
        createOnClickListener();
        btnlinearLayout1 = (LinearLayout) view.findViewById(R.id.btnlinearLayout1);
        btnlinearLayout2 = (LinearLayout) view.findViewById(R.id.btnlinearLayout2);
    }

    //all buttons re-use a single listener
    private void createOnClickListener() {
        btnOnTouchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loop through buttons and identify using tags
                btnEnum btnTag = (btnEnum) v.getTag();
                String text = "onTouch button " + btnTag.toString();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                context.getApplicationContext();
                BrowsePageView browsePageView = new BrowsePageView();
                ((Activity) context).getIntent().putExtra("Cat", btnTag.toString());
                browsePageView.setArguments(((Activity) context).getIntent().getExtras());
                updateFragment(browsePageView, "Browse Page View");
            }
        };
    }

    private void generateCategoriesImageBtn() {
        int margin = 20;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin * 2, 0, margin, margin);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.find_icon);
        ImageButton fnbBtn = new ImageButton(context);
        fnbBtn.setImageBitmap(bitmap);
        fnbBtn.setBackgroundColor(Color.parseColor("#FFf08c78"));
        fnbBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        fnbBtn.setLayoutParams(params);
        fnbBtn.setTag(btnEnum.fnbBtn);
        fnbBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(fnbBtn);

        ImageButton officeBtn = new ImageButton(context);
        officeBtn.setImageBitmap(bitmap);
        officeBtn.setBackgroundColor(Color.parseColor("#FFfd8e2f"));
        officeBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        officeBtn.setLayoutParams(params);
        officeBtn.setTag(btnEnum.officeBtn);
        officeBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(officeBtn);

        ImageButton hotelBtn = new ImageButton(context);
        hotelBtn.setImageBitmap(bitmap);
        hotelBtn.setBackgroundColor(Color.parseColor("#FFbab3b2"));
        hotelBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        hotelBtn.setLayoutParams(params);
        hotelBtn.setTag(btnEnum.hotelBtn);
        hotelBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(hotelBtn);

        ImageButton mediaBtn = new ImageButton(context);
        mediaBtn.setImageBitmap(bitmap);
        mediaBtn.setBackgroundColor(Color.parseColor("#FFb78d78"));
        mediaBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mediaBtn.setLayoutParams(params);
        mediaBtn.setTag(btnEnum.mediaBtn);
        mediaBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(mediaBtn);

        //implement selector in xml to set imagebuton behavior
        btnlinearLayout1.addView(fnbBtn);
        btnlinearLayout1.addView(officeBtn);
        btnlinearLayout1.addView(hotelBtn);
        btnlinearLayout1.addView(mediaBtn);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(0, 0, margin * 2, margin);

        ImageButton entertainmentBtn = new ImageButton(context);
        entertainmentBtn.setImageBitmap(bitmap);
        entertainmentBtn.setBackgroundColor(Color.parseColor("#FF93d6c3"));
        entertainmentBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        entertainmentBtn.setLayoutParams(params2);
        entertainmentBtn.setTag(btnEnum.entertainmentBtn);
        entertainmentBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(entertainmentBtn);

        ImageButton serviceBtn = new ImageButton(context);
        serviceBtn.setImageBitmap(bitmap);
        serviceBtn.setBackgroundColor(Color.parseColor("#FFa4d3ee"));
        serviceBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        serviceBtn.setLayoutParams(params2);
        serviceBtn.setTag(btnEnum.serviceBtn);
        serviceBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(serviceBtn);

        ImageButton saleBtn = new ImageButton(context);
        saleBtn.setImageBitmap(bitmap);
        saleBtn.setBackgroundColor(Color.parseColor("#FFfbdb65"));
        saleBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        saleBtn.setLayoutParams(params2);
        saleBtn.setTag(btnEnum.saleBtn);
        saleBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(saleBtn);

        ImageButton healthCareBtn = new ImageButton(context);
        healthCareBtn.setImageBitmap(bitmap);
        healthCareBtn.setBackgroundColor(Color.parseColor("#FFff8da1"));
        healthCareBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        healthCareBtn.setLayoutParams(params2);
        healthCareBtn.setTag(btnEnum.healthCareBtn);
        healthCareBtn.setOnClickListener(btnOnTouchListener);
        imageButtonList.add(healthCareBtn);

        btnlinearLayout2.addView(entertainmentBtn);
        btnlinearLayout2.addView(serviceBtn);
        btnlinearLayout2.addView(saleBtn);
        btnlinearLayout2.addView(healthCareBtn);
    }

    private void updateFragment(Fragment fragment, String title) {
        ((Activity) context).setTitle(title);

        FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    private View.OnClickListener btnOnTouchListener;
    private List<ImageButton> imageButtonList = new ArrayList<ImageButton>();
    private View view;
    private Context context;
    private LinearLayout btnlinearLayout1;
    private LinearLayout btnlinearLayout2;
}
