package levantien.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

import levantien.foodorderapp.Adapter.CategoryAdapter;
import levantien.foodorderapp.Adapter.SliderAdapter;
import levantien.foodorderapp.Domain.Category;
import levantien.foodorderapp.Domain.SliderItems;
import levantien.foodorderapp.Fragment.FavoritesFragment;
import levantien.foodorderapp.Fragment.HomeFragment;
import levantien.foodorderapp.Fragment.NotifyFragment;
import levantien.foodorderapp.Fragment.ProfileFragment;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
//    ActivityMainBinding binding;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        initCategory();
//        initBanner();
//        setVariable();
//    }

//    private void initBanner() {
//        DatabaseReference reference = database.getReference("Banners");
//        binding.progressBarBanner.setVisibility(View.VISIBLE);
//        ArrayList<SliderItems> items = new ArrayList<>();
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for (DataSnapshot issue: snapshot.getChildren()){
//                        items.add(issue.getValue(SliderItems.class));
//                    }
//                    banners(items);
//                    binding.progressBarBanner.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void banners(ArrayList<SliderItems> items){
//        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
//        binding.viewPager2.setClipChildren(false);
//        binding.viewPager2.setClipToPadding(false);
//        binding.viewPager2.setOffscreenPageLimit(3);
//        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//        CompositePageTransformer pageTransformer = new CompositePageTransformer();
//        pageTransformer.addTransformer(new MarginPageTransformer(40));
//        binding.viewPager2.setPageTransformer(pageTransformer);
//    }
//
//    private void setVariable() {
//        binding.bottomMenu.setItemSelected(R.id.home, true);
//    }
//
//    private void initCategory() {
//        DatabaseReference reference = database.getReference("Category");
//        binding.progressBarCategory.setVisibility(View.VISIBLE);
//        ArrayList<Category> ds = new ArrayList<>();
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue: snapshot.getChildren()){
//                        ds.add(issue.getValue(Category.class));
//                    }
//                    if(ds.size() > 0){
//                        binding.recyclerViewCategory.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
//                        binding.recyclerViewCategory.setAdapter(new CategoryAdapter(ds));
//                    }
//                    binding.progressBarCategory.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChipNavigationBar navigationView = findViewById(R.id.bnv);
        navigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment selected = null;
                if (id == R.id.home) {
                    selected = new HomeFragment();
                }
                else if(id == R.id.profile){
                    selected = new ProfileFragment();
                }
                else if(id == R.id.favorites){
                    selected = new FavoritesFragment();
                } else if (id == R.id.notify) {
                    selected = new NotifyFragment();
                }
                // Add other cases if you have more menu items

                if (selected != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selected).commit();
                }
            }
        });

        // Set the default selected item
        navigationView.setItemSelected(R.id.home, true);
    }
}