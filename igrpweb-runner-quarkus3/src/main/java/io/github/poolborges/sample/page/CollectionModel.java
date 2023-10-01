package io.github.poolborges.sample.page;

import java.util.List;

/**
 *
 * @author poolb
 */
public abstract class CollectionModel<T> {
    
    protected List<T> items;
    
    public CollectionModel(List<T> items){
        this.items = items;
    } 
    
    public List<T> getItems(){
        return items;
    }
}
