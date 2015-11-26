package com.garbacgebin.sliderlibrary;

public class SlideMenuModels {

	int image_id;
	String menu_name;

	public SlideMenuModels() {
	}

	public SlideMenuModels(String menu_name,int image_id) {
		this.menu_name = menu_name;
		this.image_id = image_id;
	}

	public int getImage_id() {
		return image_id;
	}
	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}



}
