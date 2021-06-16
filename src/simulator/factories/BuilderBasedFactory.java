package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
	
	private List<Builder<T>> _builders;//lista de constructores
	private List<JSONObject> _factoryElements;//lista de json por defecto
	
	public BuilderBasedFactory(List<Builder<T>> builders) {
		_builders=new ArrayList<>(builders);
		
		_factoryElements= new ArrayList<>();
		for(Builder<T>b: _builders) 
			_factoryElements.add(b.getBuilderInfo());
	}

	@Override
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		T obj=null;
		boolean ok=false;
		for(int i=0;i<_builders.size() && !ok;i++) {
			obj = _builders.get(i).createInstance(info);
			if(obj!=null)ok=true;
		}
		if(obj==null) throw new IllegalArgumentException();
		return obj;
	}

	@Override
	public List<JSONObject> getInfo() {
		return _factoryElements;
	}

}