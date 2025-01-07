package com.example.clientaccesscontrol.view.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.annotation.OptIn
import androidx.core.content.res.ResourcesCompat
import com.example.clientaccesscontrol.view.utils.ViewUtils
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.databinding.BottomSheetFilterBinding
import com.example.clientaccesscontrol.view.utils.FactoryViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors

class FilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFilterBinding
    private var badgeDrawable: BadgeDrawable? = null
    private val filterBottomSheetVM by viewModels<FilterBottomSheetVM> {
        FactoryViewModel.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.btnResetAll.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            @OptIn(ExperimentalBadgeUtils::class)
            override fun onGlobalLayout() {
                badgeDrawable =
                    BadgeDrawable.createFromResource(requireContext(), R.xml.filter_badge).apply {
                        horizontalOffsetWithText = binding.frameLayout.width / 4
                        verticalOffsetWithText =
                            binding.frameLayout.height / 2 + ViewUtils.dpToPx(
                                resources,
                                24f
                            )
                                .toInt() / 2
                        BadgeUtils.attachBadgeDrawable(
                            this,
                            binding.btnResetAll,
                            binding.frameLayout
                        )
                    }
                //updateBadgeDrawble()
                binding.btnResetAll.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

    }

    private fun updateBadgeDrawble(number: Int) {
        when (number > 0) {
            true -> {
                badgeDrawable?.number = number
                badgeDrawable?.backgroundColor =
                    MaterialColors.getColor(
                        requireView(),
                        com.google.android.material.R.attr.colorError
                    )
            }
            false -> {
                badgeDrawable?.clearNumber()
                badgeDrawable?.backgroundColor =
                    ResourcesCompat.getColor(resources, android.R.color.transparent, null)
            }
        }
    }
}