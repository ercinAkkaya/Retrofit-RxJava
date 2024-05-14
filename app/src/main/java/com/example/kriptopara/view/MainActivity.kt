package com.example.kriptopara.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kriptopara.R
import com.example.kriptopara.adapter.RecyclerViewAdapter
import com.example.kriptopara.model.CryptoModel
import com.example.kriptopara.service.CryptoApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects


//https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    //Disposable = kullan at
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        compositeDisposable = CompositeDisposable()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager

        loadData()


    }

    private fun loadData() {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
                .create(CryptoApi::class.java)

        compositeDisposable?.add(
            retrofit.getData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResponse)
        )

        /*
        val service = retrofit.create(CryptoApi::class.java)

        val call = service.getData()

        call.enqueue(object: Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        recyclerViewAdapter = RecyclerViewAdapter(cryptoModels!!, this@MainActivity)
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        recyclerView.adapter = recyclerViewAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                //print("hata aldık")
            }
        })*/
    }

    private fun handleResponse(cryptoList: List<CryptoModel>){

        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let{
            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.adapter = recyclerViewAdapter
        }

    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this, "clicked: {${cryptoModel.currency}}", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}