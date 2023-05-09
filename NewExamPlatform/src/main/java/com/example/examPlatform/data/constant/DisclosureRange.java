package com.example.examPlatform.data.constant;

/** 試験公開範囲 */
public class DisclosureRange {
	/** 公開 */
	private final int open = 0;
	/** 限定公開 */
	private final int limited = 1;
	/** 非公開 */
	private final int close = 2;
	
	/** 公開判定：公開ならtrue */
	public boolean isOpen(int disclosureRange) {
		return this.open == disclosureRange;
	}
	
	/** 限定公開判定：限定公開ならtrue */
	public boolean isLimited(int disclosureRange) {
		return this.limited == disclosureRange;
	}
	
	/** 非公開判定：非公開ならtrue */
	public boolean isNotClose(int disclosureRange) {
		return this.close != disclosureRange;
	}

	public int getOpen() {
		return open;
	}

	public int getLimited() {
		return limited;
	}

	public int getClose() {
		return close;
	}
}