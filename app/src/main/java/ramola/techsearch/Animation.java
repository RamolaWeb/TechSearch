package ramola.techsearch;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

public class Animation {
    public static void AnimateRecyclerItem(RecyclerView.ViewHolder holder,boolean down) {
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator scaleX=ObjectAnimator.ofFloat(holder.itemView,"scaleX",0.5f,0.8f,1.0f);
        ObjectAnimator scaleY=ObjectAnimator.ofFloat(holder.itemView,"scaleY",0.5f,0.8f,1.0f);
        ObjectAnimator translateY=ObjectAnimator.ofFloat(holder.itemView,"translationY",down==true?50:-50,0);
        animatorSet.playTogether(translateY,scaleX,scaleY);
        animatorSet.setDuration(700);
        animatorSet.start();
    }
}
