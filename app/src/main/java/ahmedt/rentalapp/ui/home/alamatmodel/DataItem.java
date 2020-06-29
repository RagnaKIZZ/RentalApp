package ahmedt.rentalapp.ui.home.alamatmodel;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("alamat_detail")
	private String alamatDetail;

	@SerializedName("kota")
	private String kota;

	@SerializedName("alamat_id")
	private String alamatId;

	public void setAlamatDetail(String alamatDetail){
		this.alamatDetail = alamatDetail;
	}

	public String getAlamatDetail(){
		return alamatDetail;
	}

	public void setKota(String kota){
		this.kota = kota;
	}

	public String getKota(){
		return kota;
	}

	public void setAlamatId(String alamatId){
		this.alamatId = alamatId;
	}

	public String getAlamatId(){
		return alamatId;
	}
}