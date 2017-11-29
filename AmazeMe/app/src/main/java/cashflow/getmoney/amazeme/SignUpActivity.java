package cashflow.getmoney.amazeme;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    EditText newUsername;
    EditText newPassword;
    EditText newPasswordConfirm;
    Button newSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        newUsername = (EditText) findViewById(R.id.newUsername);
        newPassword = (EditText) findViewById(R.id.newPassword);
        newPasswordConfirm = (EditText) findViewById(R.id.newPasswordConfirm);
        newSignup = (Button) findViewById(R.id.newSignup);

        newSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user = newUsername.getText().toString();
                final DatabaseReference currUserRef = usersRef.child(user);


                currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Display a dialog telling user about incorrect credentials
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

                        if(dataSnapshot.exists()) {
                            builder.setMessage("The username is already taken. Please try another")
                            .setTitle("Username taken");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            String pass = newPassword.getText().toString();

                            if(newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())) {
                                currUserRef.child("pass").setValue(pass);

                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                intent.putExtra("USERNAME", user);
                                startActivity(intent);
                            } else {
                                builder.setMessage("Passwords do not match. Try retyping the passwords")
                                        .setTitle("Passwords don't match");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
