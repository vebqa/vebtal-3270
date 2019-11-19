package org.vebqa.vebtal.model;

public class SpecLine {

	private int row = -1;
	private int column = -1;
	private String label = null;

	public SpecLine() {
		super();
	}

	public static SpecLine build() {
		return new SpecLine();
	}
	
	public SpecLine setRow(int row) {
		this.row = row;
		return this;
	}

	public SpecLine setColumn(int column) {
		this.column = column;
		return this;
	}
	
	public SpecLine setLabel(String aLabel) {
		this.label = aLabel;
		return this;
	}
	
	public String getLine() {
		StringBuilder sb = new StringBuilder();
		if (this.row > 0) {
			sb.append("row=" + this.row + ";");
		}
		if (this.column > 0) {
			sb.append("column=" + this.column + ";");
		}
		if (this.label != null) {
			sb.append("label=" + this.label);
		}
		return sb.toString();
	}
}
