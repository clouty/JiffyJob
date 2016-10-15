package com.jiffyjob.nimblylabs.preferenceView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by NielPC on 7/30/2016.
 */
public class PreferenceFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preference, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        populateCountries();
        init();
        updateUI();
        Activity activity = getActivity();
        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();

        YoYo.with(Techniques.SlideInUp)
                .duration(Utilities.getAnimationFast())
                .playOn(getView());
    }

    private void init() {
        View view = getView();
        if (view != null) {
            startDayTV = (TextView) view.findViewById(R.id.startDayTV);
            startMonthTV = (TextView) view.findViewById(R.id.startMonthTV);
            startYearTV = (TextView) view.findViewById(R.id.startYearTV);
            endDayTV = (TextView) view.findViewById(R.id.endDayTV);
            endMonthTV = (TextView) view.findViewById(R.id.endMonthTV);
            endYearTV = (TextView) view.findViewById(R.id.endYearTV);
            countrySpinner = (Spinner) view.findViewById(R.id.countrySpinner);
            distanceRangeTV = (TextView) view.findViewById(R.id.distanceRangeTV);
            payoutRangeTV = (TextView) view.findViewById(R.id.payoutRangeTV);
            distanceRangeBar = (RangeBar) view.findViewById(R.id.distanceRangeBar);
            payoutRangeBar = (RangeBar) view.findViewById(R.id.payoutRangeBar);
            startDateRL = (RelativeLayout) view.findViewById(R.id.startDateRL);
            endDateRL = (RelativeLayout) view.findViewById(R.id.endDateRL);

            ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, countryList);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    String country = countryList.get(position);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TARGET_COUNTRY, country);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            distanceRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    String rangeStr = String.format("%skm-%skm", leftPinValue, rightPinValue);
                    distanceRangeTV.setText(rangeStr);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(DISTANCE_START, leftPinValue);
                    editor.putString(DISTANCE_END, rightPinValue);
                    editor.apply();
                }
            });

            payoutRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    String payoutStr = String.format("$%s-$%s /hr", leftPinValue, rightPinValue);
                    payoutRangeTV.setText(payoutStr);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PAYOUT_START, leftPinValue);
                    editor.putString(PAYOUT_END, rightPinValue);
                    editor.apply();
                }
            });

            //Configure date selection, Start date
            final DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        startCalender.set(Calendar.YEAR, year);
                        startCalender.set(Calendar.MONTH, monthOfYear);
                        startCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String startDateStr = dateFormat.format(startCalender.getTime());

                        //Get date from sharedPreference because get directly from UI might give incorrect date when app first start
                        Calendar defaultCalender = Calendar.getInstance();
                        String defaultDateStr = dateFormat.format(defaultCalender.getTime());
                        String endDateStr = sharedPreferences.getString(END_DATE, defaultDateStr);
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);
                        if (startDate.before(endDate)) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(START_DATE, startDateStr);
                            editor.apply();
                            updateUI();
                        } else {
                            Toast.makeText(getActivity(), "Start date cannot be later than end date.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };

            startDateRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(), startDatePicker,
                            startCalender.get(Calendar.YEAR),
                            startCalender.get(Calendar.MONTH),
                            startCalender.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            //Configure date selection, End date
            final DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        endCalender.set(Calendar.YEAR, year);
                        endCalender.set(Calendar.MONTH, monthOfYear);
                        endCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String endDateStr = dateFormat.format(endCalender.getTime());

                        //Get date from sharedPreference because get directly from UI might give incorrect date when app first start
                        Calendar defaultCalender = Calendar.getInstance();
                        String defaultDateStr = dateFormat.format(defaultCalender.getTime());
                        String startDateStr = sharedPreferences.getString(START_DATE, defaultDateStr);
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);
                        if (endDate.after(startDate)) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(END_DATE, endDateStr);
                            editor.apply();
                            updateUI();
                        } else {
                            Toast.makeText(getActivity(), "End date cannot be earlier than start date.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };

            endDateRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(), endDatePicker,
                            endCalender.get(Calendar.YEAR),
                            endCalender.get(Calendar.MONTH),
                            endCalender.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
    }

    private void updateUI() {
        try {
            String distanceStart = sharedPreferences.getString(DISTANCE_START, "0");
            String distanceEnd = sharedPreferences.getString(DISTANCE_END, "100");
            distanceRangeBar.setRangePinsByValue(Integer.parseInt(distanceStart), Integer.parseInt(distanceEnd));
            String rangeStr = String.format("%skm-%skm", distanceStart, distanceEnd);
            distanceRangeTV.setText(rangeStr);

            String payoutStart = sharedPreferences.getString(PAYOUT_START, "0");
            String payoutEnd = sharedPreferences.getString(PAYOUT_END, "100");
            payoutRangeBar.setRangePinsByValue(Integer.parseInt(payoutStart), Integer.parseInt(payoutEnd));
            String payoutStr = String.format("$%s-$%s /hr", payoutStart, payoutEnd);
            payoutRangeTV.setText(payoutStr);

            int countryIndex = 0;
            String country = sharedPreferences.getString(TARGET_COUNTRY, "");
            if (!TextUtils.isEmpty(country)) {
                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).equalsIgnoreCase(country)) {
                        countryIndex = i;
                    }
                }
                countrySpinner.setSelection(countryIndex);
            }

            SimpleDateFormat dfYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat dfMonth = new SimpleDateFormat("MMM", Locale.ENGLISH);
            SimpleDateFormat dfDay = new SimpleDateFormat("dd", Locale.ENGLISH);

            //Create default start date, try retrieve stored start date
            Calendar defaultCalender = Calendar.getInstance();
            String defaultDateStr = dateFormat.format(defaultCalender.getTime());
            String startDateStr = sharedPreferences.getString(START_DATE, defaultDateStr);
            Date startDate = dateFormat.parse(startDateStr);

            //Start date string
            String startYear = dfYear.format(startDate);
            String startMonth = dfMonth.format(startDate);
            String startDay = dfDay.format(startDate);

            startYearTV.setText(startYear);
            startMonthTV.setText(startMonth);
            startDayTV.setText(startDay);

            //Create default end date, try retrieve stored end date
            defaultCalender.set(Calendar.MONTH, defaultCalender.get(Calendar.MONTH) + 1);
            String defaultEndDateStr = dateFormat.format(defaultCalender.getTime());
            String endDateStr = sharedPreferences.getString(END_DATE, defaultEndDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            String endYear = dfYear.format(endDate);
            String endMonth = dfMonth.format(endDate);
            String endDay = dfDay.format(endDate);

            endYearTV.setText(endYear);
            endMonthTV.setText(endMonth);
            endDayTV.setText(endDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateCountries() {
        countryList.clear();
        countryList.add("Singapore");
        countryList.add("Japan");
        countryList.add("Malaysia");
        countryList.add("USA");
        countryList.add("Australia");
    }

    private Calendar startCalender = Calendar.getInstance();
    private Calendar endCalender = Calendar.getInstance();
    private SharedPreferences sharedPreferences;

    private List<String> countryList = new ArrayList<>();
    private TextView startDayTV, startMonthTV, startYearTV;
    private TextView endDayTV, endMonthTV, endYearTV;
    private Spinner countrySpinner;
    private TextView distanceRangeTV, payoutRangeTV;
    private RangeBar distanceRangeBar, payoutRangeBar;
    private RelativeLayout startDateRL, endDateRL;
    private static final String APP_PREFERENCE = "APP_PREFERENCE";
    private static final String TARGET_COUNTRY = "TARGET_COUNTRY";
    private static final String START_DATE = "START_DATE";
    private static final String END_DATE = "END_DATE";
    private static final String PAYOUT_START = "PAYOUT_START";
    private static final String PAYOUT_END = "PAYOUT_END";
    private static final String DISTANCE_START = "DISTANCE_START";
    private static final String DISTANCE_END = "DISTANCE_END";
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.ENGLISH);
}