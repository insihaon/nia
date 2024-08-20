package com.kt.ipms.legacy.cmn.util;

import java.util.Comparator;

import com.kt.ipms.legacy.cmn.vo.IpVo;

public class IpVoSortCompare implements Comparator<IpVo> {

	@Override
	public int compare(IpVo o1, IpVo o2) {
		if (o1.getNbitmask() > o2.getNbitmask()) {
			return -1;
		} else if (o1.getNbitmask() == o2.getNbitmask()) {
			return o1.getNfirstAddr().compareTo(o2.getNfirstAddr());
		} else {
			return 1;
		}
	}

}
