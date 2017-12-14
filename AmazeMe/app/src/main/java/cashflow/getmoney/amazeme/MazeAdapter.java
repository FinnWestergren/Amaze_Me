package cashflow.getmoney.amazeme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jesus on 12/13/17.
 */

public class MazeAdapter extends RecyclerView.Adapter<MazeAdapter.ViewHolder> {
    private List<String> mazes;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView completionDate;
        public TextView index;
        public TextView score;
        public Button load;
        public View layout;
        public Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();

            layout = v;
            completionDate = (TextView) v.findViewById(R.id.completionDate);
            index = (TextView) v.findViewById(R.id.listIndex);
            score = (TextView) v.findViewById(R.id.score);
            load = (Button) v.findViewById(R.id.loadButton);
        }
    }

    public MazeAdapter(List<String> dataSet) {
        mazes = dataSet;
    }

    @Override
    public MazeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.maze_completed_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Maze maze = values.get(position);
        holder.completionDate.setText(maze.getDate());
        holder.score.setText(maze.getScore());
        holder.index.setText(maze.getIndex() + ".");
        holder.load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, GoogleMapsActivity.class);
                intent.putExtra("INDEX", holder.index.getText());
                intent.putExtra("SCORE", holder.index.getText());
                intent.putExtra("USERID", );
                holder.context.startActivity(intent);
            }
        });
    }

}
