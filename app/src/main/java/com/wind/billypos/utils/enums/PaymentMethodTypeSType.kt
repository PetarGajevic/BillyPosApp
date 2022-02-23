package com.wind.billypos.utils.enums

enum class PaymentMethodTypeSType {

    /**
     * Banknotes and coins
     *
     */
     BANKNOTE,

    /**
     * Credit and debit card
     *
     */
     CARD,

    /**
     * Bank check
     *
     */
     CHECK,

    /**
     * Single-purpose voucher
     *
     */
     SVOUCHER,

    /**
     * Multi-purpose voucher
     *
     */
     MVOUCHER,

    /**
     * Seller's company cards and similar
     *
     */
     COMPANY,

    /**
     *
     * Invoice not yet paid. It will be paid by summary invoice.
     *
     *
     */
     ORDER,

    /**
     * Transaction account
     *
     */
     ACCOUNT,

    /**
     * Factoring
     *
     */
     FACTORING,

    /**
     * Compensation
     *
     */
     COMPENSATION,

    /**
     * Transfer of rights or debts
     *
     */
     TRANSFER,

    /**
     * Waiver of debts
     *
     */
     WAIVER,

    /**
     * Payment in kind (clearing)
     *
     */
    KIND,

    /**
     * Other cashless payments
     *
     */
     OTHER

}