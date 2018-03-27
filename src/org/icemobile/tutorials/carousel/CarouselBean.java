package org.icemobile.tutorials.carousel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class CarouselBean implements Serializable {

	  private ArrayList<String> images = new ArrayList<String>();
	  private int selectedItem;

	    public CarouselBean() {
	  
	        images.add("desktop.png");
	        images.add("laptop.png");
	        images.add("monitor.png");
	        images.add("pda.png");
	        images.add("desktop.png");
	        images.add("laptop.png");
	        images.add("monitor.png");
	        images.add("pda.png");
	        images.add("desktop.png");
	    }

	    public ArrayList<String> getImages() {
	        return images;
	    }

	    public int getSelectedItem() {
	        return selectedItem;
	    }

	    public void setSelectedItem(int selectedItem) {
	        this.selectedItem = selectedItem;
	    }

}
