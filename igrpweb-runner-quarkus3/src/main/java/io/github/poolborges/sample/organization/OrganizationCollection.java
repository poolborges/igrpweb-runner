package io.github.poolborges.sample.organization;

import io.github.poolborges.sample.organization.Organization;
import io.github.poolborges.sample.page.CollectionModel;
import java.util.List;

/**
 *
 * @author poolb
 */
public class OrganizationCollection extends CollectionModel<Organization>{
    
    public OrganizationCollection(List<Organization> items){
        super(items);
    }
}
