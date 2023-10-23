package aqilla.com.pitpitpetcare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aqilla.com.pitpitpetcare.data.source.model.Schedule
import aqilla.com.pitpitpetcare.data.source.model.ScheduleDoctorItem
import aqilla.com.pitpitpetcare.databinding.ItemJadwalBinding
import aqilla.com.pitpitpetcare.databinding.ItemJadwalDokterBinding

class AdapterScheduleDoctor(
    val scheduleDoctorItemList: List<ScheduleDoctorItem>,
) :
    RecyclerView.Adapter<AdapterScheduleDoctor.ListViewHolderScheduleDoctor>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolderScheduleDoctor {
        val itemJadwalDokterBinding =
            ItemJadwalDokterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolderScheduleDoctor(itemJadwalDokterBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolderScheduleDoctor, position: Int) {
        holder.bind(scheduleDoctorItemList[position])
    }

    override fun getItemCount(): Int = scheduleDoctorItemList.size

    inner class ListViewHolderScheduleDoctor(private val itemJadwalDokterBinding: ItemJadwalDokterBinding) :
        RecyclerView.ViewHolder(itemJadwalDokterBinding.root) {
        fun bind(scheduleDoctorItem: ScheduleDoctorItem) {
            with(itemJadwalDokterBinding) {
                dayName.text = scheduleDoctorItem.day
                time.text = "${scheduleDoctorItem.jamMulai} -  ${scheduleDoctorItem.jamSelesai}"
            }
        }
    }
}