package com.example.afteruts

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.afteruts.Model.SubmitModel
import com.example.afteruts.databinding.ActivityTambahBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimpan.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val nim = binding.etNIM.text.toString()
        val namalengkap = binding.etNamaLengkap.text.toString()
        val usia = binding.etUsia.text.toString()

// Mengambil id RadioButton yang dipilih dari RadioGroup
        val genderRadioGroup = findViewById<RadioGroup>(R.id.rgGender)
        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        val selectedGender: String = when (selectedGenderId) {
            R.id.rbLakiLaki -> "Laki-Laki"
            R.id.rbPerempuan -> "Perempuan"
            else -> ""
        }

        val alamat = binding.etAlamat.text.toString()

        val retrofit = NetworkConfig().getService()

        if (nim.isNotEmpty() && namalengkap.isNotEmpty() && usia.isNotEmpty() && selectedGender.isNotEmpty() && alamat.isNotEmpty()) {
            retrofit.kirimBiodata(nim, namalengkap, usia, selectedGender, alamat)
                .enqueue(object : Callback<SubmitModel> {
                    override fun onResponse(
                        call: Call<SubmitModel>,
                        response: Response<SubmitModel>
                    ) {
                        if (response.isSuccessful) {
                            val hasil = response.body()
                            SweetAlertDialog(this@TambahActivity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Data berhasil disimpan")
                                .setConfirmClickListener {
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        it.dismissWithAnimation()
                                        finish()
                                    }, 1000) // Delay dalam milidetik (di sini 2000 ms atau 2 detik)
                                }
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Data gagal disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {4
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Kesalahan")
                .setContentText("Field tidak boleh kosong")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                }
                .show()
        }
    }
}