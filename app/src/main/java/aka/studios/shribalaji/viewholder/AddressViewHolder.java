package aka.studios.shribalaji.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;

public class AddressViewHolder extends RecyclerView.ViewHolder {

    public TextView fullName;
    public TextView type;
    public TextView landmark;
    public TextView city;
    public TextView pincode;
    public TextView mobile;
    public TextView editAddress;
    public TextView removeAddress;
    public LinearLayout editLinear;
    public RadioButton selectAddress;

    public AddressViewHolder(@NonNull View itemView) {
        super(itemView);

        fullName = (TextView) itemView.findViewById(R.id.fullName);
        type = (TextView) itemView.findViewById(R.id.type);
        landmark = (TextView) itemView.findViewById(R.id.landmark);
        city = (TextView) itemView.findViewById(R.id.city);
        pincode = (TextView) itemView.findViewById(R.id.pincode);
        mobile = (TextView) itemView.findViewById(R.id.mobile);
        editAddress = (TextView) itemView.findViewById(R.id.editAddress);
        removeAddress = (TextView) itemView.findViewById(R.id.removeAddress);
        editLinear = (LinearLayout) itemView.findViewById(R.id.editLinear);
        selectAddress = (RadioButton) itemView.findViewById(R.id.selectAddress);
    }
}
