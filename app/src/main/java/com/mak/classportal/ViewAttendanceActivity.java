package com.mak.classportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mak.classportal.adapter.OrgAppointmentAd;
import com.mak.classportal.modales.DaysData;
import com.mak.classportal.modales.StudentData;
import com.mak.classportal.utilities.AppSingleTone;
import com.mak.classportal.utilities.UserSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class ViewAttendanceActivity extends AppCompatActivity {

    int month, day;
    int Position;
    Vector<View> pages;
    AppSingleTone appSingleTone;
    UserSession userSession;
    SharedPreferences sharedPreferences;
    ViewPagerAdapter adapter;
    CustomPagerAdapter myViewPagerAdapter;
    CoordinatorLayout coordinatorLayout;
    AppoitmentAdapter mAdapter;
    String currentdate;
    SimpleDateFormat dfdate;
    TextView text;
    View layout;
    Calendar c;
    Typeface opensanssemibold;
    String error_message, auth_token, uid;
    LayoutInflater inflater1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    public static ArrayList<DaysData> appointmentList = new ArrayList<>();
    Date currentDate;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ItemTouchHelper mItemTouchHelper;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    public static void sortByKey() {


        // Display the TreeMap which is naturally sorted
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        appSingleTone = new AppSingleTone(this);
        opensanssemibold = ResourcesCompat.getFont(this, R.font.opensanssemibold);
        inflater1 = getLayoutInflater();
        layout = inflater1.inflate(R.layout.toast_layout_file,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        text = layout.findViewById(R.id.text);
        error_message = "";
        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userSession = new UserSession(sharedPreferences, sharedPreferences.edit());
        auth_token = userSession.getAttribute("auth_token");
        uid = userSession.getAttribute("uid");
        currentDate = new Date();
//        GlobleValues.appointmentDate = sdf.format(currentDate); //2016/11/16 12:08:43
        layout = getLayoutInflater().inflate(R.layout.toast_layout_file,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        text = layout.findViewById(R.id.text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Position = viewPager.getCurrentItem();
                Log.e("po", "po" + Position);
                //GlobleValues.timeslot= GlobleValues.childDatas.get(viewPager.getCurrentItem()).Title;

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


       initiliseRecylerView();

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < appointmentList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // adapter.addFragment(new AppointmentsActivity(), Globle_Values.special_child_information.get(i).Title);
            View itemView = inflater.inflate(R.layout.activity_notice_list, null);
            RecyclerView listview = itemView.findViewById(R.id.noticeList);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(RecyclerView.VERTICAL);
            final OrgAppointmentAd  mAdapter = new OrgAppointmentAd(ViewAttendanceActivity.this, appointmentList.get(i).getAppointmentData(),  userSession, 0);
            listview.setAdapter(mAdapter);
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            listview.setLayoutManager(mStaggeredLayoutManager);
            listview.setLayoutManager(new LinearLayoutManager(this));
            listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy < 0) {
                        mAdapter.notifyDataSetChanged();
                    }
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
            pages.add(itemView);


        }

/*
 for (int i = 0; i < GlobleValues.AppointmentData.size(); i++) {
            appointmentList=GlobleValues.AppointmentData.get(i);
        }
*/


        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        tabLayout.removeAllTabs();

        for (int i1 = 0; i1 < appointmentList.size(); i1++) {

            Date date = null;
            try {
                date = sdf.parse(appointmentList.get(i1).getDay());


            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date);
            int dayInt = cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("MMM");
            String month = sdf1.format(date);
            tabLayout.addTab(tabLayout.newTab().setText(month + "\n" + "" + dayInt));
            tabLayout.setTag(tabLayout.newTab().setText(month + "\n" + "" + dayInt));


            cal.setTime(currentDate);
            int dayInt1 = cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf11 = new SimpleDateFormat("MMM");
            String month1 = sdf11.format(currentDate);
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_text_layout, null);
            tv.setTypeface(opensanssemibold);
            if (tabLayout.getTabAt(i1).getText().toString().equalsIgnoreCase(month1 + "\n" + "" + dayInt1)) {
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tv.setTextColor(getResources().getColor(R.color.ivory));
            }
            tabLayout.getTabAt(i1).setCustomView(tv);

        }

    }

    void initiliseRecylerView() {


        pages = new Vector<View>();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        myViewPagerAdapter = new CustomPagerAdapter(this, pages);
        viewPager.setAdapter(myViewPagerAdapter);
        setupTabIcons();
        viewPager.setCurrentItem(1);

    }


    @Override
    protected void onResume() {

        super.onResume();

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private Vector<View> pages;

        public CustomPagerAdapter(Context context, Vector<View> pages) {
            this.mContext = context;
            this.pages = pages;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = pages.get(position);
            container.addView(page);
            return page;
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public class AppoitmentAdapter extends RecyclerView
            .Adapter<AppoitmentAdapter
            .DataObjectHolder> {


        StudentData d1;
        private String LOG_TAG = "MyRecyclerViewAdapter";
        private ArrayList<StudentData> mDataset;
        private RecyclerView recyclerView;
        private Context context;
        private UserSession userSession;

        public AppoitmentAdapter(Context context, ArrayList<StudentData> myDataset, RecyclerView recyclerView, UserSession userSession) {
            mDataset = myDataset;
            this.recyclerView = recyclerView;
            this.context = context;
            this.userSession = userSession;
        }


        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.attendance_row, parent, false);

            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            return dataObjectHolder;
        }

        @Override
        public void onBindViewHolder(final DataObjectHolder holder, int position) {


            holder.appointmenttime.setText("20:00");
            holder.label.setText("Mak");
            holder.bookingIdTxt.setText("Booking Id: ");

           /* holder.label.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                       mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });*/
        }

        public void addItem(StudentData dataObj, int index) {
            mDataset.add(index, dataObj);
            notifyItemInserted(index);
        }

        public void deleteItem(int index) {
            mDataset.remove(index);
            mAdapter.notifyItemRemoved(index);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public StudentData getItem(int i) {
            return mDataset.get(i);
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder
                implements View
                .OnClickListener {
            TextView label, bookingIdTxt;
            TextView status;
            TextView appointmenttime;
            CardView cardView;
            RelativeLayout in_outRelativeLayout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                label = itemView.findViewById(R.id.label_txt);
                bookingIdTxt = itemView.findViewById(R.id.booking_id);
                status = itemView.findViewById(R.id.statusText);
                appointmenttime = itemView.findViewById(R.id.appointmenttime);
                cardView = itemView.findViewById(R.id.card_view);
                in_outRelativeLayout = itemView.findViewById(R.id.in_outLayout);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
//                GlobleValues.Positionnew = Position;

            }

        }

    }

}
