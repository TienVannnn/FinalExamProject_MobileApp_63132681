package levantien.foodorderapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import levantien.foodorderapp.Adapter.FavoritesAdapter;
import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.FragmentFavoritesBinding;
import levantien.foodorderapp.databinding.FragmentHomeBinding;


public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersReference;
    String phoneId;
    boolean isLogin;
    private ArrayList<Foods> favoriteFoods = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        usersReference = database.getReference("users");
        phoneId = sharedPreferences.getString("phoneId", "");

        setupRecyclerView();
        if (isLogin) {
            loadFavoriteFoods();
        }

    }

    private void setupRecyclerView() {
        favoritesAdapter = new FavoritesAdapter(favoriteFoods, getContext());
        binding.recycleFav.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleFav.setAdapter(favoritesAdapter);
    }

    private void loadFavoriteFoods() {
        usersReference.child(phoneId).child("favoritesFood").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteFoods.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Foods food = dataSnapshot.getValue(Foods.class);
                    favoriteFoods.add(food);
                }
                favoritesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}