package com.comtip.tip.tangran.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.RoomDatabase.Ordering
import com.comtip.tip.tangran.textSize

/**
 * Created by TipRayong on 8/5/2561 9:58
 * TangRan
 */
class CheckingOrderAdapter(context: Context,
                           internal val data: List<Ordering>)
    : ArrayAdapter<Ordering>(context, R.layout.order_listview, data) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val mConvertView: View
        if (convertView == null) {
            mConvertView = (context as Activity).layoutInflater.inflate(R.layout.order_listview,
                    parent,
                    false)
            viewHolder = ViewHolder(mConvertView)
            mConvertView.tag = viewHolder
        } else {
            mConvertView = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        if ("UPDATE" in data[position].orderTime) {
            viewHolder.orderLayout.setBackgroundResource(R.drawable.update_list__shape)
        } else if ("CANCEL" in data[position].orderTime) {
            viewHolder.orderLayout.setBackgroundResource(R.drawable.cancel_list__shape)
        } else {
            viewHolder.orderLayout.setBackgroundResource(R.drawable.food_list__shape)
        }

        //viewHolder.clientBillingTV.text = data[position].client

        if ("CANCEL" in data[position].orderTime) {
            viewHolder.clientBillingTV.text = "CANCEL"
            viewHolder.timeOrderTV.text = data[position].orderTime.replace(" CANCEL", "")

        } else if ("UPDATE" in data[position].orderTime) {
            viewHolder.clientBillingTV.text = "UPDATE"
            viewHolder.timeOrderTV.text = data[position].orderTime.replace(" UPDATE", "")

        } else {
            viewHolder.timeOrderTV.text = data[position].orderTime
        }

        viewHolder.amountTV.text = data[position].amount.toString()
        viewHolder.additionTV.text = data[position].addition
        viewHolder.foodOrderTV.text = data[position].food

        viewHolder.foodOrderTV.setTextColor(Color.BLACK)
        viewHolder.additionTV.setTextColor(Color.BLACK)
        viewHolder.clientBillingTV.setTextColor(Color.BLACK)
        viewHolder.timeOrderTV.setTextColor(Color.BLACK)
        viewHolder.amountTV.setTextColor(Color.BLACK)

        viewHolder.foodOrderTV.setOnClickListener {
            if (viewHolder.foodOrderTV.currentTextColor == Color.BLACK) {
                viewHolder.foodOrderTV.setTextColor(Color.RED)
                viewHolder.additionTV.setTextColor(Color.RED)
                viewHolder.clientBillingTV.setTextColor(Color.RED)
                viewHolder.timeOrderTV.setTextColor(Color.RED)
                viewHolder.amountTV.setTextColor(Color.RED)
            } else {
                viewHolder.foodOrderTV.setTextColor(Color.BLACK)
                viewHolder.additionTV.setTextColor(Color.BLACK)
                viewHolder.clientBillingTV.setTextColor(Color.BLACK)
                viewHolder.timeOrderTV.setTextColor(Color.BLACK)
                viewHolder.amountTV.setTextColor(Color.BLACK)
            }
        }

        viewHolder.additionTV.setOnClickListener {
            if (viewHolder.additionTV.currentTextColor == Color.BLACK) {
                viewHolder.foodOrderTV.setTextColor(Color.RED)
                viewHolder.additionTV.setTextColor(Color.RED)
                viewHolder.clientBillingTV.setTextColor(Color.RED)
                viewHolder.timeOrderTV.setTextColor(Color.RED)
                viewHolder.amountTV.setTextColor(Color.RED)
            } else {
                viewHolder.foodOrderTV.setTextColor(Color.BLACK)
                viewHolder.additionTV.setTextColor(Color.BLACK)
                viewHolder.clientBillingTV.setTextColor(Color.BLACK)
                viewHolder.timeOrderTV.setTextColor(Color.BLACK)
                viewHolder.amountTV.setTextColor(Color.BLACK)
            }
        }

        viewHolder.clientBillingTV.setTypeface(null, Typeface.BOLD)
        viewHolder.clientBillingTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        viewHolder.timeOrderTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        viewHolder.foodOrderTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        viewHolder.amountTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        viewHolder.additionTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

        return mConvertView
    }

    private inner class ViewHolder(convertView: View) {
        val clientBillingTV: TextView = convertView.findViewById(R.id.clientBillingTV) as TextView
        val timeOrderTV: TextView = convertView.findViewById(R.id.timeOrderTV) as TextView
        val foodOrderTV: TextView = convertView.findViewById(R.id.foodOrderTV) as TextView
        val amountTV: TextView = convertView.findViewById(R.id.amountTV) as TextView
        val additionTV: TextView = convertView.findViewById(R.id.additionTV) as TextView
        val orderLayout: LinearLayout = convertView.findViewById(R.id.orderLayout) as LinearLayout
    }
}

