package de.weimar.de.Schneller.Spur;


import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import de.weimar.de.Schneller.Spur.R;

public class Products extends Activity {
	private ListView getallproducts;
	private JSONArray jsonarray;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        
        this.getallproducts = (ListView) this.findViewById(R.id.all_product);

        new GetAllProductList().execute(new ApiConnector());
        
     // for click of each item
        this.getallproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		try
        		{
        			// Get the item clicked
        			JSONObject productClicked = jsonarray.getJSONObject(position);
        			
        			// send Product ID.
        			Intent show_product_detail = new Intent(getApplicationContext(),Product_Detail.class);
        			show_product_detail.putExtra("Pid", productClicked.getInt("Pid"));
    
        			startActivity(show_product_detail);	
        			
        		}
        		catch(JSONException e)
        		{
        			e.printStackTrace();
        		}
        		
        		
        	}
        	
		});

    }

 
    private class GetAllProductList extends AsyncTask<ApiConnector,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

             return params[0].GetAllProducts("hi");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

        	setListAdapter(jsonArray);
        	
        }
    }

    public void setListAdapter (JSONArray jsonArray) {
    	this.jsonarray = jsonArray;
    	this.getallproducts.setAdapter(new Get_All_Product_list_View(jsonArray,this));


    }

    
    // my option menu
    @Override
	public boolean onCreateOptionsMenu (Menu menu){
		
		MenuInflater inflater = getMenuInflater();
		
		
		// Get user session
		String userid = null;

		SharedPreferences usersession = getSharedPreferences("usersession", 0); 
		userid = usersession.getString("userid",null);
		
		//if guest
		
		if (userid == null) {    
			inflater.inflate(R.menu.public_menu, menu);
			return true;
	
		}else   // if logged in user
		{
			inflater.inflate(R.menu.user_menu, menu);
			return true;
		}
	
		
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	switch(item.getItemId()) {
		
    	case R.id.login:
    		Intent loginintent = new Intent(Products.this,Login.class); // 
			startActivity(loginintent);
			break;
			
    	case R.id.register:
    		Intent registerintent = new Intent(Products.this,register.class); // 
			startActivity(registerintent);
			break;
			
    	case R.id.search_box:
			Intent searchintent = new Intent(Products.this,Search.class); // 
			startActivity(searchintent);
			break;
			
    	case R.id.profile:
			Intent profileintent = new Intent(Products.this,profile.class); // 
			startActivity(profileintent);
			break;
			
    	case R.id.help:
			Intent helpintent = new Intent(Products.this,Help.class); // 
			startActivity(helpintent);
			break;
			
    	case R.id.sell:
			Intent sellintent = new Intent(Products.this,sell.class); // 
			startActivity(sellintent);
			break;
		
    	case R.id.purchases_Menu:
			Intent purchaseintent = new Intent(Products.this,Purchases.class); // 
			startActivity(purchaseintent);
			break;
			
    	case R.id.Request_Menu:
			Intent requestsintent = new Intent(Products.this,requests.class); // 
			startActivity(requestsintent);
			break;
			
			
    	case R.id.logout:
			
			// create a shared preference 
			SharedPreferences usersession = getSharedPreferences("usersession", 0);
			  
		    // Edit the shared preference
			SharedPreferences.Editor spedit = usersession.edit();
			    
			// Cleans the userid string to null
			spedit.putString("userid",null );

			  // Commits the changes and closes the editor
			  spedit.commit();
			  
			//Takes the user to the login screen and displays log out message  
			Intent logoutintent = new Intent(Products.this,Products.class); // 
			startActivity(logoutintent);   // starts the BuySell activity  
			Toast.makeText(getBaseContext(),"You are logged Out", Toast.LENGTH_SHORT).show();
			
			break;

			
    	}
    	
    
    	return true;
    }
    
    
}
