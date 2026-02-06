package com.example.expensetracker.ui.accounts

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.data.entity.Account
import com.example.expensetracker.data.entity.AccountType
import com.example.expensetracker.databinding.ItemAccountBinding

class AccountsAdapter(
    private var hideBalances: Boolean,
    private val formatBalance: (Double) -> String,
    private val onAccountClick: (Account) -> Unit
) : ListAdapter<Account, AccountsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), hideBalances, formatBalance, onAccountClick)
    }

    fun updateHideBalances(hide: Boolean) {
        if (hideBalances != hide) {
            hideBalances = hide
            notifyItemRangeChanged(0, itemCount)
        }
    }

    class ViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            account: Account,
            hideBalances: Boolean,
            formatBalance: (Double) -> String,
            onAccountClick: (Account) -> Unit
        ) {
            binding.accountName.text = account.name
            binding.accountBalance.text = formatBalance(account.currentBalance)
            val changeText = when {
                account.currentBalance >= 0 -> "+0%"
                else -> "0%"
            }
            binding.accountChange.text = changeText
            binding.accountChange.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (account.currentBalance >= 0) R.color.positive_green else R.color.negative_red
                )
            )
            val color = ContextCompat.getColor(
                binding.root.context,
                when (account.type) {
                    AccountType.SAVINGS -> R.color.account_savings
                    AccountType.CURRENT -> R.color.account_checking
                    AccountType.CREDIT -> R.color.account_credit
                    AccountType.CASH -> R.color.account_cash
                }
            )
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(color)
            }
            binding.accountIconContainer.background = drawable
            binding.root.setOnClickListener { _ -> onAccountClick(account) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(a: Account, b: Account) = a.id == b.id
        override fun areContentsTheSame(a: Account, b: Account) = a == b
    }
}
