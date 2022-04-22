package com.example.supplementsco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Address extends AppCompatActivity {
    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Shipping").document(user);
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        spinner = (Spinner) findViewById(R.id.province);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.province, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        Button goToPayment = (Button) findViewById(R.id.nextPayment);
        goToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateShipping();
            }
        });

    }

    private void validateShipping() {
        String spinnerOption = spinner.getSelectedItem().toString();
        EditText editFullName = (EditText) findViewById(R.id.fullName);
        EditText editStreetAddress = (EditText) findViewById(R.id.streetAddress);
        EditText editCity = (EditText) findViewById(R.id.city);
        EditText editPostalCode = (EditText) findViewById(R.id.postalCode);
        EditText editPhoneNumber = (EditText) findViewById(R.id.phoneNumber);

        String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
        String fullName = editFullName.getText().toString().trim();
        String streetAddress = editStreetAddress.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String postalCode = editPostalCode.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();

        if (fullName.isEmpty()) {
            editFullName.setError("Full name is required!");
            editFullName.requestFocus();
            return;
        }
        if (streetAddress.isEmpty()) {
            editStreetAddress.setError("street address is required!");
            editStreetAddress.requestFocus();
            return;
        }
        if (city.isEmpty()) {
            editCity.setError("city is required!");
            editCity.requestFocus();
            return;
        }
        if (!postalCode.matches(regex)) {
            editPostalCode.setError("Please provide valid postal code");
            editPostalCode.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            editPhoneNumber.setError("Please provide valid phone number");
            editPhoneNumber.requestFocus();
            return;
        }
        checkUser();
        CollectionReference orderRef = db.collection("Shipping").document(user).collection("Cart");
        orderRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> payeeData = new HashMap<>();
                    payeeData.put("Street Address", streetAddress);
                    payeeData.put("city", city);
                    payeeData.put("Postal Code", postalCode);
                    payeeData.put("Phone Number", phoneNumber);
                    payeeData.put("Province", spinnerOption);
                    db.collection("Shipping").document(user).set(payeeData);
                }
                startActivity(new Intent(Address.this, Payment.class));
            }

        });
    }

    public void checkUser() {
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
