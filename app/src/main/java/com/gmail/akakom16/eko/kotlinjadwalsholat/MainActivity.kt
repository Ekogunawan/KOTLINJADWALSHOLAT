package com.gmail.akakom16.eko.kotlinjadwalsholat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import layout.ClientAsyncTask
import layout.Kota
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
//TODO 1 : Yaitu program diatas adalah package yang sudah tersedia dilam class java dan kita tinggal memenggil package tersebut
class MainActivity : AppCompatActivity() {

    private var listKota: MutableList<Kota>? = null
    private var mKotaAdapter: ArrayAdapter<Kota>? = null
    //TODO 2 : Yaitu program diatas adalah perintah untuk mendeklarasikan variabel apa saja yang ada di class xml dan mendeklarasikan variabel baru
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TODO 3 : Yaitu program diatas mendeklarasikan method oncreat yaitu untuk ketika aplikasi pertama di ajalankan
        listKota = ArrayList<Kota>()
        mKotaAdapter = ArrayAdapter<Kota>(this, android.R.layout.simple_spinner_item, listKota)
        mKotaAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kota.adapter = mKotaAdapter
        kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //TODO 4 : Yaitu program diatas memanggil variabel yang sudah dideklarasikan dengan mengambil nilai dari class R dan mendeklarasikan array dengan mengambil nilai dari array adapter
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val kota = mKotaAdapter!!.getItem(position)
                loadJadwal(kota.id)
            }
        }
        loadKota()
        //TODO 5 : Yaitu program diatas adalah mendeklarasikan method untuk memilih array yang nilainya ada di array kota
    }
    fun loadKota() {
        try {
            var url = "https://api.banghasan.com/sholat/format/json/kota"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {
                    Log.d("KotaData", result)
                    try {
                        val jsonObj = JSONObject(result)
                        val jsonArray = jsonObj.getJSONArray("kota")
                        var kota: Kota? = null
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            kota = Kota()
                            kota!!.id = obj.getInt("id")
                            kota!!.nama = obj.getString("nama")
                            listKota!!.add(kota)
                        }
                        mKotaAdapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
            task.execute(url)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //TODO 6: Yaitu program diatas adalah mendeklarasikan isi darai method kota dengan menisikan kota mana saja yang disediakan oleh JSON API yang di sediankan oleh banghasan.com
    private fun loadJadwal(id: Int?) {
        try {
            val id_kota = id.toString()
            val current = SimpleDateFormat("yyyy-MM-dd")
            val tanggal = current.format(Date())
            var url = "https://api.banghasan.com/sholat/format/json/jadwal/kota/$id_kota/tanggal/$tanggal"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {
                    Log.d("JadwalData", result)
                    try {
                        val jsonObj = JSONObject(result)
                        val objJadwal = jsonObj.getJSONObject("jadwal")
                        val obData = objJadwal.getJSONObject("data")
                        tv_tanggal.text = obData.getString("tanggal")
                        tv_subuh.text = obData.getString("subuh")
                        tv_dzuhur.text = obData.getString("dzuhur")
                        tv_ashar.text = obData.getString("ashar")
                        tv_maghrib.text = obData.getString("maghrib")
                        tv_isya.text = obData.getString("isya")
                        Log.d("dataJadwal", obData.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
            task.execute(url)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //TODO 7: Yaitu program diatas adalah  mendeklarasikan method jadwal sholat dengan menisikan jadwal sholat  yang disediakan oleh JSON API yang di sediankan oleh banghasan.com

}
