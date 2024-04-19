package com.example.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movie.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    boolean isValidPassword = false;
    boolean isValidConPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = binding =  DataBindingUtil.setContentView(this, R.layout.activity_register);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    private void validation() {
        String user_name = binding.edUserName.getText().toString();
        String email = binding.edEmailId.getText().toString();
        String phone_number = binding.edPhoneNum.getText().toString();

        if (user_name.trim().isEmpty()) {
            binding.edUserName.setError("Name required");
        } else if (email.trim().isEmpty()) {
            binding.edEmailId.setError("Email required");
        } else if (!validEmail(email.trim())) {
            binding.edEmailId.setError("Enter valid e-mail!");
        } else if (phone_number.trim().isEmpty()) {
            binding.edPhoneNum.setError("Phone number required");
        } else if (!validPhoneNo(phone_number.trim())) {
            binding.edPhoneNum.setError("Enter valid phone number");
        } else {
            validateEditTextPassword(binding.edPassword, binding.txtPasswordL, "password");
            if (isValidPassword) {
                validateEditTextPassword(binding.edConPassword, binding.txtConPasswordL, "conPassword");
                if (isValidConPassword) {
                    if (!binding.edPassword.getText().toString().trim().equals(binding.edConPassword.getText().toString().trim())
                    ) {
                        Log.d("PassConf", binding.edConPassword.getText().toString().trim());
                        Log.d("PassEnter", binding.edPassword.getText().toString().trim());
                        Log.d("checked", Boolean.toString(binding.edPassword.getText().toString().trim().equals(binding.edConPassword.getText().toString().trim())));
                        binding.txtConPasswordL.setError("Password doesn't match!");
                    } else {
                        binding.txtConPasswordL.setError(null);
                        register();
                    }
                }
            }
        }
    }

    private void register() {
        //loadingDialog.show();
        String emailId =  binding.edEmailId.getText().toString().trim();
        String password =  binding.edPassword.getText().toString().trim();
        HashMap registerHM = new HashMap();
        registerHM.put("username", binding.edUserName.getText().toString());
        registerHM.put("emailId", emailId);
        registerHM.put("phone_no", binding.edPhoneNum.getText().toString());

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            registerHM.put("id", id);
                            FirebaseFirestore.getInstance().collection("Users").document(id).set(registerHM)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if (task1.isSuccessful()) {
                                                //loadingDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),
                                                        "Register Successfully",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent nIntent = new Intent(
                                                        getApplicationContext(),
                                                        MainActivity.class
                                                );
                                                nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                nIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(nIntent);
                                                finish();
                                            } else {
                                                //loadingDialog.dismiss();
                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        "Account Already Existed",
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void validateEditTextPassword(
            EditText editText,
            TextInputLayout textInputLayout,
            String state
    ) {
        boolean truthState = false;
        if(editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError("Password required");
        }
        else if (editText.getText().toString().length() < 8) {
            textInputLayout.setError("Password must be 8 character!");
        }
        else {
            textInputLayout.setError(null);
            truthState = true;
        }

        if (truthState) {
            if (state == "password") {
                isValidPassword = true;
            } else if (state == "conPassword") {
                isValidConPassword = true;
            }
        } else {
            if (state == "password") {
                isValidPassword = false;
            } else if (state == "conPassword") {
                isValidConPassword = false;
            }
        }
    }

    private boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validPhoneNo(String phone_number) {
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(phone_number);
        boolean matchFound = matcher.find();
        return matchFound;
    }
}