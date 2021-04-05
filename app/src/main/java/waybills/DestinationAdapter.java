package waybills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.pstu.vmmb.waybills.R;

public class DestinationAdapter extends RecyclerView.Adapter <DestinationAdapter.ViewHolder> {
    private Context context;
    private ArrayList Destination, Mileage;

    DestinationAdapter(Context context, ArrayList destination, ArrayList mileage) {
        this.context = context;
        this.Destination = destination;
        this.Mileage = mileage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Destination.setText(String.valueOf(Destination.get(position)));
        holder.Mileage.setText(String.valueOf(Mileage.get(position)));
    }

    @Override
    public int getItemCount() {
        return Destination.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Destination, Mileage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Destination = itemView.findViewById(R.id.destination);
            Mileage = itemView.findViewById(R.id.mileage);
        }
    }
}
