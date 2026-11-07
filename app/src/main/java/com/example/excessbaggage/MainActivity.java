package com.example.excessbaggage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight, etRate;
    private RadioGroup rgClass;
    private RadioButton rbEconomy;
    private TextView tvExcessWeight, tvExcessCharge, tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.etWeight);
        etRate = findViewById(R.id.etRate);
        rgClass = findViewById(R.id.rgClass);
        rbEconomy = findViewById(R.id.rbEconomy);
        Button btnCalculate = findViewById(R.id.btnCalculate);

        tvExcessWeight = findViewById(R.id.tvExcessWeight);
        tvExcessCharge = findViewById(R.id.tvExcessCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateExcessBaggage();
            }
        });
    }

    private void calculateExcessBaggage() {
        String weightStr = etWeight.getText().toString().trim();
        String rateStr = etRate.getText().toString().trim();

        // Error handling for empty inputs
        if (weightStr.isEmpty() || rateStr.isEmpty()) {
            Toast.makeText(this, "Please enter both baggage weight and rate per kg.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double rate = Double.parseDouble(rateStr);

            if (weight < 0 || rate < 0) {
                Toast.makeText(this, "Values cannot be negative.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Determine allowance based on ticket class (Economy = 20kg, Business = 40kg)[cite: 1]
            boolean isEconomy = rbEconomy.isChecked();
            double allowance = isEconomy ? 20.0 : 40.0;

            // Calculate excess weight (never negative)[cite: 1]
            double excessWeight = weight - allowance;
            if (excessWeight < 0) {
                excessWeight = 0.0;
            }

            // Calculate charges[cite: 1]
            double excessCharge = excessWeight * rate;
            double totalAmount = excessCharge + (0.08 * excessCharge); // including 8% service tax[cite: 1]

            // Display outputs
            tvExcessWeight.setText(String.format("Excess Baggage Weight: %.2f kg", excessWeight));
            tvExcessCharge.setText(String.format("Excess Baggage Charge: RM %.2f", excessCharge));
            tvTotalAmount.setText(String.format("Total Amount Payable (incl. 8%% tax): RM %.2f", totalAmount));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric inputs.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            // Share application GitHub URL intent[cite: 1]
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Excess Baggage Calculator App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my Excess Baggage Fee Calculator app source code on GitHub: https://github.com/AdiAmirMuzaffar/Excess-Baggage-Calculator.git");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        } else if (id == R.id.action_about) {
            // Open About Activity
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}