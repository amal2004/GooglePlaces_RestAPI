package googleplaces.resources;

/**
 * Central registry of API paths. Keep endpoint strings out of step definitions.
 */
public enum APIResources {
	ADD_PLACE("/maps/api/place/add/json"), GET_PLACE("/maps/api/place/get/json"),
	DELETE_PLACE("/maps/api/place/delete/json");

	private final String path;

	APIResources(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
