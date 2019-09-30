package com.example.lpukipathshala.Equipments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lpukipathshala.Dashboard.Dashboard;
import com.example.lpukipathshala.DataModels.Add_Book_Model;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.product.ProductCardRecyclerViewAdapter;
import com.example.lpukipathshala.quoraa.MainActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static android.support.constraint.Constraints.TAG;

@SuppressLint("ValidFragment")
public class EquipmentGrid extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    MaterialButton myaccount,category,Doubts,Dashboard;
    FirebaseAuth mAuth;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton addBook,addEqui;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    StorageReference storageReference;
    ArrayList<Add_Equipment_Model> arrayList ;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout relativeLayout;
    boolean selectebranch[]= new boolean[9];
    boolean selectesort[]= new boolean[3];
    RelativeLayout branchfilter,sortFilter;
    String selected_branch="",Selected_Degree,selected_sort;
    Query.Direction direction = null;
    boolean isSelectedsorttrue=false,isSeletedbranchtrue=false;
    FrameLayout frameLayout;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_grid_fragment, container, false);
        progressDialog = new ProgressDialog(view.getContext());
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        setUpToolbar(view);
        floatingActionMenu = view.findViewById(R.id.addProduct);
        addBook = view.findViewById(R.id.addbook);
        addEqui = view.findViewById(R.id.addequi);
        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        frameLayout = view.findViewById(R.id.framelayout);
        branchfilter = view.findViewById(R.id.branchfilter);
        sortFilter = view.findViewById(R.id.sort);
        relativeLayout = view.findViewById(R.id.filter);
        addEqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Add_Equipment.class);
                startActivity(intent);
            }
        });
        addBook.setVisibility(View.GONE);

        branchfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<8;i++)
                    selectebranch[i]=false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("Choose Branch");
                View view1 = getLayoutInflater().inflate(R.layout.select_branch,null);

                EditText edittext[] = {view1.findViewById(R.id.engineering) ,view1.findViewById(R.id.management),view1.findViewById(R.id.agriculture),view1.findViewById(R.id.hotel),view1.findViewById(R.id.biotechnology),view1.findViewById(R.id.law),view1.findViewById(R.id.medicalscience),view1.findViewById(R.id.commerce),view1.findViewById(R.id.other)};
                builder.setView(view1);
                edittext[8].setVisibility(View.GONE);
                builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getFilterData(selected_branch,view);
                        branchfilter.setBackground(getResources().getDrawable(R.drawable.buttonblue));
                        isSeletedbranchtrue = true;
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("remove Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        branchfilter.setBackground(getResources().getDrawable(R.drawable.profile));
                        isSeletedbranchtrue=false;
                        if (isSelectedsorttrue)
                        {
                            getSortData(selected_sort,view);
                        }
                        else
                        {
                            fetchData(view);
                        }
                    }
                });
                builder.show();
                edittext[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[0]);
                        popup.getMenuInflater()
                                .inflate(R.menu.enginner, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[0].getHint().toString();
                                edittext[0].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[0]==false)
                        {
                            selectebranch[0] = true;
                            edittext[0].setCursorVisible(false);
                            edittext[0].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[0].setHintTextColor(Color.BLACK);

                        }
                        for(int i=1;i<9;i++)
                        {
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[1]);
                        popup.getMenuInflater()
                                .inflate(R.menu.management, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[1].getHint().toString();
                                edittext[1].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[1]==false)
                        {
                            selectebranch[1] = true;
                            edittext[1].setCursorVisible(false);
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[1].setHintTextColor(Color.BLACK);
                        }
                        for(int i=0;i<9;i++)
                        {
                            if(i==1)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setText("");
                            edittext[i].setCursorVisible(false);
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[2]);
                        popup.getMenuInflater()
                                .inflate(R.menu.agriculture, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                edittext[2].setText(selected_branch);
                                Selected_Degree = edittext[2].getHint().toString();
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[2]==false)
                        {
                            selectebranch[2] = true;
                            edittext[2].setCursorVisible(false);
                            edittext[2].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[2].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==2)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[3]);
                        popup.getMenuInflater()
                                .inflate(R.menu.hotel, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[3].getHint().toString();
                                edittext[3].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[3]==false)
                        {
                            selectebranch[3] = true;
                            edittext[3].setCursorVisible(false);
                            edittext[3].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[3].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==3)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[4].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[4]);
                        popup.getMenuInflater()
                                .inflate(R.menu.biotech, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                edittext[4].setText(selected_branch);
                                Selected_Degree = edittext[4].getHint().toString();
                                return true;
                            }
                        });
                        popup.show();

                        if(selectebranch[4]==false)
                        {
                            selectebranch[4] = true;
                            edittext[4].setCursorVisible(false);
                            edittext[4].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[4].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==4)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[6].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[6]);
                        popup.getMenuInflater()
                                .inflate(R.menu.medicalscience, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[6].getHint().toString();
                                edittext[6].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[6]==false)
                        {
                            selectebranch[6] = true;
                            edittext[6].setCursorVisible(false);
                            edittext[6].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[6].setHintTextColor(Color.BLACK);
                        }
                        for(int i=0;i<9;i++)
                        {
                            if(i==6)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[5].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[5]);
                        popup.getMenuInflater()
                                .inflate(R.menu.law, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[5].getHint().toString();
                                edittext[5].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[5]==false)
                        {
                            selectebranch[5] = true;
                            edittext[5].setCursorVisible(false);
                            edittext[5].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[5].setHintTextColor(Color.BLACK);
                        }
                        for(int i=0;i<9;i++)
                        {
                            if(i==5)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[7].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(),edittext[7]);
                        popup.getMenuInflater()
                                .inflate(R.menu.commerce, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                selected_branch = String.valueOf(item.getTitle());
                                Selected_Degree = edittext[7].getHint().toString();
                                edittext[7].setText(selected_branch);
                                return true;
                            }
                        });
                        popup.show();
                        if(selectebranch[7]==false)
                        {
                            selectebranch[7] = true;
                            edittext[7].setCursorVisible(false);
                            edittext[7].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[7].setHintTextColor(Color.BLACK);
                        }

                        for(int i=0;i<9;i++)
                        {
                            if(i==7)
                                continue;
                            selectebranch[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

                edittext[8].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectebranch[8] == false) {
                            selectebranch[8] = true;
                            edittext[8].setCursorVisible(true);
                            edittext[8].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[8].setHintTextColor(Color.GRAY);
                        }
                        else {
                            selectebranch[8] = false;
                            edittext[8].setCursorVisible(true);
                            edittext[8].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[8].setHintTextColor(Color.GRAY);
                        }
                        for (int i = 0; i < 9; i++) {
                            if (i == 8)
                                continue;
                            selectebranch[i] = false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });

            }
        });


        sortFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1 = getLayoutInflater().inflate(R.layout.sortby,null);
                builder.setMessage("Select");
                builder.setView(view1);
                builder.setPositiveButton("select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSortData(selected_sort,view);
                        sortFilter.setBackground(getResources().getDrawable(R.drawable.buttonblue));
                        isSelectedsorttrue = true;
                    }
                }).setNeutralButton("remove filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isSelectedsorttrue=false;
                        if (isSeletedbranchtrue)
                        {
                            getFilterData(selected_branch,view);
                        }
                        else
                        {
                            fetchData(view);
                        }
                        sortFilter.setBackground(getResources().getDrawable(R.drawable.profile));
                    }
                });
                builder.show();
                EditText edittext[] = {view1.findViewById(R.id.recent),view1.findViewById(R.id.ltoh),view1.findViewById(R.id.htol)};
                edittext[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectesort[0] == false) {
                            selectesort[0] = true;
                            edittext[0].setCursorVisible(true);
                            edittext[0].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[0].setHintTextColor(Color.GRAY);
                            selected_sort ="recent";
                        }
                        else {
                            selectesort[0] = false;
                            edittext[0].setCursorVisible(true);
                            edittext[0].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[0].setHintTextColor(Color.GRAY);
                        }
                        for(int i=1;i<3;i++)
                        {
                            selectesort[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectesort[1] == false) {
                            selectesort[1] = true;
                            edittext[1].setCursorVisible(true);
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[1].setHintTextColor(Color.GRAY);
                            selected_sort ="LTOH";
                        }
                        else {
                            selectesort[1] = false;
                            edittext[1].setCursorVisible(true);
                            edittext[1].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[1].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<3;i++)
                        {
                            if(i==1)
                                continue;
                            selectesort[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
                edittext[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectesort[2] == false) {
                            selectesort[2] = true;
                            edittext[2].setCursorVisible(true);
                            edittext[2].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[2].setHintTextColor(Color.GRAY);
                            selected_sort ="HTOL";
                        }
                        else {
                            selectesort[2] = false;
                            edittext[2].setCursorVisible(true);
                            edittext[2].setBackground(getResources().getDrawable(R.drawable.asscent));
                            edittext[2].setHintTextColor(Color.GRAY);
                        }
                        for(int i=0;i<3;i++)
                        {
                            if(i==2)
                                continue;
                            selectesort[i]=false;
                            edittext[i].setCursorVisible(false);
                            edittext[i].setText("");
                            edittext[i].setBackground(getResources().getDrawable(R.drawable.profile));
                            edittext[i].setHintTextColor(Color.GRAY);
                        }
                    }
                });
            }
        });

        fetchData(view);
        return view;
    }

    private void getFilterData(String selected_branch, View view) {
        if (isSelectedsorttrue)
        {
//            getSortData(selected_sort,view);
            recyclerView = getView().findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            arrayList = new ArrayList<>();
            collectionReference = firebaseFirestore.collection("Equipments");
            collectionReference
                    .whereEqualTo("branch",selected_branch)
                    .orderBy("price",direction)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                            {
                                Add_Equipment_Model productEntry = queryDocumentSnapshots1.toObject(Add_Equipment_Model.class);
                                if(!productEntry.getUserId().equalsIgnoreCase(mAuth.getUid()))
                                    arrayList.add(new Add_Equipment_Model(productEntry.getUserId(),productEntry.getType(), productEntry.getEquipmentId(),productEntry.getEquipmentName(),productEntry.getBranch(),productEntry.getDescription(),productEntry.getPrice(),productEntry.getPicUrl()));
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
                            Equipment_Grid_Adapter adapter = new Equipment_Grid_Adapter(getContext(),arrayList);
                            // StaggeredProductCardRecyclerViewAdapter adapter = new StaggeredProductCardRecyclerViewAdapter(arrayList);
                            recyclerView.setAdapter(adapter);
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    return 1;
                                }
                            });
                            recyclerView.setLayoutManager(gridLayoutManager);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            relativeLayout.setVisibility(View.VISIBLE);
                            frameLayout.setVisibility(View.VISIBLE);
                        }
                    });
        }
        else
        {
            recyclerView = getView().findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            arrayList = new ArrayList<>();
            collectionReference = firebaseFirestore.collection("Equipments");
            collectionReference.whereEqualTo("branch",selected_branch).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                    {
                        Add_Equipment_Model productEntry = queryDocumentSnapshots1.toObject(Add_Equipment_Model.class);
                        if(!productEntry.getUserId().equalsIgnoreCase(mAuth.getUid()))
                            arrayList.add(new Add_Equipment_Model(productEntry.getUserId(),productEntry.getType(), productEntry.getEquipmentId(),productEntry.getEquipmentName(),productEntry.getBranch(),productEntry.getDescription(),productEntry.getPrice(),productEntry.getPicUrl()));
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
                    Equipment_Grid_Adapter adapter = new Equipment_Grid_Adapter(getContext(),arrayList);
                    // StaggeredProductCardRecyclerViewAdapter adapter = new StaggeredProductCardRecyclerViewAdapter(arrayList);
                    recyclerView.setAdapter(adapter);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return 1;
                        }
                    });
                    recyclerView.setLayoutManager(gridLayoutManager);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                }
            });
        }

    }


    private void setUpToolbar(final View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.menu),
                getContext().getResources().getDrawable(R.drawable.close)));


        myaccount = view.findViewById(R.id.myaccount);
        myaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), AccountDetails.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        category = view.findViewById(R.id.categories);
        category.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Dashboard.class);
            startActivity(intent);
            getActivity().finish();
        });
        Doubts = view.findViewById(R.id.doubts);
        Doubts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Dashboard = view.findViewById(R.id.dashboard);
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Dashboard.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    void fetchData(View view)
    {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        collectionReference = firebaseFirestore.collection("Equipments");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                {
                    Add_Equipment_Model productEntry = queryDocumentSnapshots1.toObject(Add_Equipment_Model.class);
                    if(!productEntry.getUserId().equalsIgnoreCase(mAuth.getUid()))
                        arrayList.add(new Add_Equipment_Model(productEntry.getUserId(),productEntry.getType(), productEntry.getEquipmentId(),productEntry.getEquipmentName(),productEntry.getBranch(),productEntry.getDescription(),productEntry.getPrice(),productEntry.getPicUrl()));
                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
                Equipment_Grid_Adapter adapter = new Equipment_Grid_Adapter(view.getContext(),arrayList);
                recyclerView.setAdapter(adapter);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return 1;
                    }
                });
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setLayoutManager(gridLayoutManager);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.i("msl;fdmslf", "onFailure: ----------------------------- Fail");
            }
        });
