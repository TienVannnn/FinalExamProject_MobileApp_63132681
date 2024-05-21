package levantien.foodorderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends BaseActivity {
    ActivityChangePasswordBinding binding;
    private FirebaseDatabase myFirebaseDatabase;
    private DatabaseReference usersReference;
    private String phoneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myFirebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = myFirebaseDatabase.getReference("users");

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        phoneId = sharedPreferences.getString("phoneId", "");
        binding.edtPhoneC.setText(phoneId);
        setVariable();
    }

    private void setVariable() {
        binding.btnBackC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = binding.edtOldC.getText().toString();
                String newPass = binding.edtNewC.getText().toString();
                String confirmPass = binding.edtConfirmC.getText().toString();

                if (newPass.isEmpty() || confirmPass.isEmpty() || oldPass.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                usersReference.child(phoneId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String currentPass = dataSnapshot.child("pass").getValue(String.class);

                            if (currentPass != null && currentPass.equals(oldPass)) {
                                // Kiểm tra mật khẩu mới và mật khẩu xác nhận có khớp hay không
                                if (!newPass.equals(confirmPass)) {
                                    Toast.makeText(ChangePasswordActivity.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Cập nhật mật khẩu mới nếu tất cả điều kiện đều đúng
                                usersReference.child(phoneId).child("pass").setValue(newPass);
                                Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ChangePasswordActivity.this, "Failed to read data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}