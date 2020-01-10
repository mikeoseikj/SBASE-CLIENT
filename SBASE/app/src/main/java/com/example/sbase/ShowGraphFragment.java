package com.example.sbase;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


public class ShowGraphFragment extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_show_graph, container, false);

        String json_data=getArguments().getString("json_data");

        PieChartView pie_chart=view.findViewById(R.id.chart);
        List<SliceValue> data=new ArrayList<>();


        try {
            int c=40;
            double total=0;
            int color;



            JSONObject obj = new JSONObject(json_data);
            JSONArray arr=obj.getJSONArray("graph");

            //sum of all total marks scored in all available exam
            for(int i=0;i<arr.length();i++)
            {
                JSONObject x = arr.getJSONObject(i);
                total =total + x.getDouble("marks");
            }

            for (int i=0;i<arr.length();i++)
            {
                JSONObject graph=arr.getJSONObject(i);
                double mark=graph.getDouble("marks");
                String exam=graph.getString("exam");

                color = Color.rgb(c, c, c);
                double percentage=Math.round((mark/total) * 100);

                data.add(new SliceValue((int)mark, color).setLabel(exam +" "+(int)percentage+ "%"));
                c += 10;
            }


        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        PieChartData chart_data=new PieChartData(data);

        chart_data.setHasLabels(true).setValueLabelBackgroundAuto(true);
        chart_data.setValueLabelTypeface(Typeface.MONOSPACE);
        chart_data.setHasCenterCircle(true).setCenterText1("TransGraph").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#99ffff"));
        chart_data.setCenterText1Typeface(Typeface.MONOSPACE);
        pie_chart.setPieChartData(chart_data);


        return view;
    }

}
