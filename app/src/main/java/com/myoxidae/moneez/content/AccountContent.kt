package com.myoxidae.moneez.content

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object AccountContent {

    /**
     * An array of sample (content) items.
     */
    val ITEMS: MutableList<AccountItem> = ArrayList()

    /**
     * A map of sample (content) items, by ID.
     */
    val ITEM_MAP: MutableMap<Int, AccountItem> = HashMap()

    private val COUNT = 4

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createAccountItem(i))
        }
    }

    private fun addItem(item: AccountItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createAccountItem(position: Int): AccountItem {
        return AccountItem(position, "Account $position", 400.0)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A content item representing a piece of content.
     */

    data class AccountItem(val id: Int, val name: String, val balance: Double) {
        override fun toString(): String {
            return "Account: $name, Balance: $balance"
        }
    }
}
