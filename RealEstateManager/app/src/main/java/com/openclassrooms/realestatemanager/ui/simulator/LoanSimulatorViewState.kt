package com.openclassrooms.realestatemanager.ui.simulator

data class LoanSimulatorViewState(
    val borrowAmount: Int?,
    val interestRate: Float?,
    val loanYearDuration: Float?,
    val monthlyIncome: Int?,
    val personalContribution: Int?,
    val monthlyRepayment: Float?,
    val maxBorrowingCapacity: Int?,
    val totalInterestCost: Int?,
    val totalAmountToBorrow: Int?,
    val totalMonthlyIncome: Int?,
    val borrowDurationInYear: Float?,
    val monthlyIndebtedness: Float?,
)
