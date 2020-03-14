package com.mak.classportal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mak.classportal.utilities.AppSingleTone;
import com.mak.classportal.utilities.ExecuteAPI;
import com.mak.classportal.utilities.UserSession;

import org.json.JSONException;
import org.json.JSONObject;


public class OtpActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    TextView enter_codetxt, attempterror;
    Button submitdata;
    RelativeLayout clinic_code_layout;
    String authtoken;
    AppSingleTone appSingleTone;
    CoordinatorLayout coordinatorLayout;
    String uuid, auth_token;
    String Otp = "";
    LayoutInflater inflater, toastInflater;
    View layout;
    TextView text;
    String error_message;
    UserSession userSession;
    SharedPreferences sharedPreferences;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinFiFthDigitEditText;
    private EditText mPinSixDigitEditText;
    private EditText mPinHiddenEditText;

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * Initialize EditText fields.
     */
    private void init() {
        mPinFirstDigitEditText = findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = findViewById(R.id.pin_forth_edittext);
        mPinFiFthDigitEditText = findViewById(R.id.pin_fifth_edittext);
        mPinSixDigitEditText = findViewById(R.id.pin_six_edittext);
        mPinHiddenEditText = findViewById(R.id.pin_hidden_edittext);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainLayout(this, null));

        enter_codetxt = findViewById(R.id.entercode_txt);
        attempterror = findViewById(R.id.attempterror);
        clinic_code_layout = findViewById(R.id.clinic_code_layout);
        submitdata = findViewById(R.id.submitdata);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        final SharedPreferences mPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userSession = new UserSession(sharedPreferences, sharedPreferences.edit());
        authtoken = mPrefs.getString("auth_token", null);
        appSingleTone = new AppSingleTone(this);
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout_file,
                findViewById(R.id.toast_layout_root));
        text = layout.findViewById(R.id.text);
        error_message = getResources().getString(R.string.error_message);

        submitdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        init();
        setPINListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            case R.id.pin_fifth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            case R.id.pin_six_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 6)
                            mPinSixDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 5)
                            mPinFiFthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));
                        mPinHiddenEditText.setSelection(mPinHiddenEditText.length(), mPinHiddenEditText.length());

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);
        setDefaultPinBackground(mPinFiFthDigitEditText);
        setDefaultPinBackground(mPinSixDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(mPinHiddenEditText.getText().toString().charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(mPinHiddenEditText.getText().toString().charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(mPinHiddenEditText.getText().toString().charAt(2) + "");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 4) {
            setFocusedPinBackground(mPinFiFthDigitEditText);
            mPinForthDigitEditText.setText(mPinHiddenEditText.getText().toString().charAt(3) + "");
            mPinFiFthDigitEditText.setText("");
        } else if (s.length() == 5) {
            setFocusedPinBackground(mPinSixDigitEditText);
            mPinFiFthDigitEditText.setText(mPinHiddenEditText.getText().toString().charAt(4) + "");
            mPinSixDigitEditText.setText("");

        } else if (s.length() == 6) {
            setDefaultPinBackground(mPinSixDigitEditText);
            mPinSixDigitEditText.setText(mPinHiddenEditText.getText().toString().charAt(5) + "");
            hideSoftKeyboard(mPinSixDigitEditText);

            if(!mPinHiddenEditText.getText().toString().equals(""))
                submitOtp(mPinHiddenEditText.getText().toString());
            else showToast("Please enter OTP");

            /*Intent intent = new Intent(OtpActivity.this, RootActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.leftside_in, R.anim.leftside_out);
            finish();



            */
        }
    }

    /**
     * Sets default PIN background.
     *
     * @param editText edit text to change
     */
    private void setDefaultPinBackground(EditText editText) {
        //setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_default_holo_light));
    }

    /**
     * Sets focused PIN field background.
     *
     * @param editText edit text to change
     */
    private void setFocusedPinBackground(EditText editText) {
        //  setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_focused_holo_light));
    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);
        mPinFiFthDigitEditText.setOnFocusChangeListener(this);
        mPinSixDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinFiFthDigitEditText.setOnKeyListener(this);
        mPinSixDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }

    /**
     * Sets background of the view.
     * This method varies in implementation depending on Android SDK version.
     *
     * @param view       View to which set background
     * @param background Background to set to view
     */
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;


        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);
    }

    /**
     * Custom LinearLayout with overridden onMeasure() method
     * for handling software keyboard show and hide events.
     */
    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_otp__code__enter_, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
    TextView customToast;
    View tostLayout;
    void showToast(String toastText){
        toastInflater = getLayoutInflater();
        tostLayout = toastInflater.inflate(R.layout.toast_layout_file,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        customToast = tostLayout.findViewById(R.id.text);
        Toast toast = new Toast(getApplicationContext());
        customToast.setText(toastText);
        customToast.setTypeface(ResourcesCompat.getFont(this, R.font.opensansregular));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(tostLayout);
        toast.show();
    }
    public static String MOBILE_NUMBER = "";
    public void submitOtp(String otpString) {

            try {
                String url = appSingleTone.submitOTP;

                ExecuteAPI executeAPI = new ExecuteAPI(this, url, null);
                executeAPI.addPostParam("mobile", MOBILE_NUMBER);
                executeAPI.addPostParam("otp", otpString);
                executeAPI.addPostParam("fierbase_token", otpString);
                executeAPI.executeCallback(new ExecuteAPI.OnTaskCompleted() {
                    @Override
                    public void onResponse(JSONObject result) {
                        Log.d("Result", result.toString());
                        try {
                            if (result.has("user_details")) {
                                showToast("Authenticated Successfully");
                                JSONObject object = result.getJSONArray("user_details").getJSONObject(0);
                                userSession.setAttribute("auth_token", object.getString("auth_code"));
                                userSession.setAttribute("user_id", object.getString("user_id"));
                                userSession.setAttribute("role_id", ""+object.getInt("role_id"));
                                userSession.setAttribute("org_id", ""+object.getInt("org_id"));
                                userSession.setAttribute("orgName", ""+object.getString("org_name"));
                                if (object.getInt("role_id") == 1)
                                    userSession.setAttribute("userRole", "Admin");
                                else if (object.getInt("role_id") == 2)
                                    userSession.setAttribute("userRole", "Teacher");
                                else {
                                    userSession.setAttribute("userRole", "Student");
                                    userSession.setAttribute("class_name", object.getString("class_name"));
                                    userSession.setAttribute("class_id", object.getString("class_id"));
                                    userSession.setAttribute("division", object.getString("division_name"));
                                    userSession.setAttribute("division_id", object.getString("division_id"));
                                }
                                userSession.setAttribute("name", object.getString("name"));
                                userSession.setAttribute("email", object.getString("email"));
                                userSession.setAttribute("mobile", object.getString("mobile"));

                                RootActivity.defaultMenu = 0;
                                Intent intent = new Intent(OtpActivity.this, RootActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.leftside_in, R.anim.leftside_out);
                                finish();
                            }else if (result.getInt("error_code") == 401){
                                showToast("Invalid OTP!");
                            }else {
                                showToast("Something went wrong, Please try again later");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError result, int mStatusCode, JSONObject errorResponse) {
                        Log.d("Result", errorResponse.toString());
                    }
                });
                executeAPI.showProcessBar(true);
                executeAPI.executeStringRequest(Request.Method.POST);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
