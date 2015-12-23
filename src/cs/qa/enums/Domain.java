package cs.qa.enums;

public enum Domain {
	
	HEALTH("health"),
	SHOPPING("shopping"),
	RESTAURANT("restaurant");
	
	private String value = "";
	
	private Domain(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static Domain getByValue(String value){
	    for(Domain d : values()){
	        if(d.getValue().equals(value)){
	            return d;
	        }
	    }
	    return null;
	}
}
