package aka.studios.shribalaji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.activity.AddressActivity;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.model.Address;
import aka.studios.shribalaji.retrofit.ClientAPI;
import aka.studios.shribalaji.viewholder.AddressViewHolder;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressViewHolder> {

    ClientAPI clientAPI;
    int currentPosition = 0;

    private Context context;
    private AddressActivity activity;
    private ArrayList<Address> addressArrayList;

    public AddressAdapter(Context context, AddressActivity activity, ArrayList<Address> addressArrayList) {
        this.context = context;
        this.activity = activity;
        this.addressArrayList = addressArrayList;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_layout, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressArrayList.get(position);

        if (currentPosition == position) {
            holder.selectAddress.setChecked(true);
            holder.editLinear.setVisibility(View.VISIBLE);
        }
        else {
            holder.selectAddress.setChecked(false);
            holder.editLinear.setVisibility(View.GONE);
        }

        clientAPI = Common.getAPI();

        String fullName = address.getFirst_name() + " " + address.getLast_name();
        String landmark = address.getLandmark();
        String city = address.getCity();
        String pincode = address.getPincode();
        String mobile = address.getMobile();
        String type = String.valueOf(address.getType());

        holder.fullName.setText(fullName);
        holder.landmark.setText(landmark);
        holder.city.setText(city + "- ");
        holder.pincode.setText(pincode);
        holder.mobile.setText(mobile);

        if (type.equals("0")) {
            holder.type.setText("Home");
        }
        if (type.equals("1")) {
            holder.type.setText("Work");
        }
        if (type.equals("2")) {
            holder.type.setText("Other");
        }

        holder.selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                notifyDataSetChanged();
            }
        });

        holder.editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editAddress(position);
            }
        });

        holder.removeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeAddress(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }
}
