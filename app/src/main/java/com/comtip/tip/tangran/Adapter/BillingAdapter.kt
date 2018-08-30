package com.comtip.tip.tangran.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
class BillingAdapter(context: Context,
                     internal val data: List<Ordering>)
    : ArrayAdapter<Ordering>(context, R.layout.billing_listview, data) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val mConvertView: View
        if (convertView == null) {
            mConvertView = (context as Activity).layoutInflater.inflate(R.layout.billing_listview
                    , parent
                    , false)
            viewHolder = ViewHolder(mConvertView)
            mConvertView.tag = viewHolder
        } else {
            mConvertView = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.timeBillTV.text = data[position].orderTime

        if ("CANCEL" in data[position].orderTime){
            viewHolder.billingLayout.setBackgroundResource(R.drawable.cancel_list__shape)
        } else {
            viewHolder.billingLayout.setBackgroundResource(R.drawable.order_list__shape)
        }

        viewHolder.foodBillTV.text = data[position].food
        viewHolder.additionBillTV.text = data[position].addition

        viewHolder.priceBillTV.text = "" +
                data[position].price +
                context.getString(R.string.currency)

        viewHolder.amountBillTV.text = "× " + data[position].amount

        viewHolder.billTV.text = "" + (data[position].price * data[position].amount) +
                context.getString(R.string.currency)

       // viewHolder.priceBillTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
       // viewHolder.timeBillTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        viewHolder.foodBillTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
       // viewHolder.amountBillTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        //viewHolder.additionBillTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        viewHolder.billTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

        return mConvertView
    }

    private inner class ViewHolder(convertView: View) {
        val priceBillTV: TextView = convertView.findViewById(R.id.priceBillTV) as TextView
        val timeBillTV: TextView = convertView.findViewById(R.id.timeBillTV) as TextView
        val foodBillTV: TextView = convertView.findViewById(R.id.foodBillTV) as TextView
        val amountBillTV: TextView = convertView.findViewById(R.id.amountBillTV) as TextView
        val additionBillTV: TextView = convertView.findViewById(R.id.additionBillTV) as TextView
        val billTV: TextView = convertView.findViewById(R.id.billTV) as TextView
        val billingLayout: LinearLayout = convertView.findViewById(R.id.billingLayout) as LinearLayout
    }
}

