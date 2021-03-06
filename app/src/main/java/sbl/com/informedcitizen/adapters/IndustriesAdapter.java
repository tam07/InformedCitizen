package sbl.com.informedcitizen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.models.Industry;


public class IndustriesAdapter extends ArrayAdapter<Industry> {

    // view cache to avoid refinding view that exists
    private static class ViewHolder {
        TextView nameTV;
        TextView totalAmtTV;
        TextView pacAmtTV;
        TextView individAmtTV;
    }


    public IndustriesAdapter(Context context, ArrayList<Industry> industries) {
        // CALLING WRONG PARENT CONSTRUCTOR, needs 3 ARGUMENTS!
        //super(context, R.layout.route_item);
        super(context, R.layout.industry_item, industries);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // current model instance
        final Industry currContributor = getItem(position);

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.industry_item, parent, false);

            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.industryTV);
            viewHolder.totalAmtTV = (TextView) convertView.findViewById(R.id.totalIndustryTV);
            viewHolder.pacAmtTV = (TextView) convertView.findViewById(R.id.IndustryPacsDollarsTV);
            viewHolder.individAmtTV = (TextView) convertView.findViewById(R.id.industryIndividDollarsTV);
            // tag row with inflated views
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTV.setText(currContributor.getName());
        String divider = ": $";
        viewHolder.totalAmtTV.setText("$" + currContributor.getTotalContrib());
        viewHolder.pacAmtTV.setText(divider + currContributor.getPacContrib());
        viewHolder.individAmtTV.setText(divider + currContributor.getIndividContrib());

        return convertView;
    }
}
