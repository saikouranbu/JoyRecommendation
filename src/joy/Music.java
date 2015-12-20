package joy;

public class Music {
	private String name;
	private String artist;
	private String relation;

	/**
	 * nameを取得します。
	 *
	 * @return name
	 */
	public Music(String name, String artist, String relation) {
		this.name = name;
		this.artist = artist;
		this.relation = relation;
	}

	public String getMusic() {
		return name;
	}

	/**
	 * musicを設定します。
	 *
	 * @param name
	 *            name
	 */
	public void setMusic(String name) {
		this.name = name;
	}

	/**
	 * artistを取得します。
	 *
	 * @return artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * artistを設定します。
	 *
	 * @param artist
	 *            artist
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * relationを取得します。
	 *
	 * @return relation
	 */
	public String getRelation() {
		return relation;
	}

	/**
	 * relationを設定します。
	 *
	 * @param relation
	 *            relation
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}
}
