package com.shashank.anywheregpt.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.shashank.anywheregpt.R

class ExpandableListViewAdapter(
    private val mContext: Context, // group titles
    private val listDataGroup: List<String>,
    // child data
    private val listDataChild: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {


    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        return listDataChild[listDataGroup[groupPosition]]!![childPosititon]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        var view = convertView

        val childText = getChild(groupPosition, childPosition) as String

        if (view == null) {
            val layoutInflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.list_row_child, null)
        }


        val txtListChild = view?.findViewById(R.id.tv_row_child) as TextView
        txtListChild.text = childText

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listDataChild[listDataGroup[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return listDataGroup[groupPosition]
    }

    override fun getGroupCount(): Int {
        return listDataGroup.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var view = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val layoutInflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.list_row_group, null)
        }

        val textViewGroup = view?.findViewById<TextView>(R.id.tv_row_group)
        val imageViewRightArrow = view?.findViewById<ImageView>(R.id.iv_right_arrow)
        textViewGroup?.text = headerTitle
        if (isExpanded) {
            imageViewRightArrow?.setImageResource(R.drawable.ic_arrow_up)
        } else {
            imageViewRightArrow?.setImageResource(R.drawable.ic_arrow_down)
        }
        return view!!
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}