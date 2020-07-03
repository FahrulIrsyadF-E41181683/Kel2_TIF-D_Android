package com.bpbd.www.bpbdjember;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bpbd.www.bpbdjember.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLogin;
    EditText etnama, etusername, etalamat, etnomer, etemail, etpassword, etc_password;
    String BaseUrl, fnama, fusername, falamat, fnomer, femail, fpassword, fc_password;
    Button btn_regist;
    ProgressBar loading;
    RequestQueue requestQueue;
    Boolean CheckisEmpty;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        BaseUrl = SessionManager.BASE_URL;
        tvLogin = findViewById(R.id.tvLogin);
        loading = findViewById(R.id.loading);
        etnama = findViewById(R.id.etnama);
        etusername = findViewById(R.id.etusername);
        etalamat = findViewById(R.id.etalamat);
        etnomer = findViewById(R.id.etnomer);
        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        etc_password  = findViewById(R.id.etc_password);
        btn_regist = findViewById(R.id.btn_regist);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();

                if (CheckisEmpty){
                    if (fpassword.equals(fc_password)){
                        if (fpassword.length() >= 8){
                            Regis();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Password minimal 8 digit", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Password yang anda masukkan tidak cocok.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Mohon isi semua kolom Password", Toast.LENGTH_LONG).show();
                }
            }
        });
}

    private void Regis(){
        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);

        final String fnama = this.etnama.getText().toString().trim();
        final String fusername = this.etusername.getText().toString().trim();
        final String falamat = this.etalamat.getText().toString().trim();
        final String fnomer = this.etnomer.getText().toString().trim();
        final String femail = this.etemail.getText().toString().trim();
        final String fpassword = this.etpassword.getText().toString().trim();
        final String fc_password = this.etc_password.getText().toString().trim();

        String HttpUrl = BaseUrl + "api/login/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");

                            if (status.equals("200")){
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                btn_regist.setVisibility(View.VISIBLE);
                                finish();
                            } else {
                                btn_regist.setVisibility(View.VISIBLE);
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            btn_regist.setVisibility(View.VISIBLE);
                            Toast.makeText(RegisterActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Error Response" + volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", fnama);
                params.put("username", fusername);
                params.put("alamat", falamat);
                params.put("nomer", fnomer);
                params.put("email", femail);
                params.put("password", fpassword);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void CheckEditTextIsEmptyOrNot(){

        //Getting values from EditText.
        fnama = etnama.getText().toString().trim();
        fusername = etusername.getText().toString().trim();
        falamat = etalamat.getText().toString().trim();
        fnomer = etnomer.getText().toString().trim();
        femail = etemail.getText().toString().trim();
        fpassword = etpassword.getText().toString().trim();
        fc_password = etc_password.getText().toString().trim();


        CheckisEmpty = !TextUtils.isEmpty(fnama) && !TextUtils.isEmpty(fusername) && !TextUtils.isEmpty(falamat) && !TextUtils.isEmpty(fnomer)
                && !TextUtils.isEmpty(femail) && !TextUtils.isEmpty(fpassword) && !TextUtils.isEmpty(fc_password);
    }
}