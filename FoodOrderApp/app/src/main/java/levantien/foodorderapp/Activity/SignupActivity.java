package levantien.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityIntroBinding;
import levantien.foodorderapp.databinding.ActivitySignupBinding;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://foodorderapp-f23ad-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
        binding.btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
                String regexPhoneNumber = "^(84|0[3|5|7|8|9])+([0-9]{8})$";
                String name = binding.edtUserName.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String email = binding.edtEmail.getText().toString();
                String pass = binding.edtPass.getText().toString();
                String confirm = binding.edtConfirm.getText().toString();

                Pattern pattern = Pattern.compile(emailRegex);
                Pattern phonePattern = Pattern.compile(regexPhoneNumber);

                if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!pattern.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (!phonePattern.matcher(phone).matches()) {
                    Toast.makeText(SignupActivity.this, "Invalid number phone", Toast.LENGTH_SHORT).show();
                } else if(!pass.equals(confirm)){
                    Toast.makeText(SignupActivity.this, "Password are not matching", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone)){
                                Toast.makeText(SignupActivity.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("users").child(phone).child("name").setValue(name);
                                databaseReference.child("users").child(phone).child("email").setValue(email);
                                databaseReference.child("users").child(phone).child("pass").setValue(pass);

                                Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}