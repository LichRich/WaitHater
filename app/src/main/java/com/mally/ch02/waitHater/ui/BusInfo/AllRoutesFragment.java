package com.mally.ch02.waitHater.ui.BusInfo;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.mally.ch02.waitHater.R;
import com.mally.ch02.waitHater.ui.utils.ListItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class AllRoutesFragment extends Fragment {

//    TAG
    private final String TAG = "ALL_ROUTES_FRAGMENT";

//    Layout Components
    private EditText et_routes;
    private RecyclerView rv_routes;

//    API
    private final String url_main = "http://openapitraffic.daejeon.go.kr/api/rest";
    private final String api_busRouteInfo = "/busRouteInfo", api_stationInfo = "/stationinfo", api_busPosInfo = "/busposinfo", api_arrive = "/arrive";
    private final String[] route_operations = {"/getStaionByRoute", "/getStaionByRouteAll", "/getRouteInfo", "/getRouteInfoAll"};
    private final String[] station_operations = {"/getStationById", "/getStationByUid"};
    private final String busPos_operation = "/getBusPosByRtid";
    private final String[] arrive_operations = {"/getArrInfoByStopID", "/getArrInfoByUid"};

//    API Key
    private final String key = "?serviceKey=cC0rVYquPDL%2Bu44mxQ0ds5EabhA44uysOYBPVwBa0%2FeoGxSfKQgQCP4eCys0OB6VU6LUc9Ty2e%2BaBw7w61QB4g%3D%3D";
//    뒤에 ID 값이 붙어야하는 operation의 경우, &busRouteId=30300001 과 같은 형식을 붙인다.

//    For Data
    private ListItem bus;
    private String[][] num_dir = new String[122][3];
    private boolean[] using_tags = new boolean[3];  //[0]=ROUTE_NO, [1]=TURN_STOP_ID, [2]=ROUTE_TP
    private String bus_num = null, bus_type = null, bus_dir = null;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_routes, container, false);

        StrictMode.enableDefaults();

        String getAllRoutes_1 = url_main + api_busRouteInfo + route_operations[3] + key + "&reqPage=1";
        try {
            URL url = new URL(getAllRoutes_1);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            Log.d(TAG, "onCreateView: Start Parsing...!!!");

            while(parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("ROUTE_NO")) using_tags[0] = true;
                        else if(parser.getName().equals("TURN_STOP_ID")) using_tags[1] = true;
                        else if(parser.getName().equals("ROUTE_TP")) using_tags[2] = true;
                        break;

                    case XmlPullParser.TEXT:
//                        if(using_tags[0])
                }

            }

        } catch (Exception e) {

        }

        return root;
    }
}