//
//
//package com.app.sample.insta;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.app.sample.insta.adapter.TicketListAdapter;
//import com.app.sample.insta.data.Tools;
//import com.app.sample.insta.model.LottoTicket;
//import com.app.sample.insta.model.TicketFirestoreModel;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class ActivityTicket extends AppCompatActivity
//{
//
//    private List<LottoTicket> items_ticket = new ArrayList<>();
//
// //   private List<LottoTicket> items_ticketCamera = new ArrayList<>();
//
//
//    private TicketListAdapter mAdapter;
//
//    private Toolbar mTopToolbar;
//    private ActionBar actionbar;
//    private RecyclerView recyclerView;
//    private View view;
//
//
//    private String SerialNo;
//    private String userId;
//
//    private Boolean run = false;
//
//    private LottoTicket LT;
//
//    private List<LottoTicket> items_Pass = new ArrayList<>();
//
//    @Nullable
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ticket);
//
//        items_ticket.add((LottoTicket) getIntent().getSerializableExtra("LottoTicketObj"));
//
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerViewTicket);
//
//        mAdapter = new TicketListAdapter(items_ticket);
//
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//        // toolbar with delete
//
//        mTopToolbar = (Toolbar) findViewById(R.id.toolbarTicket);
//        setSupportActionBar(mTopToolbar);
//
//        actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(false);
//        actionbar.setTitle("");
//
//
//        // for system bar in lollipop
//        Tools.systemBarLolipop(this);
//
//
//        strText = getIntent().getStringExtra("text");
//
//       // SerialNo = getIntent().getStringExtra("text");
//
//        items_Pass.add((LottoTicket) getIntent().getSerializableExtra("text"));
//
//        LottoTicket ltP = items_Pass.get(0);
//
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//        String userId = firebaseUser.getUid();
//
//        database = FirebaseFirestore.getInstance();
//
//
//        if (ltP != null)
//        {
//            String ticketID = ltP.getTicketID();
//            String tType = ltP.getTicketType();
//            String Draw_date = ltP.getDateOfDraw();
//            String purchase_Date = ltP.getDateOfPurc();
//
//            String[] arrayList = ltP.getTBoards();
//
//            items_ticket.remove(0);
//            LT = new LottoTicket(ticketID,tType,purchase_Date,Draw_date,arrayList);
//            items_ticket.add(LT);
//            run = true;
//        }
//
//        if(run == false)
//        {
//            loadTicketData();
//        }
//
//
//
//
//    }
//
//    @Override
//    public void onStart()
//    {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//    private void loadTicketData()
//    {
//
//        LottoTicket lt = items_ticket.get(0);
//
//        LottoTicket ltKEEP = items_ticket.get(0);
//
//        LottoTicket lte = new LottoTicket();
//
//        String[] arrlist = lt.getTBoards();
//
//        items_ticket.remove(0);
//
//        if(arrlist.length > 0)
//        {
//
//            for(int i = 0; i < arrlist.length;i++)
//            {
//                lte.setTBoards(arrlist);
//                items_ticket.add(lte);
//                lte = new LottoTicket();
//            }
//
//        }
//
//        mAdapter.notifyDataSetChanged();
//
//        items_ticket.remove(0);
//        items_ticket.add(ltKEEP);
//
//
//    }
//
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        setContentView(R.layout.activity_ticket);
//    }
//
//    private void updateUI(FirebaseUser currentUser)
//    {
//        if (currentUser == null)
//        {
//            startActivity(new Intent(ActivityTicket.this, ActivityLogin.class));
//        } else
//        {
//            return;
//        }
//    }
//
//    public void getDoc(){
//               documentReference = database.collection("Users").document(userId).collection("tickets").document(SerialNo);
//
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) //TODO currently failing causing app to crash on adding a ticket
//            {
//                    if (documentSnapshot.exists())
//                    {
//                // TODO: data conversions
//                TicketFirestoreModel ticketFirestoreModel = documentSnapshot.toObject(TicketFirestoreModel.class);
//                String ticketID = documentSnapshot.getId();
////              String ticketID = ticketFirestoreModel.getDocumentID();
//                String tType= ticketFirestoreModel.getTicket_Type();
//                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//                String Draw_date = df.format(ticketFirestoreModel.getDraw_Date());
//                String Purchase_Date = df.format(ticketFirestoreModel.getPurchase_Date());
//
//                // declaration and initialise String Array
//                ArrayList<String> arrayList = ticketFirestoreModel.getBoards();
//                String str[] = new String[arrayList.size()];
//
//                // ArrayList to Array Conversion
//                for (int j = 0; j < arrayList.size(); j++)
//                {
//                    // Assign each value to String array
//                    str[j] = arrayList.get(j);
//                    System.out.println(Arrays.toString(str));
//                }
//
//                items_ticket.remove(0);
//                LT = new LottoTicket(ticketID,tType,Purchase_Date,Draw_date,str);
//                items_ticket.add(LT);
//                run = true;
//
//
//
//            }
//             }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("Failed to retrieve from firebase");
//            }
//        });
//    }
//
//    public void getDocuments()
//    {
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        String uID = firebaseUser.getUid();
//
//        database.collection("Users").document(uID).collection("tickets").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots.isEmpty()){
//                    Log.d("ActivityTicket", "onSuccess: LIST EMPTY");
//                    return;
//                }else{
//                    int count = 0;
//                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                        if (documentSnapshot.exists()){
//                            // TODO: data conversions
//                            TicketFirestoreModel ticketFirestoreModel = documentSnapshot.toObject(TicketFirestoreModel.class);
//                            //   String ticketID = ticketFirestoreModel.getDocumentID().toString();
//                            //  String tType= ticketFirestoreModel.getTicket_Type();
//                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//                            String Draw_date = df.format(ticketFirestoreModel.getDraw_Date());
//                            String Purchase_Date = df.format(ticketFirestoreModel.getPurchase_Date());
//
//
//                            // declaration and initialise String Array
//                            ArrayList<String> arrayList = ticketFirestoreModel.getBoards();
//                            String str[] = new String[arrayList.size()];
//                            // ArrayList to Array Conversion
//                            for (int j = 0; j < arrayList.size(); j++) {
//
//                                // Assign each value to String array
//                                str[j] = arrayList.get(j);
//                                System.out.println(Arrays.toString(str));
//                            }
//
//                        }
//                        count++;
//                    }
//                    System.out.println(count);
//                }
//            }
//        });
//    }
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_fragment_ticket_delete, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        LottoTicket lt = items_ticket.get(0);
//        final String serialID = lt.getTicketID();
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        final String uID = firebaseUser.getUid();
//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityTicket.this);
//        dialog.setCancelable(false);
//        dialog.setTitle("Delete Ticket!");
//        dialog.setMessage("Do you wish to delete this ticket?");
//        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (serialID == null){
//                    Toast.makeText(getApplicationContext(), "Failed to delete ticket, please try again later!", Toast.LENGTH_LONG).show();
//                }else {
//
//                    database.collection("Users").document(uID).collection("tickets").document(serialID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            //TODO: successful deletion
//                            Toast.makeText(getBaseContext(), "Ticket successfully deleted!", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(ActivityTicket.this, ActivityMain.class));
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            //TODO: failed to delete
//                        }
//                    });
//                }
//            }
//        });
//        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        dialog.create();
//        dialog.show();
//        return true;
//    }
//
//    //TODO: lotto type query.
//    public void lottoTypeQuery()
//    {
//        CollectionReference lottoType = database.collection("Users").document(userId).collection("tickets");
//
//        Query query = lottoType.whereEqualTo("Ticket_Type", "Lotto");//.whereEqualTo(String field, Object obj);
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("ActivityTicket", document.getId() + " => " + document.getData());
//                    }
//                } else {
//                    Log.d("ActivityTicket", "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//    }
//}
