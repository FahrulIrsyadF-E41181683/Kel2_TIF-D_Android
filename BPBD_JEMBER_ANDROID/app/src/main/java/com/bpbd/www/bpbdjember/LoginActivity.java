package com.bpbd.www.bpbdjember;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bpbd.www.bpbdjember.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //creating textView to link tvRegis
    TextView tvRegis;
    //creating Edit text
    EditText etusername, etpassword;
    //creating button
    Button btn_login;
    //creating volley Requestqueue
    RequestQueue requestQueue;

    //create string variabel to hold the EditText value
    String BaseUrl, fusername, fpassword;

    //creating progress dialog
    ProgressDialog progressDialog;

    Boolean CheckEditText;

    String TempServerResponseMatchedValue = "Data yang dimasukkan sama";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assigning Text View to tvRegis
        tvRegis   = (TextView) findViewById(R.id.tvRegis);
        //Assigning ID's to EditText
        etusername = (EditText) findViewById(R.id.etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);

        //Assigning ID's to Button
        btn_login = (Button) findViewById(R.id.btn_login);

        //Creating volley new requestQueue
        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        //Assigning Activity this to progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);

        //Assigning sessionManager
        sessionManager = new SessionManager(this);

        BaseUrl = SessionManager.BASE_URL;

        //Adding click listener to Button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();
                if (CheckEditText){
                    Login();
                }else {
                    Toast.makeText(LoginActivity.this, "Harap diisi terlebih dahulu", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    //Creating username login function
    public void Login(){
        //Showing Progress dialog at user login time
        progressDialog.setMessage("Mohon tunggu sebentar");
        progressDialog.show();

        //creating string request with post method
        String HttpUrl = BaseUrl + "api/login/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        //hiding the progress dialog after all task complete
                        progressDialog.dismiss();

                        //matching server response message to our text
                        try{
                            JSONObject jsonObject = new JSONObject(ServerResponse);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("ID_USR").trim();
                                    sessionManager.createSession(id);


                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    btn_login.setVisibility(View.VISIBLE);
                                    finish();
                                }
                            } else {
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //hiding the progress dialog after all task complete
                        progressDialog.dismiss();

                        //showing error message if something goes wrong
                        Toast.makeText(LoginActivity.this, "Volley" + volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                //creating Map String params
                Map<String, String> params = new HashMap<>();

                //Adding all values to Params
                params.put("USERNAME", fusername);
                params.put("PASSWORD", fpassword);
                return params;
            }
        };

        //Creating RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        //Adding  the StringRequest object into requestQueue
        requestQueue.add(stringRequest);
    }
    public void CheckEditTextIsEmptyOrNot(){
        //Getting values from EditText
        fusername = etusername.getText().toString().trim();
        fpassword = etpassword.getText().toString().trim();

        //Checking whether EditText value is empty or not
        //if any of EditText is empty then set variable value as False
        //if any of EditText value is filled then set variable value as True
        CheckEditText = !TextUtils.isEmpty(fusername) && !TextUtils.isEmpty(fusername);

    }
}