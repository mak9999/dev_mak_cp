package com.mak.classportal.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mak.classportal.NewNoticeActivity;
import com.mak.classportal.R;
import com.mak.classportal.RootActivity;
import com.mak.classportal.TestsList;
import com.mak.classportal.adapter.NoticeListAd;
import com.mak.classportal.modales.NoticeData;
import com.mak.classportal.utilities.AppSingleTone;
import com.mak.classportal.utilities.ExecuteAPI;
import com.mak.classportal.utilities.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Konstantin on 22.12.2014.
 */
public class NoticeFragment extends Fragment {


    RecyclerView mRecyclerView;
    ArrayList<NoticeData> noticeData = new ArrayList<>();
    AppSingleTone appSingleTone;
    UserSession userSession;
    SharedPreferences sharedPreferences;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_notice_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.noticeList);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        if (!RootActivity.hasPermissionToCreate)
            fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewNoticeActivity.class));
                getActivity().overridePendingTransition(R.anim.leftside_in, R.anim.leftside_out);
            }
        });
        appSingleTone = new AppSingleTone(getContext());
        sharedPreferences =  getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userSession = new UserSession(sharedPreferences, sharedPreferences.edit());
        getNoticeList();

        return rootView;
    }
    void parseNoticeList(JSONObject jsonObject){
        try {
            noticeData.clear();
            Log.e("", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("notification_list");
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                NoticeData notice = new NoticeData();
                notice.setId(object.getString("notice_id"));
                notice.setTitle(object.getString("title"));
                notice.setMediaUrl(object.getString("media_url"));
                notice.setDescription(object.getString("description"));
                notice.setCreatedOn(object.getString("received_date"));
                notice.setCreatedBy(object.getString("send_by"));
                noticeData.add(notice);
            }
            NoticeListAd adapter1 = new NoticeListAd(getContext(), noticeData);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            mRecyclerView.setAdapter(adapter1);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void getNoticeList() {

        try {
            String url = null;
            if (userSession.isStudent())
                url = appSingleTone.studentNoticeList;
            else
                url = appSingleTone.teacherNoticeList;
            ExecuteAPI executeAPI = new ExecuteAPI(getContext(), url, null);
            executeAPI.addHeader("Token", userSession.getAttribute("auth_token"));
            if (userSession.isTeacher())
                executeAPI.addPostParam("teacher_id", userSession.getAttribute("user_id"));
            else {
                executeAPI.addPostParam("student_id", userSession.getAttribute("user_id"));
                executeAPI.addPostParam("class_id", userSession.getAttribute("class_id"));
                executeAPI.addPostParam("division_id", userSession.getAttribute("division_id"));
            }
            executeAPI.executeCallback(new ExecuteAPI.OnTaskCompleted() {
                @Override
                public void onResponse(JSONObject result) {
                    Log.d("Result", result.toString());
                    parseNoticeList(result);
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

