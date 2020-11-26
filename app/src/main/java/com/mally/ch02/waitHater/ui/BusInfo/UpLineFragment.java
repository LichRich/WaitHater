package com.mally.ch02.waitHater.ui.BusInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UpLineFragment extends Fragment {

    /*
     *
     * TAG
     *
     * */
    private final String TAG = "Up Line Fragment";
    /*
     *
     * Layout Components
     *
     * */
    private RecyclerView rv_up;
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
    //    url_operations[0] = 노선 위치(버스 위치 나열)
    //    url_operations[1] = 노선 정보(정류장 목록 나열)
    private final int num_posInfo = 0;
    private final int num_routeInfo = 1;
    private final String url_key = "?serviceKey=cC0rVYquPDL%2Bu44mxQ0ds5EabhA44uysOYBPVwBa0%2FeoGxSfKQgQCP4eCys0OB6VU6LUc9Ty2e%2BaBw7w61QB4g%3D%3D&busRouteId=";
    /*
     *
     * Get ROUTE_NO from intent.
     *
     * */
    private String busRouteId;

    private UpLineAdapter upLineAdapter;

    public UpLineFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View upLineLayout = inflater.inflate(R.layout.fragment_up_line, container, false);
        TrafficStats.setThreadStatsTag(THREAD_ID);

        Intent intent = RouteInfoActivity.getSRIntent();
        busRouteId = intent.getStringExtra("routeId");

        rv_up = (RecyclerView) upLineLayout.findViewById(R.id.recycler_upLine);
        rv_up.setLayoutManager(new LinearLayoutManager(getContext()));

        return upLineLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                ArrayList<ListItem> list = (ArrayList<ListItem>) msg.obj;
                upLineAdapter = new UpLineAdapter(list);
                rv_up.setAdapter(upLineAdapter);
            }
        };

        class UpThread implements Runnable {

            final Handler handler = mHandler;

            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.obj = getInfoFromAPI(busRouteId);

                handler.sendMessage(msg);
            }
        }

        UpThread ut = new UpThread();
        Thread thread = new Thread(ut);
        thread.start();
    }

    /*
     *
     * function: get information from API.
     *
     *  */
    private ArrayList<ListItem> getInfoFromAPI(String id) {
        ArrayList<Boolean> bus_isHere = new ArrayList<>();
        ArrayList<String> station_name = new ArrayList<>(), station_id = new ArrayList<>();

        String[] url_operations = {"/busposinfo/getBusPosByRtid", "/busRouteInfo/getStaionByRoute"};
        String url_busPos = url_main + url_operations[num_posInfo] + url_key + id;
        String url_station = url_main + url_operations[num_routeInfo] + url_key + id;

        /*
         * builders to get information from API
         * */
        DocumentBuilderFactory pos_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilderFactory rt_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder pos_builder = null;
        DocumentBuilder rt_builder = null;

        /*
         *
         * get Bus Position Information
         *
         * */
        try {
            pos_builder = pos_factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document pos_doc = null;
        try {
            pos_doc = pos_builder.parse(url_busPos);
        } catch(IOException | SAXException e) {
            e.printStackTrace();
        }
        //  parsing tag = itemList
        pos_doc.getDocumentElement().normalize();
        NodeList itemList1 = pos_doc.getElementsByTagName("itemList");

        //  get station id from XML to station_id
        for(int i = 0 ; i < itemList1.getLength() ; i++) {
            Node item = itemList1.item(i);
            if(item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;
                //  상행선 버스의 위치만 저장
                if(getTagValue("DIR", element).equals("0")) {
                    station_id.add(getTagValue("BUS_STOP_ID", element));
                }
            }
        }

        /*
         *
         * get Bus Route Information
         *
         * */
        try {
            rt_builder = rt_factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document rt_doc = null;
        try {
            rt_doc = rt_builder.parse(url_station);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        rt_doc.getDocumentElement().normalize();
        NodeList itemList2 = rt_doc.getElementsByTagName("itemList");

        //  get station name from XML to station_name and check bus_isHere
        int j = 0;
        Element element = null;
        do {
            Node item = itemList2.item(j);
            if(item.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) item;
                station_name.add(getTagValue("BUSSTOP_NM", element));
                bus_isHere.add(station_id.contains(getTagValue("BUS_STOP_ID", element)));
            }
            j++;
        } while(!getTagValue("BUSSTOP_TP", element).equals("2"));

        ArrayList<ListItem> res = new ArrayList<>();

        for(int index = 0 ; index < station_name.size() ; index++) {
            ListItem item = new ListItem(bus_isHere.get(index), station_name.get(index));
            res.add(item);
        }

        return res;
    }

    private String getTagValue(String tag, Element e) {
        NodeList list = e.getElementsByTagName(tag).item(0).getChildNodes();
        Node v = (Node) list.item(0);
        if(v == null) return null;
        return v.getNodeValue();
    }
}

class UpLineAdapter extends RecyclerView.Adapter<UpLineAdapter.ViewHolder> {

    private ArrayList<ListItem> busStops;

    public UpLineAdapter(ArrayList<ListItem> list) { busStops = list; }

    @NonNull
    @Override
    public UpLineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_bus_stop, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpLineAdapter.ViewHolder holder, int position) {
        String station_name = busStops.get(position).getName();
        holder.tv_stationName.setText(station_name);

        if(busStops.get(position).isBusIsHere()) {
            holder.tv_stationName.setBackgroundColor(Color.rgb(178, 204, 255));
        } else if(!busStops.get(position).isBusIsHere()) {
            holder.tv_stationName.setBackgroundColor(Color.rgb(255, 238, 212));
        }
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_stationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_stationName = itemView.findViewById(R.id.tv_stationName);
        }
    }
}
