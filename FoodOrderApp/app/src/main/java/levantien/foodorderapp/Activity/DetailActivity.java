package levantien.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import levantien.foodorderapp.Domain.Foods;
import levantien.foodorderapp.Helper.ManagmentCart;
import levantien.foodorderapp.R;
import levantien.foodorderapp.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;
    String phoneId;
    FirebaseDatabase myFirebaseDatabase;
    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myFirebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = myFirebaseDatabase.getReference("users");

        SharedPreferences sharedPreferences = DetailActivity.this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        phoneId = sharedPreferences.getString("phoneId", "");
        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(this).load(object.getImagePath()).transform(new CenterCrop(), new RoundedCorners(60)).into(binding.picDT);
        binding.tvPrice.setText("$" + object.getPrice());
        binding.tvTitle.setText(object.getTitle());
        binding.description.setText(object.getDescription());
        binding.tvRate.setText(object.getStar() + "Rating");
        binding.ratingBar2.setRating((float) object.getStar());
        binding.tvTotal.setText((num*object.getPrice()) + "$");
        binding.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num+=1;
                binding.tvNum.setText(String.valueOf(num));
                binding.tvTotal.setText("$" + (num * object.getPrice()));
            }
        });

        binding.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num > 1){
                    num-=1;
                    binding.tvNum.setText(String.valueOf(num));
                    binding.tvTotal.setText("$" + (num * object.getPrice()));
                }
            }
        });
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                object.setNumberInCart(num);
//                managmentCart.insertFood(object);
                double price = object.getPrice();
                int quantity = Integer.parseInt(binding.tvNum.getText().toString());
                double totalPrice = price * quantity;
                usersReference.child(phoneId).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String cartItemId = usersReference.child(phoneId).child("cart").push().getKey();

                        if (cartItemId != null) {
                            DatabaseReference cartItemRef = usersReference.child(phoneId).child("cart").child(cartItemId);
                            cartItemRef.child("Title").setValue(binding.tvTitle.getText().toString());
                            cartItemRef.child("Price").setValue(object.getPrice());
                            cartItemRef.child("quantity").setValue(quantity);
                            cartItemRef.child("totalPrice").setValue(totalPrice);
                            cartItemRef.child("Description").setValue(object.getDescription());
                            cartItemRef.child("ImagePath").setValue(object.getImagePath());

                            Toast.makeText(DetailActivity.this, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}