package thundersharp.sensivisionhealth.poll.core.adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.enums.StatusCodes;
import thundersharp.sensivisionhealth.poll.core.interfaces.OnDataModified;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.models.UserPollResponseModel;
import thundersharp.sensivisionhealth.poll.core.room.PollDatabase;
import thundersharp.sensivisionhealth.poll.core.room.PollResponseDatabase;
import thundersharp.sensivisionhealth.poll.core.utils.PollDisplayUtil;
import thundersharp.sensivisionhealth.poll.ui.PollReportAcivity;

public class AdminHomeAdapter extends RecyclerView.Adapter<AdminHomeAdapter.ViewHolder> {

    private final List<PollDataModel> pollDataModels;
    private OnDataModified onDataModified;

    public AdminHomeAdapter(List<PollDataModel> pollDataModels) {
        this.pollDataModels = pollDataModels;
    }

    public AdminHomeAdapter setOnDataModifiedListener(OnDataModified onDataModified){
        this.onDataModified = onDataModified;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poll_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PollDataModel pollDataModel = pollDataModels.get(position);
        holder.questions.setText("Q: "+pollDataModel.question);
        holder.pollId.setText("Poll id: "+pollDataModel.id);
        try {
            holder.recyclerOptions.setAdapter(new AdminHomePollAdapter(new JSONArray(pollDataModel.options))
                    .setPollEditListener(jsonArray -> {
                        if (pollDataModel.status != StatusCodes.STATUS_PUBLIC) {
                            pollDataModel.options = jsonArray.toString();
                            PollDatabase.getInstance(holder.itemView.getContext())
                                    .pollPresenter()
                                    .updatePolls(pollDataModel);
                            notifyItemChanged(position, pollDataModel);
                            Toast.makeText(holder.itemView.getContext(), "Updated poll option", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(holder.itemView.getContext(), "Cant edit poll until the poll is Live !!", Toast.LENGTH_SHORT).show();
            }));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (pollDataModel.status){
            case StatusCodes.STATUS_PRIVATE:
                holder.pollStatus.setText("Poll private");
                holder.pollStatus.setTextColor(Color.BLACK);
                holder.pollStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#198CC0")));
                break;
            case StatusCodes.STATUS_PUBLIC:
                holder.pollStatus.setText("Poll live");
                holder.pollStatus.setTextColor(Color.WHITE);
                holder.pollStatus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                break;
            case StatusCodes.STATUS_CLOSED:
                holder.pollStatus.setText("Poll closed");
                holder.pollStatus.setTextColor(Color.WHITE);
                holder.pollStatus.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                break;
        }

        holder.privateStatus.setOnClickListener(n -> {
            PollDataModel pollDataModel1 = pollDataModel;
            if (pollDataModel1.status == StatusCodes.STATUS_PUBLIC){
                Toast.makeText(n.getContext(), "Poll is active now only it can be closed !!", Toast.LENGTH_SHORT).show();
            }else {
                pollDataModel1.status = StatusCodes.STATUS_PRIVATE;
                PollDatabase
                        .getInstance(holder.itemView.getContext())
                        .pollPresenter()
                        .updatePolls(pollDataModel1);
                notifyItemChanged(position, pollDataModel1);
                if (onDataModified != null) onDataModified.OnDataChanged();
            }
        });

        holder.pubLicStatus.setOnClickListener(n -> {
            PollDataModel pollDataModel1 = pollDataModel;
            pollDataModel1.status = StatusCodes.STATUS_PUBLIC;
            PollDatabase
                    .getInstance(holder.itemView.getContext())
                    .pollPresenter()
                    .updatePolls(pollDataModel1);
            notifyItemChanged(position,pollDataModel1);
            if (onDataModified != null) onDataModified.OnDataChanged();
        });

        holder.itemView.setOnClickListener(m->{
            if (pollDataModel.status != StatusCodes.STATUS_CLOSED){
                Toast.makeText(m.getContext(), "Cant show results until the poll is closed !!", Toast.LENGTH_SHORT).show();
            }else {
                PollDisplayUtil.displayDialogPoll(holder.closeStatus.getContext(), pollDataModel);
            }
        });

        holder.questions.setOnClickListener(o -> {
            if (pollDataModel.status != StatusCodes.STATUS_PUBLIC) {
                View view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.edit_dialog, null, false);
                AlertDialog alertDialog = new AlertDialog.Builder(holder.itemView.getContext())
                        .setView(view).setCancelable(false).create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                EditText data = view.findViewById(R.id.editText_email);
                data.setText(pollDataModel.question);
                view.findViewById(R.id.exit).setOnClickListener(n -> alertDialog.dismiss());
                view.findViewById(R.id.no).setOnClickListener(n -> {
                    //UPDATE LOGIC HERE
                    pollDataModel.question = data.getText().toString();
                    PollDatabase
                            .getInstance(holder.itemView.getContext())
                            .pollPresenter()
                            .updatePolls(pollDataModel);
                    Toast.makeText(holder.itemView.getContext(), "Updated poll !!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    notifyItemChanged(position,pollDataModel);

                });
            }else Toast.makeText(holder.itemView.getContext(), "Can't edit a poll when in public status.", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        if (pollDataModels!= null) return pollDataModels.size(); else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView questions;
        private final TextView pollId;
        private final TextView pollStatus;
        private final RecyclerView recyclerOptions;
        private final AppCompatButton privateStatus, pubLicStatus,closeStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questions = itemView.findViewById(R.id.tittle);
            pollId = itemView.findViewById(R.id.pollId);
            pollStatus = itemView.findViewById(R.id.status);
            recyclerOptions = itemView.findViewById(R.id.options_recycler);

            privateStatus = itemView.findViewById(R.id.privatSe);
            pubLicStatus = itemView.findViewById(R.id.publicS);
            closeStatus = itemView.findViewById(R.id.close);

            closeStatus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            PollDataModel pollDataModel1 = pollDataModels.get(getAdapterPosition());
            pollDataModel1.status = StatusCodes.STATUS_CLOSED;
            PollDatabase
                    .getInstance(view.getContext())
                    .pollPresenter()
                    .updatePolls(pollDataModel1);
            notifyItemChanged(getAdapterPosition(),pollDataModel1);
            if (onDataModified != null) onDataModified.OnDataChanged();
        }
    }

    public void removeItem(int position) {
        pollDataModels.remove(position);
        notifyItemRemoved(position);
    }

    public List<PollDataModel> getData() {
        return pollDataModels;
    }
}
