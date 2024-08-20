package com.kt.ipms.legacy.cmn.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WhitelistObjectInputStreamUtil extends ObjectInputStream {
	
	public Set whitelist;
	
	public WhitelistObjectInputStreamUtil(InputStream inputStream, Set wl) throws IOException {
	    super(inputStream);
	    whitelist = wl;
	}
	
	@Override
	protected Class <?> resolveClass(ObjectStreamClass cls) throws IOException, ClassNotFoundException {
	    if (!whitelist.contains(cls.getName())) {
	    	throw new InvalidClassException("Unexpected serialized class",cls.getName());
	    }
	    return super.resolveClass(cls);
	}
	
}