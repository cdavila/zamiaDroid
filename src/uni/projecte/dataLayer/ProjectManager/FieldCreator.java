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

package uni.projecte.dataLayer.ProjectManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import uni.projecte.R;
import uni.projecte.Activities.Projects.ProjectFieldChooser;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.dataLayer.ProjectManager.ListAdapters.ProjectFieldCreatorListAdapter;
import uni.projecte.dataLayer.bd.SecondLevelCitacionDbAdapter;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


/*
 * It's a generic class that allows us to show forms for field creation.
 * It's possible to show simple and complex forms. 
 * Depending on the constructor invoked the defined field will be sto
 * 
 */



public class FieldCreator {
	
	private Context baseContext;
	private ArrayAdapter<String> m_adapterForSpinner;
	
	private ArrayList <String> fieldList;
	
	private ArrayList <ProjectField> objFieldList;
	
	private Hashtable<String,ProjectField> fieldNames;
	
	private ListView listAttributesPres;
	private boolean createField;
	
	private ProjectSecondLevelControler rsC;
	
	 private long projId;

	 private ProjectField taxonField;
	 private ProjectFieldCreatorListAdapter dataAdapter;
	
		private boolean secondField=false;
	
	   private int secondLevelId=0;


	
	/*
	 * Creator invoked when we want to defined a field that will be added to a list 
	 * to be created later with other one's
	 * 
	 */
	 
	 
	public FieldCreator(Context context, ListView attList, long projId){
		
		this.baseContext=context;
		this.listAttributesPres=attList;
		this.createField=false;
		this.projId=projId;
		
        fieldList= new ArrayList<String>();
        objFieldList= new ArrayList<ProjectField>();
        fieldNames= new Hashtable<String, ProjectField>();

		rsC=new ProjectSecondLevelControler(baseContext);

		
	}
	
	/*
	 * Creator invoked when its necessary to define and create a field
	 * 
	 */
	
	public FieldCreator(Context context, long projId){
		
		this.baseContext=context;
		this.createField=true;
		this.projId=projId;
		
		rsC=new ProjectSecondLevelControler(baseContext);
		
		
	}
	
	
	/*
	 * 
	 * This method shows the interface that will allow us to define field's name and description.
	 * Depending on attribute createField the field will be created.
	 * 
	 * @predFieldType: will be field's type [text,photo,secondLevel,thesaurus]
	 * 
	 */
	
