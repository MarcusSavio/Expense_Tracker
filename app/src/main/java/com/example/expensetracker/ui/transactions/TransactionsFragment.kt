package com.example.expensetracker.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.data.database.DatabaseModule
import com.example.expensetracker.data.entity.Transaction
import com.example.expensetracker.data.repository.TransactionRepository
import com.example.expensetracker.databinding.FragmentTransactionsBinding
import kotlinx.coroutines.launch

class TransactionsFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DatabaseModule.getDatabase(requireContext())
        val transactionRepository = TransactionRepository(
            db.transactionDao(),
            db.accountDao()
        )

        adapter = TransactionsAdapter { transaction ->
            formatAmount(transaction)
        }

        binding.recyclerTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTransactions.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transactionRepository.getAllTransactions().collect { transactions ->
                    adapter.submitList(transactions)
                }
            }
        }

        // TODO: Navigate to add/edit transaction screen
        binding.fabAddTransaction.setOnClickListener {
            // Placeholder for add transaction flow
        }
    }

    private fun formatAmount(transaction: Transaction): String {
        val sign = when (transaction.type) {
            com.example.expensetracker.data.entity.TransactionType.INCOME -> "+"
            com.example.expensetracker.data.entity.TransactionType.EXPENSE -> "-"
            com.example.expensetracker.data.entity.TransactionType.TRANSFER -> ""
        }
        // For now, use raw amount and currency code from the transaction
        return "$sign${transaction.currency} ${"%.2f".format(transaction.amount)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

