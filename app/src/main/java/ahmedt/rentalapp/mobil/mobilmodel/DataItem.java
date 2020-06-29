package ahmedt.rentalapp.mobil.mobilmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataItem implements Parcelable {

	@SerializedName("warna")
	private String warna;

	@SerializedName("kursi")
	private String kursi;

	@SerializedName("tahun")
	private String tahun;

	@SerializedName("alamat_id")
	private String alamatId;

	@SerializedName("pintu")
	private String pintu;

	@SerializedName("air_bag")
	private String airBag;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("harga")
	private String harga;

	@SerializedName("jenis")
	private String jenis;

	@SerializedName("transmisi")
	private String transmisi;

	@SerializedName("tipe")
	private String tipe;

	@SerializedName("mobil_id")
	private String mobilId;

	@SerializedName("status")
	private String status;

	public DataItem(Parcel in) {
		warna = in.readString();
		kursi = in.readString();
		tahun = in.readString();
		alamatId = in.readString();
		pintu = in.readString();
		airBag = in.readString();
		nama = in.readString();
		foto = in.readString();
		harga = in.readString();
		jenis = in.readString();
		transmisi = in.readString();
		tipe = in.readString();
		mobilId = in.readString();
		status = in.readString();
	}

	public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
		@Override
		public DataItem createFromParcel(Parcel in) {
			return new DataItem(in);
		}

		@Override
		public DataItem[] newArray(int size) {
			return new DataItem[size];
		}
	};

    public DataItem() {

    }

	public void setWarna(String warna){
		this.warna = warna;
	}

	public String getWarna(){
		return warna;
	}

	public void setKursi(String kursi){
		this.kursi = kursi;
	}

	public String getKursi(){
		return kursi;
	}

	public void setTahun(String tahun){
		this.tahun = tahun;
	}

	public String getTahun(){
		return tahun;
	}

	public void setAlamatId(String alamatId){
		this.alamatId = alamatId;
	}

	public String getAlamatId(){
		return alamatId;
	}

	public void setPintu(String pintu){
		this.pintu = pintu;
	}

	public String getPintu(){
		return pintu;
	}

	public void setAirBag(String airBag){
		this.airBag = airBag;
	}

	public String getAirBag(){
		return airBag;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setHarga(String harga){
		this.harga = harga;
	}

	public String getHarga(){
		return harga;
	}

	public void setJenis(String jenis){
		this.jenis = jenis;
	}

	public String getJenis(){
		return jenis;
	}

	public void setTransmisi(String transmisi){
		this.transmisi = transmisi;
	}

	public String getTransmisi(){
		return transmisi;
	}

	public void setTipe(String tipe){
		this.tipe = tipe;
	}

	public String getTipe(){
		return tipe;
	}

	public void setMobilId(String mobilId){
		this.mobilId = mobilId;
	}

	public String getMobilId(){
		return mobilId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(warna);
		dest.writeString(kursi);
		dest.writeString(tahun);
		dest.writeString(alamatId);
		dest.writeString(pintu);
		dest.writeString(airBag);
		dest.writeString(nama);
		dest.writeString(foto);
		dest.writeString(harga);
		dest.writeString(jenis);
		dest.writeString(transmisi);
		dest.writeString(tipe);
		dest.writeString(mobilId);
		dest.writeString(status);
	}
}