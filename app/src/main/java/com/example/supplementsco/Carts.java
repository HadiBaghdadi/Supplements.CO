package com.example.supplementsco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

public class Carts extends AppCompatActivity {
    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("Cart").document(user).collection("Cart");
    private DocumentReference cartRef = db.collection("Cart").document(user);
    private Boolean created = false;
    private Button checout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carts);
        checkCartUser();
        Button checkout = (Button) findViewById(R.id.Checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Carts.this, Address.class));

            }
        });


    }

    public void checkCartUser() {
        cartRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    Toast.makeText(Carts.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                    created =  false;
                }
                else{
                        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    ArrayList<CartItem> products = new ArrayList<CartItem>();
                                    for (QueryDocumentSnapshot document:task.getResult()) {
                                        CartItem cartItem = new CartItem();
                                        cartItem.setQuantity(Integer.parseInt(document.get("quantity").toString()));
                                        cartItem.setPrice(Double.parseDouble(document.getDouble("price").toString()));
                                        cartItem.setName(document.getId());
                                        products.add(cartItem);


                                    }
                                        CartAdapter cartAdapter = new CartAdapter(Carts.this, products);
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
}