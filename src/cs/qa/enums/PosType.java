package cs.qa.enums;

public enum PosType {
	
	PROP_NOUN("PN"),
	NOUN("N"),
	ADJ("A"),
	ADV("R"),
	VERB("V"),
	NA("NA"),
	INTERJ("I"),
	ANY("?");
	
	private String value = "";
	
	private PosType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static PosType getByValue(String value){
	    for(PosType p : values()){
	        if(p.getValue().equals(value)){
	            return p;
	        }
	    }
	    return null;
	}
}
