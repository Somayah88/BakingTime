package com.somayahalharbi.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {
    private StepAdapterOnClickHandler mOnClickHandler;
    private List<Step> stepsList = new ArrayList<>();

    public StepsAdapter(StepAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;

    }

    @NonNull
    @Override
    public StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapterViewHolder holder, int position) {
        String description = stepsList.get(position).getShortDescription();
        int id = stepsList.get(position).getId();
        holder.descriptionTextView.setText(description);
        holder.stepIdTextView.setText(String.valueOf(id));

    }

    @Override
    public int getItemCount() {
        if (stepsList.size() == 0)
            return 0;
        return stepsList.size();
    }

    public void setData(List<Step> steps) {
        clear();
        stepsList = steps;
        notifyDataSetChanged();

    }

    public void clear() {
        int size = stepsList.size();
        stepsList.clear();
        notifyItemRangeRemoved(0, size);

    }

    public void setStepsAdapterOnClickListener(StepAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    public interface StepAdapterOnClickHandler {
        void onClick(int position);
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_description)
        TextView descriptionTextView;
        @BindView(R.id.steps_id_tv)
        TextView stepIdTextView;

        public StepsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mOnClickHandler != null) {
                mOnClickHandler.onClick(getAdapterPosition());
            }

        }
    }
}
