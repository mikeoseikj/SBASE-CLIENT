package com.example.sbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {

    String username,current_password;
    EditText pass1_input,pass2_input,hint1_input,hint2_input;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");
        current_password=bundle.getString("password");

        pass1_input=findViewById(R.id.password1);
        pass2_input=findViewById(R.id.password2);
        hint1_input=findViewById(R.id.hint1);
        hint2_input=findViewById(R.id.hint2);

        Button register=findViewById(R.id.register);
        register.setOnClickListener(Register_Listener);
    }

    View.OnClickListener Register_Listener=new View.OnClickListener()
    {

        String url_string;

        @Override
        public void onClick(View v)
        {
            String password1,password2,hint1,hint2;

            password1=pass1_input.getText().toString();
            password2=pass2_input.getText().toString();
            hint1=hint1_input.getText().toString();
            hint2=hint2_input.getText().toString();


            if(password1.compareTo(password2) !=0)
            {
                Toast.makeText(getBaseContext(),"passwords are not the same",Toast.LENGTH_SHORT).show();
                return;
            }

            if(hint1.compareTo(hint2) != 0)
            {
                Toast.makeText(getBaseContext(),"hints are not the same",Toast.LENGTH_SHORT).show();
                return;
            }

            if(password1.length() < 8 || password2.length() >25)
            {
                Toast.makeText(getBaseContext(),"password must range from 8 to 25 characters",Toast.LENGTH_SHORT).show();
                return;
            }

            if(hint1.length() <8 || hint1.length() > 50)
            {
                Toast.makeText(getBaseContext(),"hint must range from 8 to 50 characters",Toast.LENGTH_SHORT).show();
                return;
            }



            //sending url to set desired and 'permanent' password and hint
            url_string="http://192.168.43.127/android/android_flogin.php?username="+username+"&current_password="+current_password+"&password1="+password1+"&password2="+password2+"&hint1="+hint1+"&hint2="+hint2;

            new Thread()
            {
                public void run() {

                    try
                    {
                        NetworkUtility.accessNetworkResource(url_string, RegisterActivity.this, null);
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                        RegisterActivity.this.finish();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }}.start();


        }
    };
}
