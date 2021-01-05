package com.example.sbase;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;


public class ShowResultsFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_results, container, false);

        TableRow.LayoutParams t_params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.25f);

        String json_data, subject;
        double class_score, exam_score, total_score,total_marks = 0;
        int class_position = 0, form_position = 0;
        String form = "";

        json_data = getArguments().getString("json_data");

        try {
            //getting the 'default json data' received from server
            JSONObject obj = new JSONObject(json_data);
            JSONArray arr1 = obj.getJSONArray("default");
            JSONObject default_info = arr1.getJSONObject(0);
            form = default_info.getString("form");
            class_position = default_info.getInt("class_position");
            form_position = default_info.getInt("form_position");
            total_marks = default_info.getDouble("marks");
        }catch (Exception e) {
            e.printStackTrace();
        }

        /*
            THIS ROW IS USED FOR SHOWING DEFAULT INFO LIKE FORM AND POSITIONS
         */
        TextView s_form = new TextView(getActivity());
        s_form.setTextAppearance(getActivity(),R.style.defInfoAppearance);
        s_form.setText("form: " + form);
        s_form.setLayoutParams(t_params);


        TableRow d_info = new TableRow(getActivity());
        d_info.setBackgroundResource(android.R.color.holo_blue_bright);
        d_info.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        TextView c_pos = new TextView(getActivity());
        c_pos.setTextAppearance(getActivity(),R.style.defInfoAppearance);
        c_pos.setText("class rank: " + class_position);
        c_pos.setLayoutParams(t_params);

        TextView f_pos = new TextView(getActivity());
        f_pos.setTextAppearance(getActivity(),R.style.defInfoAppearance);
        f_pos.setText("form rank: " + form_position );
        f_pos.setLayoutParams(t_params);

        TextView t_marks = new TextView(getActivity());
        t_marks.setTextAppearance(getActivity(),R.style.defInfoAppearance);
        t_marks.setText("Total: " + total_marks );
        t_marks.setLayoutParams(t_params);

        d_info.addView(s_form);
        d_info.addView(c_pos);
        d_info.addView(f_pos);
        d_info.addView(t_marks);

        TableLayout table = view.findViewById(R.id.table);
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setStyle(Paint.Style.STROKE);
        border.getPaint().setColor(getResources().getColor(R.color.bannerColor));

        /*
            table headings for the results table with (4 cells)
         */

        TableRow th_row = new TableRow(getActivity());
        th_row.setBackgroundResource(R.color.bannerColor);
        th_row.setWeightSum((float) 1.0);
        th_row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView th = new TextView(getActivity());
        th.setText(R.string.hdr);
        th.setTextAppearance(getActivity(),R.style.tableHdrAppearance);
        th.setLayoutParams(t_params);
        th_row.addView(th);

        TextView th1 = new TextView(getActivity());
        th1.setText(R.string.hdr1);
        th1.setTextAppearance(getActivity(),R.style.tableHdrAppearance);
        th1.setLayoutParams(t_params);
        th_row.addView(th1);

        TextView th2 = new TextView(getActivity());
        th2.setText(R.string.hdr2);
        th2.setTextAppearance(getActivity(),R.style.tableHdrAppearance);
        th2.setLayoutParams(t_params);
        th_row.addView(th2);

        TextView th3 = new TextView(getActivity());
        th3.setText(R.string.hdr3);
        th3.setTextAppearance(getActivity(),R.style.tableHdrAppearance);
        th3.setLayoutParams(t_params);
        th_row.addView(th3);

        table.addView(d_info, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        table.addView(th_row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        try {

            JSONObject obj = new JSONObject(json_data);
            JSONArray arr = obj.getJSONArray("results");


            int id = 0;
            for (int i = 0; i < arr.length(); i++) {
                JSONObject results = arr.getJSONObject(i);

                subject = results.getString("subjectname");
                class_score = results.getDouble("classmarks");
                exam_score = results.getDouble("exammarks");
                total_score = results.getDouble("totalmarks");


                TableRow tr = new TableRow(getActivity());
                tr.setId(i);
                tr.setBackgroundResource(R.color.navBgColor);
                tr.setWeightSum((float) 1.0);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView txt = new TextView(getActivity());
                txt.setId(id);
                txt.setText(subject);
                txt.setTextAppearance(getActivity(),R.style.tableTextAppearance);
                txt.setLayoutParams(t_params);
                txt.setBackgroundDrawable(border);
                tr.addView(txt);

                TextView txt1 = new TextView(getActivity());
                txt1.setId(id + 1);
                txt1.setLayoutParams(t_params);
                txt1.setText(class_score + "");
                txt1.setTextAppearance(getActivity(),R.style.tableTextAppearance);
                txt1.setBackgroundDrawable(border);
                tr.addView(txt1);

                TextView txt2 = new TextView(getActivity());
                txt2.setId(id + 2);
                txt2.setLayoutParams(t_params);
                txt2.setText(exam_score + "");
                txt2.setTextAppearance(getActivity(),R.style.tableTextAppearance);
                txt2.setBackgroundDrawable(border);
                tr.addView(txt2);

                TextView txt3 = new TextView(getActivity());
                txt3.setId(id + 3);
                txt3.setLayoutParams(t_params);
                txt3.setText(total_score + "");
                txt3.setTextAppearance(getActivity(),R.style.tableTextAppearance);
                txt3.setBackgroundDrawable(border);
                tr.addView(txt3);

                table.addView(tr, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                id += 4;
            }
           }catch(Exception e){
                e.printStackTrace();
        }

        return view;

        }

    }
