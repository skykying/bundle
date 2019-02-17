package com.lembed.lite.studio.report.core.views;

/**
 * 本类包含四个不同数据类型的变量，分别对应数据库中的四个字段。
 * 这些变量设置成private类型的,外界通过setter和getter方法访问.
 */
public class CompilerOption {
    private String description;
    private String resource;
    private String path;
    private String Location;
    private String type;
    
    
    
    /**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
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
	
//	//---------------------------------------------------
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//    public int getAge() {
//        return age;
//    }
//    public void setAge(int age) {
//        this.age = age;
//    }
//    public boolean isSex() {
//        return sex;
//    }
//    public void setSex(boolean sex) {
//        this.sex = sex;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public Date getCreateDate() {
//        return createDate;
//    }
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }
//    
    
    
}