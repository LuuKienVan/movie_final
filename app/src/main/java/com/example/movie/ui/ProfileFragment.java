package com.example.movie.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.movie.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private boolean isEditing = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            binding.edUserName.setText(documentSnapshot.getData().get("username").toString());
                            binding.edEmailId.setText(documentSnapshot.getData().get("emailId").toString());
                            binding.edPhoneNum.setText(documentSnapshot.getData().get("phone_no").toString());
                        }else{
                            getActivity().finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //loadingDialog.dismiss();
                        Toast.makeText(
                                binding.getRoot().getContext(),
                                Objects.requireNonNull(e).toString(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        binding.BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = true;
                enableEditing(true);
            }
        });

        binding.BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing) {
                    updateProfileInfo();
                    enableEditing(false);
                    isEditing = false;
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Cho phep chinh sua khi nhan btnedit
    private void enableEditing(boolean enable) {
        binding.edUserName.setEnabled(enable);
        binding.edEmailId.setEnabled(enable);
        binding.edPhoneNum.setEnabled(enable);
    }

    private void updateProfileInfo() {
        // Lấy dữ liệu từ các TextInputEditText
        String newUserName = binding.edUserName.getText().toString();
        String newEmailId = binding.edEmailId.getText().toString();
        String newPhoneNum = binding.edPhoneNum.getText().toString();

        // Lấy đối tượng FirebaseUser để lấy UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser != null ? currentUser.getUid() : null;
        Log.d("CURR_USER_MAIL", currentUser.getEmail().toString());
        if (uid != null) {
            // Tạo một đối tượng Map để chứa dữ liệu cần cập nhật
            Map<String, Object> updates = new HashMap<>();
            updates.put("username", newUserName);
            updates.put("emailId", newEmailId);
            updates.put("phone_no", newPhoneNum);

            // Thực hiện cập nhật dữ liệu vào Firebase Firestore
            FirebaseFirestore.getInstance().collection("Users")
                    .document(uid)
                    .update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(
                                    binding.getRoot().getContext(),
                                    "Thông tin đã được cập nhật",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(
                                    binding.getRoot().getContext(),
                                    "Có lỗi xảy ra khi cập nhật thông tin",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        }
    }

}
