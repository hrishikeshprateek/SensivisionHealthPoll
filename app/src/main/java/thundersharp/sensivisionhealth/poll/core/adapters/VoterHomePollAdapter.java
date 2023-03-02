package thundersharp.sensivisionhealth.poll.core.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.enums.StatusCodes;
import thundersharp.sensivisionhealth.poll.core.interfaces.OnPollOptionClickListner;
import thundersharp.sensivisionhealth.poll.core.interfaces.PollEditListner;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.models.UserPollResponseModel;
import thundersharp.sensivisionhealth.poll.core.room.PollResponseDatabase;
import thundersharp.sensivisionhealth.poll.ui.VoterHome;

public class VoterHomePollAdapter extends RecyclerView.Adapter<VoterHomePollAdapter.ViewHolder> {

    private JSONArray pollDataModels;
    private OnPollOptionClickListner pollEditListner;
    private boolean done;

    public VoterHomePollAdapter(JSONArray pollDataModels, boolean done) {
        this.pollDataModels = pollDataModels;
        this.done = done;
    }

    public VoterHomePollAdapter setPollEditListener(OnPollOptionClickListner pollEditListner){
        this.pollEditListner = pollEditListner;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_options_choices_voters,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = pollDataModels.getJSONObject(position);

            if (!done){
                holder.card.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor("#121213"))));
            }else {
                holder.card.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor("#6F6B6B"))));
            }

            switch (position) {
                case 0:
                    holder.tittle.setText(jsonObject.getString("A"));
                    break;
                case 1:
                    holder.tittle.setText(jsonObject.getString("B"));
                    break;
                case 2:
                    holder.tittle.setText(jsonObject.getString("C"));
                    break;
                case 3:
                    holder.tittle.setText(jsonObject.getString("D"));
                    break;
            }

            holder.tittle.setOnClickListener(o ->{
                if (done){
                    pollEditListner.onPollOptionClicked(position);
                }else
                    Toast.makeText(holder.itemView.getContext(), "Not allowed to vote now !!", Toast.LENGTH_SHORT).show();
            });



        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        if (pollDataModels!= null) return pollDataModels.length(); else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tittle;
        private CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.tittle);
            card = itemView.findViewById(R.id.card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View viewC) {

        }
    }

    public static String getKeyFromPos(int pos){
        switch (pos){
            case 0 :
                return "A";
            case 1 :
                return "B";
            case 2 :
                return "C";
            default:
                return "D";
        }
    }
}
