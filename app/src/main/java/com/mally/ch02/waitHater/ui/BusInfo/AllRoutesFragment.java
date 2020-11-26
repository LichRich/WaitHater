package com.mally.ch02.waitHater.ui.BusInfo;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mally.ch02.waitHater.R;
import com.mally.ch02.waitHater.ui.utils.ListItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AllRoutesFragment extends Fragment{

    /*
    *
    * TAG
    *
    * */
    private final String TAG = "ALL_ROUTES_FRAGMENT";
    /*
    *
    * Layout Components
    *
    * */
    private EditText et_routes;
    private RecyclerView rv_routes;
    /*
     *
     * RecyclerView Adapter
     *
     * */
    RoutesListAdapter adapter;
    /*
    *
    * Setting API url
    *
    * */
    private final String url_main = "http://openapitraffic.daejeon.go.kr/api/rest";
    private final String url_key = "?serviceKey=cC0rVYquPDL%2Bu44mxQ0ds5EabhA44uysOYBPVwBa0%2FeoGxSfKQgQCP4eCys0OB6VU6LUc9Ty2e%2BaBw7w61QB4g%3D%3D";
    private final String url_param = "&reqPage=";
    /*
     *
     * Handler for using Network
     *
     * */
    private static Handler mHandler;
    private static final int THREAD_ID = 10000;
    /*
    *
    * data for intent to RouteInfoActivity
    *
    * */
    private ArrayList<String> idList;
    private RoutesListAdapter.OnItemClickListener onItemClickListener;

    public AllRoutesFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View allRoutes = inflater.inflate(R.layout.fragment_all_routes, container, false);
        TrafficStats.setThreadStatsTag(THREAD_ID);

        et_routes = (EditText) allRoutes.findViewById(R.id.et_routes);
        rv_routes = (RecyclerView) allRoutes.findViewById(R.id.rv_routes);
        rv_routes.setLayoutManager(new LinearLayoutManager(getContext()));

        onItemClickListener = position -> {
            Intent intent = new Intent(getContext(), RouteInfoActivity.class);
            intent.putExtra("routeId", idList.get(position));
            startActivity(intent);
        };

        return allRoutes;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler =new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                ArrayList<ListItem> itemList = (ArrayList<ListItem>) msg.obj;
                adapter = new RoutesListAdapter(itemList, onItemClickListener);
                rv_routes.setAdapter(adapter);
            }
        };

        class AllRoutesThread extends Thread {

            final Handler h = mHandler;

            @Override
            public void run() {
                Message msg = h.obtainMessage();
                msg.obj = getInfoFromAPI(url_main, url_key, url_param);

                h.sendMessage(msg);
            }
        }

        AllRoutesThread art = new AllRoutesThread();
        Thread th = new Thread(art);
        th.start();

    }

    /*
     *
     * function: get information from API.
     *
     *  */
    private ArrayList<ListItem> getInfoFromAPI(String mUrl, String key, String param) {
        ArrayList<String> numbers = new ArrayList<>(), starts = new ArrayList<>(), turns = new ArrayList<>();
        idList = new ArrayList<>();
        HashMap<String, String> stations = new HashMap<>();
        ArrayList<ListItem> allRoutes = new ArrayList<>();

        /*
         * total size of all routes pages is 2
         * */
        String url_op1 = "/busRouteInfo/getRouteInfoAll";
        String url_allRoutes = mUrl + url_op1 + key + param;

        /*
         * total size of all stations pages is 3
         * */
        String url_op2 = "/busRouteInfo/getStaionByRouteAll";
        String url_allStations = mUrl + url_op2 + key + param;

        /*
         * builder to get information about route number from API
         * */
        for(int i = 1 ; i <= 2 ; i++){

            DocumentBuilderFactory ar_factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder ar_builder = null;

            try {
                ar_builder = ar_factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document ar_doc = null;
            try {
                ar_doc = ar_builder.parse(url_allRoutes+i);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }
            //parsing tag = itemList
            ar_doc.getDocumentElement().normalize();
            NodeList itemList1 = ar_doc.getElementsByTagName("itemList");

            //get routeNumber, dirId from XML
            for(int j = 0 ; j < itemList1.getLength() ; j++) {
                Node node = itemList1.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type;
                    switch (getTagValue("ROUTE_TP", element).trim()) {
                        case "1":
                            type = "급행";
                            break;
                        case "5":
                            type = "마을";
                            break;
                        case "6":
                            type = "첨단";
                            break;
                        default:
                            type = "";
                            break;
                    }
                    numbers.add(type + getTagValue("ROUTE_NO", element));
                    idList.add(getTagValue("ROUTE_CD", element));
                    starts.add(getTagValue("START_NODE_ID", element));
                    turns.add(getTagValue("TURN_NODE_ID", element));
                }
            }

        }

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
                as_doc = as_builder.parse(url_allStations+i);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }
            //parsing tag = itemList
            as_doc.getDocumentElement().normalize();
            NodeList itemList2 = as_doc.getElementsByTagName("itemList");

            //get stationId, stationName from XML
            for(int j = 0 ; j < itemList2.getLength() ; j++) {
                Node node = itemList2.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    stations.put(getTagValue("BUS_NODE_ID", element), getTagValue("BUSSTOP_NM", element));
                }
            }

        }

        for(int temp = 0 ; temp < numbers.size() ; temp++) {
            String name = stations.get(starts.get(temp)) + " ~ " + stations.get(turns.get(temp));
            ListItem item = new ListItem(numbers.get(temp), name, idList.get(temp));
            allRoutes.add(item);
        }
        Collections.sort(allRoutes, (item1, item2) -> item1.getNum().compareTo(item2.getNum()));

        return allRoutes;
    }

    private String getTagValue(String tag, Element e) {
        NodeList list = e.getElementsByTagName(tag).item(0).getChildNodes();
        Node v = (Node) list.item(0);
        if(v == null) return null;
        return v.getNodeValue();
    }

}