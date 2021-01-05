package com.example.sbase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;


public class GetResultsActivity extends AppCompatActivity {

    void getLatestResultsAfterLogin(final String url_string) {

        new Thread()
        {
            public void run() {

                try {
                    NetworkUtility.accessNetworkResource(url_string, GetResultsActivity.this, new DataAccessCallbacks() {
                        @Override
                        public void onDataRequestCompleted(int request_code, Object[] results) {
                            if(results != null){
                                String target;

                                String[] result_string = (String[]) results;
                                target = result_string[0];
                                Bundle bundle = new Bundle();
                                bundle.putString("json_data", target);

                                Fragment fragment = new ShowResultsFragment();
                                fragment.setArguments(bundle);

                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.content_frame,fragment);
                                transaction.commit();

                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();

    }


    DrawerLayout drawer_layout;
    String username,password;
    int size;

    @Override
    public void onBackPressed()
    {
        return;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //getting name and list of exams passed to this activity
        Bundle data = getIntent().getExtras();

        String name = data.getString("name");
        username = data.getString("username");
        password = data.getString("password");

        ArrayList<String> Exams = data.getStringArrayList("exams");

        drawer_layout = findViewById(R.id.drawer_layout);
        NavigationView nav_view = findViewById(R.id.navigation);
        nav_view.setNavigationItemSelectedListener(Nav_Listener);


        //creating the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        setSupportActionBar(toolbar);
        new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.openDrawer,R.string.closeDrawer).syncState();



        Menu menu = nav_view.getMenu();
        //display students name
        name = name.toUpperCase();
        menu.add(0,1,0,name);

        size = Exams.size();
        for(int i = 0; i < size; i++) {
            String exam = Exams.get(i);
            menu.add(1,i+2,0,exam);
        }
        menu.add(2,size+2,0,"Graph").setIcon(R.drawable.ic_menu_show_chart);
        menu.add(3,size+3,0,"Change password").setIcon(R.drawable.ic_menu_security);
        menu.add(3,size+4,0,"close").setIcon(R.drawable.ic_menu_close);


        //get results latest results for server with with a unique exam value 'unique_id_di_euqinu'
        String url_string = "http://" + NetworkUtility.MY_IP_ADDRESS + "/android/android_results.php?username=" + username + "&password=" + password + "&exam=unique_id_di_euqinu";
        getLatestResultsAfterLogin(url_string);

    }

    NavigationView.OnNavigationItemSelectedListener Nav_Listener = new NavigationView.OnNavigationItemSelectedListener() {
        String target,url_string;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();

            if((id < size+2) && (id > 1)) {     //id is that of an exam

                String exam = menuItem.getTitle().toString();
                url_string = "http://" + NetworkUtility.MY_IP_ADDRESS + "/android/android_results.php?username=" + username + "&password=" + password + "&exam=" + exam;

                drawer_layout.closeDrawers();

                new Thread()
                {
                    public void run() {

                        try {
                            NetworkUtility.accessNetworkResource(url_string, GetResultsActivity.this, new DataAccessCallbacks() {
                                @Override
                                public void onDataRequestCompleted(int request_code, Object[] results) {
                                    if(results != null){
                                        String[] result_string = (String[]) results;
                                        target = result_string[0];
                                        Bundle bundle = new Bundle();
                                        bundle.putString("json_data", target);

                                        Fragment fragment = new ShowResultsFragment();
                                        fragment.setArguments(bundle);

                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content_frame,fragment);
                                        transaction.commit();

                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }}.start();


                return true;
            }


            //if id is that of graph
            if(id == size+2) {
                url_string = "http://" + NetworkUtility.MY_IP_ADDRESS + "/android/android_graph.php?username=" + username + "&password=" + password;

                drawer_layout.closeDrawers();

                new Thread()
                {
                    public void run() {

                        try {
                            NetworkUtility.accessNetworkResource(url_string, GetResultsActivity.this, new DataAccessCallbacks() {
                                @Override
                                public void onDataRequestCompleted(int request_code, Object[] results) {
                                    if(results != null){
                                        String[] result_string = (String[]) results;
                                        target = result_string[0];
                                        Bundle bundle = new Bundle();
                                        bundle.putString("json_data",target);


                                        Fragment fragment = new ShowGraphFragment();
                                        fragment.setArguments(bundle);

                                        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content_frame,fragment);
                                        transaction.commit();

                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }}.start();

                return true;
            }

            if(id == size+3) {
                //open the change password activity
                drawer_layout.closeDrawers();
                Intent intent = new Intent(getBaseContext(),ChangeCredentialsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",username);

                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
            if(id == size+4) {
                //close by loading Main Activity
                drawer_layout.closeDrawers();
                startActivity(new Intent(getBaseContext(),MainActivity.class));
                GetResultsActivity.this.finish();
                return true;
            }

            return false;
        }
    };
}
