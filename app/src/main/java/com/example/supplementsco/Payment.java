package com.example.supplementsco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Payment extends AppCompatActivity {
    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Order").document(user);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button checkOut = (Button) findViewById(R.id.checkOut);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePayment();
            }
        });
    }

    private void validatePayment() {
        EditText editCardNumber = (EditText) findViewById(R.id.cardNumber);
        EditText editExpirationDate = (EditText) findViewById(R.id.expirationDate);
        EditText editCVV = (EditText) findViewById(R.id.CVV);
        String cardNumber = editCardNumber.getText().toString().trim();
        String expirationDate = editExpirationDate.getText().toString().trim();
        String cvv = editCVV.getText().toString().trim();

        if (cardNumber.isEmpty()) {
            editCardNumber.setError("Credit card number is required!");
            editCardNumber.requestFocus();
            return;
        }
        if (expirationDate.isEmpty()) {
            editExpirationDate.setError("expiration date is required!");
            editExpirationDate.requestFocus();
            return;
        }
        if (cvv.isEmpty()) {
            editCVV.setError("CVV is required!");
            editCVV.requestFocus();
            return;
        }
        CollectionReference cartRef = db.collection("Cart").document(user).collection("Cart");
        ArrayList<CartItem> cartItems = new ArrayList<CartItem>();
        cartRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        CartItem cartItem = new CartItem();
                        cartItem.setName(document.getId().toString());
                        cartItem.setQuantity(Integer.parseInt(document.get("quantity").toString()));
                        cartItem.setPrice(document.getDouble("price"));
                        cartItems.add(cartItem);
                    }
                    checkUserOrder();
                    CollectionReference orderRef = db.collection("Order").document(user).collection("Cart");
                    orderRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                Double totalPrice = 0.00;
                                int totalQuantity = 0;
                                int orderId = 0;
                                Map<String, Object> payeeData = new HashMap<>();
                                payeeData.put("Card Number",cardNumber);
                                payeeData.put("Expiration Date",expirationDate);
                                payeeData.put("CVV",cvv);
                                db.collection("Credit").document(user).set(payeeData);
                                for(CartItem cartItem : cartItems){
                                    totalPrice += cartItem.getPrice();
                                    totalQuantity += cartItem.getQuantity();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("quantity", cartItem.getQuantity());
                                    data.put("price", cartItem.getPrice());
                                    db.collection("Order").document(user).collection("Cart").document(cartItem.getName()).set(data);
                                    db.collection("Cart").document(user).delete();
                                }
                                Map<String, Object> orderData = new HashMap<>();
                                orderId += 1;
                                orderData.put("orderId", orderId);
                                orderData.put("totalPrice", totalPrice);
                                orderData.put("totalQuantity", totalQuantity);
                                db.collection("Order").document(user).set(orderData);
                            }

                        }
                    });
                }
                startActivity(new Intent(Payment.this, Reciept.class));


            }
        });
    }
    public void checkUserOrder() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    userRef.set(new HashMap<String, Object>());
                }
            }
        });
    }
}