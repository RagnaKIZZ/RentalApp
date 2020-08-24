package ahmedt.rentalapp.ui.transaksi.transaksimodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataItem implements Parcelable {

    @SerializedName("end_date")
    private String endDate;

    @SerializedName("jenis_order")
    private String jenisOrder;

    @SerializedName("order_date")
    private String orderDate;

    @SerializedName("metode_pembayaran")
    private String metodePembayaran;

    @SerializedName("nama")
    private String nama;

    @SerializedName("foto")
    private String foto;

	@SerializedName("harga")
	private String harga;

	@SerializedName("user_id")
	private String userId;

    @SerializedName("finish_date")
    private String finishDate;

    @SerializedName("tipe")
    private String tipe;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("mobil_id")
    private String mobilId;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("status")
    private String status;

    protected DataItem(Parcel in) {
        endDate = in.readString();
        jenisOrder = in.readString();
        orderDate = in.readString();
        metodePembayaran = in.readString();
        nama = in.readString();
        foto = in.readString();
        harga = in.readString();
        userId = in.readString();
        finishDate = in.readString();
        tipe = in.readString();
        orderId = in.readString();
        mobilId = in.readString();
        startDate = in.readString();
        status = in.readString();
    }

    public DataItem() {

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

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setJenisOrder(String jenisOrder) {
        this.jenisOrder = jenisOrder;
    }

    public String getJenisOrder() {
        return jenisOrder;
    }

    public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setMetodePembayaran(String metodePembayaran){
		this.metodePembayaran = metodePembayaran;
	}

	public String getMetodePembayaran(){
		return metodePembayaran;
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

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setFinishDate(String finishDate){
		this.finishDate = finishDate;
	}

	public String getFinishDate(){
		return finishDate;
	}

	public void setTipe(String tipe){
		this.tipe = tipe;
	}

	public String getTipe(){
		return tipe;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setMobilId(String mobilId){
		this.mobilId = mobilId;
	}

	public String getMobilId(){
		return mobilId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(endDate);
        dest.writeString(jenisOrder);
        dest.writeString(orderDate);
        dest.writeString(metodePembayaran);
        dest.writeString(nama);
        dest.writeString(foto);
        dest.writeString(harga);
        dest.writeString(userId);
        dest.writeString(finishDate);
        dest.writeString(tipe);
        dest.writeString(orderId);
        dest.writeString(mobilId);
        dest.writeString(startDate);
        dest.writeString(status);
    }
}