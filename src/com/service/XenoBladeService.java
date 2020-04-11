package com.service;

import com.common.Code;

public class XenoBladeService {
	
	public String getComboByStart(String startAttr,String endAttr){
		String result = getAllCombo(startAttr,endAttr);
		return result;
	}
	
	private String getAllCombo(String startAttr,String endAttr){
		String result = "";
		if(!"".equals(startAttr)){
			result += "start with " + startAttr +"result : \r\n";
			for (String[] combo : Code.ALL_COMBO) {
				if (startAttr.equals(Code.getComboCd(combo, 0))) {
					result += "["+combo[0]+","+combo[1]+","+combo[2]+"]\r\n";
				}
			}
		}
		if(!"".equals(endAttr)){
			result += "\r\nend with " + endAttr +"result : \r\n";
			for (String[] combo : Code.ALL_COMBO) {
				if (endAttr.equals(Code.getComboCd(combo, 2))) {
					result += "["+combo[0]+","+combo[1]+","+combo[2]+"]\r\n";
				}
			}
		}
		return result;
	}
	
}
