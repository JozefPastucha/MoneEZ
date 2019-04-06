package com.pv239_project

import com.pv239_project.model.Account
import com.pv239_project.model.AccountType
import java.util.*

class AccountDb : ArrayList<Account>() {

    val names: List<String>
        get() = map { it.name }.toList()

    init {
        //sort?
        add(Account(AccountType.Cash, "Acc1", "info", 10.0, 20.0, 10.0, "CZK"))
        add(Account(AccountType.Savings, "Acc2", "info", 10.0, 20.0, 10.0, "CZK"))
        add(Account(AccountType.Cash, "Acc3", "info", 10.0, 20.0, 10.0, "CZK"))
        add(Account(AccountType.Regular, "Acc4", "info", 10.0, 20.0, 10.0, "CZK"))
    }
}
