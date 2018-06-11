package com.example.abhishek.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class frag_search_order extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public frag_search_order() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_search_order.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_search_order newInstance(String param1, String param2) {
        frag_search_order fragment = new frag_search_order();
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
    }

    int filter;
    String TypeFilter;
    String DeptFilter;

    Spinner dropdown;
    Button srch_bttn;
    EditText sub;
    EditText ref;
    Spinner type_filter;
    Spinner dept_filter;
    TextView dis;


    private void initialise(View v)
    {
        dropdown = v.findViewById(R.id.spinnerFilter);
        srch_bttn=v.findViewById(R.id.search_bttn);
        sub=v.findViewById(R.id.sub_filter);
        ref=v.findViewById(R.id.refno_filter);
        type_filter = v.findViewById(R.id.spinnerType);
        dept_filter = v.findViewById(R.id.spinnerDept);
        dis=v.findViewById(R.id.textView);
    }

    private void setFilter (int id)
    {
        filter=id;
    }

    private void setTypeFilter(String Type)
    {
        TypeFilter=Type;
    }

    private void setDeptFilter(String Dept)
    {
        DeptFilter=Dept;
    }

    public void DownloadTry(Query query)
    {
        final ArrayList <String> urls= new ArrayList<>();
        final ArrayList <String> names= new ArrayList<>();
        final ArrayList <String> exts= new ArrayList<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot info: dataSnapshot.getChildren()) {
                        String d_sub=info.child("subject").getValue().toString();
                        String d_type=info.child("select_type").getValue().toString();
                        String d_dept=info.child("select_department").getValue().toString();
                        String d_url=info.child("downloadurl").getValue().toString();
                        String d_ext=info.child("extension").getValue().toString();
                        urls.add(d_url);
                        names.add(d_sub+"\t\t"+d_type+"\t\t"+d_dept);
                        exts.add(d_ext);
                    }

                    Intent i=new Intent(getContext(),Results.class);
                    i.putStringArrayListExtra("urls",urls);
                    i.putStringArrayListExtra("subs",names);
                    i.putStringArrayListExtra("extension",exts);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getContext(),"No such files found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Documents/");

    private void searchSub()
    {
        String subEnter=sub.getText().toString();
        if (subEnter.equals(""))
            Toast.makeText(getContext(),"Please Enter Subject",Toast.LENGTH_SHORT).show();
        else
        {
            Query query = reference.orderByChild("subject").equalTo(subEnter);
            DownloadTry(query);
        }
    }

    private void searchType()
    {
        Query query = reference.orderByChild("select_type").equalTo(TypeFilter);
        DownloadTry(query);
    }

    private void searchDept()
    {
        Query query = reference.orderByChild("select_department").equalTo(DeptFilter);
        DownloadTry(query);
    }

    private void searchRefNo()
    {
        String refEnter=ref.getText().toString();
        if (refEnter.equals(""))
            Toast.makeText(getContext(),"Please Enter Reference Number",Toast.LENGTH_SHORT).show();
        else
        {
            Query query = reference.orderByChild("ref_no").equalTo(refEnter);
            DownloadTry(query);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.fragment_frag_search_order, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String myValue  = arguments.getString("Try");
        }
        initialise(v);
        String[] filters = new String[]{"Select Filter","Subject", "Type", "Department","RefNo"};
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,filters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        final String[] types = new String[]{"Select Type","Internal Notes", "Type2", "Type3"};
        ArrayAdapter<String>adapter_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,types);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_filter.setAdapter(adapter_type);

        //Department Filter options
        final String[] depts = new String[]{"Select Department","Information Technology Notes", "Dept2", "Dept3"};
        ArrayAdapter<String>adapter_dept = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,depts);
        adapter_dept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept_filter.setAdapter(adapter_dept);

        type_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                srch_bttn.setVisibility(View.INVISIBLE);
                setTypeFilter(types[position]);
                switch(position)
                {
                    case 0: //No item selected
                        break;
                    case 1: //Filter by Type1
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                    case 2: //Filter by Type2
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                    case 3: //Filter by Type3
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                    case 4: //Filter by Type4
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        dept_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                srch_bttn.setVisibility(View.INVISIBLE);
                setDeptFilter(depts[position]);
                switch(position)
                {
                    case 0: //No item selected
                        break;
                    case 1: //Filter by Dept1
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                    case 2: //Filter by Dept2
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                    case 3: //Filter by Dept3
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                    case 4: //Filter by Dept4
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                srch_bttn.setVisibility(View.INVISIBLE);
                sub.setVisibility(View.INVISIBLE);
                ref.setVisibility(View.INVISIBLE);
                dept_filter.setVisibility(View.INVISIBLE);
                type_filter.setVisibility(View.INVISIBLE);
                setFilter(position);
                switch(position)
                {
                    case 0: //No item selected
                            break;
                    case 1: //Filter by Subject
                        sub.setVisibility(View.VISIBLE);
                        srch_bttn.setVisibility(View.VISIBLE);
                        break;
                    case 2: //Filter by Type
                        type_filter.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        dept_filter.setVisibility(View.VISIBLE);
                        break;
                    case 4: //Filter by RefNo
                        srch_bttn.setVisibility(View.VISIBLE);
                        ref.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        srch_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (filter)
                {
                    case 1:
                        searchSub();
                        break;
                    case 2:
                        searchType();
                        break;
                    case 3:
                        searchDept();
                        break;
                    case 4:
                        searchRefNo();
                        break;
                }
            }
        });

        return v;
    }

}
