package be.singh.ramandeep.uberworks2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CustomerLoginRegisterActivity extends AppCompatActivity {

    //Making buttons
    private Button CustomerLoginBtn, CustomerRegisterBtn;
    private TextView CustomerRegisterLink, CustomerStatusLink;
    private EditText CustomerEmail, CustomerPw;
    private ProgressDialog loadingbar;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    private DatabaseReference CustomerDatabaseReference;
    private String CustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_register);

        //Initialize Firebase mAuth
        mAuth = FirebaseAuth.getInstance();

        //Calling findviewbyid, mapping the buttons on the layout with the made variables
        CustomerLoginBtn = findViewById(R.id.customer_login_button);
        CustomerRegisterBtn = findViewById(R.id.customer_register_button);
        CustomerRegisterLink = findViewById(R.id.customer_register_link);
        CustomerStatusLink = findViewById(R.id.customer_label);

        CustomerEmail = findViewById(R.id.customer_email);
        CustomerPw = findViewById(R.id.customer_password);

        loadingbar = new ProgressDialog(this);

        CustomerRegisterBtn.setVisibility(View.INVISIBLE);
        CustomerRegisterBtn.setEnabled(false);

        //Calling onClickListeners
        CustomerRegisterLink.setOnClickListener(new View.OnClickListener() {
            //Setting the register link and btn visible if register link is clicked
            @Override
            public void onClick(View view) {
                CustomerLoginBtn.setVisibility(View.INVISIBLE);
                CustomerRegisterLink.setVisibility(View.INVISIBLE);
                CustomerStatusLink.setText("Register Customer");

                CustomerRegisterBtn.setVisibility(View.VISIBLE);
                CustomerRegisterBtn.setEnabled(true);
            }
        });

        CustomerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = CustomerEmail.getText().toString();
                String pw = CustomerPw.getText().toString();

                RegisterCustomer(email, pw);
            }
        });

        CustomerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = CustomerEmail.getText().toString();
                String pw = CustomerPw.getText().toString();

                SignInCustomer(email, pw);
            }
        });
    }

    private void SignInCustomer(String email, String pw) {
        //Check if email field is empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please enter an Email...", Toast.LENGTH_LONG).show();
        }

        //Check if password field is empty
        if(TextUtils.isEmpty(pw)){
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please enter a Password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Customer Login");
            loadingbar.setMessage("Please wait while we are gathering your credentials");
            loadingbar.show();

            mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Logged In Successfully", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                        //When successful, let user go to main maps view
                        Intent customerIntent = new Intent(CustomerLoginRegisterActivity.this, CustomersMapsActivity.class);
                        startActivity(customerIntent);
                    }
                    else{
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Login Unsuccessful", Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });
        }
    }

    private void RegisterCustomer(String email, String pw) {
        //Check if email field is empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please enter an Email...", Toast.LENGTH_LONG).show();
        }

        //Check if password field is empty
        if(TextUtils.isEmpty(pw)){
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please enter a Password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Customer Registration");
            loadingbar.setMessage("Please wait while we are registering your data");
            loadingbar.show();

            mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        CustomerId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        CustomerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(CustomerId);
                        CustomerDatabaseReference.setValue(true);

                        Intent workerIntent = new Intent(CustomerLoginRegisterActivity.this, CustomersMapsActivity.class);
                        startActivity(workerIntent);

                        Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Registered Successfully", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                        //When successful, let user go to main maps view
                        Intent customerIntent = new Intent(CustomerLoginRegisterActivity.this, CustomersMapsActivity.class);
                        startActivity(customerIntent);
                    }
                    else{
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Registration Unsuccessful", Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });
        }
    }
}
