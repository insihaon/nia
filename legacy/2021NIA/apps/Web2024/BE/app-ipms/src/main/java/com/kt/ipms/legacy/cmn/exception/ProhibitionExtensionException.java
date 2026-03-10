package com.kt.ipms.legacy.cmn.exception;

import org.springframework.web.multipart.MultipartException;

@SuppressWarnings("serial")
public class ProhibitionExtensionException extends MultipartException {
	private final String prohibitionExtension;

	public ProhibitionExtensionException(String prohibitionExtension) {
		this(prohibitionExtension, null);
	}

	public ProhibitionExtensionException(String prohibitionExtension, Throwable ex) {
		super("업로드 금지 확장자 : " + prohibitionExtension, ex);
	    this.prohibitionExtension = prohibitionExtension;
	}

	public String getProhibitionExtension() {
		return this.prohibitionExtension;
	}
}
