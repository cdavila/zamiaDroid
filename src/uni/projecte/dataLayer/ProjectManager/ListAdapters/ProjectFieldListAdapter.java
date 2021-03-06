package uni.projecte.dataLayer.ProjectManager.ListAdapters;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.Activities.Projects.SubProjectInfo;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.CitationControler;

import uni.projecte.dataLayer.ProjectManager.FieldModifyDialog;
import uni.projecte.dataLayer.ProjectManager.tools.FieldTypeTranslator;
import uni.projecte.dataTypes.ProjectField;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;



public class ProjectFieldListAdapter extends BaseAdapter  {
	
	private LayoutInflater mInflater;
	private ArrayList<ProjectField> projFields;
	private Context parentContext;
	private long projId;
	private ProjectControler projCnt;
	private int n;
	private FieldTypeTranslator fieldTrans;

	
	public ProjectFieldListAdapter(Context context,ArrayList<ProjectField> projFields, ProjectControler rsC){
		
		this.projFields=projFields;
		this.parentContext=context;
		
		this.projId=rsC.getProjectId();
		this.projCnt=rsC;
		
		n=projFields.size();
		
		mInflater = LayoutInflater.from(context);
		fieldTrans=new FieldTypeTranslator(parentContext);

		
	}


	public int getCount() {
		
		return projFields.size();
		
	}

	public Object getItem(int position) {
		
		return projFields.get(position);
		
	}

	public long getItemId(int position) {
		
		return projFields.get(position).getId();
		
	}

