package be.singh.ramandeep.uberworks2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class WorkersMapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    //Making buttons for settings and logout
    private Button WorkerLogoutBtn, WorkerSettingsBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Boolean currentLogoutWorkerStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_maps);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Initialize the buttons
        WorkerLogoutBtn = findViewById(R.id.worker_logout_btn);
        WorkerSettingsBtn = findViewById(R.id.worker_settings_btn);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //Create onclicklistener for logout
        WorkerLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLogoutWorkerStatus = true;
                DisconnectWorker();

                mAuth.signOut();
                LogoutWorker();
            }
        });
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
        if(!currentLogoutWorkerStatus){
            DisconnectWorker();
        }
    }

    private void DisconnectWorker() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference DriverAvailabilityRef = FirebaseDatabase.getInstance().getReference();
    }

    private void LogoutWorker() {
        Intent welcomeIntent = new Intent(WorkersMapsActivity.this, WelcomeActivity.class);
        welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(welcomeIntent);
        finish();
    }
}
