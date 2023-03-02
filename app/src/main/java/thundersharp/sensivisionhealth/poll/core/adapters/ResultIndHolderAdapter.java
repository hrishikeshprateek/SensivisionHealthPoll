package thundersharp.sensivisionhealth.poll.core.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.format.DateFormat;
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

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.interfaces.OnPollOptionClickListner;
import thundersharp.sensivisionhealth.poll.core.models.UserPollResponseModel;

public class ResultIndHolderAdapter extends RecyclerView.Adapter<ResultIndHolderAdapter.ViewHolder> {

    private List<UserPollResponseModel> pollDataModels;
    private OnPollOptionClickListner pollEditListner;


    public ResultIndHolderAdapter(List<UserPollResponseModel> pollDataModels) {
        this.pollDataModels = pollDataModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_result,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserPollResponseModel userPollResponseModel = pollDataModels.get(position);
        holder.tittle.setText(userPollResponseModel.userId);
        holder.time.setText(getTimeFromTimeStamp(userPollResponseModel.time));
        holder.option.setText(userPollResponseModel.option_selected);

    }

    public static String getTimeFromTimeStamp(String timeStamp) throws NumberFormatException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        long time;
        time = Long.parseLong(timeStamp);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a", cal).toString();
        return date;

    }

    @Override
    public int getItemCount() {
        if (pollDataModels!= null) return pollDataModels.size(); else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tittle,time,option;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.tittle);
            time = itemView.findViewById(R.id.time);
            option = itemView.findViewById(R.id.option);
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
