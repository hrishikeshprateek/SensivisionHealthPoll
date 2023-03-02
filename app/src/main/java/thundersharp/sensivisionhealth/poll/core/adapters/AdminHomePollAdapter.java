package thundersharp.sensivisionhealth.poll.core.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.interfaces.PollEditListner;

public class AdminHomePollAdapter extends RecyclerView.Adapter<AdminHomePollAdapter.ViewHolder> {

    private JSONArray pollDataModels;
    private PollEditListner pollEditListner;

    public AdminHomePollAdapter(JSONArray pollDataModels) {
        this.pollDataModels = pollDataModels;
    }

    public AdminHomePollAdapter setPollEditListener(PollEditListner pollEditListner){
        this.pollEditListner = pollEditListner;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_options_choices,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = pollDataModels.getJSONObject(position);
            switch (position){
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.tittle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View viewC) {
            try {
                View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.edit_dialog, null, false);
                AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                        .setView(view).setCancelable(false).create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                EditText data = view.findViewById(R.id.editText_email);
                data.setText(pollDataModels.getJSONObject(getAdapterPosition())
                        .getString(getKeyFromPos(getAdapterPosition())));
                view.findViewById(R.id.exit).setOnClickListener(n -> alertDialog.dismiss());
                view.findViewById(R.id.no).setOnClickListener(n -> {
                    //UPDATE LOGIC HERE
                    try {
                        Log.e("RRRRR",getAdapterPosition()+"");
                        pollDataModels.getJSONObject(getAdapterPosition()).put(getKeyFromPos(getAdapterPosition()),data.getText().toString());
                        if (pollEditListner == null)
                            Toast.makeText(itemView.getContext(), "Interface pollEditListener() Not Called", Toast.LENGTH_SHORT).show();
                        else {
                            pollEditListner.onPollOptionEdited(pollDataModels);
                            alertDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
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