	   public void createPredFieldDialog(final String predFieldType, final Handler messageHandler) {
	        
		   final Dialog dialog;
	        
		   dialog = new Dialog(baseContext);
	    	   	
		   dialog.setContentView(R.layout.field_free);
		   dialog.setTitle(R.string.projCreationTitle);

		   Button addField = (Button)dialog.findViewById(R.id.bCreateField);
		   final RadioGroup rgFieldTypes = (RadioGroup)dialog.findViewById(R.id.rgFieldTypes);

		   final String[] predValues=baseContext.getResources().getStringArray(R.array.predValues);


		  if(predFieldType.equals("simple")){ 
			  
			  rgFieldTypes.setVisibility(View.VISIBLE);
		  
		  }
	    
	    	 	  addField.setOnClickListener(new Button.OnClickListener(){
	    	             
		    	    	
		    	    	public void onClick(View v)
		    	    		{
	    	                 			
		    	    			EditText etName=(EditText)dialog.findViewById(R.id.etFieldName);
		    	    			//EditText etLabel=(EditText)dialog.findViewById(R.id.etFieldLabel);
		    	    			EditText etDesc=(EditText)dialog.findViewById(R.id.etFieldDesc);
		    	    			
		    	    			String fieldName=etName.getText().toString();

		    	    			if(fieldName.length()==0){
		    	    				
		    	    				 Toast.makeText(baseContext, R.string.tFieldValuesMissing, 
			        		   	              Toast.LENGTH_LONG).show();
		    	    				
		    	    			} 	
		    	    			else{
		    	    				
		    	    				String selected=predFieldType;
		    	    				ProjectField a=null;
		    	    				
		    	    				if(selected.equals(predValues[0])){
		    	    					
			    	        	    	a = new ProjectField("OriginalTaxonName",etDesc.getText().toString(),fieldName,"","thesaurus");

		    	    					
		    	    				}
		    	    				else if (selected.equals("photo")){
		    	    					
			    	        	    	a = new ProjectField(fieldName,etDesc.getText().toString(),fieldName,"","photo");

			    	        	    	
		    	    				}
		    	    				else if (selected.equals("multiPhoto")){
		    	    					
			    	        	    	a = new ProjectField(fieldName,etDesc.getText().toString(),fieldName,"","multiPhoto");

			    	        	    	
		    	    				}
		    	    				else if (selected.equals("polygon")){
		    	    					
			    	        	    	a = new ProjectField(fieldName,etDesc.getText().toString(),fieldName,"","polygon");

			    	        	    	
		    	    				}
		    	    				else if (selected.equals(predValues[2])){
		    	    					
			    	        	    	a = new ProjectField("CitationNotes",etDesc.getText().toString(),fieldName,"","simple");

		    	    				}
		    	    				else if (selected.equals("number")){
		    	    					
			    	        	    	a = new ProjectField("CitationNotes",etDesc.getText().toString(),fieldName,"","simple");

		    	    				}
		    	    				else if (selected.equals("secondLevel")){
		    	    					
			    	        	    	a = new ProjectField(fieldName,etDesc.getText().toString(),fieldName,"","secondLevel");
			    	        	    	secondField=true;
			    	        	    	
			    	        	    	
		    	    				}
		    	    				else{
		    	    					
		    	    					String fieldType;
		    	    					
		    	    					//simple
		    	    					switch (rgFieldTypes.getCheckedRadioButtonId()) {
										
		    	    						case R.id.rbFieldNum:
											
		    	    							fieldType="number";

		    	    							break;
		    	    							
		    	    						case R.id.rbFieldBoolean:
												
		    	    							fieldType="boolean";
		    	    							break;

		    	    						default:
		    	    							
		    	    							fieldType="simple";
		    	    							break;
										}
		    	    					
		    	    					a = new ProjectField(etName.getText().toString(),"",fieldName,"",fieldType);
		    	    					
		    	    				}
		    	    				    	    				
		    	    				
		    	    				if(fieldNames!=null && fieldNames.containsKey(a.getName())){
		    	    					
		    	    					String rep=baseContext.getString(R.string.predFieldRepeated);
		    	    					
		    	    					 Toast.makeText(baseContext, rep+" "+selected,
				        		   	              Toast.LENGTH_LONG).show();
		    	    					
		    	    					
		    	    				}
		    	    				else{
		    	    					

		    	    					String name=baseContext.getResources().getString(R.string.fieldName);
			    	        	    	String label=baseContext.getResources().getString(R.string.fieldLabel);
			    	        	    	
			    	        	    	if(createField){
			    	        	    		
			    	        	    		long fieldId=createField(a);
			    	        	    		
			    	        	    		if(secondField) chooseSecondFieldDialog(name,fieldId);
				    	        	    				    	        	    		
			    	        	    		if(a.isMultiPhoto()) createMultiPhotoField(fieldId);
			    	        	    		
			    	        	    		if(a.isPolygon()) createPolygonField(fieldId);

			    	        	    		
			    	        	    	}
			    	        	    	else{
			    	        	    		
			    	        	 	    	objFieldList.add(a);
				    	        	    	fieldNames.put(a.getName(), a);
				    	        	    	fieldList.add(name+": "+a.getName()+" "+label+": "+a.getType());  	

				    	        	    	dataAdapter=new ProjectFieldCreatorListAdapter(baseContext, objFieldList);

				    	        	    	listAttributesPres.setAdapter(dataAdapter);
				    	        	    	
				    	        	    	if(secondField) chooseSecondFieldDialog(name,secondLevelId);			    	        	    	
				    	        	    	
				    	        	    	if(a.isMultiPhoto()) createMultiPhotoField(secondLevelId);
			    	        	    		
			    	        	    		if(a.isPolygon()) createPolygonField(secondLevelId);

			    	        	    	}
		    	    				
			    	        	    	dialog.dismiss();
			    	        	    	
			    	        	    	messageHandler.sendEmptyMessage(0);
		    	    					
		    	    				}
		    	    					
		    	    			}
 
		    	    	     }
		    	    	             
		    	    });
	    	    
	    	    dialog.show();


	    	 
	    }

	   
	public void repeatedToast(String fieldType){
			 
			 String fieldTypeName="";
			 
			 if(fieldType.equals("multiPhoto")) fieldTypeName=baseContext.getString(R.string.fieldTypePhoto);
			 else if(fieldType.equals("polygon")) fieldTypeName=baseContext.getString(R.string.fieldTypePolygon);
			 else fieldTypeName=baseContext.getString(R.string.fieldTypeSubProject);
			 
			String text=String.format(baseContext.getString(R.string.uniqueField), fieldTypeName);	
	 	    
			Utilities.showToast(text, baseContext);
	 	    
	} 
	
