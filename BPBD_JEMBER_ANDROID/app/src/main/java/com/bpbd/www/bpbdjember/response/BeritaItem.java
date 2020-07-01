package com.bpbd.www.bpbdjember.response;

import com.google.gson.annotations.SerializedName;

public class BeritaItem{

	@SerializedName("GAMBAR")
	private String gAMBAR;

	@SerializedName("LOKASI")
	private String lOKASI;

	@SerializedName("NAMA")
	private String nAMA;

	@SerializedName("NOMER")
	private String nOMER;

	@SerializedName("JUDUL")
	private String jUDUL;

	@SerializedName("GAMBAR_BRT")
	private String gAMBARBRT;

	@SerializedName("ID_USR")
	private String iDUSR;

	@SerializedName("KETERANGAN")
	private String kETERANGAN;

	@SerializedName("EMAIL")
	private String eMAIL;

	@SerializedName("TANGGAL")
	private String tANGGAL;

	@SerializedName("ROLE")
	private String rOLE;

	@SerializedName("NIK")
	private String nIK;

	@SerializedName("STATUS_BRT")
	private String sTATUSBRT;

	@SerializedName("ALAMAT")
	private String aLAMAT;

	@SerializedName("FOTO_ORG")
	private Object fOTOORG;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("KATEGORI")
	private String kATEGORI;

	@SerializedName("PASSWORD")
	private String pASSWORD;

	@SerializedName("FOTO_KTP")
	private String fOTOKTP;

	@SerializedName("ID_KTR")
	private String iDKTR;

	@SerializedName("USERNAME")
	private String uSERNAME;

	@SerializedName("ID_BRT")
	private String iDBRT;

	@SerializedName("ISI_BERITA")
	private String iSIBERITA;

	public String getGAMBAR(){
		return gAMBAR;
	}

	public String getLOKASI(){
		return lOKASI;
	}

	public String getNAMA(){
		return nAMA;
	}

	public String getNOMER(){
		return nOMER;
	}

	public String getJUDUL(){
		return jUDUL;
	}

	public String getGAMBARBRT(){
		return gAMBARBRT;
	}

	public String getIDUSR(){
		return iDUSR;
	}

	public String getKETERANGAN(){
		return kETERANGAN;
	}

	public String getEMAIL(){
		return eMAIL;
	}

	public String getTANGGAL(){
		return tANGGAL;
	}

	public String getROLE(){
		return rOLE;
	}

	public String getNIK(){
		return nIK;
	}

	public String getSTATUSBRT(){
		return sTATUSBRT;
	}

	public String getALAMAT(){
		return aLAMAT;
	}

	public Object getFOTOORG(){
		return fOTOORG;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public String getKATEGORI(){
		return kATEGORI;
	}

	public String getPASSWORD(){
		return pASSWORD;
	}

	public String getFOTOKTP(){
		return fOTOKTP;
	}

	public String getIDKTR(){
		return iDKTR;
	}

	public String getUSERNAME(){
		return uSERNAME;
	}

	public String getIDBRT(){
		return iDBRT;
	}

	public String getISIBERITA(){
		return iSIBERITA;
	}

	@Override
	public String toString(){
		return
				"BeritaItem{" +
						"penulis = '" + nAMA + '\'' +
						",foto = '" + gAMBARBRT + '\'' +
						",id = '" + iDBRT + '\'' +
						",judul_berita = '" + jUDUL + '\'' +
						",tanggal_posting = '" + tANGGAL + '\'' +
						",isi_berita = '" + iSIBERITA + '\'' +
						"}";
	}
}