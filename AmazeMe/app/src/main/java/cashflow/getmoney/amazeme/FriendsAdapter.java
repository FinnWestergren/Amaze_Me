package cashflow.getmoney.amazeme;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jesus on 11/15/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>  {
    private List<Friend> friendsList;
    private Context context;

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView name, username;

        public FriendViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameDisplay);
            username = (TextView) view.findViewById(R.id.usernameDisplay);
        }
    }

    public FriendsAdapter(Context context, List<Friend> friendsList) {
        this.friendsList = friendsList;
        this.context = context;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_item, parent, false);
        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FriendViewHolder holder, int position) {
        Friend friend = friendsList.get(position);
        holder.name.setText(friend.getName());
        holder.username.setText(friend.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Friend information here")
                        .setTitle(holder.name.getText().toString());

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

}
