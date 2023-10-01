package io.github.poolborges.sample.organization;




/**
 * Organization Entity.
 * 
 * ID field of Long type is generated on create
 *
 */

public class Organization {
    
    //Id is alreday define in PanacheEntity, if required a custom Id extend from PanacheEntityBase
    public Long id;
    
    private String name;
    
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    
}
