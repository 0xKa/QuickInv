# QuickInv - Currency Conversion Integration

## Code Changes Required

### 1. Create New Utility Class: CurrencyConverter.java

**File Location:** `app/src/main/java/com/example/quickinv/utils/CurrencyConverter.java`

```java
package com.example.quickinv.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {

    private static final String API_KEY = "4db96e63f27ceac9fa8a7e4e"; // Free tier API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";
    
    private Context context;
    private OnConversionListener listener;

    public interface OnConversionListener {
        void onSuccess(String result);
        void onError(String error);
    }

    public CurrencyConverter(Context context, OnConversionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void convertCurrency(String baseCurrency, String targetCurrency, double amount) {
        // Run API call in background thread
        new Thread(() -> {
            try {
                String urlString = API_URL + API_KEY + "/pair/" + baseCurrency.toUpperCase() + 
                                 "/" + targetCurrency.toUpperCase() + "/" + amount;
                
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(response.toString());
                    
                    if ("success".equals(jsonObject.getString("result"))) {
                        double conversionRate = jsonObject.getDouble("conversion_rate");
                        double convertedAmount = jsonObject.getDouble("conversion_result");
                        
                        String resultText = String.format(
                            "%.2f %s = %.2f %s\n(Rate: %.4f)",
                            amount, baseCurrency.toUpperCase(),
                            convertedAmount, targetCurrency.toUpperCase(),
                            conversionRate
                        );
                        
                        if (listener != null) {
                            listener.onSuccess(resultText);
                        }
                    } else {
                        String errorType = jsonObject.optString("error-type", "Unknown error");
                        if (listener != null) {
                            listener.onError("API Error: " + errorType);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError("Connection error: " + responseCode);
                    }
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError("Error: " + e.getMessage());
                }
            }
        }).start();
    }
}
```

### 2. Update build.gradle (Add JSON dependency)

**File:** `app/build.gradle`

**Find this section:**
```gradle
dependencies {
    // AndroidX
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.0'
    implementation 'androidx.appcompat:appcompat-resources:1.6.1'
    
    // Material Design
    implementation 'com.google.android.material:material:1.10.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

**Add this line to dependencies:**
```gradle
implementation 'org.json:json:20231013'
```

### 3. Update AndroidManifest.xml (Add Internet Permission)

**File:** `AndroidManifest.xml`

**Add before `<application>` tag:**
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 4. Create Currency Conversion Dialog Layout

**File:** `app/src/main/res/layout/dialog_currency_converter.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="20dp"
    android:background="@color/white">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Currency Converter"
        android:textSize="20sp"
        android:textStyle="bold"
        android:textColor="@color/text_primary"
        android:layout_marginBottom="20dp" />

    <EditText
        android:id="@+id/base_currency"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:hint="Base Currency (USD)"
        android:inputType="text"
        android:text="USD"
        android:padding="12dp"
        android:background="@color/background_color"
        android:layout_marginBottom="12dp"
        android:textColor="@color/text_primary" />

    <EditText
        android:id="@+id/target_currency"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:hint="Target Currency (EUR)"
        android:inputType="text"
        android:text="EUR"
        android:padding="12dp"
        android:background="@color/background_color"
        android:layout_marginBottom="12dp"
        android:textColor="@color/text_primary" />

    <EditText
        android:id="@+id/amount_to_convert"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:hint="Amount to Convert"
        android:inputType="numberDecimal"
        android:text="1.0"
        android:padding="12dp"
        android:background="@color/background_color"
        android:layout_marginBottom="20dp"
        android:textColor="@color/text_primary" />

    <ProgressBar
        android:id="@+id/conversion_progress"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:visibility="gone"
        android:layout_marginBottom="16dp" />

    <TextView
        android:id="@+id/conversion_result"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:textColor="@color/primary_color"
        android:gravity="center"
        android:layout_marginBottom="16dp"
        android:text="" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="end"
        android:gap="12dp">

        <Button
            android:id="@+id/cancel_button"
            android:layout_width="100dp"
            android:layout_height="40dp"
            android:text="Cancel"
            android:textColor="@color/text_primary"
            android:background="@color/background_color"
            android:layout_marginRight="8dp" />

        <Button
            android:id="@+id/convert_button"
            android:layout_width="100dp"
            android:layout_height="40dp"
            android:text="Convert"
            android:textColor="@color/white"
            android:background="@color/primary_color" />

    </LinearLayout>

</LinearLayout>
```

### 5. Update strings.xml (Add New Strings)

**File:** `app/src/main/res/values/strings.xml`

**Add these lines inside `<resources>` tag:**
```xml
    <!-- Currency Conversion -->
    <string name="convert_currency">Convert Currency</string>
    <string name="currency_converter">Currency Converter</string>
    <string name="base_currency">Base Currency</string>
    <string name="target_currency">Target Currency</string>
    <string name="amount">Amount</string>
    <string name="convert">Convert</string>
    <string name="cancel">Cancel</string>
