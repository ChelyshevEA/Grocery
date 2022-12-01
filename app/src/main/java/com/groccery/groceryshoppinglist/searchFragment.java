package com.groccery.groceryshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.groccery.groceryshoppinglist.CustomAdapter4.uv_counter;
import static com.groccery.groceryshoppinglist.ItemActivity.maintitle;
import static com.groccery.groceryshoppinglist.createList.getMonthName_Abbr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class searchFragment extends Fragment implements  backPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    SearchView searchView;
    CustomAdapter4 adapter;
    DatabaseHelper mydb;
    String myidentifir = "";
    ImageView imageView;
    ArrayList<String> arrayList = new ArrayList<String>();
    private OnFragmentInteractionListener mListener;
    DatabaseReference mref;

    public searchFragment() {
        // Required empty public constructor
    }


    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mref = FirebaseDatabase.getInstance().getReference();
        mydb = DatabaseHelper.getInstance(getActivity());


        arrayList.add("Рис");
        arrayList.add("Гречка");
        arrayList.add("Макароны");
        arrayList.add("Консервирвы");
        arrayList.add("Хлеб");
        arrayList.add("Растительное масло");
        arrayList.add("Сливочное масло");
        arrayList.add("Орехи");
        arrayList.add("Сухофрукты");
        arrayList.add("Мед");
        arrayList.add("Мука");
        arrayList.add("Сахар");
        arrayList.add("Кофе");
        arrayList.add("Чай");
        arrayList.add("Молоко");
        arrayList.add("Сливки");
        arrayList.add("Яйца");
        arrayList.add("Сок");
        arrayList.add("Йогурт");
        arrayList.add("Сыр");
        arrayList.add("Свежие фрукты");
        arrayList.add("Свежие овощи");
        arrayList.add("Мясо");
        arrayList.add("Курица");
        arrayList.add("Рыба");
        arrayList.add("Мороженое");
        arrayList.add("Соль");
        arrayList.add("Черный перец");
        arrayList.add("Чеснок");
        arrayList.add("Лук");
        arrayList.add("Кетчуп");
        arrayList.add("Майонез");
        arrayList.add("Горчица");
        arrayList.add("Шоколад");


        Collections.sort(arrayList);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_search, container, false);

        listView = (ListView)rootView.findViewById(R.id.listview20);
        searchView = (SearchView) rootView.findViewById(R.id.searchbar);
        imageView = (ImageView) rootView.findViewById(R.id.imageView862);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                startActivity(new Intent(getContext(),ItemActivity.class));
                getActivity().finish();
            }
        });


        adapter = new CustomAdapter4(getContext(),getActivity(),arrayList);


        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id444) {

                TextView textView = view.findViewById(R.id.textView50);
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                checkBox.setChecked(true);
                uv_counter = 1;

                String itemdata = textView.getText().toString().trim();
                itemdata = itemdata.replace("'","");
                int qty2 = 1;
                int id2 = 1;
                String pp_title2 = "";

                Cursor cursor = mydb.getQty_ID(maintitle,itemdata);
                if (cursor.moveToFirst()){
                    do{
                        int data = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                        int data2 = cursor.getInt(cursor.getColumnIndex("ID"));
                        String data3 = cursor.getString(cursor.getColumnIndex("TITLE"));

                        qty2 = data;
                        id2 = data2;
                        //pp_title2 = data3;

                    }
                    while(cursor.moveToNext());
                }
                cursor.close();

                if(mydb.itemExists(maintitle,itemdata) == true) {
                    qty2 = qty2 + 1;

                    boolean c = mydb.updateData(Integer.toString(id2),maintitle,itemdata,0,qty2);

                    if(c == true) {
                        Toast.makeText(getContext(),itemdata + " * " + qty2 +" updated to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getContext(),"Ошибка!",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    boolean bc = itemdata.matches(".*[a-zA-Zа-яА-Я].*");
                    if(bc == false) {
                        Toast.makeText(getContext(),"Выберите товар из списка",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Calendar calendar = Calendar.getInstance();
                        String date_created = calendar.getTime().toString();
                        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                        calendar.set(Calendar.HOUR_OF_DAY,0);
                        calendar.set(Calendar.MINUTE,0);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-6);
                        int k2 = calendar.get(Calendar.DAY_OF_MONTH);
                        String k3 = getMonthName_Abbr(calendar.get(Calendar.MONTH));
                        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+6);
                        int yr = calendar.get(Calendar.YEAR);
                        String monthnameabbr = getMonthName_Abbr(calendar.get(Calendar.MONTH)) + " " + yr;
                        String s2 = k2 + " " + k3 + " - " + calendar.get(Calendar.DAY_OF_MONTH) + " " + getMonthName_Abbr(calendar.get(Calendar.MONTH)) + " " + yr;

                        Boolean a = mydb.insertData(maintitle, itemdata, 0, 1,round(0.00,2),s2,monthnameabbr,yr,calendar.getTimeInMillis(),date_created);
                        if(a == true) {
                            Toast.makeText(getContext(),itemdata + " * 1 добавлено в список <<" + maintitle + ">>",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(),"Ошибка. Позиция не добавлена!",Toast.LENGTH_LONG).show();

                        }

                    }

                }


            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String text) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                 if(uv_counter == 1 && newText.length() > 0) {

                     searchView.setQuery("", true);
                     newText = "";
                     uv_counter = 4;
                     myidentifir = "identified";
                 }
                 else {
                     myidentifir = "";
                 }

                if(!arrayList.get(arrayList.size()-1).equals("Yogurt")) {
                    arrayList.remove(arrayList.size()-1);
                }
                adapter.getFilter().filter(newText);

                if(!arrayList.contains(newText)) {
                    arrayList.add(newText);

                    if(myidentifir.equals("identified")) {
                        arrayList.remove(arrayList.size()-1);
                    }
                }


                arrayList.remove("");
                if(newText.trim().equals("")) {
                    listView.setVisibility(View.GONE);
                }
                else {
                    listView.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    return rootView;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void on_BackPressed() {

        getActivity().getSupportFragmentManager().popBackStack();

        startActivity(new Intent(getContext(),ItemActivity.class));
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



}


