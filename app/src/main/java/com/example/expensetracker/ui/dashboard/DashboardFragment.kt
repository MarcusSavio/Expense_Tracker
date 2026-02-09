package com.example.expensetracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.expensetracker.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    combine(
                        viewModel.totalBalance,
                        viewModel.netWorth,
                        viewModel.hideBalances
                    ) { total, net, hide ->
                        Triple(total, net, hide)
                    }.collect { (total, net, hide) ->
                        binding.totalBalanceValue.text =
                            if (hide) "••••••" else total
                        binding.netWorthValue.text =
                            if (hide) "••••••" else net
                    }
                }
                launch {
                    combine(
                        viewModel.monthlyIncome,
                        viewModel.monthlyExpense,
                        viewModel.monthlyNet,
                        viewModel.hideBalances
                    ) { inc, exp, net, hide ->
                        listOf(inc, exp, net, hide)
                    }.collect { (inc, exp, net, hideAny) ->
                        val hide = hideAny as Boolean
                        binding.monthlyIncomeValue.text =
                            if (hide) "••••••" else inc as String
                        binding.monthlyExpenseValue.text =
                            if (hide) "••••••" else exp as String
                        binding.monthlyNetValue.text =
                            if (hide) "••••••" else net as String
                    }
                }
                launch {
                    combine(
                        viewModel.fireNumber,
                        viewModel.fireProgress,
                        viewModel.hideBalances
                    ) { number, progress, hide ->
                        Triple(number, progress, hide)
                    }.collect { (number, progress, hide) ->
                        binding.fireNumberValue.text =
                            if (hide) "••••••" else number
                        binding.fireProgressValue.text =
                            if (hide) "••••••" else progress
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
