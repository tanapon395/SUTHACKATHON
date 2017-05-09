package com.example.tanapon.suthackathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";
    private Button scan_btn, clare;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String text;
    TextView textVieww;
    //Realtime Text from firebase
    private String tmpStr10;
    //Realtime Text from firebase
    private TextView MsgTxt;
    private final static String keyT = "keyTai", key = "keySharedPreferences";
    ;

    //qr code scanner object

    private TextView textViewName, textViewAddress;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("users");

    DatabaseReference status = FirebaseDatabase.getInstance().getReference();
    DatabaseReference product = status.child("status_product");



    private int i = 1;
    private String value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);

        scan_btn = (Button) view.findViewById(R.id.scan_btn);
        clare = (Button) view.findViewById(R.id.clare);
        textVieww = (TextView) view.findViewById(R.id.msgTxt1);
        sharedPref = getActivity().getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        textVieww.setText(sharedPref.getString(keyT, "Please Scan QR CODE"));


        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.forSupportFragment(Tab2Fragment.this).initiateScan();
                product.child("1").setValue("0");
                Log.e("Check", "GotoScan");
            }
        });

        clare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                textVieww.setText("Please Scan QR CODE");
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Check", "onActivity");
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "You cancelled the scanning", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                text = result.getContents();
                value = Integer.toString(i);
                product.child("1").setValue("1");
                mUsersRef.child(value).setValue(text);
                editor.putString(keyT, text);
                editor.commit();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
