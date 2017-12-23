package com.dading.ssqs.bean;

public class SpaceInfo {
	public long totalSpace;
	public long freeSpace;
	public long usedSpace;
	
	
	
	public SpaceInfo(long totalSpace, long freeSpace, long userSpace) {
		super();
		this.totalSpace = totalSpace;
		this.freeSpace = freeSpace;
		this.usedSpace = userSpace;
	}
	
	
	@Override
	public String toString() {
		return "SpaceInfo [totalSpace=" + totalSpace + ", freeSpace="
				+ freeSpace + ", userSpace=" + usedSpace + "]";
	}
	
	
}
