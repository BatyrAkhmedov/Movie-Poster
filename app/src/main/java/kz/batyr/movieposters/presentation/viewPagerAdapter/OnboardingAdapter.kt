package kz.batyr.movieposters.presentation.viewPagerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.OnboardingData
import kz.batyr.movieposters.presentation.MainActivity


class OnboardingAdapter (private val context: MainActivity)
    :RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>(){
    private val items = listOf(
        OnboardingData(context.getString(R.string.onboardind_page1), R.drawable.onboarding_image_page1),
        OnboardingData(context.getString(R.string.onboardind_page2), R.drawable.onboarding_image_page2),
        OnboardingData(context.getString(R.string.onboardind_page3), R.drawable.onboarding_image_page3),
        OnboardingData("", null)
       )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_item_page, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item  = items[position]
        holder.bind(item)

        val button  = holder.itemView.findViewById<Button>(R.id.onboarding_skip_button)
        button.setOnClickListener {
            context.closeOnboarding()
        }

    }



    inner class OnboardingViewHolder (itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind (data:OnboardingData){
            val textView = itemView.findViewById<TextView>(R.id.textView)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            val loader = itemView.findViewById<View>(R.id.onboardingLoader)
            val tab1 = itemView.findViewById<ImageView>(R.id.onboarding_tab_image_1)
            val tab2 = itemView.findViewById<ImageView>(R.id.onboarding_tab_image_2)
            val tab3 = itemView.findViewById<ImageView>(R.id.onboarding_tab_image_3)

            if (data.imageId!=null){
                when (data.imageId){
                    R.drawable.onboarding_image_page2 -> {
                        tab1.setImageResource(R.drawable.onboarding_tab_image_white)
                        tab2.setImageResource(R.drawable.onboarding_tab_image_black)
                    }
                    R.drawable.onboarding_image_page3 -> {
                        tab1.setImageResource(R.drawable.onboarding_tab_image_white)
                        tab2.setImageResource(R.drawable.onboarding_tab_image_white)
                        tab3.setImageResource(R.drawable.onboarding_tab_image_black)
                    }

                }
            textView.text = data.text
            imageView.setImageResource(data.imageId!!)}
            else {
                tab1.visibility = View.GONE
                tab2.visibility = View.GONE
                tab3.visibility = View.GONE
                loader.visibility = View.VISIBLE
                textView.visibility = View.GONE
            }
        }
    }


}