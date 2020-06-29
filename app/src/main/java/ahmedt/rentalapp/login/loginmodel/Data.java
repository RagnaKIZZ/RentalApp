package ahmedt.rentalapp.login.loginmodel;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("status_login")
	private String statusLogin;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("token_firebase")
	private String tokenFirebase;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("telepon")
	private String telepon;

	@SerializedName("token_login")
	private String tokenLogin;

	@SerializedName("email")
	private String email;

	public void setStatusLogin(String statusLogin){
		this.statusLogin = statusLogin;
	}

	public String getStatusLogin(){
		return statusLogin;
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

	public void setTokenFirebase(String tokenFirebase){
		this.tokenFirebase = tokenFirebase;
	}

	public String getTokenFirebase(){
		return tokenFirebase;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setTelepon(String telepon){
		this.telepon = telepon;
	}

	public String getTelepon(){
		return telepon;
	}

	public void setTokenLogin(String tokenLogin){
		this.tokenLogin = tokenLogin;
	}

	public String getTokenLogin(){
		return tokenLogin;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}