package com.example.expensetracker.ui.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.data.entity.Transaction
import com.example.expensetracker.data.entity.TransactionType
import com.example.expensetracker.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionsAdapter(
    private val formatAmount: (Transaction) -> String
) : ListAdapter<Transaction, TransactionsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), formatAmount)
    }

    class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        fun bind(
            transaction: Transaction,
            formatAmount: (Transaction) -> String
        ) {
            binding.transactionDescription.text =
                transaction.description ?: transaction.notes ?: "Transaction"

            binding.transactionAmount.text = formatAmount(transaction)

            val date = Date(transaction.date)
            binding.transactionDate.text = dateFormatter.format(date)

            val context = binding.root.context
            val colorRes = when (transaction.type) {
                TransactionType.INCOME -> com.example.expensetracker.R.color.positive_green
                TransactionType.EXPENSE -> com.example.expensetracker.R.color.negative_red
                TransactionType.TRANSFER -> com.example.expensetracker.R.color.text_secondary
            }
            binding.transactionAmount.setTextColor(
                androidx.core.content.ContextCompat.getColor(context, colorRes)
            )
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
            oldItem == newItem
    }
}

