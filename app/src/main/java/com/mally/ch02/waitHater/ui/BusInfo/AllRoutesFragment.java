package com.mally.ch02.waitHater.ui.BusInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mally.ch02.waitHater.R;
import com.mally.ch02.waitHater.ui.utils.ListItem;

public class AllRoutesFragment extends Fragment {

//    TAG
    private final String TAG = "ALL_ROUTES_FRAGMENT";

//    Layout Components
    private EditText et_routes;

//    API
    private final String url_main = "http://openapitraffic.daejeon.go.kr/api/rest";
    private final String api_busRouteInfo = "/busRouteInfo", api_stationInfo = "/stationinfo", api_busPosInfo = "/busposinfo", api_arrive = "/arrive";
    private final String[] route_operations = {"/getStaionByRoute", "/getStaionByRouteAll", "/getRouteInfo", "/getRouteInfoAll"};

    private ListItem bus;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_routes, container, false);



        return root;
    }
}