	 public boolean repeatedFieldType(String fieldType){
		 
		 for(ProjectField field: objFieldList){
			 
			 if(field.getType().equals(fieldType)) return true;
			 			 
		 }
		 
		 return false;
		 
	 }
	 
	   
	   
	 private void createMultiPhotoField(long fieldId) {
		   
		 long secLevId;
		 
		 if(createField) {
			   
			   secLevId=fieldId;
			   
		   }
		   else{
			   
			   secondLevelId++;
			   secLevId=-secondLevelId;
			   
		   }
		 
		 
		 ProjectSecondLevelControler projSLCnt= new ProjectSecondLevelControler(baseContext);
		 projSLCnt.createField(secLevId, "Photo", "photo", "", "", "text");
		   
	}
	 
	 private void createPolygonField(long fieldId) {
		   
		 long secLevId;
		 
		 if(createField) {
			   
			   secLevId=fieldId;
			   
		   }
		   else{
			   
			   secondLevelId++;
			   secLevId=-secondLevelId;
			   
		   }
		 
		 
		 ProjectSecondLevelControler projSLCnt= new ProjectSecondLevelControler(baseContext);
		 projSLCnt.createField(secLevId, "polygonAltitude", "polygonAltitude", "", "", "text");
		   
	}

	   
	   /*
	    * 
	    * 
	    * 
	    */
	 
