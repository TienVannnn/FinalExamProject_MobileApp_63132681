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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Foods currentFood;
    private boolean isFavorite = false;

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

        checkIfFavorite();

        binding.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });
    }

    private void checkIfFavorite() {
        if (object != null) {
            usersReference.child(phoneId).child("favoritesFood").child(String.valueOf(object.getId()))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isFavorite = snapshot.exists();
                            updateFavoriteIcon();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi
                        }
                    });
        }
    }

    private void toggleFavorite() {
        if(isLogin){
            if (object != null) {
                if (isFavorite) {
                    // Xóa sản phẩm khỏi danh sách yêu thích
                    usersReference.child(phoneId).child("favoritesFood").child(String.valueOf(object.getId())).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    isFavorite = false;
                                    updateFavoriteIcon();
                                    Toast.makeText(DetailActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailActivity.this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Thêm sản phẩm vào danh sách yêu thích
                    usersReference.child(phoneId).child("favoritesFood").child(String.valueOf(object.getId())).setValue(object)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    isFavorite = true;
                                    updateFavoriteIcon();
                                    Toast.makeText(DetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(DetailActivity.this, "Error: Product or User ID is null", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "You need to log in to do this!", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            binding.btnFav.setImageResource(R.drawable.heart);
        } else {
            binding.btnFav.setImageResource(R.drawable.favorite);
        }
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
                if(!isLogin){
                    Toast.makeText(DetailActivity.this, "You need to log in to do this!", Toast.LENGTH_SHORT).show();
                }
                else {
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
                                cartItemRef.child("Quantity").setValue(quantity);
                                cartItemRef.child("TotalPrice").setValue(totalPrice);
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
            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}