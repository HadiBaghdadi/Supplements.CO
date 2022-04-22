package com.example.supplementsco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Reciept extends AppCompatActivity {
    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cartRef = db.collection("Order").document(user).collection("Cart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);
        TextView reciept = (TextView) findViewById(R.id.totalAmount);
        ListView order = (ListView) findViewById(R.id.order);
        Button home = (Button) findViewById(R.id.goHome);
        ArrayList<String> cartItems = new ArrayList<String>();
        cartRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        cartItems.add(document.getId());
                    }
                    ArrayAdapter<String> orderList = new ArrayAdapter<String>(Reciept.this, android.R.layout.simple_list_item_1, cartItems);
                    order.setAdapter(orderList);
                    orderList.notifyDataSetChanged();
                    DocumentReference userRef = db.collection("Order").document(user);
                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    Double totalPrice = document.getDouble("totalPrice");
                                    reciept.setText(totalPrice.toString());
                                }
                            }
                        }
                    });
                    home.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            startActivity(new Intent(Reciept.this, SupplementsCO.class));

                        }
                    });
                }
            }
        });
    }
}
