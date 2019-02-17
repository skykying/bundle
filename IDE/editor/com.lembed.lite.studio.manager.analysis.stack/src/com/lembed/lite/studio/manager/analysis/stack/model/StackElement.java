package com.lembed.lite.studio.manager.analysis.stack.model;


@SuppressWarnings("javadoc")
public class StackElement {
  
    private Long id; 

    private String function;
    private String size;
    private String path;
    private String Location;
    private String type;
    private String timeStamp;
    
    
    
    /**
	 * @return the description
	 */
	public String getFunction() {
		return function;
	}
	/**
	 * @param function 
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	/**
	 * @return the resource
	 */
	public String getSize() {
		return size;
	}
	/**
	 * @param size 
	 */
	public void setSize(String size) {
		this.size = size;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return Location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		Location = location;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	//---------------------------------------------------
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    /**
     * @return
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
 
}