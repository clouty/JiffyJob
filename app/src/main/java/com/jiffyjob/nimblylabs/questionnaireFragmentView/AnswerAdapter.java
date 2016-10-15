package com.jiffyjob.nimblylabs.questionnaireFragmentView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;

import java.util.List;

/**
 * Created by NielPC on 8/5/2016.
 */
public class AnswerAdapter extends ArrayAdapter<String> {
    public AnswerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.questionnair_item, null);
        }
        String answerStr = getItem(position);
        final TextView selectionTV = (TextView) convertView.findViewById(R.id.selectionTV);
        TextView contentTV = (TextView) convertView.findViewById(R.id.contentTV);

        switch (position) {
            case 0:
                selectionTV.setText("A");
                break;
            case 1:
                selectionTV.setText("B");
                break;
            case 2:
                selectionTV.setText("C");
                break;
            default:
                selectionTV.setText("");
        }

        contentTV.setText(answerStr);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), selectionTV.getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
