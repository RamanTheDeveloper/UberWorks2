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

public class WorkerLoginRegisterActivity extends AppCompatActivity {

    //Making buttons
    private Button WorkerLoginBtn, WorkerRegisterBtn;
    private TextView workerRegisterLink, WorkerStatusLink;
    private EditText WorkerEmail, WorkerPw;
    private ProgressDialog loadingbar;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    private DatabaseReference WorkerDatabaseReference;
    private String WorkerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login_register);

        //Initialize Firebase mAuth
        mAuth = FirebaseAuth.getInstance();

        //Calling findviewbyid, mapping the buttons on the layout with the made buttons variables
        WorkerLoginBtn = findViewById(R.id.worker_login_button);
        WorkerRegisterBtn = findViewById(R.id.worker_register_button);
        workerRegisterLink = findViewById(R.id.worker_register_link);
        WorkerStatusLink = findViewById(R.id.worker_label);
        WorkerEmail = findViewById(R.id.worker_email);
        WorkerPw = findViewById(R.id.worker_password);

        loadingbar = new ProgressDialog(this);

        WorkerRegisterBtn.setVisibility(View.INVISIBLE);
        WorkerRegisterBtn.setEnabled(false);

        //Calling onClickListeners
        workerRegisterLink.setOnClickListener(new View.OnClickListener() {
            //Setting the register link and btn visible if register link is clicked
            @Override
            public void onClick(View view) {
                WorkerLoginBtn.setVisibility(View.INVISIBLE);
                workerRegisterLink.setVisibility(View.INVISIBLE);
                WorkerStatusLink.setText("Register Worker");

                WorkerRegisterBtn.setVisibility(View.VISIBLE);
                WorkerRegisterBtn.setEnabled(true);
            }
        });

        WorkerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = WorkerEmail.getText().toString();
                String pw = WorkerPw.getText().toString();

                RegisterWorker(email, pw);
            }
        });

        WorkerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = WorkerEmail.getText().toString();
                String pw = WorkerPw.getText().toString();

                SignInWorker(email, pw);
            }
        });
    }

    private void SignInWorker(String email, String pw) {
        //Check if email field is empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(WorkerLoginRegisterActivity.this, "Please enter an Email...", Toast.LENGTH_LONG).show();
        }

        //Check if password field is empty
        if(TextUtils.isEmpty(pw)){
            Toast.makeText(WorkerLoginRegisterActivity.this, "Please enter a Password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Worker Login");
            loadingbar.setMessage("Please wait while we are gathering your credentials");
            loadingbar.show();

            mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(WorkerLoginRegisterActivity.this, "Worker Logged in Successfully", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                        //When successful, let user go to main maps view
                        Intent workerIntent = new Intent(WorkerLoginRegisterActivity.this, WorkersMapsActivity.class);
                        startActivity(workerIntent);
                    }
                    else{
                        Toast.makeText(WorkerLoginRegisterActivity.this, "Login Unsuccessful", Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });
        }
    }

    private void RegisterWorker(String email, String pw) {
        //Check if email field is empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(WorkerLoginRegisterActivity.this, "Please enter an Email...", Toast.LENGTH_LONG).show();
        }

        //Check if password field is empty
        if(TextUtils.isEmpty(pw)){
            Toast.makeText(WorkerLoginRegisterActivity.this, "Please enter a Password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Worker Registration");
            loadingbar.setMessage("Please wait while we are registering your data");
            loadingbar.show();

            mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        WorkerId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        WorkerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(WorkerId);
                        WorkerDatabaseReference.setValue(true);

                        Intent customerIntent = new Intent(WorkerLoginRegisterActivity.this, WorkersMapsActivity.class);
                        startActivity(customerIntent);

                        Toast.makeText(WorkerLoginRegisterActivity.this, "Worker Registered Successfully", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                        //When successful, let user go to main maps view
                        Intent workerIntent = new Intent(WorkerLoginRegisterActivity.this, WorkersMapsActivity.class);
                        startActivity(workerIntent);
                    }
                    else{
                        Toast.makeText(WorkerLoginRegisterActivity.this, "Registration Unsuccessful", Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });
        }
    }
}
