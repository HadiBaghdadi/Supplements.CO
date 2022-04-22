package com.example.supplementsco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class creatine extends AppCompatActivity {

    private static final String TAG = "Creatine";
    private static final String KEY_TITLE = "productName";
    private static final String KEY_DESCRIPTION = "productDescription";
    private static final String KEY_PRICE = "productPrice";

    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference productRef = db.collection("Product").document("OZYT9ifPviHaN4H9knAx");
    private DocumentReference cartRef = db.collection("Cart").document(user);
    private DocumentReference wishlistRef = db.collection("WishList").document(user);


    private TextView product;
    private Button creatineAddToCart;
    private Button creatineAddToWishlist;
    private int quantity = 1;
    private String title = null;
    private Double price = 29.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatine);
        product = findViewById(R.id.productData);
        creatineAddToCart = (Button) findViewById(R.id.addToCartCreatine);
        creatineAddToWishlist = (Button) findViewById(R.id.addToWishlistCreatine);


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
                                //Toast.makeText(creatine.     this, title, Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(creatine.this, "cannot be read", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        creatineAddToCart.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(creatine.this, "Item has been added to your cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> data = new HashMap<>();
                                data.put("quantity", quantity);
                                data.put("price", price);
                                db.collection("Cart").document(user).collection("Cart").document(title).set(data);
                                Toast.makeText(creatine.this, "Item has been added to your cart", Toast.LENGTH_SHORT).show();
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
        creatineAddToWishlist.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(creatine.this, "Item has been added to your wishlist", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> data = new HashMap<>();
                                data.put("quantity", quantity);
                                data.put("price", price);
                                db.collection("WishList").document(user).collection("WishList").document(title).set(data);
                                Toast.makeText(creatine.this, "Item has been added to your wishlist", Toast.LENGTH_SHORT).show();

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
