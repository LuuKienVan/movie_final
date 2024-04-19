package com.example.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.movie.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =  DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
                hideKeyboard(v);
            }
        });

        binding.createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void signIn() {
        //loadingDialog.show();
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                        binding.edEmailId.getText().toString(),
                        binding.edPassword.getText().toString()
                )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //loadingDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //loadingDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void validation() {
        String email = binding.edEmailId.getText().toString();
        String password = binding.edPassword.getText().toString();
        if (email.trim().isEmpty()) {
            binding.edEmailId.setError("Email is required");
        } else if (!validEmail(email.trim())) {
            binding.edEmailId.setError("Enter valid e-mail!");
        } else if (password.trim().isEmpty()) {
            binding.txtPasswordL.setError("Password is required");
        } else if (password.trim().length() < 8) {
            binding.txtPasswordL.setError("Password must be 8 character!");
        } else {
            binding.txtPasswordL.setError(null);
            signIn();
        }
    }

    private boolean validEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void hideKeyboard(View view) {
        try {
            InputMethodManager input_method_manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            input_method_manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            //ignore
        }
    }
}