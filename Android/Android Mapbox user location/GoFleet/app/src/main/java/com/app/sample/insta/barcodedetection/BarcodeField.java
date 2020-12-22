package com.app.sample.insta.barcodedetection;

import android.os.Parcel;
import android.os.Parcelable;

/** Information about a barcode field. */
public class BarcodeField implements Parcelable {

  public static final Creator<BarcodeField> CREATOR =
      new Creator<BarcodeField>() {
        @Override
        public BarcodeField createFromParcel(Parcel in) {
          return new BarcodeField(in);
        }

        @Override
        public BarcodeField[] newArray(int size) {
          return new BarcodeField[size];
        }
      };

  final String label;
  final String value;

  public BarcodeField(String label, String value) {
    this.label = label;
    this.value = value;
  }

  private BarcodeField(Parcel in) {
    label = in.readString();
    value = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(label);
    dest.writeString(value);
  }
}
