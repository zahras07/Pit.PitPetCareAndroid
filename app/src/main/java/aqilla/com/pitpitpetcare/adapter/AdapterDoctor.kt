package aqilla.com.pitpitpetcare.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aqilla.com.pitpitpetcare.BuildConfig
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.model.Doctor
import aqilla.com.pitpitpetcare.databinding.ItemDokterBinding
import aqilla.com.pitpitpetcare.ui.dokter.DetailDokterActivity
import com.bumptech.glide.Glide

class AdapterDoctor(val listDokter: List<Doctor>, val context: Context, val setOnClickDoctor: SetOnClickDoctor) :
    RecyclerView.Adapter<AdapterDoctor.ListViewHolderDoctor>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolderDoctor {
        val itemDokterBinding =
            ItemDokterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolderDoctor(itemDokterBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolderDoctor, position: Int) {
        holder.bind(listDokter[position])
    }

    override fun getItemCount(): Int = listDokter.size

    inner class ListViewHolderDoctor(private val itemDokterBinding: ItemDokterBinding) :
        RecyclerView.ViewHolder(itemDokterBinding.root) {
        fun bind(doctor: Doctor) {
            with(itemDokterBinding) {
                textViewNamaDokter.text = doctor.namaDokter
                textViewTTL.text = doctor.ttl
                textViewAlamatPraktek.text = doctor.alamatPraktek
                textViewNoRek.text = doctor.noRek
                Glide.with(itemDokterBinding.root).load(BuildConfig.BASE_URL_ASSET + doctor.foto)
                    .error(R.drawable.default_profile_user)
                    .placeholder(R.drawable.default_profile_user)
                    .into(itemDokterBinding.imageViewFoto)

                itemView.setOnClickListener {
                    setOnClickDoctor.onClickDoctor(doctor.id)
                }
            }
        }
    }

    interface SetOnClickDoctor{
        fun onClickDoctor(doctorId: Int)
    }
}