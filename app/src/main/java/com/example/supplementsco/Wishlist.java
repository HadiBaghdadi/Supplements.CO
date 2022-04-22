package com.example.supplementsco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wishlist extends AppCompatActivity {
    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("WishList").document(user).collection("WishList");
    private DocumentReference cartRef = db.collection("Cart").document(user);
    private Boolean created = false;
    private Button addToCart;
    private String title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        displayWishlist();
        Button checkout = (Button) findViewById(R.id.AddtoCart);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartWishlist();
                startActivity(new Intent(Wishlist.this, SupplementsCO.class));

            }
        });


    }

    public void displayWishlist() {
        cartRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    Toast.makeText(Wishlist.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                    created =  false;
                }
                else{
                    Toast.makeText(Wishlist.this, "Your cart is not empty", Toast.LENGTH_SHORT).show();

                        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    ArrayList<CartItem> products = new ArrayList<CartItem>();
                                    for (QueryDocumentSnapshot document:task.getResult()) {
                                        double price = 0.00;
                                        int quantity = 0;
                                        CartItem cartItem = new CartItem();
                                        cartItem.setQuantity(Integer.parseInt(document.get("quantity").toString()));
                                        cartItem.setPrice(Double.parseDouble(document.getDouble("price").toString()));
                                        cartItem.setName(document.getId());
                                        price = cartItem.getPrice();
                                        quantity = cartItem.getQuantity();
                                        products.add(cartItem);
                                        Double total = price * quantity;
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("quantity", quantity);
                                        data.put("price", price);
                                        //db.collection("Cart").document(user).collection("Cart").document(cartItem.getName().toString()).set(data);


                                    }
                                        CartAdapter cartAdapter = new CartAdapter(Wishlist.this, products);
                                        ListView carts = (ListView) findViewById(R.id.cartList);
                                        carts.setAdapter(cartAdapter);
                                        cartAdapter.notifyDataSetChanged();

                                }
                            }
                        });

                    }
               }

        });

    }
    public void addToCartWishlist() {
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    ArrayList<CartItem> products = new ArrayList<CartItem>();
                    for (QueryDocumentSnapshot document:task.getResult()) {
                        double price = 0.00;
                        int quantity = 0;
                        CartItem cartItem = new CartItem();
                        cartItem.setQuantity(Integer.parseInt(document.get("quantity").toString()));
                        cartItem.setPrice(Double.parseDouble(document.getDouble("price").toString()));
                        cartItem.setName(document.getId());
                        price = cartItem.getPrice();
                        quantity = cartItem.getQuantity();
                        products.add(cartItem);
                        Double total = price * quantity;
                        Map<String, Object> data = new HashMap<>();
                        data.put("quantity", quantity);
                        data.put("price", price);
                        db.collection("Cart").document(user).collection("Cart").document(cartItem.getName().toString()).set(data);


                    }

                }
            }
        });
    }

}