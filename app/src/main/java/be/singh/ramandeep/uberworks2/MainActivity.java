package be.singh.ramandeep.uberworks2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mToggle;
    private RecyclerAdapter recyclerAdapter;

    List<String> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(){
          @Override
          public void run(){
              try {
                sleep(5000);
              }
              catch(Exception e){
                e.printStackTrace();
              }
              finally {
                  Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                  startActivity(welcomeIntent);
              }
          }
        };

        thread.start();

        //Setup toolbar and bind it to to layout file navigation_actionbar
        Toolbar mToolbar = findViewById(R.id.nav_actionbar);
        setSupportActionBar(mToolbar);

        //Adding the toggle hamburger menu as well as binding to layout file activity_customers_maps
        DrawerLayout mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //List of users in a recycle view when searching
        usersList = new ArrayList<>();
        usersList.add("Alpha");
        usersList.add("Beta");
        usersList.add("Charlie");
        usersList.add("Delta");
        usersList.add("Echo");
        usersList.add("Foxtrot");
        usersList.add("Golf");
        usersList.add("Hotel");
        usersList.add("India");
        usersList.add("Juliet");
        usersList.add("Kilo");
        usersList.add("Lima");
        usersList.add("Mark");
        usersList.add("Nano");
        usersList.add("Opera");
        usersList.add("Paul");

        //Binding recycle view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(usersList);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        //Adding bottom navigation to activity_customers_maps layout file
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()){
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
