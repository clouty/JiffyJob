package com.jiffyjob.nimblylabs.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.appSettingView.AppSettingFragment;
import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.UserInfoModel;
import com.jiffyjob.nimblylabs.browseCategories.BrowseCategories;
import com.jiffyjob.nimblylabs.customui.ImageHelper;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity.MenuItemEnum;
import com.jiffyjob.nimblylabs.managePost.ManageJobFragment;
import com.jiffyjob.nimblylabs.preferenceView.PreferenceFragment;
import com.jiffyjob.nimblylabs.topNavigation.Event.TopNavigationChangedEvent;
import com.jiffyjob.nimblylabs.updateBasicInfo.ProfileFragment;
import com.jiffyjob.nimblylabs.updateBasicInfo.UpdateBasicInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by NimblyLabs on 20/1/2015.
 */
public class DrawerItemAdapter extends ArrayAdapter<DrawerItemObject> {
    private int resourceId; //custom drawer item

    public DrawerItemAdapter(Context context, int resource, List<DrawerItemObject> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            convertView.setTag(new Pair<>(position, getItem(position).menuItem));
            convertView.setClickable(true);
            if (position == 0) {
                viewList = new ArrayList<>();
            } else {
                convertView.setBackgroundResource(R.drawable.ripple_white);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<Integer, MenuItemEnum> tag = (Pair<Integer, MenuItemEnum>) v.getTag();
                    createFragment(tag.second);
                }
            });
            viewList.add(convertView);
        }
        DrawerItemObject drawItem = getItem(position);
        //Set default color to highlight browse
        if (MenuItemEnum.Browse.getValue() == position) {
            highlightPosition(MenuItemEnum.Browse);
        }

        //set selection item name and icon
        ImageButton imageViewIcon = (ImageButton) convertView.findViewById(R.id.drawer_icon);
        TextView textViewName = (TextView) convertView.findViewById(R.id.drawer_itemName);
        imageViewIcon.setBackground(drawItem.iconDrawable);
        textViewName.setText(drawItem.menuItem.toString());

        //set either user info or menu item
        RelativeLayout drawerSelection = (RelativeLayout) convertView.findViewById(R.id.drawerSelection);
        LinearLayout userInfoLayout = (LinearLayout) convertView.findViewById(R.id.userInfoLayout);
        if (position == 0) {
            drawerSelection.setVisibility(View.GONE);
            userInfoLayout.setVisibility(View.VISIBLE);
            createUserInfoView(convertView);
        } else {
            drawerSelection.setVisibility(View.VISIBLE);
            userInfoLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void createFragment(MenuItemEnum itemEnum) {
        highlightPosition(itemEnum);
        EventBus.getDefault().post(new TopNavigationChangedEvent(itemEnum));
        switch (itemEnum) {
            case Profile:
                createProfileViewFragment();
                break;
            case Browse:
                createBrowseCategoriesFragment();
                break;
            case Inbox:
                break;
            case Manage:
                createManagePostApplyFragment();
                break;
            case Shop:
                break;
            case Preferences:
                createPreferences();
                break;
            case AppSetting:
                createAppSettings();
                break;
            default:
                break;
        }
    }

    public void updateUserImage(UserInfoModel model) {
        this.model = model;
    }

    public void highlightPosition(MenuItemEnum menuItemEnum) {
        resetHighLight();
        for (View v : viewList) {
            if (((Pair<Integer, MenuItemEnum>) v.getTag()).second == menuItemEnum) {
                View view = v.findViewById(R.id.menuItemHighlight);
                view.setVisibility(View.VISIBLE);

                TextView tv = (TextView) v.findViewById(R.id.drawer_itemName);
                tv.setTextColor(ContextCompat.getColor(context, R.color.jj_green));

                ImageButton imageViewIcon = (ImageButton) v.findViewById(R.id.drawer_icon);
                imageViewIcon.setEnabled(false);
            }
        }
   /*int position = menuItemEnum.getValue();
     if (viewList != null && viewList.size() > position) {
            View v = viewList.get(position);
            View view = v.findViewById(R.id.menuItemHighlight);
            view.setVisibility(View.VISIBLE);
            //YoYo.with(Techniques.StandUp).duration(Utilities.getAnimationNormal()).playOn(view);
        }*/
    }

    private void createUserInfoView(View convertView) {
        if (model == null) return;
        ImageView userImageView = (ImageView) convertView.findViewById(R.id.userImageView);
        ImageView userInfoBgIV = (ImageView) convertView.findViewById(R.id.userInfoBgIV);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.NameTextView);
        TextView locationTextView = (TextView) convertView.findViewById(R.id.locationTextView);

        nameTextView.setText(String.format("%s %s", model.getFirstName(), model.getLastName()));
        locationTextView.setText(model.getUserLocation());

        if (model.getCoverPic() != null && model.getUserPhoto() != null) {
            ImageHelper.setAlphaGrayScaleImage(getContext(), model.getCoverPic(), userInfoBgIV);
            Glide.with(context)
                    .load(model.getUserPhoto())
                    .fitCenter()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(userImageView);
        } else if (model.getUserPhoto() != null) {
            //Loading userPhoto from LinkedIn
            Glide.with(context)
                    .load(model.getUserPhoto())
                    .fitCenter()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(userImageView);
            userInfoBgIV.setImageResource(R.drawable.transparent_dark_gradient_shape);
        }
    }

    //methods to create all fragments
    private void createProfileViewFragment() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setModel(model);
        if (!profileFragment.isAdded()) {
            updateFragment(profileFragment, "User info");
        }
    }

    private void createBrowseCategoriesFragment() {
        if (browseCategories == null) {
            browseCategories = new BrowseCategories();
        }
        if (!browseCategories.isAdded()) {
            updateFragment(browseCategories, "Browse page");
        }
    }

    private void createManagePostApplyFragment() {
        if (manageJobFragment == null) {
            manageJobFragment = new ManageJobFragment((Activity) getContext());
        }
        manageJobFragment.createConfirmJobFragment();
    }

    private void createPreferences() {
        if (preferenceFragment == null) {
            preferenceFragment = new PreferenceFragment();
        }
        if (!preferenceFragment.isAdded()) {
            updateFragment(preferenceFragment, "Preference");
        }
    }

    private void createAppSettings() {
        if (appSettingFragment == null) {
            appSettingFragment = new AppSettingFragment();
        }
        if (!appSettingFragment.isAdded()) {
            updateFragment(appSettingFragment, "App Settings");
        }
    }

    private void resetHighLight() {
        for (View v : viewList) {
            TextView tv = (TextView) v.findViewById(R.id.drawer_itemName);
            tv.setTextColor(getContext().getResources().getColor(R.color.text_black));
            View view = v.findViewById(R.id.menuItemHighlight);
            ImageButton imageViewIcon = (ImageButton) v.findViewById(R.id.drawer_icon);
            imageViewIcon.setEnabled(true);
            view.setVisibility(View.GONE);
        }
    }

    //update other fragment and set title
    private void updateFragment(Fragment fragment, String title) {
        if (!fragment.isAdded()) {
            Activity activity = (Activity) context;
            activity.setTitle(title);

            if (activity.getFragmentManager().getBackStackEntryCount() > 0) {
                activity.getFragmentManager().popBackStack();
            }
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
        /*transaction.addToBackStack(null);*/
            transaction.replace(R.id.fragment_container, fragment, JiffyJobMainActivity.FRAG_CONTAINER_TAG);
            transaction.commitAllowingStateLoss();
        }
    }

    private List<View> viewList = new ArrayList<>();
    private ManageJobFragment manageJobFragment;
    private BrowseCategories browseCategories;
    private UpdateBasicInfo updateBasicInfo;
    private ProfileFragment profileFragment;
    private PreferenceFragment preferenceFragment;
    private AppSettingFragment appSettingFragment;
    private Context context;

    private UserInfoModel model;
}

