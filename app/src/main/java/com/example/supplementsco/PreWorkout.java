package com.example.supplementsco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PreWorkout extends AppCompatActivity {

    private static final String TAG = "PreWorkout";
    private static final String KEY_TITLE = "productName";
    private static final String KEY_DESCRIPTION = "productDescription";
    private static final String KEY_PRICE = "productPrice";

    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference productRef = db.collection("Product").document("JN8ptVWCewCJNRBFFtfj");
    private DocumentReference cartRef = db.collection("Cart").document(user);
    private DocumentReference wishlistRef = db.collection("WishList").document(user);

    private TextView product;
    private Button preWorkoutAddToCart;
    private Button preWorkoutAddToWishlist;
    private int quantity = 1;
    private String title = null;
    private Double price = 49.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_workout);
        product = findViewById(R.id.productData2);
        preWorkoutAddToCart = (Button) findViewById(R.id.addToCartPreWorkout);
        preWorkoutAddToWishlist = (Button) findViewById(R.id.addToWishlistPreWorkout);

        productRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                title = documentSnapshot.getString(KEY_TITLE);
                                String description = documentSnapshot.getString(KEY_DESCRIPTION);
                                price = documentSnapshot.getDouble(KEY_PRICE);

                                product.setText("Product Name: " + title + "\n" + "\n" + "Description: " + description + "\n" + "\n" + "Price: $" + price);

                            }
                        } else {
                            Toast.makeText(PreWorkout.this, "cannot be read", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        preWorkoutAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
                DocumentReference userRef = cartRef.collection("Cart").document(title);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                quantity += 1;
                                Double total = price * quantity;
                                Map<String, Object> data = new HashMap<>();
                                data.put("quantity", quantity);
                                data.put("price", total);
                                db.collection("Cart").document(user).collection("Cart").document(title).set(data);
                            } else {
                                Map<String, Object> data = new HashMap<>();
                                data.put("quantity", quantity);
                                data.put("price", price);
                                db.collection("Cart").document(user).collection("Cart").document(title).set(data);
                            }
                        }
                    }
                });
            }

            public void checkUser() {
                cartRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            cartRef.set(new HashMap<String, Object>());
                        }
                    }
                });
            }
        });
        preWorkoutAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWishlistUser();
                DocumentReference userRef = wishlistRef.collection("WishList").document(title);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                quantity += 1;
                                Double total = price * quantity;
                                Map<String, Object> data = new HashMap<>();
                                data.put("quantity", quantity);
                                data.put("price", price);
                                db.collection("WishList").document(user).collection("WishList").document(title).set(data);
                                Toast.makeText(PreWorkout.this, "Item has been added to your wishlist", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> data = new HashMap<>();
                                data.put("quantity", quantity);
                                data.put("price", price);
                                db.collection("WishList").document(user).collection("WishList").document(title).set(data);
                                Toast.makeText(PreWorkout.this, "Item has been added to your wishlist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
            public void checkWishlistUser() {
                wishlistRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            wishlistRef.set(new HashMap<String, Object>());
                        }
                    }
                });
            }
        });
    }
}
