package com.comtip.tip.tangran.Adapter

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.textSize

/**
 * Created by TipRayong on 8/5/2561 9:58
 * TangRan
 */
class TypeAdapter(context: Context,
                  internal val data: List<String>)
    : ArrayAdapter<String>(context, R.layout.listview_custom, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val mConvertView: View
        if (convertView == null) {
            mConvertView = (context as Activity).layoutInflater.inflate(R.layout.listview_custom,
                    parent,
                    false)
            viewHolder = ViewHolder(mConvertView)
            mConvertView.tag = viewHolder
        } else {
            mConvertView = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.textviewCustom.text = data[position]
        viewHolder.textviewCustom.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        return mConvertView
    }

    private inner class ViewHolder(convertView: View) {
        val textviewCustom: TextView = convertView.findViewById(R.id.textviewCustom) as TextView
    }
}

