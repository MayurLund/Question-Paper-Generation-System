/*package com.mca.sem6.models;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("UsersConverter")
public class UsersConverter implements Converter {
 
    public Object getAsObject(FacesContext facesContext, UIComponent component, String s) {
        for (UserBean user : ConfigurationManager.get) {
            if (user.getUserName().equals(s)) {
                return user;
            }
        }
        return null;
    }
 
    public String getAsString(FacesContext facesContext, UIComponent component, Object o) {
        if (o == null) return null;
        return ((UserBean) o).getUserName();
    }
 
   
}
*/