package ahmedt.rentalapp.ui.home.destinasi;

import android.os.Parcel;
import android.os.Parcelable;

public class itemDestinasi implements Parcelable {
    String nama, alamat, deskripsi;
    int foto;

    public itemDestinasi(String nama, String alamat, String deskripsi, int foto) {
        this.nama = nama;
        this.alamat = alamat;
        this.deskripsi = deskripsi;
        this.foto = foto;
    }

    protected itemDestinasi(Parcel in) {
        nama = in.readString();
        alamat = in.readString();
        deskripsi = in.readString();
        foto = in.readInt();
    }

    public static final Creator<itemDestinasi> CREATOR = new Creator<itemDestinasi>() {
        @Override
        public itemDestinasi createFromParcel(Parcel in) {
            return new itemDestinasi(in);
        }

        @Override
        public itemDestinasi[] newArray(int size) {
            return new itemDestinasi[size];
        }
    };

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(alamat);
        dest.writeString(deskripsi);
        dest.writeInt(foto);
    }
}
