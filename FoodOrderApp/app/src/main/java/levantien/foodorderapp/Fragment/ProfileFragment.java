package levantien.foodorderapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import levantien.foodorderapp.Activity.ChangePasswordActivity;
import levantien.foodorderapp.Activity.LoginActivity;
import levantien.foodorderapp.Activity.SignupActivity;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.FragmentHomeBinding;
import levantien.foodorderapp.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    FirebaseDatabase myFirebaseDatabase;
    DatabaseReference usersReference;
    boolean isLogin;
    String phoneId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Avoid any UI initialization here; move it to onCreateView or later.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myFirebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = myFirebaseDatabase.getReference("users");
        // Lấy trạng thái đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        phoneId = sharedPreferences.getString("phoneId", "");
        setVariable();
        initProfile();
    }

    private void initProfile() {
        if(!isLogin){
            binding.welcome.setVisibility(View.VISIBLE);
            binding.profile.setVisibility(View.GONE);
        }
        else {
            binding.welcome.setVisibility(View.GONE);
            binding.profile.setVisibility(View.VISIBLE);
            loadUserData();
        }
    }


    private void loadUserData() {
        usersReference.child(phoneId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    // Cập nhật giao diện với dữ liệu người dùng
                    binding.tvName.setText(name);
                    binding.tvEmail.setText(email);
                } else {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setVariable() {
        binding.purchaseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment notifyFragment = new NotifyFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, notifyFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SignupActivity.class));
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLogin", false);
                editor.apply();
                // Cập nhật UI để phản ánh trạng thái đăng xuất
                isLogin = false;
                initProfile();
            }
        });
        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
            }
        });
    }
}