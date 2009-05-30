package com.yoursway.sadr.ddp;

class IndexEntry<T> implements IFileDependant {

	private final T value;
	final String fileName;

	public IndexEntry(T value, String file) {
		this.value = value;
		fileName = file;
	}

	public String fileName() {
		return fileName;
	}

	public T value() {
		return value;
	}

}
