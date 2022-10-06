package com.example.ids;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EmailVerify extends AppCompatActivity {
    private static final String TAG = EmailVerify.class.getSimpleName();

    private TextInputLayout textVerifyCode;
    private MaterialButton btnVerify, btnResend;
    private TextView otpCountDown;

    private SessionManager session;
    private DatabaseHandler db;

    private static final String FORMAT = "%02d:%02d";

    Bundle bundle;

    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        textVerifyCode = findViewById(R.id.verify_code);
        btnVerify = findViewById(R.id.btnVerify);
        btnResend = findViewById(R.id.btnResendCode);
        otpCountDown = findViewById(R.id.otpCountDown);

        bundle = getIntent().getExtras();

        db = new DatabaseHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        btnVerify.setOnClickListener(v -> {
            // Hide Keyboard
            Functions.hideSoftKeyboard(EmailVerify.this);

            String email = bundle.getString("email");
            String otp = Objects.requireNonNull(textVerifyCode.getEditText()).getText().toString();

            if (!otp.isEmpty()) {
                verifyCode(email, otp);
                textVerifyCode.setErrorEnabled(false);
            } else {
                textVerifyCode.setError("Please enter verification code");
            }
        });

        btnResend.setEnabled(false);
        btnResend.setOnClickListener(v -> {
            String email = bundle.getString("email");
            resendCode(email);
        });

        countDown();
    }

    private void countDown() {
        new CountDownTimer(60000, 1000) { // adjust the milli seconds here

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {
                otpCountDown.setVisibility(View.VISIBLE);
                otpCountDown.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) ));
            }

            public void onFinish() {
                otpCountDown.setVisibility(View.GONE);
                btnResend.setEnabled(true);
            }
        }.start();
    }

    private void verifyCode(final String email, final String otp) {
        // Tag used to cancel the request
        String tag_string_req = "req_verify_code";

        try {
            Toast.makeText(getApplicationContext(), "Your code: " + otp, Toast.LENGTH_LONG).show();


                            session.setLogin(true);
                            Intent upanel = new Intent(EmailVerify.this, HomeActivity.class);
                            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle b = new Bundle();
                            b.putString("email", email);
                            upanel.putExtras(b);
                            startActivity(upanel);
                            finish();

        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void resendCode(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_resend_code";

        try {

            // Check for error node in json
            Toast.makeText(getApplicationContext(), "Code successfully sent to your email!", Toast.LENGTH_LONG).show();
            btnResend.setEnabled(false);
            countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showDialog(String title) {
        Functions.showProgressDialog(EmailVerify.this, title);
    }

    private void hideDialog() {
        Functions.hideProgressDialog(EmailVerify.this);
    }

    @Override
    public void onResume(){
        super.onResume();
        countDown();
    }
}
