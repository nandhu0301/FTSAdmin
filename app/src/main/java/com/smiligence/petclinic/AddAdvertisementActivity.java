package com.smiligence.petclinic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.smiligence.petclinic.bean.AdvertisementDetails;
import com.smiligence.petclinic.bean.CategoryDetails;
import com.smiligence.petclinic.bean.ItemDetails;
import com.smiligence.petclinic.bean.ItemReviewAndRatings;
import com.smiligence.petclinic.bean.UserDetails;
import com.smiligence.petclinic.common.CommonMethods;
import com.smiligence.petclinic.common.DateUtils;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.smiligence.petclinic.common.Constant.ACTIVE_STATUS;
import static com.smiligence.petclinic.common.Constant.ADVERTISEMT_DETAILS_FIREBASE_TABLE;
import static com.smiligence.petclinic.common.Constant.CATEGORY_DETAILS_TABLE;
import static com.smiligence.petclinic.common.Constant.DETAILS_INSERTED;
import static com.smiligence.petclinic.common.Constant.ITEM_RATING_REVIEW_TABLE;
import static com.smiligence.petclinic.common.Constant.SELLER_DETAILS_TABLE;
import static com.smiligence.petclinic.common.TextUtils.removeDuplicatesList;

public class AddAdvertisementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference advertisementDataRef, logindataRef;
    StorageReference advertisementStorageRef;
    EditText addAdvertisementName;
    Button pickImage, uploadData;
    ImageView imageView;
    final static int PICK_IMAGE_REQUEST = 100;
    ArrayList<String> itemCategoryHeader = new ArrayList<>();
    Uri mimageuri;
    Drawable drawable;
    StorageTask mUploadTask;
    AdvertisementDetails advertisementDetails = new AdvertisementDetails();
    long addMaxId = 0;
    int itemid ;
    View mHeaderView;
    public static TextView textViewUsername;
    public static TextView textViewEmail;
    ImageView advertisementImage;
    String selectedStoreString;
    String selectedBrandString;
    String selectedCategoryString;
    String selectedItemName;
    Integer selecteditem ;
    String adv_TypeSelectedString;
    String adv_PricingSelectedString, advertisementSelectedString;
    int adv_Priority;
    String timecal;
    String hourcal;
    Button setTimeHour, setTimeoneWeekButton, setTime24hoursButton;
    String advertisementScheduleDate, advertisementScheduledTime;
    static int year, month, day;
    ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
    String categoryString;
    EditText brands_storesEditText, item_storesEditText;
    RadioGroup ad_TypeRadioGroup, ad_MediaRadioGroup ,ad_TypeItemGroup;
    String adv_TypeString, adv_MediaSelectedString, adv_DurationSelectedString;
    String dateDayCal, monthCal;
    ArrayList<CategoryDetails> itemDetailsArrayList = new ArrayList<>();
    Spinner durationspinner;
    int pos;
    DatabaseReference sellerDetailRef, productDetailRef, categoryRef,productDatabaseReef;

    SearchableSpinner adv_Pricing_spinner;
    Spinner brandSpinner;
    //   MultiSelectionSpinnerforStore storespinner;
    ArrayList<UserDetails> storeNameList = new ArrayList<>();
    ArrayList<String> brandList = new ArrayList<>();
    ArrayList<String> categoryList = new ArrayList<>();
    //   categoryItemList
    ArrayList<CategoryDetails> categoryItemList = new ArrayList<CategoryDetails>();
    ArrayList<String> refinedItemList = new ArrayList<>();
    ArrayList<Integer> ItemNewList = new ArrayList<>();
    ArrayAdapter<String> storeListAdapter;
    ArrayAdapter<String> brandAdapter;
    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> itemAdapter;
    Button pickMedia, uploadAdvertisement;

    LinearLayout mediaImage, mediaGIF, mediaVideo;
    EditText brandText,itemText;
    Spinner categorySpinner , itemSpinner;
    Thread thread;
    ArrayList<String> storeNamearray = new ArrayList<String>();

    ArrayAdapter<CharSequence> adv_type_spinner_adapter, adv_pricing_spinner_adapter;
    String advertismentStartDurtionstring, advertisementExpiringDuration, adv_StartingDurationDate, adv_EndingDurationDate;
    UserDetails userDetails;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    String startTime, endTime;
    TextView scheduleHourText, scheduleDayText, scheduleWeekText;
    AdvertisementDetails advertisementDetailsStatus;
    private DatePickerDialog.OnDateSetListener purchaseDateSetListener;
    static String oneDayAfterScheduledDate, scheduledsevendays;
    private MenuItem item;
    DatabaseReference userDetailsDataRef;
    String userName;
    NavigationView navigationView;
    int categoryPosition;
    Button viewAdvertisementsButton;
    TextView reviewCount;
    // ItemDetails itemDetails;
    ArrayList<CategoryDetails> categoryDetailsArrayList = new ArrayList<>();
    ArrayList<String> itemIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_advertisement);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        androidx.appcompat.widget.Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setTitle("Maintain Banners");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AddAdvertisementActivity.this);

        mHeaderView = navigationView.getHeaderView(0);
        textViewUsername = (TextView) mHeaderView.findViewById(R.id.username);
        textViewEmail = (TextView) mHeaderView.findViewById(R.id.roleName);
        textViewUsername.setText(DashBoardActivity.roleName);
        textViewEmail.setText(DashBoardActivity.userName);
        navigationView.setCheckedItem(R.id.Add_Advertisment);

        adv_Pricing_spinner = findViewById(R.id.ad_pricing_spinner);
        ad_TypeRadioGroup = findViewById(R.id.ad_typeradioGroup);
        ad_TypeItemGroup = findViewById(R.id.Itemad_typeradioGroup);

        ad_MediaRadioGroup = findViewById(R.id.admedia);
        viewAdvertisementsButton = findViewById(R.id.viewAdvertisements);

        reviewCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.reviewApproval));
        reviewCount.setGravity(Gravity.CENTER_VERTICAL);
        reviewCount.setTypeface(null, Typeface.BOLD);
        reviewCount.setTextColor(getResources().getColor(R.color.redColor));

        pickMedia = findViewById(R.id.pickMedia);
        advertisementImage = findViewById(R.id.imageView);
        uploadAdvertisement = findViewById(R.id.uploadadvertisement);
        brands_storesEditText = findViewById(R.id.invisbletext);
        item_storesEditText = findViewById(R.id.visbletext);
        brandText = findViewById(R.id.invisbletext1);


        categorySpinner = findViewById(R.id.categorySpinner);
        itemSpinner = findViewById(R.id.categoryItemSpinner);


        mediaImage = findViewById(R.id.imagelayout);
        mediaGIF = findViewById(R.id.mediagiflayout);
        mediaVideo = findViewById(R.id.mediavideolayout);

        mediaImage.setVisibility(View.INVISIBLE);
        mediaGIF.setVisibility(View.INVISIBLE);
        mediaVideo.setVisibility(View.INVISIBLE);


        sellerDetailRef = CommonMethods.fetchFirebaseDatabaseReference(SELLER_DETAILS_TABLE);
        productDetailRef = CommonMethods.fetchFirebaseDatabaseReference("ItemDetails");

        productDatabaseReef  =CommonMethods.fetchFirebaseDatabaseReference("ProductDetails");
        advertisementDataRef = CommonMethods.fetchFirebaseDatabaseReference(ADVERTISEMT_DETAILS_FIREBASE_TABLE);
        advertisementStorageRef = CommonMethods.fetchFirebaseStorageReference(ADVERTISEMT_DETAILS_FIREBASE_TABLE);
        categoryRef = CommonMethods.fetchFirebaseDatabaseReference(CATEGORY_DETAILS_TABLE);


        viewAdvertisementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAdvertisementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                categoryList.add("Select Category");
                categoryList.add("Others");
                for (DataSnapshot categorysnap : dataSnapshot.getChildren()) {
                    CategoryDetails categoryDetails = categorysnap.getValue(CategoryDetails.class);
                    categoryList.add(categoryDetails.getCategoryName());


                }
                //  Toast.makeText(AddAdvertisementActivity.this, "e"+categoryList, Toast.LENGTH_SHORT).show();
                if (categoryList != null) {

                    categoryAdapter = new ArrayAdapter<>
                            (AddAdvertisementActivity.this, android.R.layout.simple_spinner_item, categoryList);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(categoryAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        productDatabaseReef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemIDList.clear();


                for (DataSnapshot postSnapshot : snapshot.getChildren()){


                    final ItemDetails itemDetails = postSnapshot.getValue(ItemDetails.class);
                    categoryDetailsArrayList = itemDetails.getCategoryDetailsArrayList();

                    Iterator itemIterator = categoryDetailsArrayList.iterator();

                    while (itemIterator.hasNext()) {

                        CategoryDetails categoryDetails = (CategoryDetails) itemIterator.next();
                        //  Toast.makeText(AddAdvertisementActivity.this, "s"+selectedItemName, Toast.LENGTH_SHORT).show();
                        if (1 == Integer.parseInt((categoryDetails.getCategoryid()))  ) {
                            itemIDList.add(categoryDetails.getItemId());

                        }
                    }

                }

                refinedItemList.clear();

                refinedItemList.add("Select Category");
                ItemNewList.clear();
                for (int i = 0; i < itemIDList.size(); i++){
                    productDatabaseReef.child(String.valueOf(itemIDList.get(i))).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ItemDetails itemObjDetails = snapshot.getValue(ItemDetails.class);
                                if ("Available".equalsIgnoreCase(itemObjDetails.getItemStatus())) {
                                    refinedItemList.add(itemObjDetails.getItemName());
                                    ItemNewList.add(itemObjDetails.getItemId());
                                    //   itemid = itemObjDetails.getItemId();

                                    // itemIDList = refinedItemList;
                                }
                            }

                            //  Toast.makeText(AddAdvertisementActivity.this, "Available"+refinedItemList, Toast.LENGTH_SHORT).show();



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (refinedItemList != null) {
                    brandAdapter = new ArrayAdapter<>(AddAdvertisementActivity.this, android.R.layout.simple_spinner_item, refinedItemList);
                    brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(brandAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        adv_type_spinner_adapter = ArrayAdapter
                .createFromResource(AddAdvertisementActivity.this, R.array.advertismentype_spinner,
                        R.layout.support_simple_spinner_dropdown_item);
        adv_type_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        adv_pricing_spinner_adapter = ArrayAdapter
                .createFromResource(AddAdvertisementActivity.this, R.array.advertisement_price,
                        R.layout.support_simple_spinner_dropdown_item);
        adv_pricing_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adv_Pricing_spinner.setAdapter(adv_pricing_spinner_adapter);
        adv_Pricing_spinner.setTitle("Select Advertisement Placing");


        categorySpinner.setVisibility(View.VISIBLE);
        itemSpinner.setVisibility(View.VISIBLE);



/*
        productDatabaseReef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot itemsnap : snapshot.getChildren()){
                    ItemDetails   itemDetails = itemsnap.getValue(ItemDetails.class);
                    itemDetailsArrayList = itemDetails.getCategoryDetailsArrayList();
                    Iterator itemIterator = itemDetailsArrayList.iterator();

                    while (itemIterator.hasNext()) {

                        CategoryDetails categoryDetails = (CategoryDetails) itemIterator.next();
                        itemCategoryHeader.add(categoryDetails.getCategoryName());
                    }

                }
                removeDuplicatesList(itemCategoryHeader);
                if (itemCategoryHeader.size() > 0) {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/


      /*  productDatabaseReef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot itemSnap : snapshot.getChildren()) {
                    itemDetails = itemSnap.getValue(ItemDetails.class);
                    itemDetailsList.add(itemDetails);
                    itemDetailsArrayList = itemDetails.getCategoryDetailsArrayList();
                    Iterator itemIterator = itemDetailsArrayList.iterator();

                    while (itemIterator.hasNext()) {

                        CategoryDetails categoryDetails = (CategoryDetails) itemIterator.next();
                        itemCategoryHeader.add(categoryDetails.getCategoryName());
                    }

                }

                removeDuplicatesList(itemCategoryHeader);
                if (itemCategoryHeader.size() > 0) {
                    for (int i = 0; i < itemCategoryHeader.size(); i++){

                        categoryString = itemCategoryHeader.get(i);

                        for (int j = 0; j < itemDetailsList.size(); j++) {

                            ItemDetails userDetails = itemDetailsList.get(j);

                            categoryItemList = itemDetailsList.get(j).getCategoryDetailsArrayList();

                            Iterator itemIterator = categoryList.iterator();

                            while (itemIterator.hasNext()) {

                                CategoryDetails categoryDetails = (CategoryDetails) itemIterator.next();
                                if (selectedCategoryString.equalsIgnoreCase(categoryDetails.getCategoryName())) {
                                   // if (Available.equalsIgnoreCase(userDetails.getItemStatus())) {
                                        refinedItemList.add(userDetails);
                                   // }
                                }


                            }
                        }



                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/




        productDetailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {

                    brandList.clear();
                    brandList.add("Select Brands");


                    for (DataSnapshot productSnap : dataSnapshot.getChildren()) {


                        ItemDetails itemDetails = productSnap.getValue(ItemDetails.class);
                        if (!itemDetails.getItemBrand().equalsIgnoreCase("No Brand")) {
                            brandList.add(itemDetails.getItemBrand());
                        }


                    }

                    removeDuplicatesList(brandList);
                    if (brandList != null) {
                        //   brandAdapter = new ArrayAdapter<>
                        //           (AddAdvertisementActivity.this, android.R.layout.simple_spinner_item, brandList);
                        //   brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ad_TypeItemGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            checkedId = ad_TypeItemGroup.getCheckedRadioButtonId();


            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            adv_DurationSelectedString = radioButton.getText().toString();


        });



        ad_TypeRadioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            checkedId = ad_TypeRadioGroup.getCheckedRadioButtonId();


            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            adv_TypeString = radioButton.getText().toString();


        });

        mediaImage.setVisibility(View.VISIBLE);
        mediaVideo.setVisibility(View.INVISIBLE);
        mediaGIF.setVisibility(View.INVISIBLE);
        pickMedia.setOnClickListener(v -> openFileChooser());
        RadioButton imageButton = findViewById(R.id.images);
        imageButton.setChecked(true);
        mediaImage.setVisibility(View.VISIBLE);
        mediaVideo.setVisibility(View.INVISIBLE);
        mediaGIF.setVisibility(View.INVISIBLE);
        pickMedia.setOnClickListener(v -> openFileChooser());


        ad_MediaRadioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            checkedId = ad_MediaRadioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            adv_MediaSelectedString = radioButton.getText().toString();


            if (adv_MediaSelectedString.equals("Image")) {
                mediaImage.setVisibility(View.VISIBLE);
                mediaVideo.setVisibility(View.INVISIBLE);
                mediaGIF.setVisibility(View.INVISIBLE);
                pickMedia.setOnClickListener(v -> openFileChooser());


            } else if (adv_MediaSelectedString.equals("GIF")) {
                mediaImage.setVisibility(View.INVISIBLE);
                mediaVideo.setVisibility(View.INVISIBLE);
                mediaGIF.setVisibility(View.VISIBLE);

            } else if (adv_MediaSelectedString.equals("Video")) {
                mediaImage.setVisibility(View.INVISIBLE);
                mediaVideo.setVisibility(View.VISIBLE);
                mediaGIF.setVisibility(View.INVISIBLE);

            }

        });


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryString = parent.getItemAtPosition(position).toString();
                brands_storesEditText.setText(selectedCategoryString);
                categoryPosition = position;
                CategoryitemName(selectedCategoryString);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedItemName = adapterView.getItemAtPosition(pos).toString();


                item_storesEditText.setText(selectedItemName);

                if (itemIDList.size() > 0){
                    for (int i = 0; i < itemIDList.size(); i++){


                        //  Toast.makeText(AddAdvertisementActivity.this, "spinner"+itemIDList.size(), Toast.LENGTH_SHORT).show();

                        productDatabaseReef.child(String.valueOf(itemIDList.get(i))).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    ItemDetails itemObjDetails = snapshot.getValue(ItemDetails.class);

                                    if (selectedItemName.equalsIgnoreCase(itemObjDetails.getItemName())) {
                                        selecteditem = itemObjDetails.getItemId();

                                    }
                                }

                                //  Toast.makeText(AddAdvertisementActivity.this, "Available"+refinedItemList, Toast.LENGTH_SHORT).show();



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                }







            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        adv_Pricing_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adv_PricingSelectedString = parent.getItemAtPosition(position).toString();
                adv_Priority = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        uploadAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categorySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(AddAdvertisementActivity.this.getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ad_MediaRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(AddAdvertisementActivity.this.getApplicationContext(), "Select advertisement type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mimageuri == null) {
                    Toast.makeText(AddAdvertisementActivity.this.getApplicationContext(), "Select advertising Media", Toast.LENGTH_SHORT).show();
                    return;
                } else if (adv_Priority == 0) {
                    Toast.makeText(AddAdvertisementActivity.this.getApplicationContext(), "Select Advertisement Placement", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mimageuri != null) {
                    AddAdvertisementActivity.this.UploadImage();
                    return;
                }


            }
        });


        advertisementDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addMaxId = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mimageuri = data.getData();
            Glide.with(AddAdvertisementActivity.this).load(mimageuri).into(advertisementImage);
        }
    }

    private String getExtenstion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.vieworders) {
            Intent intent = new Intent(getApplicationContext(), ViewOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.category) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Add_Advertisment) {

            Intent intent = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.add_items) {
            Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (id == R.id.add_description) {
            Intent intent = new Intent(getApplicationContext(), Add_Description_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.discounts) {
            Intent intent = new Intent(getApplicationContext(), DiscountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.add_seller) {
            Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.updatestatus) {
            Intent intent = new Intent(getApplicationContext(), UpdateOrderStatusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }/*else if(id==R.id.deliveryfare){
            Intent intent = new Intent(getApplicationContext(), AddDeliveryFareActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/ else if (id == R.id.logoutscreen) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddAdvertisementActivity.this);
            bottomSheetDialog.setContentView(R.layout.logout_confirmation);
            Button logout = bottomSheetDialog.findViewById(R.id.logout);
            Button stayinapp = bottomSheetDialog.findViewById(R.id.stayinapp);

            bottomSheetDialog.show();
            bottomSheetDialog.setCancelable(false);

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences myPrefs = getSharedPreferences("LOGIN",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(AddAdvertisementActivity.this,
                            LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });
            stayinapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    navigationView.setCheckedItem(R.id.Add_Advertisment);
                }
            });
        } else if (id == R.id.contactdetails) {

            Intent intent = new Intent(getApplicationContext(), DeliverySettings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.CustomerDetails) {
            Intent intent = new Intent(getApplicationContext(), CustomerDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }  else if (id == R.id.reviewApproval) {
            Intent intent = new Intent(getApplicationContext(), ItemRatingReviewApprovalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.legal) {
            Intent intent = new Intent(getApplicationContext(), LegalDetailsUploadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.dashboard) {
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.billingScreen) {
            Intent intent = new Intent(getApplicationContext(), BillingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return false;
    }

    public void UploadImage() {
        if (mimageuri != null) {
            final SweetAlertDialog pDialog = new SweetAlertDialog(AddAdvertisementActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#1898ED"));
            pDialog.setTitleText("Uploading Image.....");
            pDialog.setCancelable(false);
            pDialog.show();

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mimageuri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();


            StorageReference fileRef = advertisementStorageRef.child("Advertisement/" + System.currentTimeMillis());
            mUploadTask = fileRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                final Uri downloadUrl = urlTask.getResult();
                String creationDate = DateUtils.fetchCurrentDate();
                advertisementDetails.setImage(String.valueOf(downloadUrl));

                advertisementDetails.setId(String.valueOf(addMaxId + 1));
                advertisementDetails.setCreationdate(creationDate);
                advertisementDetails.setAdvertisingAgent(adv_TypeString);
                advertisementDetails.setAdvertisingBrandName(brands_storesEditText.getText().toString());
                advertisementDetails.setAdvertisingMedia(adv_MediaSelectedString);
                advertisementDetails.setAdvertisingType(adv_TypeSelectedString);
                advertisementDetails.setItemName(selectedItemName);
                advertisementDetails.setAdvertisementPlacing((adv_PricingSelectedString));
                advertisementDetails.setAdvertisementpriority(String.valueOf(adv_Priority));
                advertisementDetails.setAdvertisementExpiringDuration(advertisementExpiringDuration);
                advertisementDetails.setAdvertisementStartingDuration(advertismentStartDurtionstring);
                advertisementDetails.setAdvertisingDuration(advertisementSelectedString);
                advertisementDetails.setAdvertisementstatus(ACTIVE_STATUS);
                advertisementDetails.setAdvertisementStartingDurationDate(adv_StartingDurationDate);
                advertisementDetails.setAdvertisementEndingDurationDate(adv_EndingDurationDate);
                advertisementDetails.setScheduledDate(advertisementScheduleDate);
                advertisementDetails.setScheduledTime(advertisementScheduledTime);
                pos = categoryPosition - 1;
                advertisementDetails.setCategoryId(String.valueOf(String.valueOf(pos)));

                if (selecteditem != null){
                    advertisementDetails.setItemId(selecteditem);
                }

                advertisementDetails.setAdvertisingCategoryName(brands_storesEditText.getText().toString());
                advertisementDataRef.child(String.valueOf(addMaxId + 1)).setValue(advertisementDetails);
                Toast.makeText(AddAdvertisementActivity.this, DETAILS_INSERTED, Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


                Intent intent = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                advertisementImage.setImageDrawable(null);
                mimageuri = null;

            });
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AddAdvertisementActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<ItemReviewAndRatings> list = new ArrayList<>();
        DatabaseReference
                itemReviewDataRef = CommonMethods.fetchFirebaseDatabaseReference(ITEM_RATING_REVIEW_TABLE);
        Query itemApprovalStatusQuery = itemReviewDataRef.orderByChild("itemRatingReviewStatus").equalTo("Waiting For Approval");
        itemApprovalStatusQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ItemReviewAndRatings itemReviewAndRatings = dataSnapshot1.getValue(ItemReviewAndRatings.class);

                    list.add(itemReviewAndRatings);
                }
                reviewCount.setText(String.valueOf(list.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void CategoryitemName( String selectedCategoryItem ){

        productDatabaseReef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemIDList.clear();


                for (DataSnapshot postSnapshot : snapshot.getChildren()){


                    final ItemDetails itemDetails = postSnapshot.getValue(ItemDetails.class);
                    categoryDetailsArrayList = itemDetails.getCategoryDetailsArrayList();

                    Iterator itemIterator = categoryDetailsArrayList.iterator();

                    while (itemIterator.hasNext()) {

                        CategoryDetails categoryDetails = (CategoryDetails) itemIterator.next();
                        Toast.makeText(AddAdvertisementActivity.this, "s"+selectedItemName, Toast.LENGTH_SHORT).show();
                        if (selectedCategoryItem.equals(categoryDetails.getCategoryName()))  {
                            itemIDList.add(categoryDetails.getItemId());

                        }
                    }

                }

                refinedItemList.clear();

                refinedItemList.add("Select Category");
                ItemNewList.clear();
                for (int i = 0; i < itemIDList.size(); i++){
                    productDatabaseReef.child(String.valueOf(itemIDList.get(i))).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ItemDetails itemObjDetails = snapshot.getValue(ItemDetails.class);
                                if ("Available".equalsIgnoreCase(itemObjDetails.getItemStatus())) {
                                    refinedItemList.add(itemObjDetails.getItemName());

                                }
                            }

                            //  Toast.makeText(AddAdvertisementActivity.this, "Available"+refinedItemList, Toast.LENGTH_SHORT).show();



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (refinedItemList != null) {
                    brandAdapter = new ArrayAdapter<>(AddAdvertisementActivity.this, android.R.layout.simple_spinner_item, refinedItemList);
                    brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(brandAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}