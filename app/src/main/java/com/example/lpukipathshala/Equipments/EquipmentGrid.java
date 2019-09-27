package com.example.lpukipathshala.Equipments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lpukipathshala.Dashboard.Dashboard;
import com.example.lpukipathshala.MyUtility;
import com.example.lpukipathshala.Myaccount.AccountDetails;
import com.example.lpukipathshala.R;
import com.example.lpukipathshala.quoraa.MainActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
        addEqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Add_Equipment.class);
                startActivity(intent);
            }
        });
        addBook.setVisibility(View.GONE);
        fetchData(view);
        return view;
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
