package com.example.mynotes;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {// login

    EditText emailEdittext,passwordEdittext;
    Button LoginBtn;
    ProgressBar ProgressBar;
    TextView createAccountBtnTextView,tv_reset,tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEdittext = findViewById(R.id.email_edit_text);
        passwordEdittext = findViewById(R.id.password_edit_text);
        LoginBtn = findViewById(R.id.login_btn);
        ProgressBar = findViewById(R.id.progress_bar);
        tv_reset = findViewById(R.id.tv_reset);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);
        LoginBtn.setOnClickListener((v)-> loginUser() );
        createAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)) );
        tv_reset.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,ResetPassword.class)) );


    }

    void loginUser(){
        String email  = emailEdittext.getText().toString();
        String password  = passwordEdittext.getText().toString();


        boolean isvalidated = ValidateData(email,password);
        if(!isvalidated){
            return;
        }

        loginAccountInFirebase(email,password);}
    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();//
        changeinProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {//
            @Override//
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinProgress(false);
                if(task.isSuccessful()){
                    //login is success
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to mainactivity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else{
                        Utility.showToast(LoginActivity.this,"Email not verified, Please verify your email.");
                    }

                }else{
                    //login failed
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }
    void changeinProgress(boolean inProgress){
        if(inProgress){
            ProgressBar.setVisibility(View.VISIBLE);
            LoginBtn.setVisibility(View.GONE);
        }else{
            ProgressBar.setVisibility(View.GONE);
            LoginBtn.setVisibility(View.VISIBLE);
        }
    }
    boolean ValidateData(String email,String password){//
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){//check
            emailEdittext.setError("Email is invalid");//not
            return false;
        }
        if(password.length()<6){
            passwordEdittext.setError("Password length is invalid");//not
            return false;
        }
        return true;
    }
}//done