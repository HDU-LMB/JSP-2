package hust.com.jsp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by hust on 2017/2/22.
 */

public class SyncScrollView extends ScrollView {
    private View view;


    public SyncScrollView(Context context) {
        super(context);
    }

    public SyncScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SyncScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onScrollChanged(int l,int t,int oldl,int oldt){
        super.onScrollChanged(l,t,oldl,oldt);
        if(view !=null){
            view.scrollTo(l,t);
        }
    }

    public void setScrollView(View view){
        this.view = view;
    }

}
