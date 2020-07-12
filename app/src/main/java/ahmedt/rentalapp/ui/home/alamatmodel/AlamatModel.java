package ahmedt.rentalapp.ui.home.alamatmodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlamatModel {

    @SerializedName("msg")
    private String msg;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
	private List<DataItem> data;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}
}