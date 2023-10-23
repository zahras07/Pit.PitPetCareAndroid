package aqilla.com.pitpitpetcare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.model.Paket
import aqilla.com.pitpitpetcare.databinding.ItemPackageServiceBinding
import com.bumptech.glide.Glide

class AdapterPackageService(
    val packageServiceList: List<Paket>,
    private val setOnClickPackageService: SetOnClickPackageService
) :
    RecyclerView.Adapter<AdapterPackageService.ListViewHolderPackageService>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolderPackageService {
        val itemPackageServiceBinding =
            ItemPackageServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolderPackageService(itemPackageServiceBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolderPackageService, position: Int) {
        holder.bind(packageServiceList[position])
    }

    override fun getItemCount(): Int = packageServiceList.size

    inner class ListViewHolderPackageService(private val itemPackageServiceBinding: ItemPackageServiceBinding) :
        RecyclerView.ViewHolder(itemPackageServiceBinding.root) {
        fun bind(packageService: Paket) {
            with(itemPackageServiceBinding) {
                packageServiceName.text = packageService.nama_paket

                when (packageService.layanan_id) {
                    1 -> {
                        Glide.with(itemView.context).load(R.drawable.ic_veterinary)
                            .into(imageViewFoto)
                    }

                    2 -> {
                        Glide.with(itemView.context).load(R.drawable.ic_grooming)
                            .into(imageViewFoto)
                    }

                    else -> {
                        Glide.with(itemView.context).load(R.drawable.baseline_pets_24)
                            .into(imageViewFoto)
                    }
                }

                itemView.setOnClickListener {
                    setOnClickPackageService.onClickPackageService(packageService)
                }
            }
        }
    }

    interface SetOnClickPackageService {
        fun onClickPackageService(packageService: Paket)
    }
}