	protected void chooseSecondFieldDialog(final String name, final long fieldId) {

			
		   
		   final CharSequence[] items = baseContext.getResources().getStringArray(R.array.subProjCreationOptions);
       	
       	

       	AlertDialog.Builder builder = new AlertDialog.Builder(baseContext);
       	builder.setTitle(R.string.fieldTypeMessage);
       	builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
       		
       	    public void onClick(DialogInterface dialog, int item) {
       	    	
       	    	dialog.dismiss();
       	    	
       	        if(items[item].equals(items[0])){
       	        	
       	        	
       	        	createProjectChooser(name,fieldId);
       	        	
       	        	
       	        }
       	        else if(items[item].equals(items[1])){


       	        	
       	        }
       	        else{

       	        	
       	        }
       	    }
       	});

       	AlertDialog alert = builder.create();
       	alert.show();


		   
	}
	   
	   private void invokeFieldChooser(String name,long projId,String projName,long fieldId){
		   
		   long secLevId;
		   
		   if(createField) {
			   
			   secLevId=fieldId;
			   
		   }
		   else{
			   
			   secondLevelId++;
			   secLevId=-secondLevelId;
			   
		   }
		   
		
			Intent intent = new Intent(baseContext, ProjectFieldChooser.class);
	       
			Bundle b = new Bundle();
			b.putLong("Id", projId);
			intent.putExtras(b);
			
			b = new Bundle();
			b.putString("projName", projName);
			intent.putExtras(b);

			//Utilities.showToast("Subproj "+secLevId,baseContext);


			b = new Bundle();
			b.putString("projDescription", "sub-project-desc");
			intent.putExtras(b);
			
			b = new Bundle();
			b.putLong("subProjId", secLevId);
			intent.putExtras(b);
			
			b = new Bundle();
			b.putString("subProjName", name);
			intent.putExtras(b);
			
		
			((Activity) baseContext).startActivityForResult(intent, 1);
		   
	   }

	public void createTaxonField(){
		   
			taxonField= new ProjectField("OriginalTaxonName"," ","Taxon","","thesaurus");
		   
			String name=baseContext.getResources().getString(R.string.fieldName);
	    	String label=baseContext.getResources().getString(R.string.fieldLabel);
			
		  	objFieldList.add(0, taxonField);
	    	fieldNames.put(taxonField.getName(), taxonField);
	    	fieldList.add(0,name+": "+"OriginalTaxonName"+" "+label+": "+"thesaurus");  	
	    	
	    	dataAdapter=new ProjectFieldCreatorListAdapter(baseContext,objFieldList);
	    	
	    	listAttributesPres.setAdapter(dataAdapter);
		  
		   
	   }
	   
	   /*
	    * 
	    * 
	    * 
	    */
	   
	   public void removeTaxonField(){

		   objFieldList.remove(taxonField);
		   dataAdapter=new ProjectFieldCreatorListAdapter(baseContext, objFieldList);
		   listAttributesPres.setAdapter(dataAdapter);
		   fieldNames.remove(taxonField);
		  
	   }
	   
	   private void invisibleSpinner(Spinner sp,Button b){
		   
		   if(sp.getCount()==0){
			   
			   sp.setVisibility(View.GONE);
			   b.setVisibility(View.GONE);
			   
		   }
		   else{
			   sp.setVisibility(View.VISIBLE);
			   b.setVisibility(View.VISIBLE);
			   
		   }
		   
	   }
	   
	   
	   /*
	     * Method called when addField button is clicked.
	     * It creates a dialog with a form for the creation of new Fields 
	     * 
	     * It contains three clicks listener which handler the addition 
	     * of new predefined values and the field creation.
	     * 
	     */

		   public void createComplexFieldDialog(final Handler messageHandler) {
		        
			  	final Dialog dialog;
		        

		    	  dialog = new Dialog(baseContext);
		    	   	
		    	  dialog.setContentView(R.layout.field_creator);
		    	  dialog.setTitle(R.string.projCreationTitle);
		    	   	
		    	  
		    	  Button addPredField = (Button)dialog.findViewById(R.id.bAddPredField);
		    	  final Button remPredField = (Button)dialog.findViewById(R.id.bRemovePredField);
		    	  
		    	  
		    	  
		    	  Button addField = (Button)dialog.findViewById(R.id.bCreateField);

	
		    	  final Spinner predList=(Spinner)dialog.findViewById(R.id.sPrefFields);
		    	  
		    	  m_adapterForSpinner = new ArrayAdapter<String>(baseContext, android.R.layout.simple_spinner_item);
		    	  m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    	  predList.setAdapter(m_adapterForSpinner);
		    	  
		    	  invisibleSpinner(predList, remPredField);
		    	  
		    	  
		    	  remPredField.setOnClickListener(new Button.OnClickListener(){
	    	             
		    	    	
		    	    	public void onClick(View v)
		    	    		{

		    	    				Spinner predList=(Spinner)dialog.findViewById(R.id.sPrefFields);

		    	    				String element= predList.getSelectedItem().toString();
		    	    				m_adapterForSpinner.remove(element);
		    	    				
		    	  		    	  	invisibleSpinner(predList, remPredField);

		    	    	            	 
		    	    	     }
		    	    	             
		    	    }); 
		    	  

		    	    addPredField.setOnClickListener(new Button.OnClickListener(){
		    	    	             
		    	    	
		    	    	public void onClick(View v){

		    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etPredValue);
		    	    	                 
		    	    	                 String text=et.getText().toString();

		    	    	                 if(text.compareTo("")==0){
		    	    	                	 
		    	    	                	 
		    	    	                 }
		    	    	                 
		    	    	                 else{
		    	    	                	 
		    	    	                	 m_adapterForSpinner.insert(text, 0);
		    	    	                	 
		    	    	   		    	  	invisibleSpinner(predList, remPredField);
		 		    	    	            
			    	    			    	 et.setText("");
		    	    	                	 
		    	    	                 }
		    	    	                 

		    	    	              }
		    	    	             
		    	    }); 
		    	    
		    	    //listener for the creation of a new field. Data is collected and checked from EditText fields.
		    	    //if all necessary data is correct the dialog is destroyed
		    	    
		    	 	  addField.setOnClickListener(new Button.OnClickListener(){
		    	             
			    	    	
			    	    	public void onClick(View v)
			    	    		{
  	    	                 			
			    	    			EditText etName=(EditText)dialog.findViewById(R.id.etFieldName);
			    	    			EditText etDesc=(EditText)dialog.findViewById(R.id.etFieldDesc);
			    	    			
		    	    				Spinner predList=(Spinner)dialog.findViewById(R.id.sPrefFields);

			    	    			String fieldName=etName.getText().toString();


			    	    			if(fieldName.length()==0){
			    	    				
			    	    				 Toast.makeText(baseContext, R.string.tFieldValuesMissing, 
				        		   	              Toast.LENGTH_LONG).show();
			    	    				
			    	    				
			    	    			} 	
			    	    			else{
			    	    				
				    	    				if(fieldNames!=null && fieldNames.containsKey(fieldName)){
				    	    					
				    	    					 Toast.makeText(baseContext, R.string.repeatedFieldName, 
						        		   	              Toast.LENGTH_LONG).show();
				    	    					
				    	    				}
				    	    				else {
				    	    					
				    	    					String predValue="";
				    	    					
				    	    					if(predList.getCount()>0) predValue=predList.getSelectedItem().toString();
				    	    				
					    	        	    	ProjectField a = new ProjectField(fieldName,etDesc.getText().toString(),fieldName,predValue,"text");
					    	        	    	insertSpinnerItems(predList, a);
		
					    	    				String name=baseContext.getResources().getString(R.string.fieldName);
					    	        	    	String label=baseContext.getResources().getString(R.string.fieldLabel);
					    	        	    	
					    	        	    	if(createField){
					    	        	    		
					    	        	    		createField(a);
					    	        	    		
					    	        	    	}
					    	        	    	else{
					    	        	    		
					    	        	    	  	objFieldList.add(a);
						    	        	    	fieldNames.put(fieldName,a);
						    	        	    	fieldList.add(name+": "+a.getName()+" "+label+": "+a.getType());  	
						       	       	    	
						    	        	    	dataAdapter=new ProjectFieldCreatorListAdapter(baseContext,objFieldList);

						    	        	    	listAttributesPres.setAdapter(dataAdapter);
					    	        	    		
					    	        	    		
					    	        	    	}
					    	        	    				    	    				
					    	        	    	dialog.dismiss();
					    	        	    	
					    	        	    	messageHandler.sendEmptyMessage(0);
				    	        	    	
				    	    				}
			    	    				
			    	    			}

			    	    	            	 
			    	    	     }
			    	    	             
			    	    });
		    	    
		    	    dialog.show();


		    	 
		    }
		   
			/*
			 * This method takes the items from the Spinner View 
			 * and adds them into the Attribute class
			 *    
			 */
			   
			private void insertSpinnerItems(Spinner s, ProjectField a){

				int n=s.getCount();
				
				for(int i=0;i<n;i++){
					
					a.insertPredValue(s.getItemAtPosition(i).toString());
					
				}
				
			}
			
			/*
			 * 
			 * This method stores into the DB the field contained in @ProjectField object 
			 * 
			 */
			
			
		    private long createField(ProjectField at){
		    	
		       	rsC.startTransaction();
		       	
				ArrayList <String> predValues=at.getPredValuesList();
							
				Iterator<String> itrPreValues = predValues.iterator();
				
				int numPred=predValues.size();
				
				long attId;
				
				if(numPred<1){
					
					
					if(at.getType()==null){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getType(),at.getValue(),"simple","ECO");
						
					}
					else if(at.getType().equals("thesaurus")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"thesaurus","A");

					}
					else if(at.getType().equals("CitationNotes")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"simple","A");

					}
					else if (at.getType().equals("photo")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"photo","ECO");
						
					}
					else if (at.getType().equals("multiPhoto")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"multiPhoto","ECO");
						
					}
					else if (at.getType().equals("polygon")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"polygon","ECO");
									
					}
					else if (at.getType().equals("secondLevel")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"secondLevel","ECO");

						
					}
					else if (at.getType().equals("number")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"number","ECO");

						
					}
					else if (at.getType().equals("boolean")){
						
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"boolean","ECO");

						
					}
					else{
						attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getType(),at.getValue(),"simple","ECO");
					}
				}
				else{
					
	    			attId=rsC.addProjectField(projId, at.getName(),at.getLabel(), at.getType(),at.getValue(),"complex","ECO");

					
				}


	    		while(itrPreValues.hasNext()){
		    		
	    			rsC.addFieldItem(attId, itrPreValues.next());
	    			
	    		}
	    		
	    		 Toast.makeText(baseContext,baseContext.getString(R.string.newCreatedField)+at.getLabel(), 
		   	              Toast.LENGTH_LONG).show();
	    		 
	 	    	rsC.endTransaction();

	 	    	
	 	    	Log.i("Project","Field Created: "+at.getName()+" , T: "+at.getType()+" , #predValues: "+numPred);
	 	    	
	 	    	return attId;
	 	    	
		    	
		    }
		    
			public void createProjectChooser(final String name, final long fieldId){
		    	
		    	
		    	final CharSequence [] projList=rsC.getProjectListCS();
		    	

		    	AlertDialog.Builder builder = new AlertDialog.Builder(baseContext);
		    	
		    	builder.setSingleChoiceItems(projList, -1, new DialogInterface.OnClickListener() {
		    	    public void onClick(DialogInterface dialog, int item) {
		    	        
		    	    	
		    	    	Toast.makeText(baseContext, projList[item], Toast.LENGTH_SHORT).show();
		    	    	
		    	    	rsC.loadResearchInfoByName((String) projList[item]);
		    	    	
		    	    	dialog.dismiss();
		    	    	
		    	    	invokeFieldChooser(name,rsC.getProjectId(),rsC.getName(),fieldId);
		    	    	
		    	    }
		    	});
		    	AlertDialog alert = builder.create();
		    	alert.show();
		    	
		    	
		    }
		    
		    
		    public void createSubProjTypeChoosal () {
	
		        	
		        	final CharSequence[] items = baseContext.getResources().getStringArray(R.array.subProjCreationOptions);        	

		        	AlertDialog.Builder builder = new AlertDialog.Builder(baseContext);
		        	builder.setTitle(R.string.fieldTypeMessage);
		        	builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
		        		
		        	    public void onClick(DialogInterface dialog, int item) {
		        	    	
		        	    	dialog.dismiss();
		        	    	
		        	        if(items[item].equals(items[0])){
		        	        	
		        	        	
		        	        	
		        	        	
		        	        }
		        	        else if(items[item].equals(items[1])){
		

		        	        	
		        	        }
		        	        else if(items[item].equals(items[2])){
		        	        	


		        	        }
		        	        else{
		 
		        	        	
		        	        }
		        	    }
		        	});

		        	AlertDialog alert = builder.create();
		        	alert.show();

		  
		    }
		    
		    


			public ArrayList<String> getFieldList() {
				return fieldList;
			}

			public ArrayList<ProjectField> getObjFieldList() {
				return objFieldList;
			}
		    
			
			public void updateSubFieldId(long oldId, long newId){
				
				rsC.updateSubFieldId(oldId, newId);
				
			}
			
			
			
 

}
