package com.kt.ipms.legacy.cmn.util;

import java.io.File;
import java.io.FilenameFilter;

public class WhoisFileFilterUtil implements FilenameFilter{

	@Override
	public boolean accept(File dir, String name) {
		return name.matches("whois_response_([0-9]){17}");
	}
	
}
