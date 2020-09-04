package com.example.sbase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeCredentialsActivity extends AppCompatActivity {

    String username,current_password,password1,password2,hint1,hint2;

    EditText curr_pass_input,pass1_input,pass2_input,hint1_input,hint2_input;
    String url_string,target,msg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecred);

        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");

        curr_pass_input=findViewById(R.id.cur_password);
        pass1_input=findViewById(R.id.password1);
        pass2_input=findViewById(R.id.password2);
        hint1_input=findViewById(R.id.hint1);
        hint2_input=findViewById(R.id.hint2);

        Button change=findViewById(R.id.change);
        change.setOnClickListener(Change_Listener);


    }


    View.OnClickListener Change_Listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            current_password=curr_pass_input.getText().toString();
            password1=pass1_input.getText().toString();
            password2=pass2_input.getText().toString();
            hint1=hint1_input.getText().toString();
            hint2=hint2_input.getText().toString();

            if(current_password.length() <8 )
            {
                Toast.makeText(getBaseContext(),"current password must be 8 or more characters",Toast.LENGTH_SHORT).show();
                return;
            }

            if(password1.compareTo(password2) !=0)
            {
                Toast.makeText(getBaseContext(),"provided passwords don't match",Toast.LENGTH_SHORT).show();
                return;
            }
            if(hint1.compareTo(hint2) != 0)
            {
                Toast.makeText(getBaseContext(),"hints don't match",Toast.LENGTH_SHORT).show();
                return;
            }

            if(password1.length() < 8 )
            {
                Toast.makeText(getBaseContext(),"new password must be 8 or more characters",Toast.LENGTH_SHORT).show();
                return;
            }
            if(hint1.length() < 8 )
            {
                Toast.makeText(getBaseContext(),"hint must be 8 or more characters",Toast.LENGTH_SHORT).show();
                return;
            }



            url_string="http://192.168.43.127/android/android_change.php?username="+username+"&current_password="+current_password+"&password1="+password1+"&password2="+password2+"&hint1="+hint1+"&hint2="+hint2;


            new Thread()
            {
                public void run() {

                    try
                    {
                        NetworkUtility.accessNetworkResource(url_string, ChangeCredentialsActivity.this,new DataAccessCallbacks() {
                            @Override
                            public void onDataRequestCompleted(int request_code, Object[] results) {
                                if (results != null)
                                {
                                    String[] result_string = (String[]) results;
                                    target = result_string[0];

                                    try
                                    {
                                        msg="";

                                        JSONObject obj=new JSONObject(target);
                                        if(obj.getBoolean("success")==true)
                                        {
                                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                                            ChangeCredentialsActivity.this.finish();
                                        }
                                        else
                                        {
                                            msg="failed";
                                        }
                                    }catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }


                                }
                            }
                    });

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }}.start();

            if(msg.compareTo("failed") ==0)
                Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_SHORT).show();

        }
    };


}
