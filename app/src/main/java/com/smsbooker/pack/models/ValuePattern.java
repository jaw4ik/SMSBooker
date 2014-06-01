package com.smsbooker.pack.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yuriy on 26.05.2014.
 */
public class ValuePattern implements Parcelable {
    public String previousText;
    public String nextText;

    public ValuePattern(String previousText, String nextText) {
        this.previousText = previousText;
        this.nextText = nextText;
    }

    public ValuePattern(Bundle bundle, String patternName){
        this.previousText = bundle.getString(patternName + "PreviousPattern");
        this.nextText = bundle.getString(patternName + "NextPattern");
    }

    public Bundle toBundle(String patternName){
        Bundle bundle = new Bundle();

        return toBundle(bundle, patternName);
    }

    public Bundle toBundle(Bundle bundle, String patternName){
        bundle.putString(patternName + "PreviousPattern", this.previousText);
        bundle.putString(patternName + "NextPattern", this.nextText);

        return bundle;
    }

    //Parcelable implementation
    public ValuePattern(Parcel parcel) {
        this.previousText = parcel.readString();
        this.nextText = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(previousText);
        dest.writeString(nextText);
    }

    public static final Creator<ValuePattern> CREATOR = new Creator<ValuePattern>() {
        @Override
        public ValuePattern createFromParcel(Parcel source) {
            return new ValuePattern(source);
        }

        @Override
        public ValuePattern[] newArray(int size) {
            return new ValuePattern[0];
        }
    };
}
