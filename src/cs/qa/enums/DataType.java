package cs.qa.enums;

public enum DataType {
	
	TRAIN("train"),
	DEV("dev"),
	TEST("test");
	
	private String value = "";
	
	private DataType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static DataType getByValue(String value){
	    for(DataType d : values()){
	        if(d.getValue().equals(value)){
	            return d;
	        }
	    }
	    return null;
	}
}
