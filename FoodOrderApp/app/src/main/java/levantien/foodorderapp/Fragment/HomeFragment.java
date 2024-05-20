package levantien.foodorderapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import levantien.foodorderapp.Activity.CartActivity;
import levantien.foodorderapp.Activity.ListFoodActivity;
import levantien.foodorderapp.Activity.MainActivity;
import levantien.foodorderapp.Adapter.BestFoodAdapter;
import levantien.foodorderapp.Adapter.CategoryAdapter;
import levantien.foodorderapp.Adapter.SliderAdapter;
import levantien.foodorderapp.Domain.Category;
import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.Domain.SliderItems;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersReference;
    String phoneId;
    boolean isLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Avoid any UI initialization here; move it to onCreateView or later.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        usersReference = database.getReference("users");
        phoneId = sharedPreferences.getString("phoneId", "");
        initCategory();
        initBanner();
        initBestFood();
        setVariable();
    }

    private void initBestFood() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.proBest.setVisibility(View.VISIBLE);
        ArrayList<Foods> ds = new ArrayList<>();
        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        ds.add(issue.getValue(Foods.class));
                    }
                    if(ds.size() > 0){
                        binding.recycleBestFood.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new BestFoodAdapter(ds, getContext());
                        binding.recycleBestFood.setAdapter(adapter);
                    }
                    binding.proBest.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> ds = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ds.add(issue.getValue(Category.class));
                    }
                    if (ds.size() > 0) {
                        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        binding.recyclerView.setAdapter(new CategoryAdapter(ds));
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarCategory.setVisibility(View.GONE);
            }
        });
    }
    private void initBanner() {
        DatabaseReference reference = database.getReference("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items){
        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer pageTransformer = new CompositePageTransformer();
        pageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPager2.setPageTransformer(pageTransformer);
    }

    private void setVariable() {
        if(!isLogin){
            binding.tvName.setText("Customer A");
        }
        else {
            usersReference.child(phoneId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        binding.tvName.setText(name);
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
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CartActivity.class));
            }
        });
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.edtSearch.getText().toString();
                if(!text.isEmpty()){
                    Intent intent = new Intent(getContext(), ListFoodActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("isSearch", true);
                    startActivity(intent);
                }
            }
        });
    }

}