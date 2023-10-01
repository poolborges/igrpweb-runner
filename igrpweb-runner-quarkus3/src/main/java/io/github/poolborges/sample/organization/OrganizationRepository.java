package io.github.poolborges.sample.organization;


import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author poolb
 */
@ApplicationScoped
public class OrganizationRepository{
    
    public List<Organization> findByCountry(String country){
        return new ArrayList<>();
        //return list("SELECT org FROM Organization org WHERE org.country = ?1 ORDER BY org.name", country);
    }
    
    public List<Organization> listAll(){
        return new ArrayList<>();
        //return list("SELECT org FROM Organization org WHERE org.country = ?1 ORDER BY org.name", country);
    }
    
    public Optional<Organization> findByIdOptional(long id){
        return Optional.empty();
    }

    public void persist(Organization org) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean deleteById(long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
