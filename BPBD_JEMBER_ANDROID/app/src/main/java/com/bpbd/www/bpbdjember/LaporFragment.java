package com.bpbd.www.bpbdjember;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bpbd.www.bpbdjember.helper.AdapterKategori;
import com.bpbd.www.bpbdjember.helper.DataKategori;
import com.bpbd.www.bpbdjember.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class LaporFragment extends Fragment {
    private EditText nama, alamat, email, lokasi, detail_laporan;
    private Spinner spinnerKategori;
    private Button btn_submit, btn_image;
    private List<DataKategori> listKategori = new ArrayList<DataKategori>();
    private String BaseUrl;
    private ProgressDialog progressDialog;
    private AdapterKategori adapter;
    private SessionManager sessionManager;
    private String tmpNama, tmpEmail, tmpAlamat, tmpLokasi, tmpDeskripsi, tmpKategori, tmpGambar;
    private Boolean CheckisEmpty;
    private Bitmap bitmap;
    private ImageView gambarBcn;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lapor, container, false);
        nama = view.findViewById(R.id.nama);
        alamat = view.findViewById(R.id.alamat);
        email = view.findViewById(R.id.email);
        lokasi = view.findViewById(R.id.lokasi);
        spinnerKategori = view.findViewById(R.id.spinnerKategori);
        detail_laporan = view.findViewById(R.id.detail_laporan);
        btn_image = view.findViewById(R.id.btn_image);
        btn_submit = view.findViewById(R.id.btn_submit);
        gambarBcn = view.findViewById(R.id.gambarBcn);
        sessionManager = new SessionManager(getContext());
        progressDialog = new ProgressDialog(getContext());
        BaseUrl = sessionManager.BASE_URL;


        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"Kategori yang kamu pilih :" + listKategori.get(position).getKategori(), Toast.LENGTH_LONG).show();
                tmpKategori = listKategori.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if (CheckisEmpty) {
                    inputLaporan();
                }else{
                    Toast.makeText(getContext(),"Mohon isi semua field diatas", Toast.LENGTH_LONG).show();
                }
            }
        });

        adapter = new AdapterKategori(getActivity(), listKategori);
        spinnerKategori.setAdapter(adapter);
        callData();
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callData() {

        listKategori.clear();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        showDialog();

        String URL_KATEGORI = BaseUrl + "api/lapor/getKategori";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_KATEGORI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String error = jsonObject.getString("error");
                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    DataKategori dataKategori = new DataKategori();

                                    dataKategori.setId(object.getString("ID_KTR"));
                                    dataKategori.setKategori(object.getString("KATEGORI"));

                                    listKategori.add(dataKategori);
                                }
                                adapter.notifyDataSetChanged();
                                hideDialog();
                            }else{
                                Toast.makeText(getContext(), "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        hideDialog();
                        Toast.makeText(getContext(), "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }

                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);

    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }


    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void CheckEditTextIsEmptyOrNot(){

        //Getting values from EditText.
        tmpNama = nama.getText().toString().trim();
        tmpAlamat = alamat.getText().toString().trim();
        tmpEmail = email.getText().toString().trim();
        tmpLokasi = lokasi.getText().toString().trim();
        tmpDeskripsi = detail_laporan.getText().toString().trim();

        CheckisEmpty = !TextUtils.isEmpty(tmpNama) && !TextUtils.isEmpty(tmpEmail) && !TextUtils.isEmpty(tmpAlamat) && !TextUtils.isEmpty(tmpLokasi)&& !TextUtils.isEmpty(tmpDeskripsi);
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            chooseFile();
        } else {
            Toast.makeText(getContext(), "Dibatalkan, Dibutuhkan izin akses penyimpanan", Toast.LENGTH_LONG).show();
        }
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), filePath);
                gambarBcn.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }

//            Toast.makeText(getContext(), getStringImage(bitmap), Toast.LENGTH_LONG).show();
            tmpGambar = getStringImage(bitmap);
        }
    }

    private void inputLaporan(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String URL_CHECK_EMAIL = BaseUrl + "api/lapor/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")) {
                                    Intent intent = new Intent(getContext(), MainActivity.class);
//                                    intent.putExtra("NAVIGATION", "AKUN");
                                    startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "Gagal Melaporkan Bencana", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NAMA_PELAPOR", tmpNama);
                params.put("ALAMAT", tmpAlamat);
                params.put("EMAIL", tmpEmail);
                params.put("KATEGORI", tmpKategori);
                params.put("LOKASI", tmpLokasi);
                params.put("DESKRIPSI", tmpDeskripsi);
                params.put("GAMBAR", tmpGambar);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