  	 public View getView(final int position, View convertView, ViewGroup parent) {
  		 
    	 ViewHolder holder;
    	 
    	 if (convertView == null) {
	    	 
    		 convertView = mInflater.inflate(R.layout.project_info_field_row, null);
    		 
	    	 holder = new ViewHolder();
	    	 holder.tvListText = (TextView)convertView.findViewById(R.id.tvFieldName);
	    	 holder.tvListLabel = (TextView)convertView.findViewById(R.id.tvFieldLabel);
	    	 holder.cbListCheck = (CheckBox)convertView.findViewById(R.id.cBedit);
	    	 holder.imgButton = (ImageButton)convertView.findViewById(R.id.removeImgButton);
	    	 holder.upButton = (ImageButton)convertView.findViewById(R.id.ibArrowUp);
	    	 holder.downButton= (ImageButton)convertView.findViewById(R.id.ibArrowDown);
	    	 holder.listButton=(ImageButton)convertView.findViewById(R.id.listImgButton);


	    	 convertView.setTag(holder);
	    	 
    	 }
    	 else {
    		 
    		 holder = (ViewHolder) convertView.getTag();
    		 
    	 }
    	 
    	    holder.imgButton.setBackgroundResource(android.R.drawable.ic_menu_delete);
 
    	    ProjectField pF=projFields.get(position);
                       
            holder.tvListText.setText(fieldTrans.getFieldTypeTranslated(pF.getType()));
            holder.tvListLabel.setText(pF.getLabel());
            
            if(position==0) {
            	
            	holder.upButton.setVisibility(View.INVISIBLE);
            	holder.downButton.setVisibility(View.VISIBLE);
            }
            else if(position==n-1) {
            	
            	holder.downButton.setVisibility(View.INVISIBLE);
            	holder.upButton.setVisibility(View.VISIBLE);
            	
            }
            else {
            	
            	holder.upButton.setVisibility(View.VISIBLE); 
            	holder.downButton.setVisibility(View.VISIBLE);
            
            }
            
            holder.cbListCheck.setChecked((pF.getVisible()==0)? false:true);
            
            String type=pF.getType();
            
      
           
            if(type.equals("secondLevel")) {
            	
            	if(holder.listButton!=null){
            		            	
			        holder.listButton.setBackgroundResource(android.R.drawable.ic_menu_agenda);
			        holder.listButton.setTag(holder.tvListLabel.getText().toString());
	            	
		            Log.i("Proves","Add listButton"+holder.tvListText.getText().toString());
		            
	            	holder.listButton.setId((int) pF.getId());
	            	
	            	 holder.listButton.setOnClickListener(new OnClickListener() {  
	 	            	
	 	            	public void onClick(View v) { 
	 	            		
	 	            		Intent intent = new Intent(v.getContext(), SubProjectInfo.class);
	 	                   
	 			 			Bundle b = new Bundle();
	 			 			b.putLong("Id", v.getId());
	 			 			intent.putExtras(b);
	 		 			
		 		 			b = new Bundle();
		 		 			b.putString("projName", (String) v.getTag());
		 		 			intent.putExtras(b);
		 		 			
		 		 			projCnt.loadProjectInfoById(v.getId());

		 		 			b = new Bundle();
	 		 				b.putString("projDescription", projCnt.getThName());
	 		 				intent.putExtras(b);
	 		 			
	 			 			
	 		 				((Activity) v.getContext()).startActivityForResult(intent, 1);    
	 	    	      	   	
	 	            	
	 	             } }
	 	            
	 	            );
            	 
            	}
            }
            else if(type.equals("photo")){
            	
            	holder.listButton.setBackgroundResource(android.R.drawable.ic_menu_camera);
            	
            }
            else{
            	
            	if(holder.listButton!=null)holder.listButton.setVisibility(View.GONE);
            	
            }
            
            
            holder.upButton.setOnClickListener(new OnClickListener() {  
            	
            	public void onClick(View v) { 
            		           		
            		ProjectField current=projFields.get(position);
            		ProjectField upper=projFields.get(position-1);
            		projFields.remove(current);
            		projFields.add(position-1,current);
            		
            		projCnt.setFieldOrder(current.getId(),position-1);
            		projCnt.setFieldOrder(upper.getId(),position);

            		notifyDataSetChanged();
            		
            	
            	}
            	
            	
            	}
            	
            		
            
            );
            
            
            holder.tvListLabel.setOnClickListener(new OnClickListener() {  
            	
            	public void onClick(View v) { 
            		           		
            		FieldModifyDialog modifyDialog=new FieldModifyDialog(parentContext, projId,projFields.get(position),projCnt);
            		modifyDialog.setTitle(R.string.btModifyField);
            		modifyDialog.show();
            	
            	}
            	
            }
            
            );
            
       
       holder.downButton.setOnClickListener(new OnClickListener() {  
       	
       	public void onClick(View v) { 
       	
       		
      		ProjectField current=projFields.get(position);
    		ProjectField down=projFields.get(position+1);
    		projFields.remove(current);
    		projFields.add(position+1,current);
    		
    		projCnt.setFieldOrder(current.getId(),position+1);
    		projCnt.setFieldOrder(down.getId(),position);
    		
    		notifyDataSetChanged();

    		
       	} 
       	
       }
       
       );
       
            
            holder.cbListCheck.setOnClickListener(new OnClickListener() {  
            	
            	public void onClick(View v) { 
            	
            		CheckBox cBox = (CheckBox) v; 
            		View e=(View) v.getParent().getParent();
            		
            		 TextView tv= (TextView) e.findViewById(R.id.tvFieldName);
            		
            		 
    	        	   String attName=tv.getText().toString();
    	        	
            		
            		if (cBox.isChecked()) 
            		{ 
            			projCnt.changeFieldVisibility(projId,attName,true);
            		
            		} 
            		
            		else if (!cBox.isChecked()) { 
            			
            			projCnt.changeFieldVisibility(projId,attName,false);

            		
            		} } }
            
            );
            
            
           holder.imgButton.setOnClickListener(new OnClickListener() {  
            	
            	public void onClick(View v) { 
            		
            		View e=(View) v.getParent().getParent();
            		
            		 TextView tv= (TextView) e.findViewById(R.id.tvFieldName);
            		
            		 
    	        	   String attName=tv.getText().toString();
            	   	   removeField(projFields.get(position).getName(),position);

            	
             } }
            
            );
    	 
		
		return convertView;
	}

	 static class ViewHolder {
		 
		   	TextView tvListText;
	        TextView tvListLabel;
	        CheckBox cbListCheck;
	        ImageButton imgButton;
	        ImageButton upButton;
	        ImageButton downButton;
	        ImageButton listButton;

	 }
	
	 
	  private void removeField(String fieldName, final int position){
		   
		  
		   
		   final long fieldId =projCnt.getFieldIdByName(projId, fieldName);
		   
		   final CitationControler sC= new CitationControler(parentContext);
		   
		   
		   int numCitations= sC.getCitationsWithField(fieldId);
		 
			   
		   AlertDialog.Builder builder = new AlertDialog.Builder(parentContext);
		   
		   String removeQuestion="";
		    	
		   if(numCitations>0) { 	
			   
			   removeQuestion= String.format(parentContext.getString(R.string.removeFieldQuestion), numCitations, fieldName);
			   	
		   }
		   
		   else{
			   
			   	removeQuestion= String.format(parentContext.getString(R.string.removeFieldQuestionNoCit), fieldName);

		   }
		    	
		    	builder.setMessage(removeQuestion)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	   sC.deleteCitationField(fieldId);
		    	        	   
		    	        	   projFields.remove(position);
		    	        	   
		    	        	   notifyDataSetChanged();

		    	        	   
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
	 
	 
	 
}
