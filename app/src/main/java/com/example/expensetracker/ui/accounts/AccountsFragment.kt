package com.example.expensetracker.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentAccountsBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AccountsFragment : Fragment() {
    private var _binding: FragmentAccountsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountsViewModel by viewModels()

    private lateinit var adapter: AccountsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)

        adapter = AccountsAdapter(
            hideBalances = viewModel.hideBalances.value,
            formatBalance = { viewModel.formatBalance(it) },
            onAccountClick = { /* TODO: navigate to account detail or edit */ }
        )

        binding.recyclerAccounts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAccounts.adapter = adapter

        binding.switchPrivacy.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setHideBalances(isChecked)
        }

        val toolbarRoot = binding.toolbar.root
        toolbarRoot.findViewById<View>(com.example.expensetracker.R.id.btn_privacy_toggle).setOnClickListener {
            viewModel.togglePrivacy()
            binding.switchPrivacy.isChecked = viewModel.hideBalances.value
        }
        toolbarRoot.findViewById<View>(com.example.expensetracker.R.id.btn_settings).setOnClickListener {
            // TODO: open settings
        }

        binding.btnTransfer.setOnClickListener {
            // TODO: open transfer screen
        }
        binding.btnAddAccount.setOnClickListener {
            // TODO: open add account screen
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.accounts.collect { adapter.submitList(it) }
                }
                launch {
                    combine(
                        viewModel.totalBalance,
                        viewModel.netWorth,
                        viewModel.hideBalances
                    ) { total, net, hide ->
                        Triple(total, net, hide)
                    }.collect { (total, net, hide) ->
                        binding.totalBalanceValue.text = if (hide) "••••••" else total
                        binding.netWorthValue.text = if (hide) "••••••" else net
                        if (binding.switchPrivacy.isChecked != hide) {
                            binding.switchPrivacy.isChecked = hide
                        }
                        adapter.updateHideBalances(hide)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
