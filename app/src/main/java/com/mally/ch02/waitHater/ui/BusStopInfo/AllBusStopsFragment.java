package com.mally.ch02.waitHater.ui.BusStopInfo;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mally.ch02.waitHater.R;
import com.mally.ch02.waitHater.ui.BusInfo.RouteInfoActivity;
import com.mally.ch02.waitHater.ui.utils.ListItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AllBusStopsFragment extends Fragment {

    /*
     *
     * TAG
     *
     * */
    private final String TAG = "All Stations Fragment";
    /*
     *
     * Layout Components
     *
     * */
    private RecyclerView rv_allStations;
    /*
     *
     * RecyclerView Adapter
     *
     * */
    StationListAdapter adapter;
    /*
     *
     * Handler for using Network
     *
     * */
    private Handler mHandler;
    private final int THREAD_ID = 10000;
    /*
     *
     * Setting API url
     *
     * */
    private final String url_main = "http://openapitraffic.daejeon.go.kr/api/rest";
    private final String url_operation = "/busRouteInfo/getStaionByRouteAll";
    private final String url_key = "?serviceKey=cC0rVYquPDL%2Bu44mxQ0ds5EabhA44uysOYBPVwBa0%2FeoGxSfKQgQCP4eCys0OB6VU6LUc9Ty2e%2BaBw7w61QB4g%3D%3D";
    private final String url_param = "&reqPage=";
    /*
     *
     * data for intent to BusStopActivity
     *
     * */
    private ArrayList<String> idList;
    private ArrayList<ListItem> allStationList;
    private StationListAdapter.OnItemClickListener onItemClickListener;

    public AllBusStopsFragment() {};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View allStations = inflater.inflate(R.layout.fragment_all_bus_stops, container, false);
        TrafficStats.setThreadStatsTag(THREAD_ID);

        rv_allStations = (RecyclerView) allStations.findViewById(R.id.rv_busStop);
        rv_allStations.setLayoutManager(new LinearLayoutManager(getContext()));

        onItemClickListener = position -> {
            Intent intent = new Intent(getContext(), BusStopActivity.class);
            intent.putExtra("stationId", allStationList.get(position).getId());
            startActivity(intent);
        };

        return allStations;
    }

    @Override
    public void onStart() {
        super.onStart();

        mHandler =new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                ArrayList<ListItem> itemList = (ArrayList<ListItem>) msg.obj;
                adapter = new StationListAdapter(itemList, onItemClickListener);
                rv_allStations.setAdapter(adapter);
            }
        };

        class AllStationThread extends Thread {

            final Handler h = mHandler;

            @Override
            public void run() {
                Message msg = h.obtainMessage();
                msg.obj = getInfoFromAPI();

                h.sendMessage(msg);
            }
        }

        AllStationThread art = new AllStationThread();
        art.start();

    }

    /*
     *
     * function: get information from API.
     *
     *  */
    private ArrayList<ListItem> getInfoFromAPI() {
        ArrayList<String> station_name = new ArrayList<>(), station_id = new ArrayList<>();
        allStationList = new ArrayList<>();
        String url = url_main + url_operation + url_key + url_param;

        /*
         * builder to get information about station name from API
         * */
        for(int i = 1 ; i <= 3 ; i++) {

            DocumentBuilderFactory as_factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder as_builder = null;

            try {
                as_builder = as_factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document as_doc = null;
            try {
                as_doc = as_builder.parse(url + i);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }
            //parsing tag = itemList
            as_doc.getDocumentElement().normalize();
            NodeList itemList = as_doc.getElementsByTagName("itemList");

            //get stationId, stationName from XML
            for(int j = 0 ; j < itemList.getLength() ; j++) {
                Node node = itemList.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    station_name.add(getTagValue("BUSSTOP_NM", element));
                    station_id.add(getTagValue("BUS_NODE_ID", element));
                }
            }

            for(int index = 0 ; index < station_name.size() ; index++) {
                ListItem item = new ListItem(station_name.get(index), station_id.get(index));
                allStationList.add(item);
            }
        }

        return allStationList;
    }

    private String getTagValue(String tag, Element e) {
        NodeList list = e.getElementsByTagName(tag).item(0).getChildNodes();
        Node v = (Node) list.item(0);
        if(v == null) return null;
        return v.getNodeValue();
    }

}