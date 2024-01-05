package br.com.brunoti.kotlintodolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.brunoti.kotlintodolist.R
import br.com.brunoti.kotlintodolist.databinding.ItemReminderBinding
import br.com.brunoti.kotlintodolist.model.Reminder

class ReminderListAdapter(val reminderList: MutableList<Reminder>) :
    ListAdapter<Reminder, ReminderListAdapter.ReminderViewHolder>(Reminder.DIFF_CALLBACK) {
    var listenerEdit: (Reminder) -> Unit = {}
    var listenerDelete: (Reminder) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReminderBinding.inflate(inflater, parent, false)

        return ReminderViewHolder(binding)
    }

    override fun submitList(list: List<Reminder?>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminderList[position])
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    inner class ReminderViewHolder(private val binding: ItemReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Reminder) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = "${item.date} ${item.hour}"
            binding.ivMore.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Reminder) {
            val ivMore = binding.ivMore
            val popupMenu = PopupMenu(ivMore.context, ivMore)

            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }

                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}
