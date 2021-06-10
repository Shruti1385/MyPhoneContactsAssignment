package com.example.myphonecontactsassignment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myphonecontactsassignment.Model.ContactDetails;
import com.example.myphonecontactsassignment.R;

import java.util.ArrayList;

public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.MyViewHolder>  {
    Context context;
    ArrayList<ContactDetails > contactDetailsArrayList;


    public ContactListRecyclerViewAdapter(Context context, ArrayList<ContactDetails> contactDetailsArrayList) {
        this.context = context;
        this.contactDetailsArrayList = contactDetailsArrayList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_details_item_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view); // pass the view to View Holder

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.contactName.setText(contactDetailsArrayList.get(position).getContactName());
        holder.contactPhoneNumber.setText(contactDetailsArrayList.get(position).getContactPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactDetailsArrayList.size();
    }



     public class MyViewHolder extends RecyclerView.ViewHolder {
            // init the item view's
            TextView contactName,contactPhoneNumber;

            public MyViewHolder(View itemView) {
                super(itemView);
                // get the reference of item view's
                contactName = (TextView) itemView.findViewById(R.id.contact_name);
                contactPhoneNumber = (TextView) itemView.findViewById(R.id.contact_phonenumber);

            }
     }

}
