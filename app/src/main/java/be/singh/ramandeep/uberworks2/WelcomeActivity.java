package be.singh.ramandeep.uberworks2;
/**
 * This class is the Welcome class and will display Uber logo for 5 seconds and then move on to next activity
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    //Call buttons on welcome page
    private Button welcomeWorkerBtn, welcomeCustomerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Initialize both buttons
        welcomeCustomerBtn = findViewById(R.id.welcome_customer_btn);
        welcomeWorkerBtn = findViewById(R.id.welcome_worker_btn);

        //Call in onClickListener to navigate from Welcome page to Customer Login page
        welcomeCustomerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent LoginRegisterCustomerIntent = new Intent(WelcomeActivity.this, CustomerLoginRegisterActivity.class);
                startActivity(LoginRegisterCustomerIntent);
            }
        });

        //Call in onClickListener to navigate from Welcome page to Worker Login page
        welcomeWorkerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent LoginRegisterWorkerIntent = new Intent(WelcomeActivity.this, WorkerLoginRegisterActivity.class);
                startActivity(LoginRegisterWorkerIntent);
            }
        });
    }
}
