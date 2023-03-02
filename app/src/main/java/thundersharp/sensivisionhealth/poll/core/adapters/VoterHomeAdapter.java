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
import thundersharp.sensivisionhealth.poll.ui.VoterHome;

public class VoterHomeAdapter extends RecyclerView.Adapter<VoterHomeAdapter.ViewHolder> {

    private final List<PollDataModel> pollDataModels;
    private OnDataModified onDataModified;

    public VoterHomeAdapter(List<PollDataModel> pollDataModels) {
        this.pollDataModels = pollDataModels;
    }

    public VoterHomeAdapter setOnDataModifiedListener(OnDataModified onDataModified) {
        this.onDataModified = onDataModified;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poll_view_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            PollDataModel pollDataModel = pollDataModels.get(position);
            UserPollResponseModel priorResponse = PollResponseDatabase.getInstance(holder.itemView.getContext())
                    .userPollResponsePresenter()
                    .findPollResponseByUserAndPollId(pollDataModel.id, VoterHome.loginModel.id);
            VoterHomePollAdapter voterHomePollAdapter;

            holder.yourPollStatus.setText(priorResponse == null ? "Didn't answered this poll" : "You answered this poll");
            holder.yourPollStatus.setBackgroundTintList(ColorStateList.valueOf(priorResponse == null ? Color.parseColor("#B20606") : Color.parseColor("#35AC1E")));
            holder.textResponse.setText(priorResponse == null ? "Your Response: N/A " : "Your Response: " + new JSONArray(pollDataModel.options).getJSONObject(getPosFromString(priorResponse.option_selected)).getString(priorResponse.option_selected));

            if (priorResponse == null && pollDataModel.status != StatusCodes.STATUS_CLOSED) {
                voterHomePollAdapter = new VoterHomePollAdapter(new JSONArray(pollDataModel.options), true);
            } else {
                voterHomePollAdapter = new VoterHomePollAdapter(new JSONArray(pollDataModel.options), false);
            }
            voterHomePollAdapter.setPollEditListener(pos -> {
                PollResponseDatabase.getInstance(holder.itemView.getContext())
                        .userPollResponsePresenter()
                        .insertAllPollsResponse(new UserPollResponseModel(System.currentTimeMillis() + "",
                                pollDataModel.id,
                                AdminHomePollAdapter.getKeyFromPos(pos),
                                VoterHome.loginModel.id));
                notifyDataSetChanged();
                Toast.makeText(holder.itemView.getContext(), "Response logged !! Thank you.", Toast.LENGTH_SHORT).show();
            });

            holder.recyclerOptions.setAdapter(voterHomePollAdapter);


            holder.questions.setText("Q: " + pollDataModel.question);
            holder.pollId.setText("Poll id: " + pollDataModel.id);

            switch (pollDataModel.status) {
                case StatusCodes.STATUS_PRIVATE:
                    holder.pollStatus.setText("Poll private");
                    holder.pollStatus.setTextColor(Color.BLACK);
                    holder.pollStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#198CC0")));
                    break;
                case StatusCodes.STATUS_PUBLIC:
                    holder.pollStatus.setText("Poll live");
                    holder.pollStatus.setTextColor(Color.WHITE);
                    holder.pollStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#35AC1E")));
                    break;
                case StatusCodes.STATUS_CLOSED:
                    holder.pollStatus.setText("Poll closed");
                    holder.pollStatus.setTextColor(Color.WHITE);
                    holder.pollStatus.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    break;
            }

            holder.itemView.setOnClickListener(m -> {
                if (pollDataModel.status != StatusCodes.STATUS_CLOSED) {
                    Toast.makeText(m.getContext(), "Cant show results until the poll is closed !!", Toast.LENGTH_SHORT).show();
                } else {
                    PollDisplayUtil.displayDialogPoll(holder.itemView.getContext(), pollDataModel);
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        if (pollDataModels != null) return pollDataModels.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView questions;
        private final TextView pollId;
        private final TextView pollStatus;
        private final TextView yourPollStatus;
        private final TextView textResponse;
        private final RecyclerView recyclerOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questions = itemView.findViewById(R.id.tittle);
            pollId = itemView.findViewById(R.id.pollId);
            pollStatus = itemView.findViewById(R.id.status);
            yourPollStatus = itemView.findViewById(R.id.pollStatus);
            recyclerOptions = itemView.findViewById(R.id.options_recycler);
            textResponse = itemView.findViewById(R.id.textResponse);

        }

    }


    public List<PollDataModel> getData() {
        return pollDataModels;
    }

    private int getPosFromString(String optionString) {
        switch (optionString) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            default:
                return 9;
        }
    }
}
