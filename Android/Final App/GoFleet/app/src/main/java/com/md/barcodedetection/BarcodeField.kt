/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.md.barcodedetection

import android.os.Parcel
import android.os.Parcelable

/** Information about a barcode field.  */
@Parcelize
data class BarcodeField(val label: String?, val value: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarcodeField> {
        override fun createFromParcel(parcel: Parcel): BarcodeField {
            return BarcodeField(parcel)
        }

        override fun newArray(size: Int): Array<BarcodeField?> {
            return arrayOfNulls(size)
        }
    }
}

annotation class Parcelize
