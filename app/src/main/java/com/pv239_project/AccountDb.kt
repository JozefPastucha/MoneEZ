package com.pv239_project

import java.util.*

class AccountDb : ArrayList<Account>() {

    val names: List<String>
        get() = map { it.name }.toList()

    init {
        //sort?
        add(Account(AccountType.Cash, "Acc1", "info"))
        add(Account(AccountType.Savings, "Acc2", "info"))
        add(Account(AccountType.Cash, "Acc3", "info"))
        add(Account(AccountType.Regular, "Acc4", "info"))
    }
}
