package com.example.elie.driverapp.Model.DS;


import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.*;

import com.example.elie.driverapp.Model.Backend.Backend;
import com.example.elie.driverapp.Model.Entities.ClientRequest;
import com.example.elie.driverapp.Model.Entities.ClientRequestStatus;
import com.example.elie.driverapp.Model.Entities.Driver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

import java.util.ArrayList;


public class FireBase_DSManager implements Backend

{
    /***
     * Interface Action
     * @param <T> template : type of object for the implementation of the interface
     *      OnSuccess : It receives the name of the client that is the key
     *      OnFailure : It tells us if there is a failure in the loading and throws exception
     *      OnProgress : It tells us the progress of the load of the data with a message
     *
     */

    private static FireBase_DSManager fireBase_dsManager = null;

    public static FireBase_DSManager getFireBase_dsManager()
    {
        if (fireBase_dsManager == null)
        {
            fireBase_dsManager = new FireBase_DSManager();
        }
        return fireBase_dsManager;
    }


    // creation of my databaseReference
    public static DatabaseReference ClientsRef;
    public static Driver CurrentDriver;
    public static DatabaseReference DriversRef;
    public static List<ClientRequest> ClientsList;
    public static List<Driver> DriversList;
    public static ChildEventListener clientRefChildEventListener;
    public static ChildEventListener driverRefChildEventListener;



    static
    {
        FirebaseDatabase data=FirebaseDatabase.getInstance();
        FirebaseAuth  auth=FirebaseAuth.getInstance();
        //The reference of my data of clients is Clients
        DriversList = new ArrayList<Driver>();
        ClientsRef= data.getReference("Clients");
        DriversRef=data.getReference("Drivers");
        ClientsList=new ArrayList<ClientRequest>();

    }

    public FireBase_DSManager()

    {
        if (ClientsRef!=null)
        {
            Log.d("ESSAI", "test");
        }
        if (DriversRef!=null)
        {
            Log.d("ESSAI", "test");
        }

        CurrentDriver=new Driver();
    }



    @Override
    public  void addDriverToFireBase(final Driver driver,final Action<String> action)
    {
        String key=String.valueOf(driver.getID());
        DriversRef.child(key).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.OnSuccess(driver.getName());
                action.OnProgress("Load Driver data",100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.OnFailure(e);
                action.OnProgress("Error loading data",100);
            }
        });
    }


@Override
    public   void notifyToClientList(final NotifyDataChange<ClientRequest> notifyDataChange)

    {

        if (notifyDataChange != null)
        {

            if ( clientRefChildEventListener != null)
            {
                notifyDataChange.OnFailure(new Exception("no change"));
                return;
            }

        ClientsList.clear();



            clientRefChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                ClientRequest c = dataSnapshot.getValue(ClientRequest.class);
                String ID = dataSnapshot.getKey();
                c.setId(Integer.parseInt(ID));
                ClientsList.add(c);
                notifyDataChange.OnDataAdded(c);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)

            {

                ClientRequest c=dataSnapshot.getValue(ClientRequest.class);
                String ID = dataSnapshot.getKey();
                c.setId(Integer.parseInt(ID));
                for (int i=0; i < ClientsList.size() ; i++)
                {
                    if ( ClientsList.get(i).getId() == c.getId() )
                    {
                        ClientsList.set(i,c);
                        break;
                    }
                }
                notifyDataChange.OnDataChanged(c);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                notifyDataChange.OnFailure(databaseError.toException());
            }
        };
        ClientsRef.addChildEventListener(clientRefChildEventListener);

        }

    }


    @Override
    public  void notifyToDriverList(final NotifyDataChange<Driver> notifyDataChange)

    {
        driverRefChildEventListener=  new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                 Driver d  = dataSnapshot.getValue(Driver.class);
                String ID = dataSnapshot.getKey();
                d.setID(Integer.parseInt(ID));
                DriversList.add(d);
                notifyDataChange.OnDataAdded(d);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)

            {
                Driver d=dataSnapshot.getValue(Driver.class);
                String ID = dataSnapshot.getKey();
                d.setID(Integer.parseInt(ID));
                for (int i=0; i < DriversList.size() ; i++)
                {
                    if ( DriversList.get(i).getID() == d.getID() )
                    {
                        DriversList.set(i,d);
                        break;
                    }
                }
                notifyDataChange.OnDataChanged(d);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                notifyDataChange.OnFailure(databaseError.toException());
            }
        };

        DriversRef.addChildEventListener(driverRefChildEventListener);

    }



    public   ArrayList<ClientRequest> WaitingClients()
    {

        ArrayList<ClientRequest> mylist=new ArrayList<>();

        for (ClientRequest  c : ClientsList )
        {
            if(c.getStatus()== ClientRequestStatus._Waiting)
                mylist.add(c);
        }


        return mylist;

    }

    public   ArrayList<Driver> drivers()
    {

        return (ArrayList)DriversList;

    }



    public ArrayList<ClientRequest> ClientsF()
    {

        ArrayList<ClientRequest> mylist=new ArrayList<>();

        for (ClientRequest  c : ClientsList )
        {
            if((c.getStatus()== ClientRequestStatus._Finished))
                mylist.add(c);
        }


        return mylist;

    }

    public ArrayList<ClientRequest> ClientsC()
    {

        ArrayList<ClientRequest> mylist=new ArrayList<>();

        for (ClientRequest  c : ClientsList )
        {
            if((c.getStatus()==ClientRequestStatus._Current))
                mylist.add(c);
        }



        return mylist;

    }




        public void   stopNotifyToClientList()
         {

                    if  (clientRefChildEventListener  !=  null )
                            ClientsRef.removeEventListener(clientRefChildEventListener);
                    clientRefChildEventListener=null;

        }





    }









