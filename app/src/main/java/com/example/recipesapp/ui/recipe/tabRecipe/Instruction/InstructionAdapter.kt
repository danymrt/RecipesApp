package com.example.recipesapp.ui.recipe.tabRecipe.Instruction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import java.util.ArrayList

class InstructionAdapter(val instructionList: ArrayList<String>) : RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name_instruction: TextView = itemView.findViewById(R.id.instruction)
        private var step : TextView = itemView.findViewById(R.id.step)

        fun bind(instrucion :String) {
            name_instruction.text = instrucion
            var numberstep = (absoluteAdapterPosition+1).toString()
            step.text = "Step "+ numberstep
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_instruction, parent, false)
        return InstructionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.bind( instructionList[position])
    }

    override fun getItemCount(): Int {
        return instructionList.size
    }

    fun clear() {
        val size: Int = instructionList.size
        instructionList.clear()
        notifyItemRangeRemoved(0, size)
    }

}