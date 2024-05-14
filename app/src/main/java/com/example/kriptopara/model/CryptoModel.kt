package com.example.kriptopara.model

import com.google.gson.annotations.SerializedName

data class CryptoModel(
    //normalde javada bunu yapmak zorundaydık. apideki jsonun adını serializable içine yazmalıydık ama
    // kotlinde gerek yok değişken adını aynı yapsak yeter

    //@SerializedName("currency")
    val currency: String,
   // @SerializedName("price")
    val price: String
)