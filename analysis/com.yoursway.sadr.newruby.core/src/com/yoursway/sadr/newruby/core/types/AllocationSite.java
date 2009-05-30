package com.yoursway.sadr.newruby.core.types;

import com.yoursway.sadr.newruby.core.ir.Call;

public class AllocationSite implements TypeBase {

	private final Call site;

	public AllocationSite(Call site) {
		this.site = site;
	}

	public Call site() {
		return site;
	}

}
