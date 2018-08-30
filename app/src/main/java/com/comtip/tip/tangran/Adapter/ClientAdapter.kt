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
import com.comtip.tip.tangran.RoomDatabase.Client
import com.comtip.tip.tangran.textSize

/**
 * Created by TipRayong on 8/5/2561 9:58
 * TangRan
 */
class ClientAdapter(context: Context,
                    internal val data: List<Client>)
    : ArrayAdapter<Client>(context, R.layout.client_listview, data) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val mConvertView: View

        if (convertView == null) {
            mConvertView = (context as Activity).layoutInflater.inflate(R.layout.client_listview,
                    parent,
                    false)
            viewHolder = ViewHolder(mConvertView)
            mConvertView.tag = viewHolder
        } else {
            mConvertView = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.clientNameListTV.text = data[position].name
        viewHolder.clientLocationListTV.text = data[position].location

        viewHolder.clientNameListTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        //viewHolder.clientLocationListTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

        return mConvertView
    }

    private inner class ViewHolder(convertView: View) {
        val clientNameListTV: TextView = convertView.findViewById(R.id.clientNameListTV) as TextView
        val clientLocationListTV: TextView = convertView.findViewById(R.id.clientLocationListTV) as TextView
    }
}

