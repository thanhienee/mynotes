package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    EditText newPasswordEdt,confirmPasswordEdt;

    TextView goBack;
    Button changePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPasswordEdt = findViewById(R.id.new_password);
        confirmPasswordEdt = findViewById(R.id.confirm_new_password);
        goBack = findViewById(R.id.goBack);
        changePasswordBtn = findViewById(R.id.change_password_btn);

        changePasswordBtn.setOnClickListener((v -> changePass()));
        goBack.setOnClickListener((v -> backToMain()));
    }

    private void backToMain() {
        startActivity(new Intent(ChangePassword.this,MainActivity.class));
        finish();
    }

    private void changePass() {
        String new_password = newPasswordEdt.getText().toString();
        String confirm_password = confirmPasswordEdt.getText().toString();

        boolean check = validate(new_password,confirm_password);
         if(!check){
             return;
         }
        changePassword(new_password);

         startActivity(new Intent(ChangePassword.this,MainActivity.class));
         finish();

    }

    boolean validate(String newPassword,String confirmNewPassword){
        if(newPassword.isEmpty()){
            newPasswordEdt.setError("Can not be empty");
            return false;
        } else if (confirmNewPassword.isEmpty()){
            confirmPasswordEdt.setError("Can not be empty");
            return false;
        } else {
            //check length
            if (newPassword.length() < 6) {
                newPasswordEdt.setError("Password length is invalid");
                return false;
            }
            //check if new password match confirm password
            if (!newPassword.equals(confirmNewPassword)) {
                confirmPasswordEdt.setError("Password not matched");
                return false;
            }
        }
        return true;
    }
    private void changePassword(String newPassword){
              FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
              user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      //check if change success
                      if(task.isSuccessful()){
                          Utility.showToast(ChangePassword.this,"Change password successfully");
                      }
                  }
              });
    }
}