package com.donvigo.GettingThingsDone.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.donvigo.GettingThingsDone.R;
import com.donvigo.GettingThingsDone.Wrappers.Airline;
import com.donvigo.GettingThingsDone.Wrappers.ViewHolderAirlinesList;

import java.util.ArrayList;

/**
 * List adapter, which fills <code>listView</code> with data from <code>ArrayList</code>
 */
public class AirlinesListAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Activity activity;

	private ArrayList<Airline> items;

	public AirlinesListAdapter(Activity activity) {
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.activity = activity;
		items = new ArrayList<Airline>();
	}
	
    public void addItem(final Airline row) {
        items.add(row);
        notifyDataSetChanged();
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
        ViewHolderAirlinesList holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.airlines_list_row, null);
            holder = new ViewHolderAirlinesList();
            holder.airlineName = (TextView)convertView.findViewById(R.id.textViewAirlineName);
            holder.amount = (TextView)convertView.findViewById(R.id.textViewAmount);
            holder.chooser = (CheckBox)convertView.findViewById(R.id.checkBoxAirlineChooser);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderAirlinesList)convertView.getTag();
        }

        Airline row = items.get(position);
        initializeRowViews(holder, row, position);

        return convertView;
	}

    private void initializeRowViews(ViewHolderAirlinesList holder, final Airline row, int position){
        holder.airlineName.setText(row.getName());
        holder.amount.setText(String.valueOf(row.getTotalAmount()) + " " + row.getCurrency());
        holder.chooser.setFocusable(false);
        holder.chooser.setFocusableInTouchMode(false);

        holder.chooser.setChecked(row.isSelected());
        holder.chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox)view;
                row.setSelected(checkBox.isChecked());
            }
        });
    }

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}	
}