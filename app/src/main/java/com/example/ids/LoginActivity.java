package com.example.ids;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    private MaterialButton btnLogin, btnLinkToRegister, btnForgotPass;
    private TextInputLayout inputEmail, inputPassword;

    private SessionManager session;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.edit_email);
        inputPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.button_login);
        btnLinkToRegister = findViewById(R.id.button_register);
        btnForgotPass = findViewById(R.id.button_reset);

        // create sqlite database
        db = new DatabaseHandler(this);
        // session manager
        session = new SessionManager(this);

        // check user is already logged in
        if (session.isLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

        SharedPreferences sp;
        sp = getSharedPreferences("sharedPref", MODE_PRIVATE);
        String mesaj = sp.getString("mesaj", "");
        int valoare = sp.getInt("valoare", 0);
      //  Toast.makeText(this,
        //        mesaj + ":" + valoare,
        //        Toast.LENGTH_SHORT).show();


    }

    private void init() {
        // Login button Click Event
        btnLogin.setOnClickListener(view -> {
            // Hide Keyboard
            Functions.hideSoftKeyboard(LoginActivity.this);

            String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();

            // Check for empty data in the form
            if (!email.isEmpty() && !password.isEmpty()) {
                if (Functions.isValidEmailAddress(email)) {
                    // login user
                    loginProcess(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
            }
        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        // Forgot Password Dialog
        btnForgotPass.setOnClickListener(v -> forgotPasswordDialog());
    }

    private void forgotPasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.reset_password, null);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Forgot Password")
                .setCancelable(false)
                .setPositiveButton("Reset", (dialog, which) -> {
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create();

        TextInputLayout mEditEmail = dialogView.findViewById(R.id.edit_email);

        Objects.requireNonNull(mEditEmail.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditEmail.getEditText().getText().length() > 0) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        alertDialog.setOnShowListener(dialog -> {
            final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setEnabled(false);

            b.setOnClickListener(view -> {
                String email = mEditEmail.getEditText().getText().toString();

                if (!email.isEmpty()) {
                    if (Functions.isValidEmailAddress(email)) {
                        resetPassword(email);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all values!", Toast.LENGTH_SHORT).show();
                }

            });
        });

        alertDialog.show();
    }

    private void loginProcess(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        int ok=1;
        int x=0;
        Log.d(TAG, "Login Response: ");
        List<String> users = db.getUserEmails();


        try {
            Log.d(TAG, "Login : "+ users);

                if(users.contains(email))
                {
                    Log.d(TAG, "gasit -> parola ? " +db.getPassword(email));
                   // Toast.makeText(getApplicationContext(), email+" - ", Toast.LENGTH_LONG).show();
                    x=users.indexOf(email);
                    ok=1;
                    if(db.getPassword(email).equals(password))
                    {
                        Log.d(TAG, "gasita parola");
                        ok=0;
                    }
                }
            if (ok == 0)
            {
                Toast.makeText(getApplicationContext(), email+" - "+ users.get(x), Toast.LENGTH_LONG).show();
                Intent upanel = new Intent(LoginActivity.this, HomeActivity.class);
                upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                session.setLogin(true);
                Bundle b = new Bundle();
                b.putString("email", email);
              //  b.putString("name", KEY_NAME);
                upanel.putExtras(b);
                startActivityForResult(upanel,50);
            }
            else if (ok == 1){
                Toast.makeText(getApplicationContext(), "Parola incorecta!", Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Nu esti inregistrat!", Toast.LENGTH_LONG).show();
            }

        finish();
    }
    catch(Exception e)
    {
        e.printStackTrace();
        Toast.makeText(getApplicationContext(), "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    }

    private void resetPassword(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_reset_pass";

      //  showDialog("Please wait...");

        Log.d(TAG, "Reset Password Response: " );
        try {
            Toast.makeText(getApplicationContext(), " Succesful message", Toast.LENGTH_SHORT).show();
            hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
        hideDialog();

    }



    private void showDialog(String title) {
        Functions.showProgressDialog(LoginActivity.this, title);
    }

    private void hideDialog() {
        Functions.hideProgressDialog(LoginActivity.this);
    }
}
