package com.beeblebroxlabs.newsunrisealarm.presentation;

import static java.lang.Boolean.TRUE;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import butterknife.ButterKnife;
import com.beeblebroxlabs.newsunrisealarm.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import com.beeblebroxlabs.newsunrisealarm.presentation.ui.MainActivity;
import java.util.List;

/**
 * Created by devgr on 15-Sep-17.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder>{
  private List<String> values;
  private static final String TAG = "AlarmListAdapter";

  public class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.alarmDetailsTextView)
    public TextView alarmDetailsTextView;

    @BindView(R.id.alarmSwitch)
    public Switch alarmSwitch;
    public View layout;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }
  }
  public void add(int position, String item) {
    values.add(position, item);
    notifyItemInserted(position);
  }

  public void remove(int position) {
    values.remove(position);
    notifyItemRemoved(position);
  }

  public AlarmListAdapter(List<String> values) {
    this.values = values;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(
        parent.getContext());
    View v =
        inflater.inflate(R.layout.view_alarm_element, parent, false);
    ViewHolder viewHolder = new ViewHolder(v);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.alarmDetailsTextView.setText(values.get(position));
    holder.alarmSwitch.setChecked(TRUE);
    holder.alarmSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.v(TAG,"Alarm has been disabled");
      }
    });
  }

  @Override
  public int getItemCount() {
    return values.size();
  }
}
