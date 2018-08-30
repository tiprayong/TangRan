package com.comtip.tip.tangran.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.RoomDatabase.Food
import com.comtip.tip.tangran.isServer
import com.comtip.tip.tangran.textSize

/**
 * Created by TipRayong on 8/5/2561 9:58
 * TangRan
 */
class FoodAdapter(context: Context,
                  internal val data: List<Food>)
    : ArrayAdapter<Food>(context, R.layout.food_listview, data) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val mConvertView: View
        if (convertView == null) {
            mConvertView = (context as Activity).layoutInflater.inflate(R.layout.food_listview
                    , parent
                    , false)
            viewHolder = ViewHolder(mConvertView)
            mConvertView.tag = viewHolder
        } else {
            mConvertView = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.typeFoodTV.text = data[position].type
        viewHolder.nameFoodTV.text = data[position].name
        viewHolder.priceFoodTV.text = data[position].price.toString() + context.getString(R.string.currency)
        if (isServer) {
            viewHolder.mediaFoodTV.text = data[position].mediaUrl
        }

        //  viewHolder.typeFoodTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        viewHolder.nameFoodTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        viewHolder.priceFoodTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        // viewHolder.mediaFoodTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

        return mConvertView
    }

    private inner class ViewHolder(convertView: View) {
        val typeFoodTV: TextView = convertView.findViewById(R.id.typeFoodTV) as TextView
        val nameFoodTV: TextView = convertView.findViewById(R.id.nameFoodTV) as TextView
        val priceFoodTV: TextView = convertView.findViewById(R.id.priceFoodTV) as TextView
        val mediaFoodTV: TextView = convertView.findViewById(R.id.mediaFoodTV) as TextView
    }
}

