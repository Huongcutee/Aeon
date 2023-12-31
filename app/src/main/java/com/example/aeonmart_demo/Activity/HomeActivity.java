package com.example.aeonmart_demo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aeonmart_demo.Adapter.HomeAdapter;
import com.example.aeonmart_demo.Adapter.SlideAdapter;
import com.example.aeonmart_demo.Detail_HoSo_Activity.ProfileActivity;
import com.example.aeonmart_demo.Model.HomeModel;
import com.example.aeonmart_demo.Model.SlideViewModel;
import com.example.aeonmart_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    StorageReference storageRef;
    FirebaseFirestore db;
    ArrayList<HomeModel> homeModels;
    HomeAdapter homeAdapter;
    ArrayList<SlideViewModel> slideViewModels;
    SlideAdapter slideViewAdapter;
    RecyclerView rv_Home;
    ImageButton cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Trang chủ");
        getSupportActionBar().setLogo(R.drawable.aeonminilogo);

        SpannableString spannableTitle = new SpannableString(" Trang chủ");
        spannableTitle.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableTitle);

        getSupportActionBar().setDisplayUseLogoEnabled(true);

        BottomNavigationView bottomNav = findViewById(R.id.Home_bottomnav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome) {
                } else if (itemId == R.id.navVoucher) {
                    startActivity(new Intent(HomeActivity.this, VoucherListActivity.class));
                    finish();
                } else if (itemId == R.id.navUser) {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    finish();
                }
                return true;
            }
        });
        bottomNav.setSelectedItemId(R.id.navHome);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        FirebaseApp.initializeApp(this);
        SliderView sliderView = findViewById(R.id.sliderView);
        slideViewAdapter = new SlideAdapter(this);
        sliderView.setSliderAdapter(slideViewAdapter);
        sliderView.setCurrentPagePosition(0);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycle(true);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(3);
        renewItems(sliderView);
        //endregion
        homeModels = new ArrayList<>();
        homeAdapter = new HomeAdapter(homeModels,this);
        db = FirebaseFirestore.getInstance();
        rv_Home = findViewById(R.id.rv_Home);
//        rv_Home.setLayoutFrozen(true);
//        rv_Home.isLayoutSuppressed();
        rv_Home.setAdapter(homeAdapter);
        rv_Home.setLayoutManager(new GridLayoutManager(this,2));

        loadSlider();
        loadProductdata();

        ImageButton cart, tichxubt,tintucbt,doitrabt,danhgiabt, yeuthichbt, vongquaybt, thongbaobt;
        cart         = (ImageButton) findViewById(R.id.cart);
        tichxubt    = (ImageButton) findViewById(R.id.IB_Coin) ;
        tintucbt    = (ImageButton) findViewById(R.id.IB_News);
        doitrabt    = (ImageButton) findViewById(R.id.IB_Return);
        danhgiabt   = (ImageButton) findViewById(R.id.IB_Review);
        yeuthichbt  = (ImageButton) findViewById(R.id.IB_Fav);
        vongquaybt  = (ImageButton) findViewById(R.id.IB_Lucky);
        thongbaobt  = (ImageButton) findViewById(R.id.IB_Noti);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, GioHangActivity.class);
                startActivity(intent);
            }
        });

        tichxubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, TichXuActivity.class);
                startActivity(intent);
            }
        });

        tintucbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
        doitrabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, TraHang_Activity.class);
                startActivity(intent);
            }
        });
        danhgiabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, Danh_Gia_SP_Activity.class);
                startActivity(intent);
            }
        });
        yeuthichbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, FavouriteActivity.class);
                startActivity(intent);
            }
        });
        vongquaybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, Quay_ThuongActivity.class);
                startActivity(intent);
            }
        });
        thongbaobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu_toolbar,menu);
        //region Search
        MenuItem item = menu.findItem(R.id.mn_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //call when press search button
                seachData(s, HomeActivity.this);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                //call when type letter
                if (s.isEmpty()){
                    homeModels.clear();
                    slideViewModels.clear();
                    loadProductdata();
                }else {
                    rv_Home.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        //endregion
        return super.onCreateOptionsMenu(menu);
    }
    void seachData(String s, Context context){
        Query query = db.collection("Product").orderBy("Name").startAt(s).endAt(s+"\uf8ff");      // \uf8ff match all unicode value start with s
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                homeModels.clear();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    String category = documentSnapshot.get("Category").toString();
                    String description = documentSnapshot.get("Description").toString();
                    Boolean favstatus = Boolean.valueOf(documentSnapshot.get("FavStatus").toString());
                    String image = documentSnapshot.get("Image").toString();
                    String masp = documentSnapshot.get("MaSp").toString();
                    String name = documentSnapshot.get("Name").toString();
                    String origin = documentSnapshot.get("Origin").toString();
                    Double price = documentSnapshot.getDouble("Price").doubleValue();
                    String rate = documentSnapshot.get("Rate").toString();
                    homeModels.add(new HomeModel(category, description,favstatus,image, masp, name, origin, price, rate));
                }
                homeAdapter = new HomeAdapter(homeModels,context);
                rv_Home.setAdapter(homeAdapter);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        });
    }
    private void loadSlider(){
        db.collection("Banner").get().addOnCompleteListener(task -> {
            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                String Name = queryDocumentSnapshot.get("Name").toString();
                String Pic = queryDocumentSnapshot.get("Pic").toString();
                slideViewModels.add(new SlideViewModel(Name,Pic));
            }
            slideViewAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Error!!!", Toast.LENGTH_SHORT).show());
    }
    @SuppressLint("NotifyDataSetChanged")
    private void loadProductdata() {
        db.collection("Product").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                String category = documentSnapshot.get("Category").toString();
                String description = documentSnapshot.get("Description").toString();
                Boolean favstatus = Boolean.valueOf(documentSnapshot.get("FavStatus").toString());

                String image = documentSnapshot.get("Image").toString();
                String masp = documentSnapshot.get("MaSp").toString();
                String name = documentSnapshot.get("Name").toString();
                String origin = documentSnapshot.get("Origin").toString();
                Double price = documentSnapshot.getDouble("Price").doubleValue();
                String rate = documentSnapshot.get("Rate").toString();
                homeModels.add(new HomeModel(category, description,favstatus,image, masp, name, origin, price, rate));
            }
            homeAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "ERORR!!!", Toast.LENGTH_LONG).show();
        });
    }
    private void renewItems(View view) {
        slideViewModels = new ArrayList<>();
        SlideViewModel dataItems = new SlideViewModel();
        slideViewModels.add(dataItems);
        slideViewAdapter.renewItems(slideViewModels);
        slideViewAdapter.deleteItems(0);
    }

}