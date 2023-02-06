package com.openclassrooms.realestatemanager.ui.simulator

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.openclassrooms.realestatemanager.databinding.ActivityLoanSimulatorBinding
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt

@AndroidEntryPoint
class LoanSimulatorActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoanSimulatorViewModel>()

    private  val numberFormat = DecimalFormat("##,###.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoanSimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.borrowInput.doAfterTextChanged { viewModel.updateBorrowAmount(binding.borrowInput.text.toString()) }
        binding.interestInput.doAfterTextChanged { viewModel.updateInterestRate(binding.interestInput.text.toString()) }
        binding.durationInput.doAfterTextChanged { viewModel.updateLoanYearDuration(binding.durationInput.text.toString()) }
        binding.incomeInput.doAfterTextChanged { viewModel.updateIncome(binding.incomeInput.text.toString()) }
        binding.durationBorrowInput.doAfterTextChanged { viewModel.updateBorrowDuration(binding.durationBorrowInput.text.toString()) }

        binding.personalContributionInput.doAfterTextChanged {
            viewModel.updatePersonalContribution(
                binding.personalContributionInput.text.toString()
            )
        }

        binding.sliderInterest.addOnChangeListener { slider, _, _ ->
            // Responds to when slider's value is changed
            viewModel.updateInterestRate(slider.value.toString())

        }
        binding.sliderDuration.addOnChangeListener { slider, _, _ ->
            // Responds to when slider's value is changed
            viewModel.updateLoanYearDuration(slider.value.toString())

        }
        binding.sliderDurationBorrow.addOnChangeListener { slider, _, _ ->
            viewModel.updateBorrowDuration(slider.value.toString())
        }

        viewModel.loanSimulatorViewStateLiveData.observe(this) { viewState ->
            numberFormat.roundingMode = RoundingMode.UP
            binding.borrowInput.setTextKeepState(viewState.borrowAmount.toString())
            if (viewState.interestRate != null) {
                binding.interestInput.setTextKeepState((viewState.interestRate / 100).toString())
            }

            binding.durationInput.setTextKeepState(
                viewState.loanYearDuration?.roundToInt().toString()
            )
            binding.personalContributionInput.setTextKeepState(viewState.personalContribution.toString())
            binding.monthlyRepaymentValue.text =
                numberFormat.format(viewState.monthlyRepayment).toString()
            binding.totalInterestCostValue.text =
                numberFormat.format(viewState.totalInterestCost).toString()
            binding.totalBorrowValue.text =
                numberFormat.format(viewState.totalAmountToBorrow).toString()
            binding.monthlyRepaymentCapacityValue.text =
                numberFormat.format(viewState.monthlyIndebtedness).toString()
            binding.totalAmountLoanWithInterestValue.text =
                numberFormat.format(viewState.maxBorrowingCapacity).toString()
            binding.durationBorrowInput.setTextKeepState(
                viewState.borrowDurationInYear?.roundToInt().toString()
            )
            //sliders
            binding.sliderInterest.isEnabled = true
            binding.sliderInterest.value =
                if (viewState.interestRate != 0f && viewState.interestRate != null) {
                viewState.interestRate.div(100)
            } else {
                0F
            }
            binding.sliderInterest.setLabelFormatter { value: Float ->
                val format = NumberFormat.getInstance()
                format.maximumFractionDigits = 2
                format.format(value)
            }
            binding.sliderDuration.isEnabled = true
            if (viewState.loanYearDuration != null) {
                binding.sliderDuration.value = viewState.loanYearDuration.toFloat()
                binding.sliderDuration.setLabelFormatter { value: Float ->
                    val format = NumberFormat.getInstance()
                    format.maximumFractionDigits = 0
                    format.format(value)
                }
            }
            binding.sliderDurationBorrow.isEnabled = true
            if (viewState.borrowDurationInYear != null) {
                binding.sliderDurationBorrow.value = viewState.borrowDurationInYear.toFloat()
                binding.sliderDurationBorrow.setLabelFormatter { value: Float ->
                    val format = NumberFormat.getInstance()
                    format.maximumFractionDigits = 0
                    format.format(value)
                }
            }
        }
    }
}