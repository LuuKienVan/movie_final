package com.example.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movie.databinding.ActivitySeatingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class SeatingActivity extends AppCompatActivity {
    ActivitySeatingBinding binding;
    public View row_view_1;
    public View row_view_3;
    public View row_view_2;
    public View row_view_4;
    public View row_view_5;
    ArrayList<Integer> textIdList =  new ArrayList<>(Arrays.asList(
            R.id.column_text_1,
            R.id.column_text_2,
            R.id.column_text_3,
            R.id.column_text_4,
            R.id.column_text_5,
            R.id.column_text_6,
            R.id.column_text_7,
            R.id.column_text_8));
    public int textViewCount = 8;
    TextView[] col_text_view_array_1;
    TextView[] col_text_view_array_2;
    TextView[] col_text_view_array_3;
    TextView[] col_text_view_array_4;
    TextView[] col_text_view_array_5;
    int quantityCount = 0;
    int price = 0;
    private ArrayList<Integer> seat_price = new ArrayList<Integer>() {{
        add(5);
        add(5);
        add(7);
        add(10);
        add(10);
        add(7);
        add(5);
        add(5);
    }};
    ArrayList<Integer> selectedSeat = new ArrayList<>();
    String rbString = "10:00 am to 12:00 am";
    String dateString;
    CheckBox chkboxCouple;
    public DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seating);

        String moviename = intent.getStringExtra("movie_name");
        String cinema_name = intent.getStringExtra("cinema_name");
        price = intent.getIntExtra("price", 0);

        binding.txtPrice.setText("$" + price);

        row_view_1 = findViewById(R.id.row_layout_A);
        row_view_2 = findViewById(R.id.row_layout_B);
        row_view_3 = findViewById(R.id.row_layout_C);
        row_view_4 = findViewById(R.id.row_layout_D);
        row_view_5 = findViewById(R.id.row_layout_E);

        col_text_view_array_1 = new TextView[textViewCount];
        col_text_view_array_2 = new TextView[textViewCount];
        col_text_view_array_3 = new TextView[textViewCount];
        col_text_view_array_4 = new TextView[textViewCount];
        col_text_view_array_5 = new TextView[textViewCount];

        // couple ticket
        chkboxCouple = binding.chkboxCouple;
        chkboxCouple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chkboxCouple.setChecked(isChecked);
                for (int i = 0; i < textViewCount; i++) {
                    int idx = i;
                    if (chkboxCouple.isChecked()) {
                        Log.d("COUPLE_CHECK", Boolean.toString(chkboxCouple.isChecked()));
                        checkSeatSelected(col_text_view_array_1, row_view_1, idx,true);
                        checkSeatSelected(col_text_view_array_2, row_view_2, idx, true);
                        checkSeatSelected(col_text_view_array_3, row_view_3, idx, true);
                        checkSeatSelected(col_text_view_array_4, row_view_4, idx, true);
                        checkSeatSelected(col_text_view_array_5, row_view_5, idx, true);
                    } else {
                        checkSeatSelected(col_text_view_array_1, row_view_1, idx,false);
                        checkSeatSelected(col_text_view_array_2, row_view_2, idx, false);
                        checkSeatSelected(col_text_view_array_3, row_view_3, idx, false);
                        checkSeatSelected(col_text_view_array_4, row_view_4, idx, false);
                        checkSeatSelected(col_text_view_array_5, row_view_5, idx, false);
                    }
                }
            }
        });
        chkboxCouple.setChecked(false);

        Button pickDateBtn = findViewById(R.id.pickDateBtn);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        dateString = day + "/" + (month + 1) + "/" + year;
        pickDateBtn.setText(dateString);
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeatingActivity.this.datePicker = new DatePickerDialog(getApplicationContext(), (DatePickerDialog.OnDateSetListener)(new DatePickerDialog.OnDateSetListener() {
                    public final void onDateSet(DatePicker view, int yearx, int monthx, int dayOfMonth) {
                        pickDateBtn.setText((CharSequence)(dayOfMonth + "/" + (monthx + 1) + "/" + yearx));
                        SeatingActivity.this.dateString = dayOfMonth + "/" + (monthx + 1) + "/" + yearx;

                        for(int i = 0; i < SeatingActivity.this.textViewCount; i++) {
                            col_text_view_array_1[i].setEnabled(true);
                            col_text_view_array_2[i].setEnabled(true);
                            col_text_view_array_3[i].setEnabled(true);
                            col_text_view_array_4[i].setEnabled(true);
                            col_text_view_array_5[i].setEnabled(true);

                            drawSeat(col_text_view_array_1[i], i);
                            drawSeat(col_text_view_array_2[i], i);
                            drawSeat(col_text_view_array_3[i], i);
                            drawSeat(col_text_view_array_4[i], i);
                            drawSeat(col_text_view_array_5[i], i);
                        }

                        quantityCount = 0;
                        binding.txtQuantity.setText("0");
                        binding.txtTotalPrice.setText("$0");
                        check(moviename, dateString, rbString, cinema_name);
                    }
                }), year, month, day);
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                for (int i = 0; i < textViewCount; i++) {
                    col_text_view_array_1[i].setEnabled(true);
                    col_text_view_array_2[i].setEnabled(true);
                    col_text_view_array_3[i].setEnabled(true);
                    col_text_view_array_4[i].setEnabled(true);
                    col_text_view_array_5[i].setEnabled(true);

                    drawSeat(col_text_view_array_1[i], i);
                    drawSeat(col_text_view_array_2[i], i);
                    drawSeat(col_text_view_array_3[i], i);
                    drawSeat(col_text_view_array_4[i], i);
                    drawSeat(col_text_view_array_5[i], i);
                }
                rbString = rb.getText().toString();
                check(moviename, dateString, rbString, cinema_name);
            }
        });
        check(moviename, dateString, "10:00 am to 12:00 am", cinema_name);

        String[] payment_methods = {"Cash", "Bank", "E-Wallet"};

        AlertDialog.Builder payment_alert_builder = new AlertDialog.Builder(this);
        payment_alert_builder.setTitle("Pick a payment method");
        payment_alert_builder.setItems(payment_methods, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Cash".equals(payment_methods[which])) {
                    payTicket();
                } else if ("E-Wallet".equals(payment_methods[which])) {
                    // to-be-implement
                } else if ("Bank".equals(payment_methods[which])) {
                    // te-be-implement
                }
            }
        });

        binding.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickDateBtn.getText().toString() != "Pick date") {
                    if (quantityCount > 0) {
                        payment_alert_builder.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select atleast 1 seat", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkSeatSelected(TextView[] arrayView, View rowView, int idx, boolean couple) {
        if (!couple) {
            arrayView[idx] = (TextView) rowView.findViewById(textIdList.get(idx));
            arrayView[idx].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayView[idx].setSelected((!arrayView[idx].isSelected()));
                    if (arrayView[idx].isSelected()) {
                        quantityCount = quantityCount + 1;
                        selectedSeat.add((Integer) seat_price.get(idx));
                    } else {
                        if (quantityCount == 0) {
                            quantityCount = 0;
                            selectedSeat.clear();
                        } else {
                            quantityCount = quantityCount - 1;
                            selectedSeat.remove((Integer) seat_price.get(idx));
                        }
                    }
                    setBackgroundTextView(arrayView, idx);
                    quantityCheck();
                }
            });
        } else {
            if (idx < textViewCount-1) {
                arrayView[idx] = (TextView) rowView.findViewById(textIdList.get(idx));
                arrayView[idx+1] = (TextView) rowView.findViewById(textIdList.get(idx+1));
                if (arrayView[idx+1].isEnabled()) {
                    arrayView[idx].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // same status
                            if (arrayView[idx].isSelected() == arrayView[idx+1].isSelected()) {
                                arrayView[idx].setSelected((!arrayView[idx].isSelected()));
                                arrayView[idx+1].setSelected(arrayView[idx].isSelected());
                                if (arrayView[idx].isSelected()) {
                                    quantityCount = quantityCount + 2;
                                    selectedSeat.add((Integer) seat_price.get(idx));
                                    selectedSeat.add((Integer) seat_price.get(idx+1));
                                } else {
                                    if (quantityCount == 0) {
                                        quantityCount = 0;
                                        selectedSeat.clear();
                                    } else {
                                        quantityCount = quantityCount - 2;
                                        selectedSeat.remove((Integer) seat_price.get(idx));
                                        selectedSeat.remove((Integer) seat_price.get(idx+1));
                                    }
                                }
                                setBackgroundTextView(arrayView, idx);
                                setBackgroundTextView(arrayView, idx+1);
                                quantityCheck();
                            }
                            // different status
                            else {
                                arrayView[idx].setSelected((!arrayView[idx].isSelected()));
                                arrayView[idx+1].setSelected(arrayView[idx].isSelected());
                                if (arrayView[idx].isSelected()) {
                                    quantityCount = quantityCount + 1;
                                    selectedSeat.add((Integer) seat_price.get(idx));
                                } else {
                                    if (quantityCount == 0) {
                                        quantityCount = 0;
                                        selectedSeat.clear();
                                    } else {
                                        quantityCount = quantityCount - 1;
                                        selectedSeat.remove((Integer) seat_price.get(idx));
                                    }
                                }
                                setBackgroundTextView(arrayView, idx);
                                setBackgroundTextView(arrayView, idx+1);
                                quantityCheck();
                            }
                        }
                    });
                } else {
                   arrayView[idx].setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Toast.makeText(getApplicationContext(), "Seat taken", Toast.LENGTH_SHORT).show();
                       }
                   });
                }
            } else {
                arrayView[idx] = (TextView) rowView.findViewById(textIdList.get(idx));
                arrayView[idx-1] = (TextView) rowView.findViewById(textIdList.get(idx-1));
                if (arrayView[idx-1].isEnabled()) {
                    arrayView[idx].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // same status
                            if (arrayView[idx].isSelected() == arrayView[idx-1].isSelected()) {
                                arrayView[idx].setSelected((!arrayView[idx].isSelected()));
                                arrayView[idx-1].setSelected(arrayView[idx].isSelected());
                                if (arrayView[idx].isSelected()) {
                                    quantityCount = quantityCount + 2;
                                    selectedSeat.add((Integer) seat_price.get(idx));
                                    selectedSeat.add((Integer) seat_price.get(idx-1));
                                } else {
                                    if (quantityCount == 0) {
                                        quantityCount = 0;
                                        selectedSeat.clear();
                                    } else {
                                        quantityCount = quantityCount - 2;
                                        selectedSeat.remove((Integer) seat_price.get(idx));
                                        selectedSeat.remove((Integer) seat_price.get(idx-1));
                                    }
                                }
                                setBackgroundTextView(arrayView, idx);
                                setBackgroundTextView(arrayView, idx-1);
                                quantityCheck();
                            }
                            // different status
                            else {
                                arrayView[idx].setSelected((!arrayView[idx].isSelected()));
                                arrayView[idx-1].setSelected(arrayView[idx].isSelected());
                                if (arrayView[idx].isSelected()) {
                                    quantityCount = quantityCount + 1;
                                    selectedSeat.add((Integer) seat_price.get(idx));
                                } else {
                                    if (quantityCount == 0) {
                                        quantityCount = 0;
                                        selectedSeat.clear();
                                    } else {
                                        quantityCount = quantityCount - 1;
                                        selectedSeat.remove((Integer) seat_price.get(idx));
                                    }
                                }
                                setBackgroundTextView(arrayView, idx);
                                setBackgroundTextView(arrayView, idx-1);
                                quantityCheck();
                            }
                        }
                    });
                } else {
                    arrayView[idx].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "Seat taken", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void check(String moviename, String dateString, String radioStr, String cinema_name){
        FirebaseFirestore.getInstance().collection("Tickets").document(moviename)
                .collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("CHECK_IPT", Boolean.toString(queryDocumentSnapshots.isEmpty()));
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> _list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentList: _list) {
                                Log.d("DOC_SNAP", documentList.getData().toString());
                                if (dateString.equals(documentList.get("date").toString()) &&
                                        radioStr.equals(documentList.get("time").toString()) &&
                                        cinema_name.equals(documentList.get("cinema_name").toString())
                                ) {
                                    String resultSeating = documentList.get("seat_no").toString();
                                    List<String> lstValues = Arrays.stream(resultSeating.split(",")).map(l -> l.trim()).collect(Collectors.toList());
                                    Log.d("lstValues", lstValues.toString());
                                    for (String s: lstValues) {
                                        int result = Integer.valueOf(String.valueOf(s.charAt(1)));
                                        if (String.valueOf(s.charAt(0)).equals("A")) {
                                            col_text_view_array_1[result].setEnabled(false);
                                            col_text_view_array_1[result].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_set_dialog_outline, null));
                                        } else if (String.valueOf(s.charAt(0)).equals("B")) {
                                            col_text_view_array_2[result].setEnabled(false);
                                            col_text_view_array_2[result].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_set_dialog_outline, null));
                                        } else if (String.valueOf(s.charAt(0)).equals("C")) {
                                            col_text_view_array_3[result].setEnabled(false);
                                            col_text_view_array_3[result].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_set_dialog_outline, null));
                                        } else if (String.valueOf(s.charAt(0)).equals("D")) {
                                            col_text_view_array_4[result].setEnabled(false);
                                            col_text_view_array_4[result].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_set_dialog_outline, null));
                                        } else if (String.valueOf(s.charAt(0)).equals("E")) {
                                            col_text_view_array_5[result].setEnabled(false);
                                            col_text_view_array_5[result].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_set_dialog_outline, null));
                                        }
                                    }
                                    quantityCount = 0;
                                    binding.txtQuantity.setText("0");
                                    binding.txtTotalPrice.setText("$0");
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void drawSeat(TextView view, int i) {
        if (i < 2 || i > 5) {
            view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_outline, null));
        } else if (i == 2 || i == 5) {
            view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_outline1, null));
        } else {
            view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_outline2, null));
        }
    }
    // get all seat from row
    private StringBuilder textResult(String rowName, TextView[] col_text_view_array) {
        StringBuilder row_result = new StringBuilder();
        for (int i = 0; i < textViewCount; i++) {
            if (col_text_view_array[i].isSelected()) {
                row_result.append(rowName + i + ",");
            }
        }
        return row_result;
    }

    private void payTicket() {
        int selectedId = binding.radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        StringBuilder SeatingStr = new StringBuilder();
        SeatingStr.append(textResult("A", col_text_view_array_1));
        SeatingStr.append(textResult("B", col_text_view_array_2));
        SeatingStr.append(textResult("C", col_text_view_array_3));
        SeatingStr.append(textResult("D", col_text_view_array_4));
        SeatingStr.append(textResult("E", col_text_view_array_5));

        SeatingStr.deleteCharAt(SeatingStr.length() - 1);
        Toast.makeText(getApplicationContext(), SeatingStr.toString(), Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        String moviename = intent.getStringExtra("movie_name");
        String cinema_name = intent.getStringExtra("cinema_name");
        HashMap registerHM = new HashMap();
        registerHM.put("movie_name", moviename);
        registerHM.put("movie_id", intent.getStringExtra("movieId"));
        registerHM.put("banner_image", intent.getStringExtra("banner_image"));
        registerHM.put("user_id",  FirebaseAuth.getInstance().getCurrentUser().getUid());
        registerHM.put("seat_no", SeatingStr.toString());
        registerHM.put("date", binding.pickDateBtn.getText().toString());
        registerHM.put("time", radioButton.getText().toString());
        double totalPrice = (double) price;
        registerHM.put("total_price", totalPrice);
        registerHM.put("price", price);
        registerHM.put("quantity", quantityCount);
        registerHM.put("cinema_name", cinema_name);
        registerHM.put("cinema_location", intent.getStringExtra("cinema_location"));

        FirebaseFirestore.getInstance().collection("Tickets").document(moviename)
                .collection("Users").add(registerHM).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task1) {
                        if (task1.isSuccessful()) {
                            FirebaseFirestore.getInstance().collection("Users_Tickets")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Users").add(registerHM).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task2) {
                                            if (task2.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                                                Intent intnt = new Intent(getApplicationContext(), MainActivity.class);
                                                intnt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intnt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intnt);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task2.getException()).toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task1.getException()).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // utils methods
    private void setBackgroundTextView(TextView[] textViewArray, int i) {
        if (textViewArray[i].isSelected()) {
            textViewArray[i].setBackground(ResourcesCompat.getDrawable(
                    getResources(),
                    R.drawable.button_light_set_dialog_outline,
                    null
            ));

        } else {
            drawSeat(textViewArray[i], i);
        }
    }

    private void quantityCheck() {
        binding.txtQuantity.setText(Integer.toString(quantityCount));
        Log.d("Seat", selectedSeat.toString());
        price = 0;
        if (quantityCount > 0) {
            for (int i = 0; i < selectedSeat.size(); i++) {
                price = price + selectedSeat.get(i);
            }
            //binding.txtTotalPrice.setText("$" + (price * quantityCount));
            binding.txtTotalPrice.setText("$" + price);
        } else {
            price = 0;
            binding.txtTotalPrice.setText("$" + price);
            Toast.makeText(this, "Please Select atleast 1 seat", Toast.LENGTH_LONG).show();
        }
    }
}