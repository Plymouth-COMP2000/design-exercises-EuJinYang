package com.example.hungryneat.guest;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryneat.R;
import com.example.hungryneat.reservation.Reservation;
import com.example.hungryneat.reservation.ReservationManager;
import com.example.hungryneat.table.Table;
import com.example.hungryneat.table.TableAdapter;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

public class G_Reservation extends AppCompatActivity {

    private GridView tableGrid;
    private ReservationManager reservationManager;
    private TableAdapter adapter;
    private AlertDialog currentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_reservation_layout);

        // Initialize reservation manager
        reservationManager = ReservationManager.getInstance();

        setupTableGrid();
        setupBottomNavigation();
    }

    private void setupTableGrid() {
        tableGrid = findViewById(R.id.tableGrid);
        adapter = new TableAdapter(this, reservationManager.getTables());
        tableGrid.setAdapter(adapter);

        tableGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Table selectedTable = reservationManager.getTables().get(position);
                handleTableClick(selectedTable);
            }
        });
    }

    private void handleTableClick(Table table) {
        switch (table.getStatus()) {
            case AVAILABLE:
                showReservationDialog(table);
                break;
            case PENDING:
                showPendingReservationDialog(table);
                break;
            case CONFIRMED:
                showConfirmedReservationDialog(table);
                break;
        }
    }

    private void showReservationDialog(Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(table.getName());

        // Add input fields for reservation details
        final View dialogView = getLayoutInflater().inflate(R.layout.g_reservation_make_dialog_layout, null);
        builder.setView(dialogView);

        // Remove the default buttons and use custom ones from XML
        builder.setCancelable(true);

        // Create the dialog
        currentDialog = builder.create();

        // Setup date and time pickers
        setupDatePicker(dialogView);
        setupTimePicker(dialogView);

        // Setup custom button listeners
        setupDialogButtons(dialogView, table, currentDialog);

        currentDialog.show();
    }

    private void setupDatePicker(View dialogView) {
        EditText reservationDate = dialogView.findViewById(R.id.reservation_date);

        reservationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(reservationDate);
            }
        });
    }

    private void showDatePicker(final EditText dateEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                        dateEditText.setText(selectedDate);
                    }
                },
                year, month, day
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Optional: Set maximum date to 1 year from now
        calendar.add(Calendar.YEAR, 1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void setupTimePicker(View dialogView) {
        NumberPicker reservationTime = dialogView.findViewById(R.id.reservation_time);

        // Set time values
        String[] timeValues = new String[] {
                "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM",
                "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM",
                "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM",
                "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM"
        };

        reservationTime.setMinValue(0);
        reservationTime.setMaxValue(timeValues.length - 1);
        reservationTime.setDisplayedValues(timeValues);
        reservationTime.setWrapSelectorWheel(false);

        // Set default value to a reasonable time (6:00 PM)
        reservationTime.setValue(12);
    }

    private void setupDialogButtons(View dialogView, Table table, AlertDialog dialog) {
        Button confirmButton = dialogView.findViewById(R.id.confirm_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameInput = dialogView.findViewById(R.id.customer_name);
                EditText dateInput = dialogView.findViewById(R.id.reservation_date);
                NumberPicker timeInput = dialogView.findViewById(R.id.reservation_time);

                String customerName = nameInput.getText().toString();
                String reservationDate = dateInput.getText().toString();
                String reservationTime = getSelectedTime(timeInput);

                if (!customerName.isEmpty() && !reservationDate.isEmpty()) {
                    // Combine date and time for the reservation
                    String fullReservationTime = reservationDate + " at " + reservationTime;

                    boolean success = reservationManager.makeReservation(
                            table.getName(), customerName, fullReservationTime);

                    if (success) {
                        adapter.notifyDataSetChanged();

                        // Close the current reservation dialog
                        dialog.dismiss();

                        // Show success dialog
                        showSuccessDialog(table);
                    } else {
                        Toast.makeText(G_Reservation.this,
                                "Failed to make reservation", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(G_Reservation.this,
                            "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private String getSelectedTime(NumberPicker timePicker) {
        String[] timeValues = new String[] {
                "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM",
                "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM",
                "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM",
                "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM"
        };
        return timeValues[timePicker.getValue()];
    }

    private void showSuccessDialog(Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.g_reservation_make_success_dialog_layout, null);
        builder.setView(dialogView);

        // Create the dialog
        AlertDialog successDialog = builder.create();
        successDialog.setCancelable(false);

        // Setup the back button
        Button backButton = dialogView.findViewById(R.id.back_reservation_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                // The table grid will already be refreshed, so we're back to reservation view
            }
        });

        successDialog.show();
    }

    private void showPendingReservationDialog(Table table) {
        Reservation reservation = reservationManager.getReservation(table.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(table.getName());

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.g_reservation_pending_dialog_layout, null);
        builder.setView(dialogView);

        // Create the dialog
        currentDialog = builder.create();
        currentDialog.setCancelable(true);

        // Set reservation data to the custom layout
        setupPendingDialogViews(dialogView, reservation, table);

        currentDialog.show();
    }

    private void setupPendingDialogViews(View dialogView, Reservation reservation, Table table) {
        // Set reservation details
        TextView nameText = dialogView.findViewById(R.id.nameText);
        nameText.setText(reservation.getCustomerName());

        // Set phone number
        TextView phoneText = dialogView.findViewById(R.id.phoneText);
        if (reservation.getPhoneNumber() != null && !reservation.getPhoneNumber().isEmpty()) {
            phoneText.setText(reservation.getPhoneNumber());
        } else {
            phoneText.setText("N/A");
        }

        // Parse party size from reservation time or set default
        TextView partySizeText = dialogView.findViewById(R.id.partySizeText);
        partySizeText.setText("1"); // Extract this from reservation data

        // Parse and format date
        TextView dateText = dialogView.findViewById(R.id.dateText);
        String reservationTime = reservation.getReservationTime();

        // Extract date part
        if (reservationTime.contains(" at ")) {
            String datePart = reservationTime.split(" at ")[0];
            dateText.setText(formatDate(datePart));
        } else {
            dateText.setText(reservationTime);
        }

        // Parse and format time
        TextView timeText = dialogView.findViewById(R.id.timeText);
        if (reservationTime.contains(" at ")) {
            String timePart = reservationTime.split(" at ")[1];
            timeText.setText(timePart);
        } else {
            timeText.setText(reservationTime);
        }

        // Setup button listeners
        setupPendingDialogButtons(dialogView, table, currentDialog);
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, MMM d", Locale.US);

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString; // Return original if parsing fails
        }
    }

    private void setupPendingDialogButtons(View dialogView, Table table, AlertDialog dialog) {
        Button editButton = dialogView.findViewById(R.id.editButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button closeButton = dialogView.findViewById(R.id.closeButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the pending dialog
                dialog.dismiss();

                // Show the edit reservation dialog
                showEditReservationDialog(table);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the pending dialog first
                dialog.dismiss();

                // Show the cancel confirmation dialog
                showCancelReservationDialog(table);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showCancelReservationDialog(Table table) {
        Reservation reservation = reservationManager.getReservation(table.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(table.getName());

        // Inflate the custom cancel dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.g_reservation_cancel_dialog_layout, null);
        builder.setView(dialogView);

        // Create the dialog
        AlertDialog cancelDialog = builder.create();
        cancelDialog.setCancelable(true);

        // Set reservation data to the custom layout
        setupCancelDialogViews(dialogView, reservation);

        // Setup button listeners
        setupCancelDialogButtons(dialogView, table, cancelDialog);

        cancelDialog.show();
    }

    private void setupCancelDialogViews(View dialogView, Reservation reservation) {
        // Set reservation details
        TextView nameText = dialogView.findViewById(R.id.nameText);
        nameText.setText(reservation.getCustomerName());

        // Set phone number
        TextView phoneText = dialogView.findViewById(R.id.phoneText);
        if (reservation.getPhoneNumber() != null && !reservation.getPhoneNumber().isEmpty()) {
            phoneText.setText(reservation.getPhoneNumber());
        } else {
            phoneText.setText("N/A");
        }

        // Parse party size from reservation time or set default
        TextView partySizeText = dialogView.findViewById(R.id.partySizeText);
        partySizeText.setText("1"); // You might want to extract this from reservation data if available

        // Parse and format date
        TextView dateText = dialogView.findViewById(R.id.dateText);
        String reservationTime = reservation.getReservationTime();

        // Extract date part
        if (reservationTime.contains(" at ")) {
            String datePart = reservationTime.split(" at ")[0];
            dateText.setText(formatDate(datePart));
        } else {
            dateText.setText(reservationTime);
        }

        // Parse and format time
        TextView timeText = dialogView.findViewById(R.id.timeText);
        if (reservationTime.contains(" at ")) {
            String timePart = reservationTime.split(" at ")[1];
            timeText.setText(timePart);
        } else {
            timeText.setText(reservationTime);
        }
    }

    private void setupCancelDialogButtons(View dialogView, Table table, AlertDialog dialog) {
        Button yesButton = dialogView.findViewById(R.id.yesButton);
        Button noButton = dialogView.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = reservationManager.cancelReservation(table.getName());
                if (success) {
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    showCancelSuccessDialog(table);
                } else {
                    Toast.makeText(G_Reservation.this,
                            "Failed to cancel reservation",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showCancelSuccessDialog(Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom cancel success layout
        View dialogView = getLayoutInflater().inflate(R.layout.g_reservation_cancel_success_dialog_layout, null);
        builder.setView(dialogView);

        // Create the dialog
        AlertDialog successDialog = builder.create();
        successDialog.setCancelable(false);

        // Setup the back button
        Button backButton = dialogView.findViewById(R.id.back_reservation_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                // The table grid will already be refreshed
            }
        });

        successDialog.show();
    }

    private void showEditReservationDialog(Table table) {
        Reservation reservation = reservationManager.getReservation(table.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Reservation - " + table.getName());

        final View dialogView = getLayoutInflater().inflate(R.layout.g_reservation_edit_dialog_layout, null);
        builder.setView(dialogView);

        // Pre-fill existing data
        prefillReservationData(dialogView, reservation);

        // Setup date and time pickers
        setupDatePicker(dialogView);
        setupTimePicker(dialogView);

        // Create the dialog
        currentDialog = builder.create();

        // Setup custom button listeners for editing
        setupEditDialogButtons(dialogView, table, reservation, currentDialog);

        currentDialog.show();
    }

    private void prefillReservationData(View dialogView, Reservation reservation) {
        EditText nameInput = dialogView.findViewById(R.id.customer_name);
        EditText dateInput = dialogView.findViewById(R.id.reservation_date);
        NumberPicker timeInput = dialogView.findViewById(R.id.reservation_time);

        nameInput.setText(reservation.getCustomerName());

        // Parse reservation time to pre-fill date and time
        String reservationTime = reservation.getReservationTime();
        if (reservationTime.contains(" at ")) {
            String[] parts = reservationTime.split(" at ");
            dateInput.setText(parts[0]); // Date part
            setTimePickerValue(timeInput, parts[1]); // Time part
        }
    }

    private void setTimePickerValue(NumberPicker timePicker, String time) {
        String[] timeValues = new String[] {
                "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM",
                "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM",
                "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM",
                "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM"
        };

        for (int i = 0; i < timeValues.length; i++) {
            if (timeValues[i].equals(time)) {
                timePicker.setValue(i);
                break;
            }
        }
    }

    private void setupEditDialogButtons(View dialogView, Table table, Reservation existingReservation, AlertDialog dialog) {
        Button updateButton = dialogView.findViewById(R.id.update_button);
        Button closeButton = dialogView.findViewById(R.id.close_button);

        // Pre-fill existing data
        prefillReservationData(dialogView, existingReservation);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameInput = dialogView.findViewById(R.id.customer_name);
                EditText dateInput = dialogView.findViewById(R.id.reservation_date);
                NumberPicker timeInput = dialogView.findViewById(R.id.reservation_time);

                String customerName = nameInput.getText().toString();
                String reservationDate = dateInput.getText().toString();
                String reservationTime = getSelectedTime(timeInput);

                if (!customerName.isEmpty() && !reservationDate.isEmpty()) {
                    // Combine date and time for the reservation
                    String fullReservationTime = reservationDate + " at " + reservationTime;

                    // For editing, we need to cancel the existing reservation and create a new one
                    boolean cancelSuccess = reservationManager.cancelReservation(table.getName());
                    if (cancelSuccess) {
                        boolean makeSuccess = reservationManager.makeReservation(
                                table.getName(), customerName, fullReservationTime);

                        if (makeSuccess) {
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                            showUpdateSuccessDialog(table);
                        } else {
                            Toast.makeText(G_Reservation.this,
                                    "Failed to update reservation", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(G_Reservation.this,
                                "Failed to cancel existing reservation", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(G_Reservation.this,
                            "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Setup date picker
        setupDatePicker(dialogView);

        // Setup time picker
        setupTimePicker(dialogView);
    }

    private void showUpdateSuccessDialog(Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.g_reservation_edit_success_dialog_layout, null);
        builder.setView(dialogView);

        // Create the dialog
        AlertDialog successDialog = builder.create();
        successDialog.setCancelable(false);

        // Setup the back button
        Button backButton = dialogView.findViewById(R.id.back_reservation_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                // The table grid will already be refreshed, so we're back to reservation view
            }
        });

        successDialog.show();
    }

    private void showConfirmedReservationDialog(Table table) {
        Reservation reservation = reservationManager.getReservation(table.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmed Reservation");
        builder.setMessage("Reservation for " + table.getName() +
                "\nCustomer: " + reservation.getCustomerName() +
                "\nTime: " + reservation.getReservationTime() +
                "\nStatus: " + reservation.getStatus());

        builder.setPositiveButton("Close", null);
        builder.show();
    }

    private void setupBottomNavigation() {
        Button reservationBtn = findViewById(R.id.reservation_btn);
        Button menuBtn = findViewById(R.id.menu_btn);
        Button profileBtn = findViewById(R.id.profile_btn);

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the table view
                adapter.notifyDataSetChanged();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Reservation.this, G_Menu.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Reservation.this, G_Profile.class);
                startActivity(intent);
            }
        });
    }
}