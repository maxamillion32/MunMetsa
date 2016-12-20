package com.example.asus.munmestsa0_1;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.DB.DatabaseHelper;
import com.example.asus.munmestsa0_1.adapter.CustomNoteAdapter;
import com.example.asus.munmestsa0_1.adapter.MetsaAdapter;
import com.example.asus.munmestsa0_1.adapter.ViewPagerAdapter;
import com.example.asus.munmestsa0_1.fragments.HomeFragment;
import com.example.asus.munmestsa0_1.fragments.ThreeFragment;
import com.example.asus.munmestsa0_1.fragments.TwoFragment;
import com.example.asus.munmestsa0_1.model.MapMarker;
import com.example.asus.munmestsa0_1.model.Metsa;
import com.example.asus.munmestsa0_1.model.Note;
import com.example.asus.munmestsa0_1.model.Receipt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainController extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private FirebaseDatabase database;
    private DatabaseHelper localDB;
    private HashMap<String, Metsa> metsaMap;
    private HashMap<String, List<String>> metsaKeyMap;
    private ExpandableListView expandableListView;
    private Metsa currentMetsa;
    private HashMap<String, DatabaseReference> FBreferences;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private HomeFragment homeFragment;
    private TwoFragment noteFragment;
    private ThreeFragment receiptFragment;

    private MapMarker tempMapMarker;
    private EditText tempMapMarkerEditText;
    private boolean newMapMarkerOn;

    private String receiptKey;

    static final int REQUEST_IMAGE_CAPTRUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        homeFragment = new HomeFragment();noteFragment = new TwoFragment();receiptFragment = new ThreeFragment();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(homeFragment, "Kartta");
        viewPagerAdapter.addFragments(noteFragment, "Muistio");
        viewPagerAdapter.addFragments(receiptFragment, "Kuitit");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tempMapMarker = new MapMarker();
        tempMapMarkerEditText = (EditText)findViewById(R.id.newMarkerText);

        newMapMarkerOn = false;


        metsaMap = new HashMap<>();
        currentMetsa = null;

        expandableListView = (ExpandableListView) findViewById(R.id.metsaList);

        database = FirebaseDatabase.getInstance();
        localDB = new DatabaseHelper(this);

        metsaKeyMap = new HashMap<>();

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        FBreferences = new HashMap<>();
        Cursor res = localDB.getAllData();
        if (res.getCount() == 0) {return;}
        while (res.moveToNext()) {
            FBreferences.put(res.getString(1), database.getReference("metsat/"+res.getString(1)));
        }

        for(DatabaseReference r : FBreferences.values()){
            r.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Metsa m = dataSnapshot.getValue(Metsa.class);
                    metsaMap.put(m.getTitle(), m);
                    currentMetsa = m;
                    refresh();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                viewReceipts();
                viewNotes();
                if (position == 0) {
                    showMap(1);
                } else {
                    showMap(0);
                }
            }
        });

    }


    //-------------------------------------------------------Basic--------------------------------------------------------
    public void refresh(){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentMetsa.getLatitude(), currentMetsa.getLongitude()), 15));
        listItems();
        viewNotes();
        viewReceipts();
    }

    public void listItems(){
        ArrayList<String> titles = new ArrayList<>();
        for(Metsa s : this.metsaMap.values()){
            if(s.getTitle()!=currentMetsa.getTitle())
                titles.add(s.getTitle());
        }
        metsaKeyMap.put(currentMetsa.getTitle(), titles);

        ArrayList<String> t = new ArrayList<>();
        t.add(currentMetsa.getTitle());
        ExpandableListAdapter metsaAdapter = new MetsaAdapter(this, t,metsaKeyMap, metsaMap);
        expandableListView.setAdapter(metsaAdapter);
    }
    public void childClicked(View v){
        String title = ((TextView)v.findViewById(R.id.child_item)).getText().toString();
        currentMetsa = metsaMap.get(title);
        refresh();
        Toast.makeText(getApplicationContext(),title + " valittu", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int res_id = item.getItemId();
        if(res_id==R.id.action_add){
            Intent intent = new Intent(this, MetsaAddActivity.class);
            startActivity(intent);
        }else if(res_id==R.id.menu_settings){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        return true;
    }




    //-------------------------------------------Fragment_1 MAP------------------------------------------------------------
    public void showMap(int t) {
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        if (t==1) {
            params.height = ViewPager.LayoutParams.MATCH_PARENT;
            newMapMarkerOn = false;
        } else if(t==0){
            params.height = 0;
            newMapMarkerOn = false;
        }else if(t==2){
            params.height = 400;
        }
        mapFragment.getView().setLayoutParams(params);
    }
    public void activateNewMapMarker(View v){
        if(newMapMarkerOn){
            showMap(1);
        }else{
            showMap(2);
            newMapMarkerOn = true;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng latLng) {
                if(newMapMarkerOn) {
                    mMap.clear();
                    tempMapMarker.setLatitude(latLng.latitude);
                    tempMapMarker.setLongitude(latLng.longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });
    }

    public void addNewMarker(View v){

        Toast.makeText(getApplicationContext(), " Toustia " + tempMapMarkerEditText.getText().toString(), Toast.LENGTH_LONG).show();
    }




    //-------------------------------------------Fragment_2 NOTES-------------------------------------------
    public void viewNotes(){
        ArrayList l = null;
        if(currentMetsa.getNotes()!=null)
            l = new ArrayList<Note>(currentMetsa.getNotes().values());

        noteFragment.viewNotes(l);
    }
    public void addNote(View v){
        EditText noteText = (EditText) findViewById(R.id.noteText);
        if(noteText.getText().toString().length()>1){

            String key = FBreferences.get(currentMetsa.getId()).child("notes").push().getKey();
            Note newNote = new Note(noteText.getText().toString(), new Date(), key, currentMetsa.getId(), localDB.getUsername());

            FBreferences.get(currentMetsa.getId()).child("notes").child(key).setValue(newNote);
            Toast.makeText(getApplicationContext(), "Merkintä lisätty.", Toast.LENGTH_LONG).show();
            noteText.setText("");
        }else {
            Toast.makeText(getApplicationContext(), "Et voi lisätä alle 2 merkkiä pitkää merkintää.", Toast.LENGTH_LONG).show();
        }
    }
    public void noteClicked(View v){
        final String id = ((TextView)v.findViewById(R.id.noteId)).getText().toString();
        CharSequence choose[] = new CharSequence[] {"Poista"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Valitse toiminto");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    deleteNote(id);
                    Toast.makeText(getApplicationContext(), id + " poistettu", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }
    public void deleteNote(String key){
        FBreferences.get(currentMetsa.getId()).child("notes").child(key).removeValue();
    }






    //-------------------------------------------Fragment_3 RECEIPTS-------------------------------------------
    public void viewReceipts(){
        ArrayList l = null;
        if(currentMetsa.getReceipts()!=null)
            l = new ArrayList<Receipt>(currentMetsa.getReceipts().values());

        receiptFragment.viewReceipts(l);
    }
    public void addReceipt(View v){
        EditText receiptText = (EditText) findViewById(R.id.receiptText);
        EditText receiptPrice = (EditText) findViewById(R.id.receiptPrice);
        EditText receiptTax = (EditText) findViewById(R.id.receiptTax);
        if(receiptText.getText().toString().length()>1 && receiptPrice.getText().toString().length()>0 && receiptTax.getText().toString().length()>0){
            receiptKey = FBreferences.get(currentMetsa.getId()).child("receipts").push().getKey();

            Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cIntent, REQUEST_IMAGE_CAPTRUE);

            Receipt newNote = new Receipt(new Date(), receiptText.getText().toString(),  receiptKey, currentMetsa.getId(), Integer.parseInt(receiptPrice.getText().toString()), Integer.parseInt(receiptTax.getText().toString()));
            FBreferences.get(currentMetsa.getId()).child("receipts").child(receiptKey).setValue(newNote);
            receiptText.setText("");
            receiptPrice.setText("");
            receiptTax.setText("24");
        }
    }
    public void receiptClicked(View v){
        final String id = ((TextView)v.findViewById(R.id.receiptId)).getText().toString();
        CharSequence choose[] = new CharSequence[] {"Poista", "Katso kuittia"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Valitse toiminto");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    deleteReceipt(id);
                    Toast.makeText(getApplicationContext(), id + " poistettu", Toast.LENGTH_LONG).show();
                }else if(which==1){
                    Intent intent = new Intent(getApplicationContext(), ReceiptImage.class);
                    intent.putExtra("receiptId", id);
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }
    public void deleteReceipt(String key){
        FBreferences.get(currentMetsa.getId()).child("receipts").child(key).removeValue();
        mStorage.child("Receipts").child(key).delete();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTRUE && resultCode == RESULT_OK){
            mProgress.setMessage("Ladataan kuvaa..");
            mProgress.show();
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] bdata = baos.toByteArray();
            StorageReference filepath = mStorage.child("Receipts").child(receiptKey);
            UploadTask uploadTask = filepath.putBytes(bdata);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Kuitti lisätty", Toast.LENGTH_LONG).show();
                }
            });
        }
    }












}
