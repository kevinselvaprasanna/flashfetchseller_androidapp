package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import in.flashfetch.sellerapp.R;

/**
 * Created by SAM10795 on 30-01-2016.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    Typeface font;
    //TODO: Set typeface for text
    private List<String> headcategory; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> subcategory;
    private HashMap<String,List<Boolean>> checks;

    public CategoryAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, HashMap<String,List<Boolean>> checks) {
        this.context = context;
        headcategory = listDataHeader;
        subcategory = listChildData;
        this.checks = checks;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return subcategory.get(headcategory.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);


       /* font = Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Medium.ttf");*/

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_subcategory, null);
        }

        TextView subcat = (TextView) convertView
                .findViewById(R.id.subcat);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setChecked(checks.get(headcategory.get(groupPosition)).get(childPosition));
        final int gp = groupPosition;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Boolean> bools = checks.remove(headcategory.get(gp));
                bools.set(childPosition,!bools.get(childPosition));
                checks.put(headcategory.get(gp),bools);
            }
        });

        subcat.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return subcategory.get(headcategory.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headcategory.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return headcategory.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_main_category, null);
        }

        TextView category = (TextView) convertView
                .findViewById(R.id.main_category);
        category.setText(headerTitle);
        ImageView tap = (ImageView)convertView.findViewById(R.id.tap);
        if(isExpanded)
        {
            tap.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.arrow_up));
        }
        else
        {
            tap.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.arrow_down));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public HashMap<String, List<Boolean>> getChecks() {
        return checks;
    }
}
