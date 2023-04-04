package com.shashank.anywheregpt.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shashank.anywheregpt.R
import com.shashank.anywheregpt.databinding.FragmentFaqBinding
import com.shashank.anywheregpt.ui.base.BaseFragment
import com.shashank.anywheregpt.ui.viewmodels.EmptyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.HashMap

@AndroidEntryPoint
class FaqFragment : BaseFragment<EmptyViewModel, FragmentFaqBinding>() {

    private lateinit var expandableListViewAdapter: ExpandableListViewAdapter
    private lateinit var listDataGroup: MutableList<String>
    private lateinit var listDataChild: HashMap<String, List<String>>

    override val mViewModel: EmptyViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFaqBinding {
        return FragmentFaqBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        mViewBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            (activity as? AppCompatActivity)?.setSupportActionBar(layoutToolbar.toolbar)
            layoutToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white)
            layoutToolbar.tvToolbarTitle.text = getString(R.string.toolbar_title_faqs)
            layoutToolbar.toolbar.setNavigationOnClickListener {
                onBackNavigation()
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                onBackNavigation()
            }

            // ExpandableListView on child click listener
            elvFaq.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                //requireContext().showToast(listDataGroup[groupPosition] + " : " + listDataChild[listDataGroup[groupPosition]]!![childPosition])
                false
            }
            // ExpandableListView Group expanded listener
            elvFaq.setOnGroupExpandListener { groupPosition ->
                //requireContext().showToast(listDataGroup[groupPosition] + " " + getString(R.string.text_expanded))
            }
            // ExpandableListView Group collapsed listener
            elvFaq.setOnGroupCollapseListener { groupPosition ->
                //requireContext().showToast(listDataGroup[groupPosition] + " " + getString(R.string.text_collapsed))
            }
            // initializing the objects
            initObjects()
            // preparing list data
            initListData()
        }
    }


    /**
     * method to initialize the objects
     */
    private fun initObjects() {
        // initializing the list of groups
        listDataGroup = ArrayList()
        // initializing the list of child
        listDataChild = HashMap()
        // initializing the adapter object
        expandableListViewAdapter = ExpandableListViewAdapter(requireContext(), listDataGroup, listDataChild)
        // setting list adapter
        mViewBinding.elvFaq.setAdapter(expandableListViewAdapter)
    }

    /**
     * Preparing the list data
     */
    private fun initListData() {
        // Adding group data
        listDataGroup.add(getString(R.string.first_row))
        listDataGroup.add(getString(R.string.second_row))
        listDataGroup.add(getString(R.string.third_row))
        listDataGroup.add(getString(R.string.fourth_row))
        listDataGroup.add(getString(R.string.fifth_row))
        listDataGroup.add(getString(R.string.sixth_row))

        // list of First Row
        val firstRowList: MutableList<String> = ArrayList()
        var mArray: Array<String> = resources.getStringArray(R.array.string_array_first_row)
        for (item in mArray) {
            firstRowList.add(item)
        }
        // list of Second Row
        val secondRowList: MutableList<String> = ArrayList()
        mArray = resources.getStringArray(R.array.string_array_second_row)
        for (item in mArray) {
            secondRowList.add(item)
        }
        // list of Third Row
        val thirdRowList: MutableList<String> = ArrayList()
        mArray = resources.getStringArray(R.array.string_array_third_row)
        for (item in mArray) {
            thirdRowList.add(item)
        }
        // list of Fourth Row
        val fourthRowList: MutableList<String> = ArrayList()
        mArray = resources.getStringArray(R.array.string_array_fourth_row)
        for (item in mArray) {
            fourthRowList.add(item)
        }
        // list of Fifth Row
        val fifthRowList: MutableList<String> = ArrayList()
        mArray = resources.getStringArray(R.array.string_array_fifth_row)
        for (item in mArray) {
            fifthRowList.add(item)
        }

        // list of Fifth Row
        val sixthRowList: MutableList<String> = ArrayList()
        mArray = resources.getStringArray(R.array.string_array_sixth_row)
        for (item in mArray) {
            sixthRowList.add(item)
        }

        // Adding child data
        listDataChild[listDataGroup[0]] = firstRowList
        listDataChild[listDataGroup[1]] = secondRowList
        listDataChild[listDataGroup[2]] = thirdRowList
        listDataChild[listDataGroup[3]] = fourthRowList
        listDataChild[listDataGroup[4]] = fifthRowList
        listDataChild[listDataGroup[5]] = sixthRowList
        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged()
    }

    private fun onBackNavigation() {
        findNavController().popBackStack()
    }

}