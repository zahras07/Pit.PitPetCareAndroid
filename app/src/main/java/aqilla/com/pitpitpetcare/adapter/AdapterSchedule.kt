package aqilla.com.pitpitpetcare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aqilla.com.pitpitpetcare.data.source.model.Schedule
import aqilla.com.pitpitpetcare.databinding.ItemJadwalBinding

class AdapterSchedule(
    val scheduleList: List<Schedule>,
) :
    RecyclerView.Adapter<AdapterSchedule.ListViewHolderSchedule>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolderSchedule {
        val itemJadwalBinding =
            ItemJadwalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolderSchedule(itemJadwalBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolderSchedule, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int = scheduleList.size

    inner class ListViewHolderSchedule(private val itemJadwalBinding: ItemJadwalBinding) :
        RecyclerView.ViewHolder(itemJadwalBinding.root) {
        fun bind(schedule: Schedule) {
            with(itemJadwalBinding) {
                dayName.text = schedule.name
                val stringBuilder = StringBuilder()

                schedule.scheduleDoctor.forEachIndexed { index, it ->
                    stringBuilder.append(it.dokter?.namaDokter)
                    if (index < schedule.scheduleDoctor.size - 1) {
                        stringBuilder.append(", ")
                    }
                }

                if (stringBuilder.toString().isEmpty()){
                    dokterName.text = "Tutup"
                }else{
                    dokterName.text = stringBuilder.toString()
                }



            }
        }
    }
}