package com.bpbd.www.bpbdjember;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class PengaturanFragment extends Fragment {
    private EditText Username, Nama, Email, Alamat, Nomer;
    private Menu action;
    private Bitmap bitmap;
    //creating ImageView
    private CircleImageView profile_image;
    //creating button
    private Button Button_editphoto;
    private SessionManager sessionManager;
    private String getId;
    private String BaseUrl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        //Assign ID's to TextView and button
        setHasOptionsMenu(true);
        Username = view.findViewById(R.id.username);
        Nama = view.findViewById(R.id.nama);
        Email = view.findViewById(R.id.email);
        Nomer = view.findViewById(R.id.nomer);
        Alamat = view.findViewById(R.id.alamat);
//        EditButton = (Button) findViewById(R.id.button_edit);
        Button_editphoto = view.findViewById(R.id.button_editfoto);
        profile_image = view.findViewById(R.id.foto_profil);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        BaseUrl = SessionManager.BASE_URL;




        getUserDetail();

        //Receiving value into activity using intent
//        String TempHolder1 = getActivity().getIntent().getStringExtra("UsernameTAG");

//Adding click listener to logout button
        Button_editphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Showing echo response message from server
                chooseFile();          }
        });

        return view;
    }
    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String URL_SAVE = BaseUrl + "api/Profil/Profil";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
//                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("200")){
                                for (int i =0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strUsername = object.getString("USERNAME").trim();
                                    String strNama = object.getString("NAMA").trim();
                                    String strAlamat = object.getString("ALAMAT").trim();
                                    String strNomer = object.getString("NOMER").trim();
                                    String strEmail = object.getString("EMAIL").trim();

                                    Username.setText(strUsername);
                                    Nama.setText(strNama);
                                    Alamat.setText(strAlamat);
                                    Nomer.setText(strNomer);
                                    Email.setText(strEmail);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error Reading Detail" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error Detail" +error.toString() + BaseUrl + getId, Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID_USR", getId);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    @Override
    public void onResume() {
        super.onResume();
        getUserDetail();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit:
                Username.setFocusableInTouchMode(true);
                Nama.setFocusableInTouchMode(true);
                Alamat.setFocusableInTouchMode(true);
                Nomer.setFocusableInTouchMode(true);
                Email.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(Username, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return  true;
            case R.id.menu_save:
                SaveEditDetail();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                Username.setFocusableInTouchMode(false);
                Nama.setFocusableInTouchMode(false);
                Alamat.setFocusableInTouchMode(false);
                Nomer.setFocusableInTouchMode(false);
                Email.setFocusableInTouchMode(false);

                Username.setFocusable(false);
                Nama.setFocusable(false);
                Alamat.setFocusable(false);
                Nomer.setFocusable(false);
                Email.setFocusable(false);
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }

    }

    private void SaveEditDetail() {
        final String Username = this.Username.getText().toString().trim();
        final String Nama = this.Nama.getText().toString().trim();
        final String Alamat = this.Alamat.getText().toString().trim();
        final String Nomer = this.Nomer.getText().toString().trim();
        final String Email = this.Email.getText().toString().trim();
        final String Id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();

        String URL_EDIT = BaseUrl + "api/Profil/getprofil";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String success = jsonObject.getString("data");

                            if (success.equals("200")){
                                Toast.makeText(getContext(), "sukses", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("USERNAME", Username);
                params.put("NAMA", Nama);
                params.put("ALAMAT", Alamat);
                params.put("NOMER", Nomer);
                params.put("EMAIL", Email);
                params.put("ID_USR", Id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            }   catch (IOException e){
                e.printStackTrace();
            }
            UploadPicture(getId, getStringImage(bitmap));


        }
    }

    private void UploadPicture(final String id, final String GAMBAR) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        String URL_UPLOAD = BaseUrl + "api/Profil/getFoto";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");
                            if (status.equals("200") && error.equals("false")){
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Coba Lagi" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("ID_USR", id);
                params.put("GAMBAR", GAMBAR);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
    public  String  getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedimage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return  encodedimage;
    }

}