```

### 6. Update activity_inventory.xml (Add Currency Button)

**File:** `app/src/main/res/layout/activity_inventory.xml`

**LOCATE THIS SECTION:**
```xml
    <Button
        android:id="@+id/add_item_button"
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:text="@string/add_item"
        android:textStyle="bold"
        android:textSize="16sp"
        android:background="@color/primary_color"
        android:textColor="@color/white"
        android:layout_margin="16dp" />

</LinearLayout>
```

**REPLACE WITH:**
```xml
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp"
        android:gap="8dp">

        <Button
            android:id="@+id/add_item_button"
            android:layout_width="0dp"
            android:layout_height="56dp"
            android:layout_weight="1"
            android:text="@string/add_item"
            android:textStyle="bold"
            android:textSize="14sp"
            android:background="@color/primary_color"
            android:textColor="@color/white" />

        <Button
            android:id="@+id/currency_button"
            android:layout_width="wrap_content"
            android:layout_height="56dp"
            android:text="💱"
            android:textSize="24sp"
            android:background="@color/accent_color"
            android:textColor="@color/white"
            android:paddingLeft="16dp"
            android:paddingRight="16dp" />

    </LinearLayout>

</LinearLayout>
```

### 7. Update InventoryActivity.java (Add Currency Button Logic)

**File:** `app/src/main/java/com/example/quickinv/activities/InventoryActivity.java`

**ADD THIS IMPORT at the top:**
```java
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.quickinv.utils.CurrencyConverter;
```

**LOCATE THIS SECTION (in onCreate method after addItemButton initialization):**
```java
        // Set up button listener
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });
```

**ADD THIS CODE RIGHT AFTER IT:**
```java
        // Currency converter button
        Button currencyButton = findViewById(R.id.currency_button);
        currencyButton.setOnClickListener(v -> showCurrencyConverter());
```

**ADD THIS NEW METHOD at the end of the InventoryActivity class (before the closing brace):**
```java
    private void showCurrencyConverter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        // Create custom view
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);
        android.view.View dialogView = inflater.inflate(R.layout.dialog_currency_converter, null);
        
        EditText baseCurrencyEditText = dialogView.findViewById(R.id.base_currency);
        EditText targetCurrencyEditText = dialogView.findViewById(R.id.target_currency);
        EditText amountEditText = dialogView.findViewById(R.id.amount_to_convert);
        ProgressBar progressBar = dialogView.findViewById(R.id.conversion_progress);
        TextView resultTextView = dialogView.findViewById(R.id.conversion_result);
        Button convertButton = dialogView.findViewById(R.id.convert_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        
        convertButton.setOnClickListener(v -> {
            String baseCurrency = baseCurrencyEditText.getText().toString().trim();
            String targetCurrency = targetCurrencyEditText.getText().toString().trim();
            String amountStr = amountEditText.getText().toString().trim();
            
            if (baseCurrency.isEmpty() || targetCurrency.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(InventoryActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                double amount = Double.parseDouble(amountStr);
                
                progressBar.setVisibility(android.view.View.VISIBLE);
                resultTextView.setText("");
                convertButton.setEnabled(false);
                
                CurrencyConverter converter = new CurrencyConverter(InventoryActivity.this, 
                    new CurrencyConverter.OnConversionListener() {
                        @Override
                        public void onSuccess(String result) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(android.view.View.GONE);
                                resultTextView.setText(result);
                                resultTextView.setTextColor(getResources().getColor(R.color.success_color));
                                convertButton.setEnabled(true);
                            });
                        }
                        
                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(android.view.View.GONE);
                                resultTextView.setText(error);
                                resultTextView.setTextColor(getResources().getColor(R.color.error_color));
                                convertButton.setEnabled(true);
                                Toast.makeText(InventoryActivity.this, error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                );
                
                converter.convertCurrency(baseCurrency, targetCurrency, amount);
                
            } catch (NumberFormatException e) {
                Toast.makeText(InventoryActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });
        
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }
```

## Summary of Changes

| File | Change Type | What to Do |
|------|------------|-----------|
| `CurrencyConverter.java` | **NEW** | Create this utility class |
| `build.gradle` | **UPDATE** | Add JSON dependency |
| `AndroidManifest.xml` | **UPDATE** | Add internet permission |
| `dialog_currency_converter.xml` | **NEW** | Create this layout |
| `strings.xml` | **UPDATE** | Add 7 new string entries |
| `activity_inventory.xml` | **UPDATE** | Replace button section (add currency button) |
| `InventoryActivity.java` | **UPDATE** | Add imports + button click listener + showCurrencyConverter() method |

## How to Use

1. Copy all code exactly as shown
2. Add internet permission to manifest
3. Update build.gradle and sync
4. Run the app
5. On Inventory screen, click the 💱 button
6. Enter base currency (e.g., USD), target currency (e.g., EUR), and amount
7. Click Convert to see real-time exchange rates

## API Details

- **Free Tier**: 1,500 requests/month
- **API Key**: Included (shared free key: `4db96e63f27ceac9fa8a7e4e`)
- **Response Time**: ~1-2 seconds
- **Supported Currencies**: 160+ currencies (all ISO 4217 codes)

## Example

```
Input:
Base: USD
Target: EUR
Amount: 100

Output:
100.00 USD = 92.45 EUR
(Rate: 0.9245)
```