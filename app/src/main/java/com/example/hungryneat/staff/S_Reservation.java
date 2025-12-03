package com.example.hungryneat.staff;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryneat.R;
import com.example.hungryneat.reservation.Reservation;
import com.example.hungryneat.reservation.ReservationManager;
import com.example.hungryneat.table.Table;
import com.example.hungryneat.table.TableAdapter;

public class S_Reservation extends AppCompatActivity {

    private GridView tableGrid;
    private ReservationManager reservationManager;
    private TableAdapter adapter;

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
            case PENDING:
                showPendingReservationDialog(table);
                break;
            case CONFIRMED:
                showConfirmedReservationDialog(table);
                break;
        }
    }

    private void showPendingReservationDialog(Table table) {
        Reservation reservation = reservationManager.getReservation(table.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(table.getName());
        builder.setMessage("Pending Approval" +
                "\nName: " + reservation.getCustomerName() +
                "\nTime: " + reservation.getReservationTime());

        // Staff actions (if this is staff interface)
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean success = reservationManager.confirmReservation(table.getName());
                if (success) {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(S_Reservation.this,
                            "Reservation confirmed for " + table.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel Reservation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean success = reservationManager.cancelReservation(table.getName());
                if (success) {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(S_Reservation.this,
                            "Reservation cancelled for " + table.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNeutralButton("Close", null);
        builder.show();
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
                Intent intent = new Intent(S_Reservation.this, S_Menu.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(S_Reservation.this, S_Profile.class);
                startActivity(intent);
            }
        });
    }
}