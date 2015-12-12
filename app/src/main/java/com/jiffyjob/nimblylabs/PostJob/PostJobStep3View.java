package com.jiffyjob.nimblylabs.postJob;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobStep3Event;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobStep4Event;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 26/3/2015.
 */
public class PostJobStep3View extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_step3, container, false);
        context = view.getContext();
        geocoder = new Geocoder(this.context, Locale.ENGLISH);
        init();
        initEvent();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        YoYo.with(Techniques.SlideInLeft)
                .duration(Utilities.getAnimationNormal())
                .playOn(view.findViewById(R.id.mainLayout));
        updateUI();
    }

    @Override
    public void onStop() {
        updatePostJobModel();
        super.onStop();
    }

    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
        currentLocale = getResources().getConfiguration().locale;
        payoutSwitch = (Switch) view.findViewById(R.id.payoutSwitch);
        payoutSwitchTV = (TextView) view.findViewById(R.id.payoutSwitchTV);
        autocomplete_location = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_location);
        searchLocationBtn = (ImageButton) view.findViewById(R.id.searchBtn);
        startDateTime = (EditText) view.findViewById(R.id.startDateTime);
        endDateTime = (EditText) view.findViewById(R.id.endDateTime);
        payoutet = (EditText) view.findViewById(R.id.payoutet);
        boostPostcb = (CheckBox) view.findViewById(R.id.boostPostcb);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        readMoreTV = (TextView) view.findViewById(R.id.readMoreTV);
    }

    private void initEvent() {
        autocomplete_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAddress = addressList.get(position);
                //TODO: change date time between US and rest of countries
            }
        });

        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationStr = autocomplete_location.getText().toString();
                doLocationSearch(locationStr);
            }
        });

        startDateTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isEditDateTimeOpen) {
                    startDateTimePickerInit();
                }
                return true;
            }
        });

        endDateTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isEditDateTimeOpen) {
                    endDateTimePickerInit();
                }
                return true;
            }
        });

        readMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        boostPostcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                postJobModel.setIsBoostPost(boostPostcb.isChecked());
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePostJobModel();
                postStickyEvent();
                proceedToNextFragment();
            }
        });

        payoutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    payoutSwitchTV.setText("Hour");
                } else {
                    payoutSwitchTV.setText("Day");
                }
            }
        });
    }

    private void startDateTimePickerInit() {
        isEditDateTimeOpen = true;
        final Calendar myCalendar = Calendar.getInstance(currentLocale);
        final DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateStr = dateFormat.format(myCalendar.getTime());
                startDateTime.setText(dateStr);
            }
        };

        final TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                String dateStr = dateFormat.format(myCalendar.getTime());
                startDateTime.setText(dateStr);
            }
        };

        startTimePickerDialog = new TimePickerDialog(context, startTimeSetListener, 10, 00, false);
        startTimePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isEditDateTimeOpen = false;
            }
        });
        startDatePickerDialog = new DatePickerDialog(context, startDateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        startDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startTimePickerDialog.show();
            }
        });
        startDatePickerDialog.show();
    }

    private void endDateTimePickerInit() {
        isEditDateTimeOpen = true;
        final Calendar myCalendar = Calendar.getInstance(currentLocale);
        final DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateStr = dateFormat.format(myCalendar.getTime());
                endDateTime.setText(dateStr);
            }
        };

        final TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                String dateStr = dateFormat.format(myCalendar.getTime());
                endDateTime.setText(dateStr);
            }
        };

        endTimePickerDialog = new TimePickerDialog(context, endTimeSetListener, 10, 00, false);
        endTimePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isEditDateTimeOpen = false;
            }
        });
        endDatePickerDialog = new DatePickerDialog(context, endDateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                endTimePickerDialog.show();
            }
        });
        endDatePickerDialog.show();
    }

    private void doLocationSearch(String query) {
        try {
            addressList = geocoder.getFromLocationName(query, maxResults);
            if (addressList == null || addressList.isEmpty()) {
                Toast.makeText(context, "Location not found.", Toast.LENGTH_SHORT).show();
            } else {
                locationNameList.clear();
                for (Address address : addressList) {
                    String addressStr = "";
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressStr += address.getAddressLine(i);
                        if (i < address.getMaxAddressLineIndex() - 1) {
                            addressStr += ", ";
                        }
                    }
                    locationNameList.add(addressStr);
                }
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, locationNameList);
                autocomplete_location.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                autocomplete_location.setSelected(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Use when user press back and update UI to current postJobModel variables
    private void updateUI() {
        Address address = postJobModel.getAddress();
        if (address != null) {
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                autocomplete_location.setText(address.getAddressLine(i) + "\n");
            }
        }

        if (postJobModel.getStartTime() != null) {
            Date startTime = postJobModel.getStartTime().getTime();
            startDateTime.setText(dateFormat.format(startTime));
        }

        if (postJobModel.getEndTime() != null) {
            Date endTime = postJobModel.getEndTime().getTime();
            endDateTime.setText(dateFormat.format(endTime));
        }

        double payout = postJobModel.getPayout();
        if (payout != 0) {
            payoutet.setText("" + payout);
        }

        if (postJobModel.isBoostPost()) {
            boostPostcb.setChecked(true);
        } else {
            boostPostcb.setChecked(false);
        }
    }

    private void updatePostJobModel() {
        try {
            if (selectedAddress != null) {
                postJobModel.setAddress(selectedAddress);
                postJobModel.setSalaryCurrencyCode(selectedAddress.getCountryCode());
            }

            if (!startDateTime.getText().toString().isEmpty()) {
                Calendar startCalendar = Calendar.getInstance(currentLocale);
                Date startTime = dateFormat.parse(startDateTime.getText().toString());
                startCalendar.setTime(startTime);
                postJobModel.setStartTime(startCalendar);
            }

            if (!endDateTime.getText().toString().isEmpty()) {
                Calendar endCalendar = Calendar.getInstance(currentLocale);
                Date endTime = dateFormat.parse(endDateTime.getText().toString());
                endCalendar.setTime(endTime);
                postJobModel.setEndTime(endCalendar);
            }

            if (!payoutet.getText().toString().isEmpty()) {
                double payout = Double.parseDouble(payoutet.getText().toString());
                postJobModel.setPayout(payout);
            }

            postJobModel.setSalaryTypeDaily(payoutSwitch.isChecked());

            postJobModel.setIsBoostPost(boostPostcb.isChecked());
        } catch (ParseException e) {
            Toast.makeText(context, "Error saving information", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void postStickyEvent() {
        PostJobStep4Event postJobStep4Event = new PostJobStep4Event(postJobModel);
        EventBus.getDefault().postSticky(postJobStep4Event);
    }

    private void proceedToNextFragment() {
        PostJobStep4View postJobStep4View = new PostJobStep4View();
        FragmentManager manager = this.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.postJobStepView, postJobStep4View, PostJobStep4View.class.getSimpleName());
        transaction.addToBackStack(PostJobStep3View.class.getSimpleName());
        transaction.commit();
    }

    //All event handlers are written here
    public void onEvent(PostJobStep3Event eventModel) {
        postJobModel = eventModel.getPostJobModel();
    }

    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    private final SimpleDateFormat dmyDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    private final SimpleDateFormat mdyDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    private boolean isEditDateTimeOpen = false;
    private Locale currentLocale;
    private List<Address> addressList = new ArrayList<>();
    private Address selectedAddress = null;
    private Switch payoutSwitch;
    private TextView payoutSwitchTV;
    private TextView readMoreTV;
    private Button submitBtn;
    private CheckBox boostPostcb;
    private ImageButton searchLocationBtn;
    private EditText startDateTime, endDateTime, payoutet;
    private PostJobModel postJobModel;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autocomplete_location;
    private View view;
    private Context context;
    private Geocoder geocoder;
    private final static int maxResults = 5;
    private List<String> locationNameList = new ArrayList<String>();
}
