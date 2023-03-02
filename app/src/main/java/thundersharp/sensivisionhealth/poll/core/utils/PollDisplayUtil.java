package thundersharp.sensivisionhealth.poll.core.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.adapters.ResultIndHolderAdapter;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.models.UserPollResponseModel;
import thundersharp.sensivisionhealth.poll.core.room.PollResponseDatabase;

public class PollDisplayUtil {

    public void displayDialogPoll(Context context, PollDataModel pollDataModel){
        View view = LayoutInflater.from(context).inflate(R.layout.viewpoll_dialog, null, false);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view).setCancelable(false).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        List<UserPollResponseModel> totalPolls = PollResponseDatabase.getInstance(view.getContext()).userPollResponsePresenter().loadPollResponseByPollId(pollDataModel.id);
        List<UserPollResponseModel> totalPollsOptA = PollResponseDatabase.getInstance(view.getContext()).userPollResponsePresenter().findPollResponseByPollAndResponse(pollDataModel.id,"A");
        List<UserPollResponseModel> totalPollsOptB = PollResponseDatabase.getInstance(view.getContext()).userPollResponsePresenter().findPollResponseByPollAndResponse(pollDataModel.id,"B");
        List<UserPollResponseModel> totalPollsOptC = PollResponseDatabase.getInstance(view.getContext()).userPollResponsePresenter().findPollResponseByPollAndResponse(pollDataModel.id,"C");
        List<UserPollResponseModel> totalPollsOptD = PollResponseDatabase.getInstance(view.getContext()).userPollResponsePresenter().findPollResponseByPollAndResponse(pollDataModel.id,"D");


        RecyclerView recyclerView = view.findViewById(R.id.usersOption);
        TextView resultTextOne = view.findViewById(R.id.resTxt1);
        TextView resultTextTwo = view.findViewById(R.id.resTxt2);
        TextView resultTextThree = view.findViewById(R.id.resTxt3);
        TextView resultTextFour = view.findViewById(R.id.resTxt4);

        ProgressBar progressBarOne = view.findViewById(R.id.progressOpt1);
        ProgressBar progressBarTwo = view.findViewById(R.id.progressOpt2);
        ProgressBar progressBarThree = view.findViewById(R.id.progressOpt3);
        ProgressBar progressBarFour = view.findViewById(R.id.progressOpt4);

        try {
            resultTextOne.setText("Option A : Total poll percent is " + ((totalPollsOptA.size() / (totalPolls.size()*1.0)) * 100) + "%");
            resultTextTwo.setText("Option B : Total poll percent is " + ((totalPollsOptB.size()/(totalPolls.size()*1.0)) * 100) + "%");
            resultTextThree.setText("Option C : Total poll percent is " + ((totalPollsOptC.size()/(totalPolls.size()*1.0)) * 100) + "%");
            resultTextFour.setText("Option D : Total poll percent is " + (( totalPollsOptD.size()/(totalPolls.size()*1.0)) * 100) + "%");

            progressBarOne.setProgress((int) ((totalPollsOptA.size() / (totalPolls.size()*1.0)) * 100));
            progressBarTwo.setProgress((int) ((totalPollsOptB.size()/(totalPolls.size()*1.0)) * 100));
            progressBarThree.setProgress((int) ((totalPollsOptC.size()/(totalPolls.size()*1.0)) * 100));
            progressBarFour.setProgress((int) (( totalPollsOptD.size()/(totalPolls.size()*1.0)) * 100));

        }catch (ArithmeticException e){

        }

        recyclerView.setAdapter(new
                ResultIndHolderAdapter(totalPolls));

        view.findViewById(R.id.exit).setOnClickListener(n -> alertDialog.dismiss());
    }
}
