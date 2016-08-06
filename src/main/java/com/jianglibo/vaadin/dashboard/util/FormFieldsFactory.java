package com.jianglibo.vaadin.dashboard.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.FormFields;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;

@Component
public class FormFieldsFactory {
	
	@Autowired
	private ComboBoxFieldFactory comboBoxFieldFactory;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private TwinColSelectFieldFactory twinColSelectFieldFactory;
	
	public List<PropertyIdAndField> buildFields(VaadinTable vt, FormFields ffs) {
		List<PropertyIdAndField> fields = Lists.newArrayList();
		
        for(VaadinFormFieldWrapper vfw : ffs.getFields()) {
        	switch (vfw.getVff().fieldType()) {
			case COMBO_BOX:
				ComboBox cb = comboBoxFieldFactory.createCombo(vt, vfw);
				addStyleName(vfw, cb);
				fields.add(new PropertyIdAndField(vfw.getName(), cb));
				break;
			case TEXT_AREA:
				TextArea ta = new TextArea(MsgUtil.getFieldMsg(messageSource, vt.messagePrefix(), vfw));
				ta.setNullRepresentation("");
				addStyleName(vfw, ta);
				fields.add(new PropertyIdAndField(vfw.getName(), ta));
				break;
			case TWIN_COL_SELECT:
				TwinColSelect tcs = twinColSelectFieldFactory.create(vt, vfw);
				addStyleName(vfw, tcs);
				fields.add(new PropertyIdAndField(vfw.getName(), tcs));
				break;
			default:
				String caption = vfw.getVff().caption();
				if (Strings.isNullOrEmpty(caption)) {
					caption = vfw.getName();
				}
				try {
					caption = messageSource.getMessage(vt.messagePrefix() + "field." + caption, null, UI.getCurrent().getLocale());
				} catch (NoSuchMessageException e) {
				}
				TextField tf = new TextField(caption);
				tf.setNullRepresentation("");
				addStyleName(vfw, tf);
				fields.add(new PropertyIdAndField(vfw.getName(), tf));
				break;
			}
        }
        return fields;
	}
	
	private void addStyleName(VaadinFormFieldWrapper vfw, Field<?> f) {
		if (vfw.getVff().styleNames().length > 0) {
			for(String sn: vfw.getVff().styleNames()) {
				f.addStyleName(sn);
			}
		}
	}
	
	public static class PropertyIdAndField {
		private String propertyId;
		private Field<?> field;
		
		public PropertyIdAndField(String propertyId, Field<?> field) {
			super();
			this.propertyId = propertyId;
			this.field = field;
		}

		public String getPropertyId() {
			return propertyId;
		}

		public void setPropertyId(String propertyId) {
			this.propertyId = propertyId;
		}

		public Field<?> getField() {
			return field;
		}

		public void setField(Field<?> field) {
			this.field = field;
		}
	}
}