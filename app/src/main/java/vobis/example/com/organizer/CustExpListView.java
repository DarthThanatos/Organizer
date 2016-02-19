package vobis.example.com.organizer;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * Created by Vobis on 2015-08-02.
 */
public class CustExpListView extends ExpandableListView {

    public CustExpListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(600, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}