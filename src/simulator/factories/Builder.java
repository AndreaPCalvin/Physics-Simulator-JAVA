package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder<T> {
	protected String type;
	protected String description;
	
	public Builder(String type, String desc){
		this.type=type;
		description=desc;
	}
	
	public T createInstance(JSONObject info) throws IllegalArgumentException{
		T obj=null;
		if(type!=null)
			if(type.equals(info.getString("type"))) {
				obj=createTheInstance(info.getJSONObject("data"));
			}
		return obj;
	}
	
	public JSONObject getBuilderInfo() {
		JSONObject info=new JSONObject();
		info.put("type", type);
		info.put("data", createData());//DEPENDE DE CADA OBJETO
		info.put("desc", description);
		return info;
	}
	
	protected double[] jsonArrayTodoubleArray(JSONArray j_array) {
		double[] double_array=new double[j_array.length()];
		for(int i=0;i<double_array.length;i++) {
			double_array[i]=j_array.getDouble(i);
		}
		return double_array;
	}
	
	protected JSONObject createData() {
		return new JSONObject();
	}
	
	protected abstract T createTheInstance(JSONObject j) throws IllegalArgumentException;
}
