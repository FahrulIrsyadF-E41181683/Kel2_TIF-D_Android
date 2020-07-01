package com.bpbd.www.bpbdjember.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBerita{

	@SerializedName("berita")
	private List<BeritaItem> berita;

	@SerializedName("totalPages")
	private int totalPages;

	@SerializedName("status")
	private boolean status;

	public List<BeritaItem> getBerita(){
		return berita;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
	public String toString(){
		return
				"ResponseBerita{" +
						"berita = '" + berita + '\'' +
						",status = '" + status + '\'' +
						"}";
	}
}