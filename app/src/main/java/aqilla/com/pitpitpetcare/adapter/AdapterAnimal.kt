package aqilla.com.pitpitpetcare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aqilla.com.pitpitpetcare.data.source.model.Animal
import aqilla.com.pitpitpetcare.databinding.ItemHewanBinding


class AdapterAnimal(val listHewan: List<Animal>) :
    RecyclerView.Adapter<AdapterAnimal.ListViewHolderAnimal>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolderAnimal {
        val itemAnimalBinding =
            ItemHewanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolderAnimal(itemAnimalBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolderAnimal, position: Int) {

        holder.bind(listHewan[position])
    }

    override fun getItemCount(): Int = listHewan.size

    inner class ListViewHolderAnimal(private val itemAnimalBinding: ItemHewanBinding) :
        RecyclerView.ViewHolder(itemAnimalBinding.root) {

        fun bind(animal: Animal) {
            with(itemAnimalBinding) {
                textViewNamaHewan.text = animal.namaHewan
                textViewNamaPemilik.text = animal.pelangganName
                textViewBerat.text = animal.berat + " Kg"
                textViewUmur.text = animal.umur
                textViewJenisHewan.text = animal.jenisHewan
            }
        }
    }
}

