package wahrr.wahr.harshu.justjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import static wahrr.wahr.harshu.justjava.LoginActivity.*;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    public int amount = 0;
   public int quantity = 0;
   public int price = 5;
    public int toppingPrice = 2;
     public String message;
    public boolean hasWhippedCream;
    public boolean hasChocolate;
    public FirebaseAnalytics analytics_obj;
    public FirebaseDatabase database;
    public FirebaseAuth auth_obj;
    public Button button, logout_button;
    public TextView textView;
    public String full_name;
    public String emailid;


    @SuppressLint("RestrictedApi")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        //Intent intent=new Intent();
        full_name= getIntent().getStringExtra("full_name");
        emailid= getIntent().getStringExtra("emailid_intent");


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                textView.setText(orderSummary());

                Toast.makeText(getApplicationContext(), "order successfull", Toast.LENGTH_SHORT).show();
                DatabaseReference postsRef = database.getReference("Orders");

                DatabaseReference myRef = postsRef.push();
                 myRef.child("userid").setValue(auth_obj.getInstance().getCurrentUser().getUid());

                //value v = new value(quantity, amount, hasWhippedCream, hasChocolate);


               // DatabaseReference myRef = database.getReference(auth_obj.getInstance().getCurrentUser().getUid());
                Date now = new Date();
                //Date alsoNow = Calendar.getInstance().getTime();
                String nowAsString = new SimpleDateFormat("yyyy-MM-dd").format(now);
               // myRef.setValue("Orders");

               // myRef.setValue(auth_obj.getInstance().getCurrentUser().getUid());
                myRef.child("name").setValue(full_name);
                myRef.child("emailid").setValue(emailid);
                myRef.child("Registered on").setValue(nowAsString);
                myRef.child("quantity").setValue(quantity);
                myRef.child("amount").setValue(amount);
                myRef.child("hasWhippedCream").setValue(hasWhippedCream);
                myRef.child("hasChocolate").setValue(hasChocolate);

                //databaseReference.setValue(v);



            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

              /*  Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "btn_click");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Next Activty");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");

                analytics_obj.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                analytics_obj.setAnalyticsCollectionEnabled(true);

                analytics_obj.setMinimumSessionDuration(20000);

                analytics_obj.setSessionTimeoutDuration(500);

                //analytics_obj.setUserId("123");

                //analytics_obj.setUserProperty("123", "Values");*/

                auth_obj.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

    }

    protected void initialize(){

        button = (Button) findViewById(R.id.main_buttonid);
        logout_button = (Button) findViewById(R.id.logout_buttonid);
        textView = (TextView) findViewById(R.id.quantity_text_view2);
        CheckBox checkobj1 = (CheckBox) findViewById(R.id.checkboxid1);
        hasWhippedCream = checkobj1.isChecked();
        CheckBox checkobj2 = (CheckBox) findViewById(R.id.checkboxid2);
        hasChocolate = checkobj2.isChecked();
        auth_obj = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        analytics_obj=FirebaseAnalytics.getInstance(this);

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("quan", quantity);


    }

    //Here the quantity of coffee is maintained on the screen upon rotation by the user.
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantity = savedInstanceState.getInt("quan");

    }


  /*  public class value {
        public int Quantity;
        public int Amount;
        public boolean hasWhippedCream;
        public boolean hasChocolate;

        value(int Quantity, int Amount, boolean hasWhippedCream, boolean hasChocolate) {
            this.Amount = Amount;
            this.Quantity = Quantity;
            this.hasChocolate = hasChocolate;
            this.hasWhippedCream = hasWhippedCream;
        }
    }*/




    public String orderSummary() {

        message = "Total Amount $" + calculatePrice() + "\n Thankyou for your order "+ full_name;
        message = message + "\n has Whipped Cream? " + hasWhippedCream;
        message = message + "\n has Chocolate? " + hasChocolate;
        message = message + "\n quantity " + quantity;
        //TextView quantityTextView2 = (TextView) findViewById(R.id.quantity_text_view2);
        Log.v("my message", message);
        // quantityTextView2.setText(message);
        return message;

    }

    public int calculatePrice() {


        if (hasWhippedCream && hasChocolate) {
            amount = quantity * (price + 2 * toppingPrice);
            Log.d("both", "calculatePrice() returned: " + amount);
            return amount;
        } else if (hasChocolate || hasWhippedCream) {

            amount = quantity * (price + toppingPrice);
            Log.i("one", "calculatePrice: " + amount);
            return amount;

        } else {
            amount = quantity * price;
            Log.i("nothing", "calculatePrice: " + amount);
            return amount;
        }


    }


    public void incrementNum(View view) {

        quantity = quantity + 1;
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);

    }

    public void decrementNum(View view) {

        {
            quantity = quantity - 1;
            TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
            quantityTextView.setText("" + quantity);

        }


    }
}
