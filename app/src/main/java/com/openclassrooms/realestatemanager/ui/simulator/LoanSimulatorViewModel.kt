package com.openclassrooms.realestatemanager.ui.simulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt

@HiltViewModel
class LoanSimulatorViewModel @Inject constructor() : ViewModel() {

    private val loanSimulatorInitialViewState = LoanSimulatorViewState(
        borrowAmount = 0,
        interestRate = 0f,
        loanYearDuration = 1.0f,
        monthlyIncome = 0,
        personalContribution = 0,
        monthlyRepayment = 0.0f,
        maxBorrowingCapacity = 0,
        totalInterestCost = 0,
        totalAmountToBorrow = 0,
        totalMonthlyIncome = 0,
        borrowDurationInYear = 1.0f,
        monthlyIndebtedness = 0f,
    )

    private val loanSimulatorViewStateMutableStateFlow =
        MutableStateFlow(loanSimulatorInitialViewState)

    val loanSimulatorViewStateLiveData: LiveData<LoanSimulatorViewState> =
        loanSimulatorViewStateMutableStateFlow.asLiveData()

    private fun getLoanSimulatorViewState(): LoanSimulatorViewState {
        return loanSimulatorViewStateMutableStateFlow.value
    }

    private fun updateLoanSimulatorViewState(viewState: LoanSimulatorViewState) {
        loanSimulatorViewStateMutableStateFlow.value = viewState
    }

    private fun simulateMonthlyRepayment(viewState: LoanSimulatorViewState) {
        val amountToLoan: Int? =
            if (viewState.personalContribution != null && viewState.personalContribution > 0 && viewState.borrowAmount != null) {
                viewState.borrowAmount - viewState.personalContribution
            } else {
                viewState.borrowAmount
            }
        if (viewState.interestRate != null && viewState.interestRate != 0f && viewState.loanYearDuration != null && viewState.loanYearDuration != 0f && amountToLoan != null) {
            val monthlyInterest = viewState.interestRate / 100 / 12 / 100
            val nbrOfPeriod = viewState.loanYearDuration * 12

            val monthlyRepayment: Double = (amountToLoan * monthlyInterest) / (1 - (1 + (monthlyInterest).toDouble()).pow(
                (-(nbrOfPeriod).toDouble())
            ))
            val totalInterestCost: Int = ((monthlyRepayment * nbrOfPeriod) - amountToLoan).toInt()
            val totalAmountToBorrow: Int = amountToLoan + totalInterestCost

            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    monthlyRepayment = monthlyRepayment.absoluteValue.toFloat(),
                    totalInterestCost = totalInterestCost,
                    totalAmountToBorrow = totalAmountToBorrow
                )
            )
        }
    }

    private fun simulateIndebtednessCapacity(viewState: LoanSimulatorViewState) {
        if (viewState.totalMonthlyIncome != null && viewState.borrowDurationInYear != null) {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    monthlyIndebtedness = (viewState.totalMonthlyIncome / 3).toFloat(),
                    maxBorrowingCapacity = (viewState.totalMonthlyIncome / 3 * viewState.borrowDurationInYear * 12).toInt()
                )
            )
        }
    }

    fun updateBorrowAmount(loanAmount: String) {
        if (loanAmount != "") {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    borrowAmount = loanAmount.toInt()
                )
            )
        } else {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    borrowAmount = 0
                )
            )
        }
        simulateMonthlyRepayment(getLoanSimulatorViewState())
    }

    fun updateInterestRate(interestRate: String) {
        if (interestRate != "") {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    interestRate = (interestRate.toFloat() * 100).roundToInt().toFloat()
                )
            )
        } else {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    interestRate = 0f
                )
            )
        }
        simulateMonthlyRepayment(getLoanSimulatorViewState())
    }

    fun updateLoanYearDuration(loanYearDuration: String) {

        if (loanYearDuration != "") {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    loanYearDuration = loanYearDuration.toFloat()
                )
            )
        } else {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    loanYearDuration = 1f
                )
            )
        }
        simulateMonthlyRepayment(getLoanSimulatorViewState())
    }

    fun updatePersonalContribution(personalContribution: String) {
        if (personalContribution != "") {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    personalContribution = personalContribution.toInt()
                )
            )
        } else {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    personalContribution = 0
                )
            )
        }
        simulateMonthlyRepayment(getLoanSimulatorViewState())
    }

    fun updateIncome(income: String) {
        if (income != "") {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    totalMonthlyIncome = income.toInt()
                )
            )
        } else {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    totalMonthlyIncome = 0
                )
            )
        }
        simulateIndebtednessCapacity(getLoanSimulatorViewState())
    }

    fun updateBorrowDuration(borrowDuration: String) {
        if (borrowDuration != "") {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    borrowDurationInYear = borrowDuration.toFloat()
                )
            )
        } else {
            updateLoanSimulatorViewState(
                getLoanSimulatorViewState().copy(
                    borrowDurationInYear = 0f
                )
            )
        }
        simulateIndebtednessCapacity(getLoanSimulatorViewState())
    }
}