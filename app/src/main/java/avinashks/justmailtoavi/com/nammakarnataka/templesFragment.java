package avinashks.justmailtoavi.com.nammakarnataka;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avinashks.justmailtoavi.com.nammakarnataka.adapter.temples_adapter;


public class templesFragment extends Fragment {


    private List<temples_adapter> temples_adapterList = new ArrayList<>();


    View view;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_temples, container , false);
        context = getActivity().getApplicationContext();
        temples_adapterList.add(new temples_adapter("image1","ganesh temple","very awesome","Bangalore",23.78,67.32));
        temples_adapterList.add(new temples_adapter("image2","Krishna temple","nice","Mysuru",23.78,67.32));
        temples_adapterList.add(new temples_adapter("image3","Shiva temple","peas","Bellary",23.78,67.32));
        temples_adapterList.add(new temples_adapter("image4","Parvthi temple","Cool","Singapore",23.78,67.32));
        temples_adapterList.add(new temples_adapter("image5","Godz temple","Marvellous","MKhundi",23.78,67.32));
        temples_adapterList.add(new temples_adapter("image6","Karthik temple","awesome","Tumkur",23.78,67.32));


        displayList();

        return view;
    }

    private void displayList() {
        ArrayAdapter<temples_adapter> adapter = new myTempleListAdapterClass();
        ListView list = (ListView)view.findViewById(R.id.templeList);
        list.setAdapter(adapter);
    }


    public class myTempleListAdapterClass extends ArrayAdapter<temples_adapter>{

        myTempleListAdapterClass() {
            super(context, R.layout.temples_item, temples_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                itemView = inflater.inflate(R.layout.temples_item, parent, false);

            }
            temples_adapter current = temples_adapterList.get(position);

            ImageView image = (ImageView)itemView.findViewById(R.id.item_templeImage);
            //Code to download image from url and paste.

            TextView t_name = (TextView)itemView.findViewById(R.id.item_templeTitle);
            t_name.setText(current.getTempleTitle());

            TextView t_dist = (TextView)itemView.findViewById(R.id.item_templeDistrict);
            t_dist.setText(current.getDistrict());

            return itemView;
        }


    }
}
