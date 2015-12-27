package joy;

public class Music {
	private int point;
	private String id;
	private String name;
	private String artist;
	private String relation;

	/**
	 * nameを取得します。
	 *
	 * @return name
	 */
	public Music(String id, String name, String artist, String relation) {
		this.id = id;
		this.name = name;
		this.artist = artist;
		this.relation = relation;
		point = 0;
	}

	public void plusPoint(int point){
		this.point = this.point + point;
	}

	/**
	 * pointを取得します。
	 * @return point
	 */
	public int getPoint() {
	    return point;
	}

	/**
	 * pointを設定します。
	 * @param point point
	 */
	public void setPoint(int point) {
	    this.point = point;
	}

	/**
	 * idを取得します。
	 * @return id
	 */
	public String getId() {
	    return id;
	}

	/**
	 * idを設定します。
	 * @param id id
	 */
	public void setId(String id) {
	    this.id = id;
	}

	/**
	 * nameを取得します。
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * nameを設定します。
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * artistを取得します。
	 * @return artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * artistを設定します。
	 * @param artist artist
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * relationを取得します。
	 * @return relation
	 */
	public String getRelation() {
		return relation;
	}

	/**
	 * relationを設定します。
	 * @param relation relation
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}
}
