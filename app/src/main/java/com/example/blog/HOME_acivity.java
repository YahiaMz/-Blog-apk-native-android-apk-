package com.example.blog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import Fragments.AccountFragment;
import Fragments.HomeFragment;

public class HOME_acivity extends AppCompatActivity {
private FragmentManager fragmentManager ;
private FloatingActionButton fab;
private BottomNavigationView bottomNavigationView;
private int PICK_FROM_GALLERY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_acivity);
        fragmentManager = getSupportFragmentManager();
        initial();

        fragmentManager.beginTransaction().replace(R.id.frameLayout , new HomeFragment()
                , AccountFragment.class.getSimpleName()).commit();
    }

    private  void initial( ){

        fab = findViewById(R.id.floatingActionButton);
        bottomNavigationView  = findViewById(R.id.bottom_NavigationView);



 bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
     @Override
     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
         switch (item.getItemId()) {


         case R.id.item_home:
             Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
         /*if( account != null) {
             fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
             fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit(); }
*/
             fragmentManager.beginTransaction().replace(R.id.frameLayout , new HomeFragment()
                     , AccountFragment.class.getSimpleName()).commit();

         break;

         case R.id.item_profile:
             Toast.makeText(getApplicationContext(), "Profile" +
                     "", Toast.LENGTH_SHORT).show();

         /*fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();

         if( account != null) {
             fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();

         } else {
             fragmentManager.beginTransaction().add(R.id.frameLayout , new AccountFragment() ,AccountFragment.class.getSimpleName()).commit();
         }*/
             fragmentManager.beginTransaction().replace(R.id.frameLayout , new AccountFragment()
                     , AccountFragment.class.getSimpleName()).commit();

         break;
     }
     return true;
 }

 });

/*
nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

          *//*switch (item.getItemId()) {


              case R.id.item_home:
                Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                     if( account != null) {
                         fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                         fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                     }

                  break;
              case R.id.item_profile:
                  Fragment accoun = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                  fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();

                  if( accoun != null) {
                      fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();

                  } else {
                 fragmentManager.beginTransaction().add(R.id.frameLayout , new AccountFragment() ,AccountFragment.class.getSimpleName()).commit();
                  }

                  break;
          }
*//*        return true;
    }

});*/


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

   /*             PopupMenu popupMenu = new PopupMenu(getApplicationContext() , fab);
                popupMenu.inflate(R.menu.post_popupmenu);
                popupMenu.show();*/

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_FROM_GALLERY);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Intent i = new Intent(HOME_acivity.this , Create_Post.class );
              i.setData(uri);
             startActivity(i);
        }}

}