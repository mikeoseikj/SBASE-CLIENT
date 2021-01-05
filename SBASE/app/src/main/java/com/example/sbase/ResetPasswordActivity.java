package com.example.sbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText username_input, chint_input, pass1_input, pass2_input, hint1_input, hint2_input;
    String url_string;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset);

    username_input = findViewById(R.id.username);
    chint_input = findViewById(R.id.cur_hint);
    pass1_input = findViewById(R.id.password1);
    pass2_input = findViewById(R.id.password2);
    hint1_input = findViewById(R.id.hint1);
    hint2_input = findViewById(R.id.hint2);

    //recover button
    Button recover = findViewById(R.id.recover);
    recover.setOnClickListener(Recover_Listener);

}


View.OnClickListener Recover_Listener = new View.OnClickListener() {

    String msg = "", target = "";

    @Override
    public void onClick(View v) {

        String username,curr_hint,password1,password2,hint1,hint2;

        username = username_input.getText().toString();
        curr_hint = chint_input.getText().toString();
        password1 = pass1_input.getText().toString();
        password2 = pass2_input.getText().toString();
        hint1 = hint1_input.getText().toString();
        hint2 = hint2_input.getText().toString();

        //START DOING CHECKS
        if (username.length() != 12) {
            Toast.makeText(getBaseContext(), "username must be 12 characters long", Toast.LENGTH_LONG).show();
            return;
        }
        if (curr_hint.length() < 8) {
            Toast.makeText(getBaseContext(), "current hint length must be 8 or more characters", Toast.LENGTH_LONG).show();
            return;
        }

        //doing comparison between inputs
        if (password1.compareTo(password2) != 0) {
            Toast.makeText(getBaseContext(),"input passwords are not the same", Toast.LENGTH_LONG).show();
            return;
        }
        if (hint1.compareTo(hint2) != 0) {
            Toast.makeText(getBaseContext(),"input hints are not the same", Toast.LENGTH_LONG).show();
            return;
        }
        if(password1.length() < 8) {
            Toast.makeText(getBaseContext(),"password must be 8 or more characters", Toast.LENGTH_LONG).show();
            return;
        }
        if(hint1.length() < 8) {
            Toast.makeText(getBaseContext(),"new hint length must be 8 or more characters ", Toast.LENGTH_LONG).show();
            return;
        }

        //DONE DOING CHECKS

        url_string = "http://" + NetworkUtility.MY_IP_ADDRESS + "/android/android_recover.php?username=" + username + "&current_hint=" + curr_hint + "&password1=" + password1 + "&password2=" + password2 + "&hint1=" + hint1 + "&hint2=" + hint2;


        new Thread()
        {
            public void run() {

                try {
                    NetworkUtility.accessNetworkResource(url_string, ResetPasswordActivity.this, new DataAccessCallbacks() {
                        @Override
                        public void onDataRequestCompleted(int request_code, Object[] results) {
                            if(results != null){
                                String[] result_string = (String[]) results;
                                target = result_string[0];

                                try {

                                    JSONObject obj = new JSONObject(target);

                                    if(obj.getBoolean("success") == false) {
                                        msg = "failed";
                                    } else {
                                        msg = "";
                                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();

        if(msg.compareTo("failed") == 0)
            Toast.makeText(getBaseContext(),"Wrong credentials provided!", Toast.LENGTH_LONG).show();

    }
};


}
