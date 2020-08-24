package be.singh.ramandeep.uberworks2;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mToggle;
    private RecyclerView recyclerView;
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

        Toolbar mToolbar = findViewById(R.id.nav_actionbar);
        setSupportActionBar(mToolbar);

        DrawerLayout mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(usersList);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

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
