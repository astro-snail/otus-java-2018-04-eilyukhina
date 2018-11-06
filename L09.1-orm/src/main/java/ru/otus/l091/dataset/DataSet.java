package ru.otus.l091.dataset;

public abstract class DataSet {
	private long id;

	public DataSet() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
