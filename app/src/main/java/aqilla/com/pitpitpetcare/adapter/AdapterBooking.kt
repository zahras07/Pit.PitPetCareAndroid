package aqilla.com.pitpitpetcare.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.model.Booking
import aqilla.com.pitpitpetcare.data.source.model.User
import aqilla.com.pitpitpetcare.databinding.ItemBookingBinding
import com.explorindo.tokonedwiplseller.utils.ConvertCurrency

class AdapterBooking(
    val listBooking: List<Booking>,
    val user: User,
    private val setOnClickBooking: SetOnClickBooking
) :
    RecyclerView.Adapter<AdapterBooking.ListViewHolderBooking>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolderBooking {
        val itemBookingBinding =
            ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolderBooking(itemBookingBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolderBooking, position: Int) {
        holder.bind(listBooking[position])
    }

    override fun getItemCount(): Int = listBooking.size

    inner class ListViewHolderBooking(private val itemBookingBinding: ItemBookingBinding) :
        RecyclerView.ViewHolder(itemBookingBinding.root) {
        fun bind(booking: Booking) {
            with(itemBookingBinding) {
                bookingDate.text = booking.tgl_transaksi
                title.text = itemBookingBinding.root.context.resources.getString(R.string.booking)  +  " - Antrian Ke " + booking.nomor_antrian.toString()
                statusBooking.text = booking.status
                price.text = ConvertCurrency().toRupiah(booking.total.toDouble())
                serviceName.text = booking.layanan.layanan + " (" + booking.paket.nama_paket + ")"
                animalName.text = "Nama hewan ${booking.animal.namaHewan}"
                if (booking.dokter != null) {
                    doctorName.visibility = View.VISIBLE
                    doctorName.text = "Akan ditangani oleh ${booking.dokter.namaDokter}"
                }

                if (booking.status == "belum dikonfirmasi") {
                    cancelBooking.visibility = View.VISIBLE
                    cancelBooking.paintFlags = cancelBooking.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    cancelBooking.setOnClickListener {
                        setOnClickBooking.onClickCancelBooking(booking.id)
                    }
                }else {
                    cancelBooking.visibility = View.GONE
                }
            }
        }
    }

    interface SetOnClickBooking {
        fun onClickCancelBooking(bookingId: Int)
    }
}