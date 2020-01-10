package com.example.sbase;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    EditText username, password;
    Button login_btn, reset_btn;

    @Override
    public void onBackPressed()
    {
        MainActivity.this.finish();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        username = findViewById(R.id.username);
        password= findViewById(R.id.password);

        login_btn = findViewById(R.id.login);
        login_btn.setOnClickListener(LoginBtnListener);
        reset_btn = findViewById(R.id.reset);
        reset_btn.setOnClickListener(ResetBtnListener);


    }

    View.OnClickListener ResetBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(getBaseContext(), ResetPasswordActivity.class));
        }
    };


    View.OnClickListener LoginBtnListener = new View.OnClickListener() {
        String target = "";
        boolean FIRST_LOGIN, LOGIN_SUCCESS;
        String Name = "";
        ArrayList<String> Exams = new ArrayList<>();

        String check="failed";

        @Override
        public void onClick(View v) {
            if (username.getText().length() != 12) {
                Toast.makeText(getBaseContext(), "length of username must be 12", Toast.LENGTH_SHORT).show();
            } else if ((password.getText().length() < 8) || (password.getText().length() > 16)) {
                Toast.makeText(getBaseContext(), "length of password must range from 8 to 25 characters", Toast.LENGTH_SHORT).show();
            }


            //if password meets predefined requirements
            else {


                new Thread() {
                    public void run() {

                        String url_string = "http://192.168.43.127/android/android_login.php?username=" + username.getText().toString() + "&password=" + password.getText().toString();

                        try {

                            NetworkUtility.accessNetworkResource(url_string, MainActivity.this, new DataAccessCallbacks() {
                                @Override
                                public void onDataRequestCompleted(int request_code, Object[] results) {
                                    JSONObject obj;

                                    if(results!=null){
                                    String[] result_string = (String[]) results;
                                    target = result_string[0];

                                    try {
                                        obj = new JSONObject(target);
                                        FIRST_LOGIN = obj.getBoolean("first_login");

                                        if (FIRST_LOGIN)
                                        {
                                            Intent intent=new Intent(getBaseContext(),RegisterActivity.class);
                                            Bundle bundle=new Bundle();
                                            bundle.putString("username",username.getText().toString());
                                            bundle.putString("password",password.getText().toString());

                                            intent.putExtras(bundle);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            return;
                                        }

                                        LOGIN_SUCCESS = obj.getBoolean("success");
                                        if (LOGIN_SUCCESS)
                                        {

                                            Name = obj.getString("student_name");

                                            //getting list of exams
                                            JSONArray e_arr = obj.getJSONArray("exams");

                                            Exams.clear();
                                            for (int i = 0; i < e_arr.length(); i++) {
                                                Exams.add(e_arr.getString(i));

                                            }

                                            //passing dynamic data to result.java after successful login
                                            Intent intent = new Intent(getBaseContext(), GetResultsActivity.class);
                                            Bundle data = new Bundle();
                                            data.putString("name", Name);
                                            data.putString("username", username.getText().toString());
                                            data.putString("password", password.getText().toString());
                                            data.putStringArrayList("exams", Exams);

                                            intent.putExtras(data);
                                            startActivity(intent);
                                            check="";

                                            MainActivity.this.finish();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }}

                                }
                            });
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }.start();




            }//for else
             if (check.compareTo("failed")==0)
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();




        } //for onClick

    };


}
