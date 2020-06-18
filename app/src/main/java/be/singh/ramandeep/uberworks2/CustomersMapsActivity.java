package be.singh.ramandeep.uberworks2;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CustomersMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Making buttons for settings and logout
    private Button CustomerLogoutBtn, CustomerSettingsBtn, SearchForWorkersBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Boolean currentLogoutCustomerStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_maps);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Initialize the buttons
        CustomerLogoutBtn = findViewById(R.id.customer_logout_btn);
        CustomerSettingsBtn = findViewById(R.id.customer_settings_btn);
        SearchForWorkersBtn = findViewById(R.id.search_customer_btn);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //Create onclicklistener for logout
        CustomerLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLogoutCustomerStatus = true;
                DisconnectCustomer();

                mAuth.signOut();
                LogoutCustomer();
            }
        });

        //Calling onClickListener for search for workers button
        SearchForWorkersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchForWorkersBtn.setText("Searching For Available Workers");
                GetClosestWorker();
            }
        });
    }

    private void GetClosestWorker() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Belgium and move the camera
        LatLng belgium = new LatLng(50, 4);
        mMap.addMarker(new MarkerOptions().position(belgium).title("Marker in Belgium"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(belgium));
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Disconnecting user from the app
        if(!currentLogoutCustomerStatus){
            DisconnectCustomer();
        }
    }

    private void DisconnectCustomer() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference WorkerAvailabilityRef = FirebaseDatabase.getInstance().getReference();
    }

    private void LogoutCustomer() {
        Intent welcomeIntent = new Intent(CustomersMapsActivity.this, WelcomeActivity.class);
        welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(welcomeIntent);
        finish();
    }
}
