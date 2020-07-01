package com.bpbd.www.bpbdjember.network;

import com.bpbd.www.bpbdjember.response.ResponseBerita;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServices {

    //@TIPEMETHOD("API_END_POINT")
    @GET("berita")
    Call<ResponseBerita> request_show_all_berita();
    // <ModelData> nama_method()

}
