package com.mak.classportal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mak.classportal.swap_plugin.SwipeStack;

import java.util.ArrayList;
import java.util.List;

public class RunTest extends AppCompatActivity implements SwipeStack.SwipeStackListener, View.OnClickListener {

    private Button mButtonLeft, mButtonRight;

    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    TextView customToast;
    LayoutInflater inflater;
    View tostLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_test);
        mSwipeStack = findViewById(R.id.swipeStack);
        mButtonLeft = findViewById(R.id.buttonSwipeLeft);
        mButtonRight = findViewById(R.id.buttonSwipeRight);

        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        fillWithTestData();
    }

    private void fillWithTestData() {
        for (int x = 1; x < 25; x++) {
            mData.add("Q"+x+". "+getString(R.string.dummy_text));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mButtonLeft)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mButtonRight)) {
            mSwipeStack.swipeTopViewToRight();
        } /*else if (v.equals(mFab)) {
            mData.add(getString(R.string.dummy_fab));
            mAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);
    }

    @Override
    public void onStackEmpty() {
        //Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RunTest.this, FinishTestActivity.class));
        overridePendingTransition(R.anim.leftside_in, R.anim.leftside_out);
        showToast("Test Completed...");
        finish();
    }
    void showToast(String toastText){
        inflater = getLayoutInflater();
        tostLayout = inflater.inflate(R.layout.toast_layout_file,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        customToast = tostLayout.findViewById(R.id.text);
        Toast toast = new Toast(getApplicationContext());
        customToast.setText(toastText);
        customToast.setTypeface(ResourcesCompat.getFont(this, R.font.opensansregular));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(tostLayout);
        toast.show();
    }
    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        View view;
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.question_card, parent, false);
            }
            TextView textViewCard = view.findViewById(R.id.textViewCard);
            textViewCard.setText(mData.get(position));
            final RadioButton one = view.findViewById(R.id.one);
            final RadioButton two = view.findViewById(R.id.two);
            final RadioButton three = view.findViewById(R.id.three);
            final RadioButton four = view.findViewById(R.id.four);
            RadioGroup optionGroup = view.findViewById(R.id.optionView);
            optionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.one:
                            one.setBackgroundResource(R.drawable.redio_selected);
                            two.setBackgroundResource(R.drawable.layout_border);
                            three.setBackgroundResource(R.drawable.layout_border);
                            four.setBackgroundResource(R.drawable.layout_border);
                            break;
                        case R.id.two:
                            one.setBackgroundResource(R.drawable.layout_border);
                            two.setBackgroundResource(R.drawable.redio_selected);
                            three.setBackgroundResource(R.drawable.layout_border);
                            four.setBackgroundResource(R.drawable.layout_border);
                            break;
                        case R.id.three:
                            one.setBackgroundResource(R.drawable.layout_border);
                            two.setBackgroundResource(R.drawable.layout_border);
                            three.setBackgroundResource(R.drawable.redio_selected);
                            four.setBackgroundResource(R.drawable.layout_border);
                            break;
                        case R.id.four:
                            one.setBackgroundResource(R.drawable.layout_border);
                            two.setBackgroundResource(R.drawable.layout_border);
                            three.setBackgroundResource(R.drawable.layout_border);
                            four.setBackgroundResource(R.drawable.redio_selected);
                            break;
                    }
                }
            });

            return view;
        }
    }
}
