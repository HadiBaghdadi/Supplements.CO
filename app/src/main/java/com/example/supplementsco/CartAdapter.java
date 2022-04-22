package com.example.supplementsco;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {

    private final Context context;
    private final ArrayList<CartItem> cartItems;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems) {
        super(context, R.layout.activity_cart_item, cartItems);

        this.context = context;
        this.cartItems = cartItems;

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        CartItem cartItem = cartItems.get(position);
        if(convertView == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.activity_cart_item, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
//            Button addToCart = (Button) convertView.findViewById(R.id.btnAddtoCart);
//            addToCart.setOnClickListener(
//                    new View.OnClickListener()
//                    {
//                        public void onClick(View view)
//                        {
//                            Log.v("EditText", name.getText().toString());
//                        }
//                    });

            name.setText(cartItem.getName());
            price.setText(cartItem.getPrice().toString());
            quantity.setText(String.valueOf(cartItem.getQuantity()));
        }
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public CartItem getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}