//

    }
    private void getSortData(String selected_sort,View view) {
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        collectionReference = firebaseFirestore.collection("Equipments");

        if (selected_sort.equalsIgnoreCase("ltoh"))
        {
            direction = Query.Direction.ASCENDING;
            sortby(direction,view);
        }
        else if(selected_sort.equalsIgnoreCase("htol"))
        {
            direction = Query.Direction.DESCENDING;
            sortby(direction,view);
        }
        else
        {

            isSelectedsorttrue=false;
            sortFilter.setBackground(getResources().getDrawable(R.drawable.profile));
            if (isSeletedbranchtrue)
            {
                arrayList = new ArrayList<>();
                getFilterData(selected_branch,view);
            }
            else
            {
                arrayList = new ArrayList<>();
                fetchData(view);
            }
        }

    }

    void sortby(Query.Direction direction, View view)
    {
        if (isSeletedbranchtrue)
        {
//            getFilterData(selected_branch, view);
            arrayList = new ArrayList<>();
            collectionReference = firebaseFirestore.collection("Equipments");
            collectionReference
                    .whereEqualTo("branch",selected_branch)
                    .orderBy("price", direction).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                    {
                        Add_Equipment_Model productEntry = queryDocumentSnapshots1.toObject(Add_Equipment_Model.class);
                        if(!productEntry.getUserId().equalsIgnoreCase(mAuth.getUid()))
                            arrayList.add(new Add_Equipment_Model(productEntry.getUserId(), productEntry.getType(), productEntry.getEquipmentId(),productEntry.getEquipmentName(),productEntry.getBranch(),productEntry.getDescription(),productEntry.getPrice(),productEntry.getPicUrl()));
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
                    Equipment_Grid_Adapter adapter = new Equipment_Grid_Adapter(getContext(),arrayList);
                    // StaggeredProductCardRecyclerViewAdapter adapter = new StaggeredProductCardRecyclerViewAdapter(arrayList);
                    recyclerView.setAdapter(adapter);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return 1;
                        }
                    });
                    recyclerView.setLayoutManager(gridLayoutManager);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: -------------------->" + e);
                }
            });
        }
        else
        {
            arrayList = new ArrayList<>();
            collectionReference.orderBy("price", direction).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                    {
                        Add_Equipment_Model productEntry = queryDocumentSnapshots1.toObject(Add_Equipment_Model.class);
                        if(!productEntry.getUserId().equalsIgnoreCase(mAuth.getUid()))
                            arrayList.add(new Add_Equipment_Model(productEntry.getUserId(), productEntry.getType(), productEntry.getEquipmentId(),productEntry.getEquipmentName(),productEntry.getBranch(),productEntry.getDescription(),productEntry.getPrice(),productEntry.getPicUrl()));
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
                    Equipment_Grid_Adapter adapter = new Equipment_Grid_Adapter(getContext(),arrayList);
                    // StaggeredProductCardRecyclerViewAdapter adapter = new StaggeredProductCardRecyclerViewAdapter(arrayList);
                    recyclerView.setAdapter(adapter);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return 1;
                        }
                    });
                    recyclerView.setLayoutManager(gridLayoutManager);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: -----------------------" + e);
                }
            });
        }


    }



    void getData(String q)
    {
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        collectionReference = firebaseFirestore.collection("Equipments");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arrayList = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                {
                    Add_Equipment_Model productEntry = queryDocumentSnapshots1.toObject(Add_Equipment_Model.class);
                    if(!productEntry.getUserId().equalsIgnoreCase(mAuth.getUid()))
                    {
                        String bookName = productEntry.getEquipmentName();
                        bookName= bookName.toLowerCase();
                        String s1 = q.toLowerCase();
                        if(bookName.contains(s1))
                            arrayList.add(new Add_Equipment_Model(productEntry.getUserId(), productEntry.getType(), productEntry.getEquipmentId(),productEntry.getEquipmentName(),productEntry.getBranch(),productEntry.getDescription(),productEntry.getPrice(),productEntry.getPicUrl()));

                    }

                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
                Equipment_Grid_Adapter adapter = new Equipment_Grid_Adapter(getView().getContext(),arrayList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setLayoutManager(gridLayoutManager);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.i("msl;fdmslf", "onFailure: ----------------------------- Fail");
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_option, menu);
        MenuItem search = menu.findItem(R.id.search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(newText);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.filter :
                Log.i("Filter","-------------->");
                break;
            case R.id.search :

                break;
        }
        return true;
    }



}
