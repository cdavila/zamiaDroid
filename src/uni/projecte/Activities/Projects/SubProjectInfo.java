/*    	This file is part of ZamiaDroid.
*
*	ZamiaDroid is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*    	ZamiaDroid is distributed in the hope that it will be useful,
*    	but WITHOUT ANY WARRANTY; without even the implied warranty of
*    	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    	GNU General Public License for more details.
*
*    	You should have received a copy of the GNU General Public License
*    	along with ZamiaDroid.  If not, see <http://www.gnu.org/licenses/>.
*/


package uni.projecte.Activities.Projects;

import uni.projecte.R;
import uni.projecte.R.array;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ProjectManager.FieldCreator;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class SubProjectInfo extends Activity{
	
	   private ListView llista;

	   /* project*/
	   private long projId;
	   private String projectName;
	   private long fieldId;
	   
	   
	   private ThesaurusControler tc;
	   private TextView thName;
	   private ProjectSecondLevelControler rsC;
	   private CitationControler sC;
	   private FieldCreator fc;	   
	   //private static final int DIALOG_RS_FIELDS=1;
	   private static final int ADD_FIELD=Menu.FIRST;
	   private static final int CHANGE_TH=Menu.FIRST+1;

		String attributes;
		
		private ArrayAdapter<String> m_adapterForSpinner;
		

	   @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);
	        setContentView(R.layout.project_info);
	        
	        
        	tc= new ThesaurusControler(this);
	        
	        TextView tip= (TextView)findViewById(R.id.tvRschName);
	        
	        projectName=getIntent().getExtras().getString("projName");
	       
	        
	        String projNameText=getApplicationContext().getString(R.string.tvProjectName);

	        tip.setText(Html.fromHtml("<b>"+projNameText+"</b> "+projectName));
	        thName= (TextView)findViewById(R.id.tvProjTh);
	        
	        
	        String defTh=getApplicationContext().getString(R.string.tvDefaultTh);
	        thName.setText(Html.fromHtml("<b>"+defTh+"</b> "+getIntent().getExtras().getString("projDescription"))); 
	        
	        projId= getIntent().getExtras().getLong("Id"); 
	        
	       /* Button btAddField = (Button)findViewById(R.id.bAddNewField);
	        btAddField.setOnClickListener(bAddNewFieldListener);*/
	        
	        
    
	        llista = (ListView)findViewById(R.id.lFields);
	        
	        llista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	        llista.setItemsCanFocus(true);

	        
	    //    llista.setOnItemClickListener(theListListener);


	        rsC=new ProjectSecondLevelControler(this);
	        fillFieldList();
	        
	          

	        //listener for item cliked more than 3 seconds
	       llista.setOnItemLongClickListener(theListLongListener);

	       fc=new FieldCreator(this, projId);
	        
	        
	   }
	   
	   public void fillFieldList(){
		   
			
		   Cursor cFields=rsC.getProjectFieldsCursor(projId);
	       startManagingCursor(cFields);
		   
	        // Now create an array adapter and set it to display using our row
	       ProjectFieldAdapter fieldsAdapter = new ProjectFieldAdapter(this, cFields);
	        
	        llista.setAdapter(fieldsAdapter);
		   
	   }
	   
	   public OnItemLongClickListener theListLongListener = new OnItemLongClickListener() {
		    
		    public boolean onItemLongClick(android.widget.AdapterView<?> parent, final View v, int position, long id) {
		        
		    	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		    	
		    	
		    	builder.setMessage(R.string.deleteProjQuestion)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	// removeResearch(rsName);
		    	        
		    	        	 //loadResearches();

		    	        	   
		    	           }
		    	       })
		    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                
		    	        	   dialog.cancel();
		    	                
		    	           }
		    	       });
		    	AlertDialog alert = builder.create();
		    	
		    	alert.show();
		    	   
		    	   return true;
		    	
		    }
		    
		    
		    };
	    
	   
	   
	   private class ProjectFieldAdapter extends ResourceCursorAdapter {

	        public ProjectFieldAdapter(Context context, Cursor cur) {
	            super(context, R.layout.project_info_field_row, cur);
	            
	        }

	        @Override
	        public View newView(Context context, Cursor cur, ViewGroup parent) {
	            LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            return li.inflate(R.layout.project_info_field_row, parent, false);
	        }

	        @Override
	        public void bindView(View view, Context context, Cursor cur) {
	            
	        	TextView tvListText = (TextView)view.findViewById(R.id.tvFieldName);
	            TextView tvListLabel = (TextView)view.findViewById(R.id.tvFieldLabel);
	            CheckBox cbListCheck = (CheckBox)view.findViewById(R.id.cBedit);
	            ImageButton imgButton = (ImageButton)view.findViewById(R.id.removeImgButton);
	            imgButton.setBackgroundResource(android.R.drawable.ic_menu_delete);
	            
	            imgButton.setVisibility(View.INVISIBLE);
	            
	            
	            ImageButton listButton = (ImageButton)view.findViewById(R.id.listImgButton);

	            tvListText.setText(cur.getString(cur.getColumnIndex(ProjectDbAdapter.PROJ_NAME)));
	            tvListLabel.setText(cur.getString(cur.getColumnIndex(ProjectDbAdapter.LABEL)));
	            cbListCheck.setChecked((cur.getInt(cur.getColumnIndex(ProjectDbAdapter.VISIBLE))==0? false:true));
	            
	            String type=cur.getString(cur.getColumnIndex(ProjectDbAdapter.TYPE));
	            

	           
	            if(type.equals("secondLevel")) {
	            	
	            	if(listButton!=null){
	            		            	
				        listButton.setBackgroundResource(android.R.drawable.ic_menu_agenda);
		            	
			            Log.i("Proves","Add listButton"+tvListText.getText().toString());
			            
		            	listButton.setId((cur.getInt(cur.getColumnIndex(ProjectDbAdapter.KEY_ROWID))));
		            	
		            	 listButton.setOnClickListener(new OnClickListener() {  
		 	            	
		 	            	public void onClick(View v) { 
		 	            		
		 	            		View e=(View) v.getParent().getParent();
		 	            		
		 	            		 TextView tv= (TextView) e.findViewById(R.id.tvFieldName);
		 	            		 
		 	    	        	 String attName=tv.getText().toString();
		 	    	      	   	
		 	            	
		 	             } }
		 	            
		 	            );
	            	 
	            	}
	            }
	            else if(type.equals("photo")){
	            	
	            	listButton.setBackgroundResource(android.R.drawable.ic_menu_camera);
	            	
	            }
	            else{
	            	
	            	if(listButton!=null)listButton.setVisibility(View.INVISIBLE);
	            	
	            }
	            
	            
	            cbListCheck.setOnClickListener(new OnClickListener() {  
	            	
	            	public void onClick(View v) { 
	            	
	            		CheckBox cBox = (CheckBox) v; 
	            		View e=(View) v.getParent().getParent();
	            		
	            		 TextView tv= (TextView) e.findViewById(R.id.tvFieldName);
	            		
	            		 
	    	        	   String attName=tv.getText().toString();
	    	        	
	            		
	            		if (cBox.isChecked()) 
	            		{ 
	            			rsC.changeFieldVisibility(projId,attName,true);
	            		
	            		} 
	            		
	            		else if (!cBox.isChecked()) { 
	            			
	            			rsC.changeFieldVisibility(projId,attName,false);

	            		
	            		} } }
	            
	            );
	            
	            
	            imgButton.setOnClickListener(new OnClickListener() {  
	            	
	            	public void onClick(View v) { 
	            		
	            		View e=(View) v.getParent().getParent();
	            		
	            		 TextView tv= (TextView) e.findViewById(R.id.tvFieldName);
	            		
	            		 
	    	        	   String attName=tv.getText().toString();
	            		
	    	      	   	removeField(attName);

	            	
	             } }
	            
	            );
	            
	           


	        }
	    }

	   

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
	    	

	    	//menu.add(0, ADD_FIELD, 0,R.string.mAddField).setIcon(android.R.drawable.ic_menu_add);
	    	//menu.add(0, CHANGE_TH, 0,R.string.mChangeTh).setIcon(android.R.drawable.ic_menu_agenda);

	    	
	    	return super.onCreateOptionsMenu(menu);
	    }

		
		public void createFieldDialogType(){
			
			final CharSequence[] items = getBaseContext().getResources().getStringArray(R.array.newFieldTypes);

        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle("Escull un tipus");
        	builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	
        	    	dialog.dismiss();
        	    	
        	    	 if(items[item].equals(items[0])){
	        	        	
	        	        	// camps lliures o simples
	        	        	fc.createPredFieldDialog("simple",messageHandler);
	        	        	
	        	        }
	        	        else if(items[item].equals(items[1])){
	        	        	
	        	        	// camps pred-field
	        	        	fc.createComplexFieldDialog(messageHandler);
	        	        	
	        	        }
	        	        else if(items[item].equals(items[2])){
	        	        	
	        	        	//camp fotografia
	        	        	fc.createPredFieldDialog("photo",messageHandler);
	        	        	
	        	        }
	        	        else if(items[item].equals(items[3])){
	        	        	
	        	        	//camp fotografia
	        	        	fc.createPredFieldDialog("multiPhoto",messageHandler);
	        	        	
	        	        }
        	    	    else{
	        	        	

	        	        	fc.createPredFieldDialog("secondLevel",messageHandler);
	        	        	
	        	        }
        	    	 
        	    }
        	});
        	AlertDialog alert = builder.create();
        	alert.show();
			
			
		}

	    
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	
			switch (item.getItemId()) {
			case ADD_FIELD:
				
				createFieldDialogType();
				 			 
				break;
				
			case CHANGE_TH:
				
				changeTh();
				 			 
				break;
				
			}
			
		
			return super.onOptionsItemSelected(item);
		}
	    
		   private Handler messageHandler = new Handler() {

			      @Override
			      public void handleMessage(Message msg) {  
			          switch(msg.what) {
			            
			          case 0:
			        	  fillFieldList();
			          	
			          
			          }
			      }

			  };
	
	    
	    
	   private void removeField(String fieldName){
		   
		   ProjectControler rC= new ProjectControler(this);
		   
		   fieldId =rC.getFieldIdByName(projId, fieldName);
		   
		   sC= new CitationControler(this);
		   
		   
		   int numCitations= sC.getCitationsWithField(fieldId);
		 
			   
		   AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   
		   String removeQuestion="";
		    	
		   if(numCitations>0) { 	
			   
			   removeQuestion= String.format(getString(R.string.removeFieldQuestion), numCitations, fieldName);
			   	
		   }
		   
		   else{
			   
			   	removeQuestion= String.format(getString(R.string.removeFieldQuestionNoCit), fieldName);

			   
		   }
		    	
		    	builder.setMessage(removeQuestion)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	   sC.deleteCitationField(fieldId);
		    	        	   fillFieldList();
		    	        	   
		    	           }
		    	       })
		    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                
		    	        	   dialog.cancel();
		    	                
		    	           }
		    	       });
		    	AlertDialog alert = builder.create();
		    	
		    	alert.show();
			   
		   
		
		   
		   
		   
	   } 
	    
	 
	   private void changeTh(){
		   
		   final String [] thList=tc.getThList();

       	
       	if(thList.length==0){
       		

   			Toast.makeText(getBaseContext(), 
	                    R.string.emptyThList, 
	                    Toast.LENGTH_SHORT).show();
   		   
       		
       	}
       	else{
       	
	        	AlertDialog.Builder builder;
	        	
	        	builder= new AlertDialog.Builder(this);
	        	
	        	
	        	builder.setTitle(R.string.thChooseIntro);
	        	
	        	
	        	builder.setSingleChoiceItems(thList, -1, new DialogInterface.OnClickListener() {
	        	    
	        		
	        		public void onClick(DialogInterface dialog, int item) {
	        	        
	        			String thChanged=getApplicationContext().getString(R.string.thChangedText);
	        			
	        			Toast.makeText(getApplicationContext(),thChanged+" "+thList[item], Toast.LENGTH_SHORT).show();
	        	        
	        	        tc.changeProjectTh(projId,thList[item]);
	        	        
	        	        String defTh=getApplicationContext().getString(R.string.tvDefaultTh);
	        	        thName.setText(Html.fromHtml("<b>"+defTh+"</b>"+thList[item])); 

	        	        
	        	        dialog.dismiss();
	        	     
	        	        
	        	        
	        	        
	        	    }
	        	});
	        	AlertDialog alert = builder.create();
	        	
	        	alert.show();
		   
		   
	   } 
   
	   }
	   
	   
}