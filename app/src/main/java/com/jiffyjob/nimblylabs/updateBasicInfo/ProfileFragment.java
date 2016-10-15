package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.UserInfoModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.QuestionnaireFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by himur on 4/24/2016.
 */
public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        Activity activity = getActivity();
        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();

        YoYo.with(Techniques.SlideInUp)
                .duration(Utilities.getAnimationFast())
                .playOn(getView());
    }

    public void setModel(BasicInforEventModel model) {
        this.basicInforEventModel = model;
    }

    public void setModel(UserInfoModel model) {
        this.userInfoModel = model;
    }

    private void init() {
        View view = getView();
        if (view == null) return;
        final ImageView profileIV = (ImageView) view.findViewById(R.id.profileIV);
        final ImageView profileBgIV = (ImageView) view.findViewById(R.id.profileBgIV);

        TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        TextView ageGenderTV = (TextView) view.findViewById(R.id.ageGenderTV);
        TextView locationTV = (TextView) view.findViewById(R.id.locationTV);
        ListView detailLV = (ListView) view.findViewById(R.id.detailLV);
        profileAdapter = new ProfileAdapter(getActivity(), 0, detailModelList);
        detailLV.setAdapter(profileAdapter);
        questionnairLayout = (RelativeLayout) view.findViewById(R.id.questionnairLayout);

        nameTV.setText(String.format("%s %s", userInfoModel.getFirstName(), userInfoModel.getLastName()));
        locationTV.setText(userInfoModel.getUserLocation());
        if (userInfoModel.getDob() != null) {
            ageGenderTV.setText(String.format(Locale.ENGLISH, "%d / %s", findAge(userInfoModel.getDob()), userInfoModel.getGender()));
        } else {
            ageGenderTV.setVisibility(View.GONE);
        }
        if (userInfoModel != null) {
            if (userInfoModel.getCoverPic() != null) {
                Glide.with(getActivity())
                        .load(userInfoModel.getCoverPic())
                        .fitCenter()
                        //.bitmapTransform(new RoundedCornersTransformation(getActivity(), 20, 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(profileBgIV);
            } else {
                profileBgIV.setImageResource(R.drawable.transparent_dark_gradient_shape);
            }

            Glide.with(getActivity())
                    .load(userInfoModel.getUserPhoto())
                    .fitCenter()
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(profileIV);
        }

        initListener();
        populateDetailModel();
    }

    private void initListener() {
        questionnairLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new QuestionnaireFragment());
                fragmentTransaction.addToBackStack(this.getClass().getSimpleName());
                fragmentTransaction.commit();
            }
        });
    }

    private int findAge(Date dob) {
        Calendar dobCalendar = Calendar.getInstance();
        dobCalendar.setTime(dob);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    private void populateDetailModel() {
        detailModelList.clear();
        detailModelList.add(new ProfileDetailModel("ABOUT MYSELF", userInfoModel.getShortBio()));
        detailModelList.add(new ProfileDetailModel("EDUCATION", null));
        detailModelList.add(new ProfileDetailModel("INTEREST", userInfoModel.getInterest()));
        detailModelList.add(new ProfileDetailModel("BADGES", "None"));
        profileAdapter.notifyDataSetChanged();
    }

    private UserInfoModel userInfoModel;
    private RelativeLayout questionnairLayout;
    private BasicInforEventModel basicInforEventModel;
    private List<ProfileDetailModel> detailModelList = new ArrayList<>();
    private ProfileAdapter profileAdapter;
}
