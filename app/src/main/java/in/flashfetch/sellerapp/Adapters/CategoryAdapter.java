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
    private List<String> headCategory;
    private HashMap<String, List<String>> subCategory;
    private HashMap<String,List<Boolean>> checks;

    public CategoryAdapter(Context context, List<String> listDataHeader,HashMap<String, List<String>> listChildData, HashMap<String,List<Boolean>> checks) {
        this.context = context;
        headCategory = listDataHeader;
        subCategory = listChildData;
        this.checks = checks;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subCategory.get(headCategory.get(groupPosition))
                .get(childPosition);
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
        checkBox.setChecked(checks.get(headCategory.get(groupPosition)).get(childPosition));
        final int gp = groupPosition;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Boolean> bools = checks.remove(headCategory.get(gp));
                bools.set(childPosition,!bools.get(childPosition));
                checks.put(headCategory.get(gp),bools);
            }
        });

        subcat.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return subCategory.get(headCategory.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headCategory.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return headCategory.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_main_category, null);
        }

        TextView category = (TextView) convertView.findViewById(R.id.main_category);
        category.setText(headerTitle);

        ImageView tap = (ImageView)convertView.findViewById(R.id.tap);

        tap.setImageDrawable(isExpanded ? ContextCompat.getDrawable(context,R.drawable.arrow_up) : ContextCompat.getDrawable(context,R.drawable.arrow_down));
        convertView.setBackgroundColor(context.getResources().getColor((isExpanded ? R.color.ff_green : R.color.icons)));
//        if(isExpanded)
//        {
//            tap.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.arrow_up));
//        }
//        else
//        {
//            tap.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.arrow_down));
//        }

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
